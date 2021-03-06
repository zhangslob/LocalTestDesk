package com.zongteng.ztetl.entity.zy_wms

case class ProductInventory (
                              pi_id:String,
                              product_barcode:String,
                              customer_id:String,
                              customer_code:String,
                              product_id:String,
                              warehouse_id:String,
                              pi_planned:String,
                              pi_onway:String,
                              pi_pending:String,
                              pi_sellable:String,
                              pi_unsellable:String,
                              pi_reserved:String,
                              pi_outbound:String,
                              pi_shipped:String,
                              pi_hold:String,
                              pi_no_stock:String,
                              pi_warning_qty:String,
                              pi_shared:String,
                              pi_sold_shared:String,
                              buyer_id:String,
                              pi_add_time:String,
                              pi_update_time:String,
                              pi_stocking:String,
                              pi_tune_out:String,
                              pi_tune_in:String
                            )
