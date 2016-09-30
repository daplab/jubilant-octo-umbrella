package ch.daplab.stream_processing.flink;

import ch.daplab.config.Config;
import ch.daplab.jubilantoctoumbrella.model.Transaction;
import ch.daplab.yarn.AbstractZkAndKafkaAndTopicAppLauncher;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.builder.Tuple3Builder;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.AbstractDeserializationSchema;
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

    @Override
    protected int internalRunWithZkAndKafkaAndTopic(
            String zkConnect, String topic, String brokerList) throws Exception {

        String groupId = (String)getOptions().valueOf(OPTION_GROUP_ID);
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", brokerList);
        properties.setProperty("zookeeper.connect", zkConnect);
        properties.setProperty("group.id", groupId);

        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
//        streamExecutionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


        FlinkKafkaConsumer09<Transaction> consumer = new FlinkKafkaConsumer09<>(
                topic,
                new TransactionDeserializer(),
                properties);
//        consumer.assignTimestampsAndWatermarks(new TransactionTSExtractor());

        final DataStream<Transaction> dataStream = streamExecutionEnvironment.addSource(consumer);


//        SingleOutputStreamOperator<Tuple3<String, String, Long>> map =
                dataStream
//                .rebalance()
//                .map(new TransactionToTupleMap())
//                  .map(t -> Tuple3.of(t.getCardNumber(), t.getClientCountry(), t.getTimestamp()))
                  .map(new MapFunction<Transaction, Tuple3<String, String, Long>>() {
                    public Tuple3<String, String, Long> map(Transaction t) throws Exception {
                        return Tuple3.of(t.getCardNumber(), t.getClientCountry(), t.getTimestamp());
                    }
                  })
//                .keyBy(1)
                .addSink(s -> System.out.println(s));

//                .timeWindow(Time.minutes(1), Time.seconds(15))
//                .apply()
;
//        map.print();

        streamExecutionEnvironment.execute();

        return ReturnCode.ALL_GOOD;
    }

    @Override
    protected void initParser() {

        super.initParser();

        getParser().accepts(OPTION_GROUP_ID, "Kafka group id to use.")
                .withRequiredArg().defaultsTo(DEFAULT_GROUPE_ID);

    }


    public static class TransactionTSExtractor extends BoundedOutOfOrdernessTimestampExtractor<Transaction> {

        public TransactionTSExtractor() {
            super(Time.seconds(60));
        }

        @Override
        public long extractTimestamp(Transaction tx) {
            return tx.getTimestamp();
        }
    }

    public static class TransactionToTupleMap /*extends AbstractResultTypeQueryable<Tuple3<String, String, Long>>*/ implements MapFunction<Transaction, Tuple3<String, String, Long>> {

        @Override
        public Tuple3<String, String, Long> map(Transaction t) throws Exception {
            return Tuple3.of(t.getCardNumber(), t.getClientCountry(), t.getTimestamp());
        }
    }


}
