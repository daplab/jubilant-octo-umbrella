package ch.daplab.stream_processing.flink;

import ch.daplab.config.Config;
import ch.daplab.jubilantoctoumbrella.model.Transaction;
import ch.daplab.yarn.AbstractKafkaWithTopicAppLauncher;
import ch.daplab.yarn.AbstractZKAppLauncher;
import ch.daplab.yarn.AbstractZkAndKafkaAndTopicAppLauncher;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import java.util.Properties;

/**
 * Created by bperroud on 29/09/16.
 */
public class RunFlink extends AbstractZkAndKafkaAndTopicAppLauncher {


    public static final String OPTION_GROUP_ID = "group.id";

    public static final String DEFAULT_GROUPE_ID = "CC-Generator";

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new RunFlink(), args);
        System.exit(res);
    }

    static String topic = Config.topic;


    @Override
    protected int internalRunWithZkAndKafkaAndTopic(String zkConnect, String topic, String brokerList) throws Exception {

        String groupId = (String)getOptions().valueOf(OPTION_GROUP_ID);
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", brokerList);
        properties.setProperty("zookeeper.connect", zkConnect);
        properties.setProperty("group.id", groupId);

        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStream<Transaction> dataStream = streamExecutionEnvironment.addSource(new FlinkKafkaConsumer<>(
                topic,
                new TransactionDeserializer(),
                properties,
                FlinkKafkaConsumer.OffsetStore.KAFKA,
                FlinkKafkaConsumer.FetcherType.NEW_HIGH_LEVEL));


        dataStream.rebalance()
                .map(t ->
                     String.format("%s %s=%f", t.getName())
                )
                .addSink(new SinkFunction<String>() {
                    @Override
                    public void invoke(String value) throws Exception {
                        System.out.println(value);
                    }
                });

        return ReturnCode.ALL_GOOD;
    }
}
