package com.it.search.process

import com.it.search.common.convert.DataConvert
import com.it.search.config.EsConfigUtil
import com.it.search.es.admin.AdminUtil
import com.it.search.es.client.ESclientUtil
import org.apache.spark.streaming.dstream.DStream
import org.elasticsearch.spark.rdd.EsSpark

/**
 * @description:
 * @author: Delusion
 * @date: 2021-06-08 15:22
 */
object Spark2es {
  def process(xiao_so_DataStream: DStream[java.util.HashMap[String, String]]) = {
    val array = Array("baijiahao", "toutiao", "shuimu", "zhihu", "hupu", "chouti", "douyin", "weibo", "36ke", "pengpai", "guanchazhe", "xinjingbao", "xinlang", "baidu", "bili", "gongzhongh")
    array.foreach(ty => {
      val typeDStreaming = xiao_so_DataStream.filter(map => {
        ty.equals(map.get("infotype"))
      })
      typeDStreaming.print()
      typeDStreaming.foreachRDD(rdd => {
        val client = ESclientUtil.getClient
        val typeRDD = rdd.map(x => (x.get("infotype"))).distinct().collect()
        typeRDD.foreach(type_ => {
          val dataRDD = rdd.filter(map => {
            type_.equals(map.get("infotype"))
          }).map(map => {
            DataConvert.strMap2esObjectMap(map)
          })
          //创建动态索引
          val index = s"target_${ty}"
          //创建索引，先判断是不是已经存在了 mapping
          if (!AdminUtil.indexExists(client, index)) {
            val path = "mapping/test.json"
            AdminUtil.buildIndexAndMapping(index, index, path, 3, 1)
          }
          EsSpark.saveToEs(dataRDD, s"${index}/${index}", EsConfigUtil.getEsParams("id"))
        })
      })
    })
  }
}
