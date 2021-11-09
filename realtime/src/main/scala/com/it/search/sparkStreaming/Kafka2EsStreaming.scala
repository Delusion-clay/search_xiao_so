package com.it.search.sparkStreaming

import java.util.Timer

import com.it.search.common.SscFactory
import com.it.search.kafka.config.KafkaConfig
import com.it.search.process.{ESAll, SensitiveWordFilteringProcess, Spark2es}
import com.it.search.timer.SyncRule2RedisTimer
import com.it.search.utils.KafkaSparkUtil
import org.apache.spark.internal.Logging
import org.apache.spark.storage.StorageLevel

/**
 * @description:
 * @author: Delusion
 * @date: 2021-06-01 8:25
 */
object Kafka2EsStreaming extends Serializable with Logging {
  def main(args: Array[String]): Unit = {
    val timer = new Timer
    timer.schedule(new SyncRule2RedisTimer, 0, 1 * 3 * 1000)

    val topic = "xiao_so_succ"
    val groupId = "Kafka2esStreaming"
    val ssc = SscFactory.newLocalSSC("Kafka2esStreaming", 3L)
    val kafkaParams = KafkaConfig.getKafkaConfig(groupId)
    val kafkaSparkUtil = new KafkaSparkUtil(true)
    val resultDS = kafkaSparkUtil.getMapDSwithOffset(ssc,
      kafkaParams.asInstanceOf[java.util.Map[String, String]], groupId, topic).map(x => {
      var infotype = x.get("type")
      infotype match {
        case "百家号" => infotype = "baijiahao"
        case "头条" => infotype = "toutiao"
        case "水木" => infotype = "shuimu"
        case "知乎" => infotype = "zhihu"
        case "虎扑" => infotype = "hupu"
        case "抽屉" => infotype = "chouti"

        case "抖音" => infotype = "douyin"

        case "微博" => infotype = "weibo"
        case "36氪" => infotype = "36ke"
        case "澎湃" => infotype = "pengpai"
        case "观察者" => infotype = "guanchazhe"
        case "新京报" => infotype = "xinjingbao"
        case "新浪" => infotype = "xinlang"
        case "百度" => infotype = "baidu"
        case "哔哩" => infotype = "bili"
        case "公众号" => infotype = "gongzhonghao"
      }
      x.put("infotype",infotype)
      x
    })
      .persist(StorageLevel.MEMORY_AND_DISK)
    //resultDS.print()
    SensitiveWordFilteringProcess.process(resultDS)
    Spark2es.process(resultDS)
    ESAll.process(resultDS)

    ssc.start()
    ssc.awaitTermination()

  }

}
