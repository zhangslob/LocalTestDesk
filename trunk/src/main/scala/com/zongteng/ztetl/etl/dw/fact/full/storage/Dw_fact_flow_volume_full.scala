package com.zongteng.ztetl.etl.dw.fact.full.storage

import com.zongteng.ztetl.common.{Dw_dim_common, Dw_fact_common, Dw_par_val_list_cache, SystemCodeUtil}
import com.zongteng.ztetl.util.DateUtil

object Dw_fact_flow_volume_full {
  //任务名称(一般同类名)
  private val task = "Dw_fact_flow_volume_full"

  //dw层类名
  private val tableName = "fact_flow_volume"

  // 获取当天的时间
  private val nowDate: String = DateUtil.getNowTime()

  //要执行的sql语句
  private val gc_wms = "select fl.row_wid " +
    " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
    " ,fl.datasource_num_id as datasource_num_id" +
    " ,fl.data_flag as  data_flag" +
    " ,fl.integration_id as  integration_id" +
    " ,fl.created_on_dt as created_on_dt" +
    " ,fl.changed_on_dt as changed_on_dt" +
    " ,case " +
    " when tz.timezone_season_type='winner_time'  then  " +
    "     case when fl.fv_add_time between tz.timezone_season_start and tz.timezone_season_end then tz.timezone_winner_time_dif_val else tz.timezone_summer_time_dif_val end " +
    " when   tz.timezone_season_type='summer_time' then " +
    "     case when fl.fv_add_time between tz.timezone_season_start and tz.timezone_season_end then tz.timezone_summer_time_dif_val else tz.timezone_winner_time_dif_val end " +
    " else null end as timezone" +
    " ,null as exchange_rate" +
    " ,concat(fl.datasource_num_id,fl.warehouse_id) warehouse_key" +
    " ,concat(fl.datasource_num_id,fl.product_id) product_key" +
    " ,c.row_wid customer_key" +
    " ,wp.row_wid wp_key" +
    " ,fl.fv_id as fv_id" +
    " ,fl.product_id fv_product_id" +
    " ,fl.product_barcode fv_product_barcode" +
    " ,c.customer_id  fv_customer_id " +
    " ,fl.customer_code fv_customer_code" +
    " ,fl.warehouse_id fv_warehouse_id" +
    " ,fl.warehouse_code fv_warehouse_code" +
    " ,wp.wp_id fv_wp_id" +
    " ,fl.wp_code fv_wp_code" +
    " ,fl.fv_pending_quantity" +
    " ,fl.fv_quantity" +
    " ,fl.fv_adjustment_lock" +
    " ,nvl(pvl1.vl_bi_name,fl.fv_adjustment_lock) fv_adjustment_lock_val" +
    " ,fl.fv_add_time" +
    " ,fl.fv_update_time" +
    " ,fl.fv_processing_priority" +
    " ,nvl(pvl2.vl_bi_name,fl.fv_processing_priority) fv_processing_priority_val" +
    " ,date_format(fl.created_on_dt,'yyyyMM') as month" +
    " from  (select * from ods.gc_wms_flow_volume where  data_flag<>'delete') fl" +
    s" left join ${Dw_dim_common.getDimSql("gc_wms_warehouse_physical","wp")} on wp.wp_code=fl.wp_code " +
    s" left join ${Dw_dim_common.getDimSql("gc_wms_customer","c")} on c.customer_code=fl.customer_code " +
    " left join dw.par_timezone tz on tz.warehouse_code=fl.warehouse_code and  tz.timezone_year=year(fl.fv_add_time)" +
    " left join  (select * from dw.par_val_list as pvl where pvl.datasource_num_id='9004' and pvl.vl_type='fv_adjustment_lock' and pvl.vl_datasource_table='gc_wms_flow_volume') " +
    "            as pvl1 on pvl1.vl_value=fl.fv_adjustment_lock" +
    " left join  (select * from dw.par_val_list as pvl where pvl.datasource_num_id='9004' and pvl.vl_type='fv_processing_priority' and pvl.vl_datasource_table='gc_wms_flow_volume') " +
    "            as pvl2 on pvl2.vl_value=fl.fv_processing_priority"

  def main(args: Array[String]): Unit = {
    val sqlArray: Array[String] = Array(gc_wms).map(_.replaceAll("dw.par_val_list",Dw_par_val_list_cache.TEMP_PAR_VAL_LIST_NAME))
    Dw_fact_common.getRunCode_hive_full_Into(task,tableName,sqlArray,Array(SystemCodeUtil.GC_WMS))
  }
}

