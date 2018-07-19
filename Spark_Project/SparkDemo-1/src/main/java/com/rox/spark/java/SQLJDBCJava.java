package com.rox.spark.java;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class SQLJDBCJava {

    public static void main(String[] args) {

//        SparkConf conf = new SparkConf();

        /*
         * 这里相当于同时设置了 SparkConf 和 SparkSession
         */
        SparkSession sess = SparkSession.builder()
                .appName("SQLJava")
                .config("spark.master", "local")
                .getOrCreate();

        String url = "jdbc:mysql://cs2:3306/test";
        String tname = "student";

        // 查询数据库
        Dataset<Row> jdbcDF = sess.read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", tname)
                .option("user", "root")
                .option("password", "123")
                .option("driver","com.mysql.jdbc.Driver")
                .load();
        jdbcDF.show();

        // 投影查询
        Dataset<Row> df2 = jdbcDF.select(new Column("score"),new Column("name"));
        // distinct() 去重
        df2 = df2.where("name like 'xu%'").distinct();

        // Saving data to a JDBC source  (当然还可以用 上面的方式, 把read()改为 write 就行)
        Properties prop = new Properties();
        prop.put("user", "root");
        prop.put("password", "123");
        prop.put("driver", "com.mysql.jdbc.Driver");

        df2.write().jdbc(url,"sub_stu",prop);
        df2.show();

    }
}