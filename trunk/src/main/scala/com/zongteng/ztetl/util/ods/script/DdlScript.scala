package com.zongteng.ztetl.util.ods.script

// 将mysql的建表语句，直接转成hive的建表语句：维度表
object DdlScript {

  private val surrogateKeys : List[String] = List[String]( // 代理键
    "  row_wid	STRING,",
    "  etl_proc_wid	string,",
    "  w_insert_dt timestamp,",
    "  w_update_dt timestamp,",
    "  datasource_num_id string,",
    "  data_flag string,",
    "  integration_id STRING,",
    "  created_on_dt timestamp,",
    "  changed_on_dt timestamp,"
  )

  private val surrogateKeyHbase : List[String] = List[String]( // 代理键
    ":key,",
    "etl_proc_wid,",
    "w_insert_dt,",
    "w_update_dt,",
    "datasource_num_id,",
    "data_flag,",
    "integration_id,",
    "created_on_dt,",
    "changed_on_dt,"
  )

  // 2、添加drop、create、
  def makeCreateHiveTableSql(ods_table: String, filed: List[String], tableType: String, mysqlFiled: List[String], cf: String) = {

    val shellStart: String = ("#!/bin/bash" +
      "\necho \"ods.table建表开始...\"\n").replace("table", ods_table)

    val dropHql: String = "DROP TABLE IF EXISTS ods.table;".replace("table", ods_table)

    val createHqlStart: String = "CREATE TABLE ods.table (".replace("table", ods_table)

    val createHqlEnd: List[String] = getCreateHqlEndByTableType(tableType, ods_table, mysqlFiled, cf)

    val shellEnd: String = ("hive -e \"$sql\"" +
      "\necho \"ods.table建表成功...\"").replace("table", ods_table)

    val ddlScript = List[String](shellStart, "sql=\"", dropHql, createHqlStart) ::: surrogateKeys ::: filed ::: createHqlEnd ::: shellEnd :: Nil

    ddlScript
  }

  def getCreateHqlEndByTableType(tableType: String, hbase_table: String, mysqlFiled: List[String], cf: String) = {

    if ("dim".equalsIgnoreCase(tableType)) {
      val createHqlEnd = ")\nCOMMENT ''" +
        "\nPARTITIONED BY (day STRING)" +
        "\nSTORED AS PARQUET " +
        "\nTBLPROPERTIES('parquet.compression'='SNAPPY');\n\"\n"

      List[String](createHqlEnd)
    } else {

      val createHqlEnd_1: String = ")\nSTORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'    " +
        "\nWITH SERDEPROPERTIES " +
        "\n('hbase.columns.mapping' = " +
        "\n'"

      val createHqlEnd_3: String = s"')\nTBLPROPERTIES('hbase.table.name' = '$hbase_table');" + "\n\"";

      createHqlEnd_1 :: surrogateKeyHbase.map((sk: String) => {
        if (!":key,".equalsIgnoreCase(sk)) {
          cf + ":" + sk
        } else {
          sk
        }
      }) ::: mysqlFiled.map(cf + ":" + _) ::: createHqlEnd_3 :: Nil
    }
  }

}
