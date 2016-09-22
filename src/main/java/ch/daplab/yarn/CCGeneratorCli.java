package ch.daplab.yarn;

import ch.daplab.config.Config;
import ch.daplab.jubilantoctoumbrella.DummyRandomGenerator;
import ch.daplab.jubilantoctoumbrella.EventGenerator;
import ch.daplab.kafka.sink.rx.KafkaObserver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observer;

import java.util.concurrent.atomic.AtomicInteger;

public class CCGeneratorCli extends AbstractAppLauncher {

    public static final String OPTION_KAFKA_CONNECT = "kafka.connect";
    public static final String OPTION_TOPIC = "topic";
    public static final String OPTION_RATE_LIMIT = "rate.limit";

    public static final Integer DEFAULT_RATE_LIMIT = 1000;

    private final AtomicInteger atomicRateLimit = new AtomicInteger(DEFAULT_RATE_LIMIT);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CCGeneratorCli(), args);
        System.exit(res);
    }

    @Override
    protected int internalRun() throws Exception {

        final String topic = (String)getOptions().valueOf(OPTION_TOPIC);
        final Integer rateLimit = (Integer)getOptions().valueOf(OPTION_RATE_LIMIT);
        final String brokerList = (String)getOptions().valueOf(OPTION_KAFKA_CONNECT);

        generateMessages(topic, brokerList, rateLimit);

        // Good, let it simply run on his own.
        return ReturnCode.ALL_GOOD;
    }

    public void generateMessages(String topic, String brokerList, Integer rateLimit) {

        updateRateLimit(rateLimit);

        Observer<byte[]> sink = new KafkaObserver(topic, brokerList);
        EventGenerator eventGenerator = new EventGenerator(new DummyRandomGenerator());

        eventGenerator.getTransactions().map(new TransactionToJson()).map(new RateLimiter(atomicRateLimit)).subscribe(sink);

    }

    @Override
    protected void initParser() {

        getParser().accepts(OPTION_KAFKA_CONNECT, "List of KAfka host:port brokers, comma-separated.")
                .withRequiredArg().defaultsTo(Config.brokerList);
        getParser().accepts(OPTION_TOPIC, "Topic to publish messages to, must be created before hand")
                .withRequiredArg().defaultsTo(Config.topic);
        getParser().accepts(OPTION_RATE_LIMIT, "Rate limitation in msg/sec at which to inject messages.")
                .withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_RATE_LIMIT);

    }

    public void updateRateLimit(int newRateLimit) {
        atomicRateLimit.set(newRateLimit);
    }
}
