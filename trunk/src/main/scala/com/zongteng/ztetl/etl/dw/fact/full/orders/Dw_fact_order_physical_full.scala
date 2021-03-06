package com.zongteng.ztetl.etl.dw.fact.full.orders

import com.zongteng.ztetl.common.{Dw_dim_common, Dw_fact_common}
import com.zongteng.ztetl.util.DateUtil

object Dw_fact_order_physical_full {

  // 任务名称(一般同类名)
  private val task = "Dw_fact_order_physical_full"

  // dw层类名
  private val tableName = "fact_order_physical"

  // 获取当天的时间（yyyyMMdd）
  private val nowDate: String = DateUtil.getNowTime()

  private val gc_wms = "SELECT \n" +
    "    CONCAT(opr.datasource_num_id, opr.opr_id) as row_wid,\n" +
    "    cast(from_unixtime( unix_timestamp( current_date ( ) ), 'yyyyMMdd' ) AS string ) AS etl_proc_wid,\n" +
    "    current_timestamp ( ) as w_insert_dt,\n" +
    "    current_timestamp ( ) as w_update_dt,\n" +
    "    opr.datasource_num_id as datasource_num_id,\n" +
    "    opr.data_flag as data_flag,\n" +
    "    opr.integration_id as integration_id,\n" +
    "    opr.created_on_dt as created_on_dt,\n" +
    "    opr.changed_on_dt as changed_on_dt,\n" +
    "    0 AS timezone ,\n" +
    "    0.00 AS exchange_rate,\n" +
    "    \n" +
    "    CONCAT(opr.datasource_num_id, od.order_id) as order_key,\n" +
    "    CONCAT(opr.datasource_num_id, od.customer_id) as customer_key,\n" +
    "    CONCAT(opr.datasource_num_id, od.warehouse_id) as warehouse_key,\n" +
    "    CONCAT(opr.datasource_num_id, od.to_warehouse_id) as to_warehouse_key,\n" +
    "    CONCAT(opr.datasource_num_id, sm.sm_id) as sm_key,\n" +
    "\n" +
    "     opr.opr_id as opr_id,\n" +
    "     opr.wp_code as opr_wp_code,\n" +
    "     opr.order_code as opr_order_code,\n" +
    "     date_format(od.add_time, 'yyyyMM') as month\n" +
    "FROM (SELECT * FROM ods.gc_wms_order_physical_relation WHERE data_flag != 'DELETE') AS opr\n" +
    "LEFT JOIN (SELECT * FROM ods.gc_wms_orders WHERE data_flag != 'DELETE') AS od ON opr.order_code = od.order_code\n" +
    s"LEFT JOIN ${Dw_dim_common.getDimSql("gc_wms_shipping_method","sm")} ON od.sm_code = sm.sm_code"

  def main(args: Array[String]): Unit = {
    Dw_fact_common.getRunCode_hive_full_Into(task, tableName, Array(gc_wms))
  }

}
