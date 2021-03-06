package com.zongteng.ztetl.etl.dw.fact.full.storage

import com.zongteng.ztetl.common.{Dw_dim_common, Dw_fact_common, Dw_par_val_list_cache, SystemCodeUtil}
import com.zongteng.ztetl.util.DateUtil

object Dw_fact_take_stock_full {
  //任务名称(一般同类名)
  private val task = "Dw_fact_take_stock_full"

  //dw层类名
  private val tableName = "fact_take_stock"

  // 获取当天的时间
  private val nowDate: String = DateUtil.getNowTime()

  //要执行的sql语句
  private val gc_wms = "select ts.row_wid " +
    " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
    " ,ts.datasource_num_id as datasource_num_id" +
    " ,ts.data_flag as  data_flag" +
    " ,ts.integration_id as  integration_id" +
    " ,ts.created_on_dt as created_on_dt" +
    " ,ts.changed_on_dt as changed_on_dt" +
    " ,case " +
    " when tz.timezone_season_type='winner_time'  then  " +
    "     case when ts.ts_add_time between tz.timezone_season_start and tz.timezone_season_end then tz.timezone_winner_time_dif_val else tz.timezone_summer_time_dif_val end " +
    " when   tz.timezone_season_type='summer_time' then " +
    "     case when ts.ts_add_time between tz.timezone_season_start and tz.timezone_season_end then tz.timezone_summer_time_dif_val else tz.timezone_winner_time_dif_val end " +
    " else null end as timezone" +
    " ,null as exchange_rate" +
    " ,concat(w.datasource_num_id,w.warehouse_id) warehouse_key" +
    " ,wp.row_wid wp_key" +
    " ,ts.ts_id" +
    " ,ts.ts_code" +
    " ,ts.warehouse_id ts_warehouse_id" +
    " ,w.warehouse_code ts_warehouse_code" +
    " ,ts.wp_code ts_wp_code" +
    " ,wp.wp_id ts_wp_id" +
    " ,ts.user_id ts_user_id" +
    " ,ts.ts_last_update_user_id" +
    " ,ts.ts_type" +
    " ,nvl(pvl1.vl_bi_name,ts.ts_type) as ts_type_val" +
    " ,ts.ts_status" +
    " ,nvl(pvl2.vl_bi_name,ts.ts_status) as ts_status_val" +
    " ,ts.ts_add_time " +
    " ,ts.ts_update_time" +
    " ,ts.ts_is_product" +
    " ,nvl(pvl3.vl_bi_name,ts.ts_is_product) as ts_is_product_val" +
    " ,ts.ts_is_quantity" +
    " ,nvl(pvl4.vl_bi_name,ts.ts_is_quantity) as ts_is_quantity_val" +
    " ,ts.ts_user_id ts_ts_user_id" +
    " ,date_format(ts.created_on_dt,'yyyyMM') as month" +
    " from   (select * from ods.gc_wms_take_stock where data_flag<>'delete') as ts" +
    s" left join  ${Dw_dim_common.getDimSql("gc_wms_warehouse","w")} on w.warehouse_id=ts.warehouse_id" +
    s" left join  ${Dw_dim_common.getDimSql("gc_wms_warehouse_physical","wp")} on wp.wp_code=ts.wp_code" +
    " left join dw.par_timezone tz on tz.warehouse_code=w.warehouse_code and  tz.timezone_year=year(ts.ts_add_time)" +
    " left join  (select * from dw.par_val_list as pvl where pvl.datasource_num_id='9004' and pvl.vl_type='ts_type' and pvl.vl_datasource_table='gc_wms_take_stock') " +
    "            as pvl1 on pvl1.vl_value=ts.ts_type" +
    " left join  (select * from dw.par_val_list as pv1 where pv1.datasource_num_id='9004' and pv1.vl_type='ts_status' and pv1.vl_datasource_table='gc_wms_take_stock') " +
    "            as pvl2 on pvl2.vl_value=ts.ts_status" +
    " left join  (select * from dw.par_val_list as pv1 where pv1.datasource_num_id='9004' and pv1.vl_type='ts_is_product' and pv1.vl_datasource_table='gc_wms_take_stock') " +
    "            as pvl3 on pvl3.vl_value=ts.ts_is_product" +
    " left join  (select * from dw.par_val_list as pv1 where pv1.datasource_num_id='9004' and pv1.vl_type='ts_is_quantity' and pv1.vl_datasource_table='gc_wms_take_stock') " +
    "            as pvl4 on pvl4.vl_value=ts.ts_is_quantity"
  private val zy_wms = "select ts.row_wid " +
    " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
    " ,ts.datasource_num_id as datasource_num_id" +
    " ,ts.data_flag as  data_flag" +
    " ,ts.integration_id as  integration_id" +
    " ,ts.created_on_dt as created_on_dt" +
    " ,ts.changed_on_dt as changed_on_dt" +
    " ,case " +
    " when tz.timezone_season_type='winner_time'  then  " +
    "     case when ts.ts_add_time between tz.timezone_season_start and tz.timezone_season_end then tz.timezone_winner_time_dif_val else tz.timezone_summer_time_dif_val end " +
    " when   tz.timezone_season_type='summer_time' then " +
    "     case when ts.ts_add_time between tz.timezone_season_start and tz.timezone_season_end then tz.timezone_summer_time_dif_val else tz.timezone_winner_time_dif_val end " +
    " else null end as timezone" +
    " ,null as exchange_rate" +
    " ,concat(w.datasource_num_id,w.warehouse_id) warehouse_key" +
    " ,null wp_key" +
    " ,ts.ts_id" +
    " ,ts.ts_code" +
    " ,ts.warehouse_id ts_warehouse_id" +
    " ,w.warehouse_code ts_warehouse_code" +
    " ,null ts_wp_code" +
    " ,null ts_wp_id" +
    " ,ts.user_id ts_user_id" +
    " ,ts.ts_last_update_user_id" +
    " ,ts.ts_type" +
    " ,nvl(pvl1.vl_bi_name,ts.ts_type) as ts_type_val" +
    " ,ts.ts_status" +
    " ,nvl(pvl2.vl_bi_name,ts.ts_status) as ts_status_val" +
    " ,ts.ts_add_time " +
    " ,ts.ts_update_time" +
    " ,ts.ts_is_product" +
    " ,nvl(pvl3.vl_bi_name,ts.ts_is_product) as ts_is_product_val" +
    " ,ts.ts_is_quantity" +
    " ,nvl(pvl4.vl_bi_name,ts.ts_is_quantity) as ts_is_quantity_val" +
    " ,ts.ts_user_id ts_ts_user_id" +
    " ,date_format(ts.created_on_dt,'yyyyMM') as month" +
    " from   (select * from ods.zy_wms_take_stock where data_flag<>'delete') as ts" +
    s" left join   ${Dw_dim_common.getDimSql("zy_wms_warehouse","w")} on w.warehouse_id=ts.warehouse_id" +
    " left join dw.par_timezone as tz on tz.warehouse_code=w.warehouse_code and  tz.timezone_year=year(ts.ts_add_time)" +
    " left join  (select * from dw.par_val_list as pvl where pvl.datasource_num_id='9004' and pvl.vl_type='ts_type' and pvl.vl_datasource_table='zy_wms_take_stock') " +
    "            as pvl1 on pvl1.vl_value=ts.ts_type" +
    " left join  (select * from dw.par_val_list as pv1 where pv1.datasource_num_id='9004' and pv1.vl_type='ts_status' and pv1.vl_datasource_table='zy_wms_take_stock') " +
    "            as pvl2 on pvl2.vl_value=ts.ts_status" +
    " left join  (select * from dw.par_val_list as pv1 where pv1.datasource_num_id='9004' and pv1.vl_type='ts_is_product' and pv1.vl_datasource_table='zy_wms_take_stock') " +
    "            as pvl3 on pvl3.vl_value=ts.ts_is_product" +
    " left join  (select * from dw.par_val_list as pv1 where pv1.datasource_num_id='9004' and pv1.vl_type='ts_is_quantity' and pv1.vl_datasource_table='zy_wms_take_stock') " +
    "            as pvl4 on pvl4.vl_value=ts.ts_is_quantity"

  def main(args: Array[String]): Unit = {
    val sqlArray: Array[String] = Array(gc_wms,zy_wms).map(_.replaceAll("dw.par_val_list",Dw_par_val_list_cache.TEMP_PAR_VAL_LIST_NAME))
    Dw_fact_common.getRunCode_hive_full_Into(task,tableName,sqlArray,Array(SystemCodeUtil.GC_WMS,SystemCodeUtil.ZY_WMS))
  }
}
