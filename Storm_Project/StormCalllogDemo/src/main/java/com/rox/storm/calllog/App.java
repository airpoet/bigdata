package com.rox.storm.calllog;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * App
 */
public class App {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        //设置Spout
        builder.setSpout("spout", new CallLogSpout());
        //设置creator-Bolt
        builder.setBolt("creator-bolt", new CallLogCreatorBolt()).shuffleGrouping("spout");
        //设置counter-Bolt
        builder.setBolt("counter-bolt", new CallLogCounterBolt()).fieldsGrouping("creator-bolt", new Fields("call"));

        Config conf = new Config();
        conf.setDebug(false);

        /**
         * 本地模式storm
         */
        /*LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("LogAnalyserStorm", conf, builder.createTopology());
        Thread.sleep(10000);
        // 停止集群
        cluster.shutdown();*/

        // 运行在集群上
        StormSubmitter.submitTopology("mytop", conf, builder.createTopology());
    }
}
