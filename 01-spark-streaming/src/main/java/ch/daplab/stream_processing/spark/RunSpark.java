package ch.daplab.stream_processing.spark; /**
 * Created by cbovigny on 8/7/15.
 */

import ch.daplab.config.Config;
import ch.daplab.jubilantoctoumbrella.model.Transaction;
import ch.daplab.yarn.AbstractKafkaWithTopicAppLauncher;
import ch.daplab.yarn.AbstractZKAppLauncher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.Tuple2;

public class RunSpark extends AbstractZKAppLauncher {

    public static final String OPTION_GROUP_ID = "group.id";

    public static final String DEFAULT_GROUPE_ID = "CC-Generator";

    @Override
    protected int internalRunWithZk(String zkConnect) throws Exception {

        String groupId = (String)getOptions().valueOf(OPTION_GROUP_ID);

        boolean log4jInitialized = Logger.getRootLogger().getAllAppenders().hasMoreElements();
        if (!log4jInitialized) {
            Logger.getRootLogger().setLevel(Level.ALL);
        }

        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName(groupId);
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(TimeUnit.SECONDS.toMillis(15)));

        Map<String, Integer> topicsMap = Collections.singletonMap(Config.topic, 4);

        JavaPairReceiverInputDStream<String, String> kafkaStream =
                KafkaUtils.createStream(jssc,
                        zkConnect, groupId + RandomUtils.nextLong(0, Long.MAX_VALUE), topicsMap);

        JavaDStream<Transaction> tx = kafkaStream.map(t -> {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(t._2, Transaction.class);
        });

        JavaPairDStream<String, Integer> nameCounts = tx
                .mapToPair(t -> new Tuple2<>(t.getClientCountry(), 1))
                .reduceByKey((i1, i2) -> i1 + i2);

        nameCounts.print();

        jssc.start();
        jssc.awaitTermination();

        return ReturnCode.ALL_GOOD;
    }


    @Override
    protected void initParser() {

        super.initParser();

        getParser().accepts(OPTION_GROUP_ID, "Kafka group id to use.")
                .withRequiredArg().defaultsTo(DEFAULT_GROUPE_ID);

    }

}
