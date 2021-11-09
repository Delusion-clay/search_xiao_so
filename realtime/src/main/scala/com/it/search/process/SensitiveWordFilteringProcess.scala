package com.it.search.process

import java.util

import com.alibaba.fastjson.{JSON, JSONObject, TypeReference}
import com.huaban.analysis.jieba.{JiebaSegmenter, SegToken}
import com.it.search.bean.JobInfoEntity
import com.it.search.common.convert.DataConvert
import com.it.search.config.EsConfigUtil
import com.it.search.dao.WarningMessageDao
import com.it.search.domain.WarningMessage
import com.it.search.es.admin.AdminUtil
import com.it.search.es.client.ESclientUtil
import com.it.search.utils.{JedisUtil, TimeTranstationUtils}
import org.apache.spark.internal.Logging
import org.apache.spark.streaming.dstream.DStream
import org.elasticsearch.spark.rdd.EsSpark
import redis.clients.jedis.Jedis

import scala.collection.JavaConversions._

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 17:02
 */
object SensitiveWordFilteringProcess extends Serializable with Logging {

  def process(xiao_so_DataStream: DStream[java.util.HashMap[String, String]]) = {
    xiao_so_DataStream.foreachRDD(rdd => {
      rdd.foreach(x => {
        val jedis = JedisUtil.getJedis(15)
        val title = x.get("title")
        val jiebaSegmenter = new JiebaSegmenter
        val tokens = jiebaSegmenter.process(title.toString, JiebaSegmenter.SegMode.INDEX)
        tokens.foreach(words => {
          val redisKey = words.word + ""
          if (jedis.exists(redisKey)) {
            filterSave2Mysql(redisKey, x)
            println("==========过滤消息为" + x)
          }
        })
      })
    })
  }
  def filterSave2Mysql(redisKey: String, data: java.util.Map[String, String]) = {
    val url = data.get("url")
    val infomessage = data.get("infomessage")
    val `type` = data.get("type")
    val title = data.get("title")
    val sensitiveWords = redisKey
    val warningMessage = new WarningMessage
    warningMessage.setId(data.get("id"))
    warningMessage.setInfomessage(infomessage)
    warningMessage.setSensitiveWords(sensitiveWords)
    warningMessage.setTitle(title)
    warningMessage.setType(`type`)
    warningMessage.setUrl(url)
    WarningMessageDao.insertWarningMessage(warningMessage)
  }
}
