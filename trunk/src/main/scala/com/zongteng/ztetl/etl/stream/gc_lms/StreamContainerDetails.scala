package com.zongteng.ztetl.etl.stream.gc_lms

import com.zongteng.ztetl.common.MysqlDatabase

object StreamContainerDetails {

  def main(args: Array[String]): Unit = {

    // 作业名称
    val appName = "Stream_Container_Details"

    // 消费时间间隔
    val interval = 5

    // kafka中topic名称
    val topic_name = "gc_owms"

    // 消费组
    val group_Id = "consumer_Stream_Container_Details_Ta2"

    // mysql数据库名称（数据的来源）
    val mysqlDatabases = Array(MysqlDatabase.GC_LMS_AU,
                               MysqlDatabase.GC_LMS_CZ,
                               MysqlDatabase.GC_LMS_ES,
                               MysqlDatabase.GC_LMS_FRVI,
                               MysqlDatabase.GC_LMS_IT,
                               MysqlDatabase.GC_LMS_UK,
                               MysqlDatabase.GC_LMS_USWE,
                               MysqlDatabase.GC_LMS_USEA,
                               MysqlDatabase.GC_LMS_USSC
    )


    StreamContainerDetailsCodeUtil.getRunCode(appName, interval, Array(topic_name), group_Id, mysqlDatabases)
  }


}
