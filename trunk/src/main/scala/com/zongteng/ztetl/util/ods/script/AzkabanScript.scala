package com.zongteng.ztetl.util.ods.script

import java.io.FileOutputStream

import scala.collection.mutable.ListBuffer

object AzkabanScript {

  val linux_base_path = "/azkaban_scheduling/theme_picking/ods"
  val linux_ddl_path = s"${linux_base_path}/fact_ddl"   // linux系统上ddl脚本存储的路径
  val linux_sqoop_path = s"${linux_base_path}/fact_sqoop" // linux系统上sqoop脚本存储的路径

  var ddlJods = ListBuffer[String]();     // ddlAzkaban脚本集合
  var sqoopJods =  ListBuffer[String](); // sqoopAzkaban脚本集合

  /**
    *
    * 将azkaban调度job脚本（ddl.job，sqoop.job），输出到本地
    * azkaban_path 脚本存放路径
    * ods_table ods层表的名字
    * @param azkaban_path
    * @param ods_table
    */
  def outputAzkabanScript(azkaban_path: String, ods_table: String): Unit = {

    ddlJods += (s"ddl_${ods_table}")
    sqoopJods += (s"sqoop_ods_${ods_table}_full")

    val ddl_content = "type=command" +
      s"\ncommand=sh ${linux_ddl_path}/ddl_${ods_table}.sh"

    val sqoop_content =  "type=command" +
      s"\ncommand=sh ${linux_sqoop_path}/sqoop_ods_${ods_table}_full.sh" +
      "\nretries=2" +
      "\nretry.backoff=300000"

    val ddl_azkaban_path = s"${azkaban_path}\\ddl_${ods_table}.job"
    val sqoop_azkaban_path = s"${azkaban_path}\\sqoop_ods_${ods_table}_full.job"

    println("ddl_azkaban_path路径：" + ddl_azkaban_path)
    println("sqoop_azkaban_path路径：" + sqoop_azkaban_path)

    var ddlStream: FileOutputStream = null
    var sqoopStream: FileOutputStream = null
    try {
      ddlStream = new FileOutputStream(ddl_azkaban_path)
      sqoopStream = new FileOutputStream(sqoop_azkaban_path)

      ddlStream.write(ddl_content.getBytes())
      sqoopStream.write(sqoop_content.getBytes())
    } catch {
      case e: Exception => println("sqoop_io异常")
    } finally {
      ddlStream.close()
      sqoopStream.close()
    }

    endOdsDdl(azkaban_path)
    middleOdsDw(azkaban_path)
  }

  /**
    * 所有的ddl执行完毕之后，在执行endDdl
    *
    */
  def endOdsDdl(azkaban_path: String) = {

    val end_ddl_content = "type=command" +
      s"\ndependencies=${ddlJods.toArray.mkString(",")}" +
      "\ncommand=echo 'ddl建表成功'"
      "\ncommand.1= sleep 5"

    val end_ddl_path = s"${azkaban_path}\\create_table.job"

    var stream: FileOutputStream = null
    try {
      stream = new FileOutputStream(end_ddl_path)
      stream.write(end_ddl_content.getBytes())
    } catch {
      case e: Exception => println("sqoop_io异常")
    } finally {
      stream.close()
    }

  }

  def middleOdsDw(azkaban_path: String) = {

    val middle_ods_dw_content = "type=command" +
      s"\ndependencies=${sqoopJods.toArray.mkString(",")}" +
      "\ncommand=echo 'sqoop导入数据成功'" +
      "\ncommand= sleep 5"

    val middle_ods_dw_path = s"${azkaban_path}\\end_sqoop.job"

    var stream: FileOutputStream = null
    try {
      stream = new FileOutputStream(middle_ods_dw_path)
      stream.write(middle_ods_dw_content.getBytes())
    } catch {
      case e: Exception => println("sqoop_io异常")
    } finally {
      stream.close()
    }
  }





}
