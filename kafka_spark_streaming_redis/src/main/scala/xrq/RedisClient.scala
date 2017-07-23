package xrq

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

/**
  * Created by xrq on 17-7-12.
  */
object RedisClient extends Serializable {
  private val REDIS_HOST = "localhost"
  private val REDIS_PORT = 6379

//  private val MAX_TOTAL = 10
//  private val MAX_IDLE = 5
//  private val MAX_WAIT = 3000
//  private val TEST_ON_BORROW = true
//  private val TIMEOUT = 30000
//
//  private val jedisPoolConfig = new JedisPoolConfig()
//  jedisPoolConfig.setMaxTotal(MAX_TOTAL)
//  jedisPoolConfig.setMaxIdle(MAX_IDLE)
//  jedisPoolConfig.setMaxWaitMillis(MAX_WAIT)
//  jedisPoolConfig.setTestOnBorrow(TEST_ON_BORROW)
//
//  private val jedisPool = new JedisPool(jedisPoolConfig, REDIS_HOST, REDIS_PORT, TIMEOUT)

  def getJedis(): Jedis = {
//    val jedis = jedisPool.getResource
//    jedis
    new Jedis(REDIS_HOST, REDIS_PORT)
  }

  def returnResource(jedis: Jedis): Unit = {
//    jedisPool.returnResource(jedis)
  }

  def main(args: Array[String]): Unit = {
    val jedis = getJedis()
    jedis.set("user", "xierenqiang")
    println(jedis.get("user"))
    returnResource(jedis)
  }
}
