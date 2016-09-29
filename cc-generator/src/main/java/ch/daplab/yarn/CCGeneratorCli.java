package ch.daplab.yarn;

import ch.daplab.jubilantoctoumbrella.EventGenerator;
import ch.daplab.jubilantoctoumbrella.RandomGenerator;
import ch.daplab.kafka.sink.rx.KafkaObserver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import rx.Observer;

import java.util.concurrent.atomic.AtomicInteger;

public class CCGeneratorCli extends AbstractKafkaWithTopicAppLauncher {

    public static final String OPTION_RATE_LIMIT = "rate.limit";

    public static final Integer DEFAULT_RATE_LIMIT = 10;

    private final AtomicInteger atomicRateLimit = new AtomicInteger(DEFAULT_RATE_LIMIT);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CCGeneratorCli(), args);
        System.exit(res);
    }

    @Override
    protected int internalRunWithKafkaAndTopic(String topic, String brokerList) throws Exception {

        final Integer rateLimit = (Integer)getOptions().valueOf(OPTION_RATE_LIMIT);

        generateMessages(topic, brokerList, rateLimit);

        // Good, let it simply run on his own.
        return ReturnCode.ALL_GOOD;
    }

    public void generateMessages(String topic, String brokerList, Integer rateLimit) {

        updateRateLimit(rateLimit);

        Observer<byte[]> sink = new KafkaObserver(topic, brokerList);
        EventGenerator eventGenerator = new EventGenerator(new RandomGenerator());

        eventGenerator.getTransactions().map(new TransactionToJson()).map(new RateLimiter(atomicRateLimit)).subscribe(sink);

    }

    @Override
    protected void initParser() {

        super.initParser();

        getParser().accepts(OPTION_RATE_LIMIT, "Rate limitation in msg/sec at which to inject messages.")
                .withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_RATE_LIMIT);

    }

    public void updateRateLimit(int newRateLimit) {
        atomicRateLimit.set(newRateLimit);
    }
}
