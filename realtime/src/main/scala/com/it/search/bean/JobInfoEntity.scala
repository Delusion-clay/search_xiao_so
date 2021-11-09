package com.it.search.bean

import com.alibaba.fastjson.JSON

import scala.beans.BeanProperty

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 15:10
 */
case class JobInfoEntity(
                        @BeanProperty id:String,
                          @BeanProperty `type`: String,
                          @BeanProperty title: String,
                          @BeanProperty url: String,
                          @BeanProperty infomessage: String,
                          @BeanProperty SensitiveWords:String
                        )