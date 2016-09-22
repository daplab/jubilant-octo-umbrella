package ch.daplab.kafka;

import info.batey.kafka.unit.KafkaUnit;
import kafka.utils.*;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;

/**
 * Created by bperroud on 31-Mar-15.
 */
public abstract class SetupSimpleKafkaCluster {

    protected int zkConnectionTimeout = 6000;
    protected int zkSessionTimeout = 6000;

    protected String zkConnect;
    protected String kafkaConnect;
    KafkaUnit kafkaUnitServer;
    protected ZkClient zkClient;

    protected CuratorFramework curatorFramework;

    @Before
    public void setup() throws Exception {

        kafkaUnitServer = new KafkaUnit();
        kafkaUnitServer.startup();

        // setup Zookeeper
        zkConnect = "localhost:" + kafkaUnitServer.getZkPort();
        zkClient = new ZkClient(zkConnect, zkConnectionTimeout, zkSessionTimeout, ZKStringSerializer$.MODULE$);

        // setup Broker
        kafkaConnect = kafkaUnitServer.getKafkaConnect();

        curatorFramework = CuratorFrameworkFactory.builder().connectString(zkConnect)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(zkConnectionTimeout).sessionTimeoutMs(zkSessionTimeout)
                .build();
        curatorFramework.start();
    }

    @After
    public void tearDown() {

        if (curatorFramework != null) {
            curatorFramework.close();
        }

        if (zkClient != null) {
            zkClient.close();
        }

        if (kafkaUnitServer != null) {
            kafkaUnitServer.shutdown();
        }

    }

}
