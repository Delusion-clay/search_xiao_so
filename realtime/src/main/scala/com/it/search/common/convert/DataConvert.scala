package com.it.search.common.convert

import java.util

import com.it.search.convert.BaseDataConvert
import com.it.search.utils.ConfigUtil
import org.apache.spark.internal.Logging

import scala.collection.JavaConversions._

/**
  * description: RDD数据类型转换，将map[String,String]转为map[string,Object]
  * 转换需要按照mapping格式进行
  */
object DataConvert extends Serializable with Logging {

  //转换
  def strMap2esObjectMap(map: java.util.Map[String, String]): java.util.Map[String, Object] = {
    val objectMap = new java.util.HashMap[String, Object]

    //获取真实数据的所有key
    val iterator = map.keySet().iterator()
    while (iterator.hasNext){
      val field = iterator.next()
      println(field)
      //通过字段获取类型 string
      val dataType = "string"
      dataType match {
        case "long"=> BaseDataConvert.mapString2Long(map,field,objectMap)
        case "string"=> BaseDataConvert.mapString2String(map,field,objectMap)
        case "double"=> BaseDataConvert.mapString2Double(map,field,objectMap)
        case  _=> BaseDataConvert.mapString2String(map,field,objectMap)
      }
    }
    objectMap
  }
}
