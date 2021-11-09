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
 * @date: 2021-06-15 8:06
 */
object ESAll {
  def process(xiao_so_DataStream: DStream[java.util.HashMap[String, String]]) = {

    xiao_so_DataStream.foreachRDD(rdd => {
      val client = ESclientUtil.getClient
      val typeRDD = rdd.map(x => (x.get("infotype"))).distinct().collect()
      typeRDD.foreach(data =>{
        val type_RDD = rdd.map(map => DataConvert.strMap2esObjectMap(map))
        //创建动态索引
        val index = "target_all"
        //创建索引，先判断是不是已经存在了 mapping
        if (!AdminUtil.indexExists(client, index)) {
          val path = "mapping/test.json"
          AdminUtil.buildIndexAndMapping(index, index, path, 3, 1)
        }
        EsSpark.saveToEs(type_RDD, s"${index}/${index}", EsConfigUtil.getEsParams("id"))
      })
    })
  }

}
