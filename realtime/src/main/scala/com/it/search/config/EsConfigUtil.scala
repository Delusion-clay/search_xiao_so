package com.it.search.config

import org.apache.spark.internal.Logging

object EsConfigUtil extends Serializable {
    def getEsParams(idField:String): Map[String,String] ={
      Map[String,String](
        "es.nodes"->"192.168.2.111",
        "es.port"->"9200",
        "es.clustername"->"apache-hadoop-es",
        "es.mapping.id"->idField
      )
    }
}
