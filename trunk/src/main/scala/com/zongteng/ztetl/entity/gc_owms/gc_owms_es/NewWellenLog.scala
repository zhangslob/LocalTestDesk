package com.zongteng.ztetl.entity.gc_owms.gc_owms_es

case class NewWellenLog (
    nwl_id: String,
    wellen_id: String,
    user_id: String,
    `type`: String,
    note: String,
    create_time: String
)