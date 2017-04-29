package xrq

import redis.clients.jedis.Jedis

object RedisDemo {
  def main(args: Array[String]): Unit = {
    val redisHost = "127.0.0.1"
    val redisPort = 6379
    val redisClient = new Jedis(redisHost, redisPort)

    println(redisClient.ping())

    redisClient.set("user", "xierenqiang")
    println(redisClient.get("user"))

    redisClient.sadd("fruits", "apple", "banana", "orange")
    println(redisClient.smembers("fruits"))

    redisClient.lpush("NBAplayers", "James", "Kobe", "Wade")
    val NBAplayers = redisClient.lrange("NBAplayers", 0, 2)
    println(NBAplayers)
    for (i <- 0 until NBAplayers.size()) {
      println(NBAplayers.get(i))
    }

    val keys = redisClient.keys("*")
    keys.forEach(println)

    redisClient.flushAll()

    redisClient.quit()
  }
}
