package ch.daplab.yarn;

import ch.daplab.jubilantoctoumbrella.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bperroud on 01/09/16.
 */
public class RateLimiter implements Func1<byte[], byte[]> {

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
