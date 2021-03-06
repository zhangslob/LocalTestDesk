package com.zongteng.ztetl.etl.dw.fact.full.orders

import com.zongteng.ztetl.common.{Dw_dim_common, Dw_fact_common}
import com.zongteng.ztetl.util.DateUtil

object Dw_fact_order_address_book_full {
  //任务名称(一般同类名)
  private val task = "Dw_fact_order_address_book_full"

  //dw层类名
  private val tableName = "fact_order_address_book"

  // 获取当天的时间
  private val nowDate: String = DateUtil.getNowTime()

  //关联表名称
  private  val relationTable="country"
  //尾表名称
  private  val endTable="order_address_book"

  //一共那些系统
  private  val odsTableHeads=Array(
    "gc_owms_usea",
    "gc_owms_uswe",
    "gc_owms_au",
    "gc_owms_cz",
    "gc_owms_de",
    "gc_owms_es",
    "gc_owms_frvi",
    "gc_owms_it",
    "gc_owms_jp",
    "gc_owms_uk",
    "gc_owms_ukob",
    "gc_owms_usnb",
    "gc_owms_usot",
    "gc_owms_ussc",
    "zy_owms_au",
    "zy_owms_cz",
    "zy_owms_de",
    "zy_owms_ru",
    "zy_owms_uk",
    "zy_owms_usea",
    "zy_owms_uswe",
    "zy_owms_ussc")

  val gc_wms = "select oab.row_wid as row_id" +
    " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
    " ,oab.datasource_num_id as datasource_num_id" +
    " ,oab.data_flag as  data_flag" +
    " ,oab.oab_id as  integration_id" +
    " ,oab.created_on_dt as created_on_dt" +
    " ,oab.changed_on_dt as changed_on_dt" +
    " ,null  as timezone" +
    " ,null  as exchange_rate" +
    " ,cast(concat(oab.datasource_num_id,oab.oab_country_id) as bigint) as country_key" +
    " ,oab.oab_id as oab_id" +
    " ,oab.order_id as oab_order_id" +
    " ,oab.order_code as oab_order_code" +
    " ,oab.oab_firstname  as oab_firstname" +
    " ,oab.oab_lastname" +
    " ,oab.oab_company" +
    " ,oab.oab_country_id" +
    " ,c.country_code as oab_country_code" +
    " ,oab.oab_zone_id" +
    " ,oab.oab_postcode" +
    " ,oab.oab_state" +
    " ,oab.oab_city" +
    " ,oab.oab_suburb" +
    " ,oab.oab_street_address1" +
    " ,oab.oab_street_address2" +
    " ,oab.oab_doorplate" +
    " ,oab.oab_phone" +
    " ,oab.oab_cell_phone" +
    " ,oab.oab_fax" +
    " ,oab.oab_email" +
    " ,oab.oab_note" +
    " ,oab.oab_update_time" +
    " ,oab.address_validate_token oab_address_validate_token" +
    " from (select * from ods.gc_wms_order_address_book where data_flag<>'delete') as oab" +
    s" left join ${Dw_dim_common.getDimSql("gc_wms_country","c")} on oab.oab_country_id=c.country_id "
  val zy_wms = " select oab.row_wid as row_id" +
    " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
    " ,oab.datasource_num_id as datasource_num_id" +
    " ,oab.data_flag as  data_flag" +
    " ,oab.oab_id as  integration_id" +
    " ,oab.created_on_dt as created_on_dt" +
    " ,oab.changed_on_dt as changed_on_dt" +
    " ,null  as timezone" +
    " ,null  as exchange_rate" +
    " ,cast(concat(oab.datasource_num_id,oab.oab_country_id) as bigint) as country_key" +
    " ,oab.oab_id as oab_id" +
    " ,oab.order_id as oab_order_id" +
    " ,oab.order_code as oab_order_code" +
    " ,oab.oab_firstname  as oab_firstname" +
    " ,oab.oab_lastname" +
    " ,oab.oab_company" +
    " ,oab.oab_country_id" +
    " ,c.country_code as oab_country_code" +
    " ,oab.oab_zone_id" +
    " ,oab.oab_postcode" +
    " ,oab.oab_state" +
    " ,oab.oab_city" +
    " ,oab.oab_suburb" +
    " ,oab.oab_street_address1" +
    " ,oab.oab_street_address2" +
    " ,oab.oab_doorplate" +
    " ,oab.oab_phone" +
    " ,oab.oab_cell_phone" +
    " ,oab.oab_fax" +
    " ,oab.oab_email" +
    " ,oab.oab_note" +
    " ,oab.oab_update_time" +
    " ,oab.address_validate_token oab_address_validate_token" +
    " from (select * from ods.zy_wms_order_address_book where data_flag<>'delete') as oab" +
    s" left join ${Dw_dim_common.getDimSql("zy_wms_country","c")} on oab.oab_country_id=c.country_id"

  private val gc_owms_zy_owms =" select oab.row_wid as row_id" +
    " ,from_unixtime(unix_timestamp(),'yyyyMMdd') as etl_proc_wid" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_insert_dt" +
    " ,from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as w_update_dt" +
    " ,oab.datasource_num_id as datasource_num_id" +
    " ,oab.data_flag as  data_flag" +
    " ,oab.oab_id as  integration_id" +
    " ,oab.created_on_dt as created_on_dt" +
    " ,oab.changed_on_dt as changed_on_dt" +
    " ,null  as timezone" +
    " ,null  as exchange_rate" +
    " ,cast(concat(oab.datasource_num_id,c.country_id ) as bigint) as country_key" +
    " ,oab.oab_id as oab_id" +
    " ,oab.order_id as oab_order_id" +
    " ,oab.order_code as oab_order_code" +
    " ,oab.oab_firstname  as oab_firstname" +
    " ,oab.oab_lastname" +
    " ,oab.oab_company" +
    " ,c.country_id  as oab_country_id" +
    " ,oab.oab_country_code  as oab_country_code" +
    " ,oab.oab_zone_id" +
    " ,oab.oab_postcode" +
    " ,oab.oab_state" +
    " ,oab.oab_city" +
    " ,oab.oab_suburb" +
    " ,oab.oab_street_address1" +
    " ,oab.oab_street_address2" +
    " ,oab.oab_doorplate" +
    " ,oab.oab_phone" +
    " ,oab.oab_cell_phone" +
    " ,oab.oab_fax" +
    " ,oab.oab_email" +
    " ,oab.oab_note" +
    " ,oab.oab_update_time" +
    " ,null oab_address_validate_token" +
    " from (select * from ods.EveryOrderAddressBook where data_flag<>'delete') as oab" +
    s" left join ${Dw_dim_common.getDimSql("EveryCountry","c")} on oab.oab_country_code=c.country_code"

  def makeSelectSql()={
    odsTableHeads.map((str:String)=>{
      gc_owms_zy_owms.replace("EveryOrderAddressBook",str.concat("_").concat(endTable))
        .replace("EveryCountry",str.concat("_").concat(relationTable))
    })
  }

  def main(args: Array[String]): Unit = {
    var selectSql: Array[String] = makeSelectSql()
    Dw_fact_common.getRunCode_hive_nopartition_full_Into(task,tableName,gc_wms+:zy_wms+:selectSql)
  }
}
