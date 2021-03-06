package com.zongteng.ztetl.etl.dw.fact.full.receiving

import com.zongteng.ztetl.common.{Dw_dim_common, Dw_fact_common, Dw_par_val_list_cache, SystemCodeUtil}
import com.zongteng.ztetl.util.DateUtil

object Dw_fact_receiving_batch_full {

  // 任务名称(一般同类名)
  private val task = "Dw_fact_receiving_batch_full"

  // dw层类名
  private val tableName = "fact_receiving_batch"

  // 获取当天的时间
  private val nowDate: String = DateUtil.getNowTime()

  val gc_wms = "SELECT \n" +
    " rcb.row_wid AS row_wid,\n" +
    " cast(from_unixtime( unix_timestamp( current_date ( ) ), 'yyyyMMdd' ) AS string ) AS etl_proc_wid,\n" +
    " current_timestamp ( ) AS w_insert_dt,\n" +
    " current_timestamp ( ) AS w_update_dt,\n" +
    " rcb.datasource_num_id AS datasource_num_id,\n" +
    " rcb.data_flag AS data_flag,\n" +
    " rcb.rbl_id AS integration_id,\n" +
    " rcb.created_on_dt AS created_on_dt,\n" +
    " rcb.changed_on_dt AS changed_on_dt,\n" +
    " 0 AS timezone,\n" +
    " 0.00 exchange_rate,\n" +
    "\n" +
    " null AS sm_key,\n" +
    "    concat(rcb.datasource_num_id, wa.warehouse_id) AS warehouse_key,\n" +
    "    null AS transit_warehouse_key,\n" +
    "    concat(rc.datasource_num_id, cu.customer_id) as customer_key,\n" +
    "    null AS to_warehouse_key,\n" +
    " concat(rcb.datasource_num_id, rc.receiving_id) AS receiving_key,\n" +
    " concat(rcb.datasource_num_id, pd.product_id) AS product_key,\n" +
    "\n" +
    " rcb.rbl_id AS rcb_rdb_id,\n" +
    " rc.receiving_id AS rcb_receiving_id,\n" +
    " null AS rcb_qc_code,\n" +
    " rcb.receiving_code AS rcb_receiving_code,\n" +
    " null AS rcb_receiving_line_no,\n" +
    " rcb.product_barcode AS rcb_product_barcode,\n" +
    " pd.product_id AS rcb_product_id,\n" +
    " null AS rcb_rdb_weight,\n" +
    " null AS rcb_rdb_putaway_qty,\n" +
    " rcb.received_qty AS rcb_rdb_received_qty,\n" +
    " null AS rcb_packaged,\n" +
    " null AS rcb_non_packaged_qty,\n" +
    " null AS rcb_labeled,\n" +
    " null AS rcb_non_labeled_qty,\n" +
    " null AS rcb_rdb_note,\n" +
    " null AS rcb_receiving_user_id,\n" +
    " null AS rcb_rdb_add_time,\n" +
    " null AS rcb_rdb_update_time, \n" +
    "\n" +
    " rcb.box_no AS rcb_box_no,\n" +
    " rcb.batch AS rcb_batch,\n" +
    " rcb.received_time AS rcb_received_time,\n" +
    " rcb.received_user_name AS rcb_received_user_name,\n" +
    " rcb.received_user_code AS rcb_received_user_code,\n" +
    " rcb.product_real_length AS rcb_product_real_length,\n" +
    " rcb.product_real_width AS rcb_product_real_width,\n" +
    " rcb.product_real_height AS rcb_product_real_height,\n" +
    " rcb.product_real_weight AS rcb_product_real_weight,\n" +
    " date_format(rc.receiving_add_time, 'yyyyMM') AS month\n" +
    "FROM (SELECT * FROM ods.gc_wms_gc_receiving_batch WHERE data_flag != 'DELETE') rcb\n" +
    "LEFT JOIN (SELECT * FROM ods.gc_wms_gc_receiving  WHERE data_flag != 'DELETE') rc ON rcb.receiving_code = rc.receiving_code\n" +
    s"LEFT JOIN ${Dw_dim_common.getDimSql("gc_wms_warehouse","wa")} ON wa.warehouse_code = rc.warehouse_code\n" +
    s"LEFT JOIN ${Dw_dim_common.getDimSql("gc_wms_customer","cu")} ON cu.customer_code = rc.customer_code\n" +
    s"LEFT JOIN ${Dw_dim_common.getDimSql("gc_wms_product","pd")} ON rcb.product_barcode = pd.product_barcode"

  val zy_wms = "SELECT \n" +
    " rdb.row_wid AS row_wid,\n" +
    " cast(from_unixtime( unix_timestamp( current_date ( ) ), 'yyyyMMdd' ) AS string ) AS etl_proc_wid,\n" +
    " current_timestamp ( ) AS w_insert_dt,\n" +
    " current_timestamp ( ) AS w_update_dt,\n" +
    " rdb.datasource_num_id AS datasource_num_id,\n" +
    " rdb.data_flag AS data_flag,\n" +
    " nvl(rdb.rdb_id, 0) AS integration_id,\n" +
    " rdb.created_on_dt AS created_on_dt,\n" +
    " rdb.changed_on_dt AS changed_on_dt,\n" +
    " 0 AS timezone,\n" +
    " 0.00 exchange_rate,\n" +
    "\n" +
    " concat(rc.datasource_num_id, sm.sm_id) AS sm_key,\n" +
    " concat(rc.datasource_num_id, rc.warehouse_id) AS warehouse_key,\n" +
    " concat(rc.datasource_num_id, rc.transit_warehouse_id) AS transit_warehouse_key,\n" +
    " concat(rc.datasource_num_id, rc.customer_id) AS customer_key,\n" +
    " concat(rc.datasource_num_id, rc.to_warehouse_id) AS to_warehouse_key,\n" +
    " concat(rdb.datasource_num_id, rdb.receiving_id) AS receiving_key,\n" +
    " concat(rdb.datasource_num_id, rdb.product_id) AS product_key,\n" +
    "\n" +
    " rdb.rdb_id AS rcb_rdb_id,\n" +
    " rdb.receiving_id AS rcb_receiving_id,\n" +
    " rdb.qc_code AS rcb_qc_code,\n" +
    " rdb.receiving_code AS rcb_receiving_code,\n" +
    " rdb.receiving_line_no AS rcb_receiving_line_no,\n" +
    " rdb.product_barcode AS rcb_product_barcode,\n" +
    " rdb.product_id AS rcb_product_id,\n" +
    " rdb.rdb_weight AS rcb_rdb_weight,\n" +
    " rdb.rdb_putaway_qty AS rcb_rdb_putaway_qty,\n" +
    " rdb.rdb_received_qty AS rcb_rdb_received_qty,\n" +
    " rdb.packaged AS rcb_packaged,\n" +
    " rdb.non_packaged_qty AS rcb_non_packaged_qty,\n" +
    " rdb.labeled AS rcb_labeled,\n" +
    " rdb.non_labeled_qty AS rcb_non_labeled_qty,\n" +
    " rdb.rdb_note AS rcb_rdb_note,\n" +
    " rdb.receiving_user_id AS rcb_receiving_user_id,\n" +
    " rdb.rdb_add_time AS rcb_rdb_add_time,\n" +
    " rdb.rdb_update_time AS rcb_rdb_update_time,\n" +
    " null AS rcb_box_no,\n" +
    " null  AS rcb_batch,\n" +
    " null AS rcb_received_time,\n" +
    " null AS rcb_received_user_name,\n" +
    " null AS rcb_received_user_code,\n" +
    " null AS rcb_product_real_length,\n" +
    " null AS rcb_product_real_width,\n" +
    " null AS rcb_product_real_height,\n" +
    " null AS rcb_product_real_weight,\n" +
    " date_format(rdb.rdb_add_time, 'yyyyMM') AS month\n" +
    "FROM (SELECT * FROM ods.zy_wms_receiving_detail_batch WHERE data_flag != 'DELETE') AS rdb\n" +
    "LEFT JOIN  (SELECT * FROM ods.zy_wms_receiving WHERE data_flag != 'DELETE') AS rc ON rdb.receiving_id = rc.receiving_id\n" +
    s"LEFT JOIN ${Dw_dim_common.getDimSql("zy_wms_shipping_method","sm")} ON sm.sm_code = rc.sm_code "

  def main(args: Array[String]): Unit = {

    Dw_fact_common.getRunCode_hive_full_Into(task, tableName, Array(gc_wms, zy_wms), Dw_par_val_list_cache.EMPTY_PAR_VAL_LIST)
  }
}
