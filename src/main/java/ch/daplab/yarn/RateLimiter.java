package ch.daplab.yarn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bperroud on 01/09/16.
 */
public class RateLimiter implements Func1<byte[], byte[]> {

    private static final Logger LOG = LoggerFactory.getLogger(RateLimiter.class);

    private final AtomicInteger rateLimit;
    long counter = 0L;
    long startTime = 0L;

    public RateLimiter(AtomicInteger rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Override
    public byte[] call(byte[] tx) {

        counter++;

        long currentTime = System.currentTimeMillis();

        if (startTime == 0L) {
            startTime = currentTime;
        }

        int limit = rateLimit.get();
        if (limit > 0) {

            if (counter > limit) {
                long sleepTime = TimeUnit.SECONDS.toMillis(1) + startTime - currentTime;

                if (sleepTime > 0) {
                    LOG.info("Alread {} message, sleeping {} ms", counter, sleepTime);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                counter = 0L;
                startTime = 0L;
            }

        }


        return tx;

    }
}
