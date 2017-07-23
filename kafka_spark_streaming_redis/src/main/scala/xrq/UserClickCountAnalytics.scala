package xrq

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.codehaus.jettison.json.JSONObject
/**
  * Created by xrq on 17-7-12.
  */
object UserClickCountAnalytics {
  def main(args: Array[String]): Unit = {
    var masterUrl = "local[1]"
    if (args.length > 0) {
      masterUrl = args(0)
    }

    // Create a StreamingContext with the given master URL
    val conf = new SparkConf().setMaster(masterUrl).setAppName("UserClickCountAnalytics")
    val ssc = new StreamingContext(conf, Seconds(5))

    // Kafka configurations
    val topics = Set("user_events")
    val brokers = "localhost:9092"
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder")

    val dbIndex = 1
    val clickHashKey = "app::users::click"

    // Create a direct stream
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    val events = kafkaStream.flatMap(line => {
      val data = new JSONObject(line._2)
      Some(data)
    })

    // Compute user click times
    val userClicks = events.map(x => (x.getString("uid"), x.getInt("click_count"))).reduceByKey(_ + _)
    userClicks.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(pair => {
          val uid = pair._1
          val clickCount = pair._2

          val jedis = RedisClient.getJedis()
          jedis.select(dbIndex)
          jedis.hincrBy(clickHashKey, uid, clickCount)
          RedisClient.returnResource(jedis)

          println(uid + "\t" + clickCount)
        })
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
