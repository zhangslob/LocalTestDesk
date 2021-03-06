package com.zongteng.ztetl.etl.dw.dim.all

import com.zongteng.ztetl.common.Dw_dim_common
import com.zongteng.ztetl.util.DateUtil

object DimTcmsZoneScheme {
  //任务名称(一般同类名)
  private val task = "DimTcmsZoneScheme"

  //dw层类名
  private val tableName = "dim_tcms_zone_scheme"

  //获取当天的时间
  private val nowDate: String = DateUtil.getNowTime()

  //要执行的sql语句
    private val gc_tcms="SELECT zs.row_wid as row_wid" +
      " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
      " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
      " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
      " ,zs.datasource_num_id as datasource_num_id" +
      " ,zs.data_flag as data_flag" +
      " ,zs.integration_id as integration_id" +
      " ,zs.created_on_dt as created_on_dt" +
      " ,zs.changed_on_dt as changed_on_dt" +
      " ,zs.zs_id as zs_id" +
      " ,zs.zs_name as zs_name" +
      " ,zs.ss_code as zs_ss_code" +
      " ,zs.pg_code as zs_pg_code" +
      " ,zs.sn_id as zs_sn_id" +
      " ,zs.st_id_create as zs_st_id_create" +
      " ,zs.zs_createdate as zs_createdate" +
      " ,zs.st_id_modify as zs_st_id_modify" +
      " ,zs.zs_modifydate as zs_modifydate" +
      " ,zs.zs_note as zs_note" +
      " ,zs.tms_id as zs_tms_id" +
      " ,zs.last_update_time as zs_last_update_time" +
      " FROM (select * from ods.gc_tcms_xtd_zone_scheme where day="+ nowDate +" ) as zs"

  def main(args: Array[String]): Unit = {
    Dw_dim_common.getRunCode_full(task, tableName, Array(gc_tcms))
  }
}
