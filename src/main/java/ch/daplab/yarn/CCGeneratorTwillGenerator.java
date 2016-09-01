package ch.daplab.yarn;

import ch.daplab.config.Config;
import ch.daplab.jubilantoctoumbrella.DummyRandomGenerator;
import ch.daplab.jubilantoctoumbrella.EventGenerator;
import ch.daplab.kafka.sink.rx.KafkaObserver;
import org.apache.twill.api.AbstractTwillRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observer;

import java.util.concurrent.atomic.AtomicInteger;

public class CCGeneratorTwillGenerator extends AbstractTwillRunnable {

    private static final Logger LOG = LoggerFactory.getLogger(CCGeneratorTwillGenerator.class);

    private final AtomicInteger rateLimit = new AtomicInteger(10);
    /**
     * Called by YARN nodeManager, i.e. remote (not on the same JVM) from the command line
     * which starts it.
     */
    @Override
    public void run() {

        Observer<byte[]> sink = new KafkaObserver(Config.topic, Config.brokerList);
        EventGenerator eventGenerator = new EventGenerator(new DummyRandomGenerator());

        eventGenerator.getTransactions().map(new TransactionToJson()).map(new RateLimiter(rateLimit)).subscribe(sink);

    }
}
