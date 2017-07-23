package xrq

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
import org.codehaus.jettison.json.JSONObject

import scala.util.Random

object KafkaEventProducer {
  private val users = Array(
    "id1", "id2",
    "id3", "id4",
    "id5", "id6",
    "id7", "id8",
    "id9", "id10")

  private val random = new Random()

  private var pointer = -1

  def getUserID(): String = {
    pointer = pointer + 1
    if (pointer >= users.length) {
      pointer = 0
    }
    users(pointer)
  }

  def click(): Int = {
    random.nextInt(10)
  }

  // bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic user_events --replication-factor 1 --partitions 1
  // bin/kafka-topics.sh --zookeeper localhost:2181 --list
  // bin/kafka-topics.sh --zookeeper localhost:2181 --describe user_events

  def main(args: Array[String]): Unit = {
    val topic = "user_events"
    val brokers = "localhost:9092"
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")

    val producerConfig = new ProducerConfig(props)
    val producer = new Producer[String, String](producerConfig)

    while (true) {
      // prepare event data
      val event = new JSONObject()
      event
        .put("uid", getUserID)
        .put("event_time", System.currentTimeMillis.toString)
        .put("os_type", "Android")
        .put("click_count", click)

      // produce event message
      producer.send(new KeyedMessage[String, String](topic, event.toString))
      println("Message sent: " + event)

      Thread.sleep(1000)
    }
  }

  // bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic user_events --from-beginning
}
