package com.zongteng.ztetl.common

import java.io.File

import com.zongteng.ztetl.api.SparkDataFrame
import com.zongteng.ztetl.util.{HQLFactory, PropertyFile}
import org.apache.spark.sql.SparkSession

import scala.xml.XML

/**
  *
  * linux要分发到每个节点，否则spark集群模式执行任务会找不到资源
  * mkdir -p /ztscript/table/dw
  * sync_file.sh /ztscript/table/dw/
  *
  */
object DwCreateTable {

  // 9个代理键
  val surrogate =
    "          row_wid STRING,\n" +
    "          etl_proc_wid STRING,\n" +
    "          w_insert_dt TIMESTAMP,\n" +
    "          w_update_dt TIMESTAMP,\n" +
    "          datasource_num_id STRING,\n" +
    "          data_flag STRING,\n" +
    "          integration_id STRING,\n" +
    "          created_on_dt TIMESTAMP,\n" +
    "          changed_on_dt TIMESTAMP,\n"
  // 事实表才加上
  val surrogate2 =
    "          timezone int,\n" +
    "          exchange_rate decimal(18,4),"

  // 允许的表
  private val tables = Array(
    //维度表(par开头的配置表手动执行吧,par_currency_rate,par_search_filter,par_tcms_work_groupmember)
    "dim_country",
    "dim_warehouse",
    "dim_warehouse_physical",
    "dim_warehouse_area",
    "dim_warehouse_location",
    "dim_customer",
    "dim_employee",
    "dim_employee_department",
    "dim_employee_position",
    "dim_server",
    "dim_server_channel",
    "dim_product",
    "dim_shipping_method",
    "dim_zone",
    "dim_zone_scheme",
    "dim_currency",
    "dim_fee_type",
    "dim_tcms_city",
    "dim_tcms_country",
    "dim_tcms_customer",
    "dim_tcms_employee",
    "dim_tcms_server",
    "dim_tcms_server_channel",
    "dim_tcms_work_group",
    "dim_tcms_product",
    "dim_tcms_zone",
    "dim_tcms_zone_scheme",
    "dim_tcms_organization",
    "dim_tcms_currency",
    "dim_tcms_fee_type",
    "dim_tcms_issue_type",
    //仓储(storage,9张),
    "fact_inventory_batch",
    "fact_product_inventory",
    "fact_take_stock",
    "fact_take_stock_assignment",
    "fact_take_stock_item",
    "fact_flow_volume",
    "fact_inventory_difference",
    "fact_inventory_difference_detail",
    "fact_inventory_batch_log",
    //入库单(receiving,9张),
    "fact_receiving_box",
    "fact_receiving",
    "fact_receiving_batch",
    "fact_receiving_log",
    "fact_putaway",
    "fact_putaway_detail",
    "fact_receiving_detail",
    "fact_receiving_box_detail",
    "fact_quality_control",
    //订单(orders,10张),
    "fact_order_product",
    "fact_order_physical",
    "fact_order_product_physical",
    "fact_oversea_abnormal_order",
    "fact_orders",
    "fact_oms_orders",
    "fact_order_log",
    "fact_order_operation_time",
    "fact_ship_order",
    "fact_order_address_book",
    //出库(picking,8张),
    "fact_wellen_sc",
    "fact_advance_picking_detail",
    "fact_wellen_area",
    "fact_wellen_log",
    "fact_picking_physical",
    "fact_picking",
    "fact_picking_detail",
    "fact_wellen_rule"
  ).distinct


  def createTable(SparkSession: SparkSession, tableName: String) = {

    // 临时方案：等dw层全部表的建表语句迁移过来，要去掉
    if (tables.contains(tableName)) {

      // 删除
      SparkDataFrame.dropTable(SparkSession, "dw", tableName)

      // 创建
      val createSql: String = getHQL(tableName)
      SparkSession.sql(createSql)
    }

  }

  /**
    * 获取 CREATE TABLE SQL
    */
  def getHQL(tableName: String) :String = {

    try{

      val hqls: List[String] = getFileNames().map(x => {

        val xmlString = XML.loadFile(x);

        // 把xml文件所有匹配的，拼接成为一个字符串返回
        val hsql: String = (xmlString \\ "hql").
          filter(x => ((x \ "@schema").text) == "dw").
          filter(x => ((x \ "@tableName").text) == tableName).text

        hsql
      }).filter(!"".equals(_)).map(x => {
        var sql: String = ""
        if (tableName.startsWith("dim")) {
          sql = x.replaceFirst("\\(", "(\n" +surrogate)
        } else  if (tableName.startsWith("fact")) {
          sql = x.replaceFirst("\\(", "(\n" + (surrogate + surrogate2))
        }
        sql.replaceAll(";", "")
      })

      assert(hqls.size <= 1, s"表名（${tableName}）在xml文件命名重复，请检查")
      assert(hqls.size == 1, s"表名（${tableName}）在xml文件找不到创建表的语句，请检查")

      if (hqls.size == 1) hqls(0) else ""
    }catch{
      case ex: Exception => throw ex
    }
  }

  /**
    * 获取table/dw下所有的建表xml文件绝对路径
    * @return
    */
  def getFileNames() = {
    val path: String = getXmlPath()

    val file = new File(path)

    file.listFiles().filterNot(x => "path.txt".equals(x.getName)).map(_.getAbsolutePath).toList
  }


  def getXmlPath() = {
    // LINUX
    PropertyFile.getProperty("hiveTable")

    // WINDOWS
    //getClass.getClassLoader.getResource("table/dw").getPath
  }

  def main(args: Array[String]): Unit = {
    println(getHQL("dim_city"))
  }

}
