package com.it.search.sparkStreaming

import com.huaban.analysis.jieba.JiebaSegmenter

import scala.collection.mutable.ListBuffer

/**
 * @description:
 * @author: Delusion
 * @date: 2021-06-08 9:31
 */
object Test {
  def main(args: Array[String]): Unit = {
    val sec = "AACCCDD"
    sec.map(x => {
      (x, 1)
    }).groupBy(_._1).foreach(x =>{
      println((x._1, x._2.size))
    })
  }

}
