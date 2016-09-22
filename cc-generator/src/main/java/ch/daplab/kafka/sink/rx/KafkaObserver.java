package ch.daplab.kafka.sink.rx;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observer;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

@NotThreadSafe
public class KafkaObserver implements Observer<byte[]> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaObserver.class);
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private final AtomicBoolean closeRef = new AtomicBoolean(false);

    private final String topic;
    private final KafkaProducer producer;

    public KafkaObserver(String topic, String brokerList) {
        this.topic = topic;

        Properties props = new Properties();

        props.put("bootstrap.servers", brokerList);
        props.put("acks", "0");
//        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        producer = new KafkaProducer(props);
    }

    @Override
    public void onCompleted() {
        internalClose();
    }

    @Override
    public void onError(Throwable throwable) {
        LOG.warn("Got an exception from the Observable.", throwable);
        internalClose();
    }

    @Override
    public void onNext(byte[] buffer) {

        if (closeRef.get()) {
            return;
        }

        ProducerRecord<Integer, byte[]> data = new ProducerRecord<>(topic, buffer);
        producer.send(data);

    }

    private void internalClose() {
        if (closeRef.compareAndSet(false, true)) {
            if (producer != null) {
                producer.close();
            }
        }
    }

}
