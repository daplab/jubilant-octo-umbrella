package ch.daplab.stream_processing.kafka_streams;

import ch.daplab.yarn.AbstractZkAndKafkaAndTopicAppLauncher;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import java.util.Properties;

public class RunKafkaStreams extends AbstractZkAndKafkaAndTopicAppLauncher {

    public static final String OPTION_GROUP_ID = "group.id";

    public static final String DEFAULT_GROUPE_ID = "CC-Generator-Kafka-Streams";

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new RunKafkaStreams(), args);
        System.exit(res);
    }

    @Override
    protected int internalRunWithZkAndKafkaAndTopic(
            String zkConnect, String topic, String brokerList) throws Exception {

        String groupId = (String)getOptions().valueOf(OPTION_GROUP_ID);
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", brokerList);
        properties.setProperty("zookeeper.connect", zkConnect);
        properties.setProperty("group.id", groupId);


        // Kafka Streams



        return ReturnCode.ALL_GOOD;
    }

    @Override
    protected void initParser() {

        super.initParser();

        getParser().accepts(OPTION_GROUP_ID, "Kafka group id to use.")
                .withRequiredArg().defaultsTo(DEFAULT_GROUPE_ID);

    }

}
