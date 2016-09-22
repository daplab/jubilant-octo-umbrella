package ch.daplab.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.utils.TestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aliya on 09/04/15.
 */
public class TwitterProducer extends SetupSimpleKafkaCluster {

    public static final int NUMBER_OF_MESSAGES = 100;
    private static final Logger logger = LoggerFactory.getLogger(TwitterProducer.class);

    // The actual Twitter stream. It's set up to collect raw JSON data
    private TwitterStream twitterStream;
    private CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_MESSAGES);
    private AtomicInteger counter = new AtomicInteger(0);

    @Test
    public void testProducerWriteTwitterStatus() throws InterruptedException, TimeoutException {

        // Setup producer
        Properties producerProperties = TestUtils.getProducerConfig(kafkaConnect);
        ProducerConfig producerConfig = new ProducerConfig(producerProperties);
        final Producer producer = new Producer(producerConfig);

        Properties consumerProperties = TestUtils.createConsumerProperties(zkConnect, "my-random-group-id", "my-random-consumer-id", 10000L);
        ConsumerConfig consumerConfig = new ConsumerConfig(consumerProperties);
//        final Consumer consumer =


        // Information necessary for accessing the Twitter API
        String consumerKey = TwitterSourceConstant.CONSUMER_KEY_KEY;
        String consumerSecret = TwitterSourceConstant.CONSUMER_SECRET_KEY;
        String accessToken = TwitterSourceConstant.ACCESS_TOKEN_KEY;
        String accessTokenSecret = TwitterSourceConstant.ACCESS_TOKEN_SECRET_KEY;

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);
        cb.setJSONStoreEnabled(true);
        cb.setIncludeEntitiesEnabled(true);

        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        // Twitter listener
        StatusListener listener = new StatusListener() {
            // The onStatus method is executed every time a new tweet comes in.
            public void onStatus(Status status) {
                int i = counter.incrementAndGet();
                if (i <= NUMBER_OF_MESSAGES) {
                    countDownLatch.countDown();
                    // The EventBuilder is used to build an event using the raw JSON of a tweet
                    logger.info(status.getUser().getScreenName() + ": " + status.getText());
                    KeyedMessage<Integer, byte[]> data = new KeyedMessage<>(TwitterSourceConstant.KAFKA_TOPIC, TwitterObjectFactory.getRawJSON(status).getBytes());
                    producer.send(data);
                }
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            public void onScrubGeo(long userId, long upToStatusId) {
            }

            public void onException(Exception ex) {
                logger.info("Shutting down Twitter sample stream...");
                twitterStream.shutdown();
            }

            public void onStallWarning(StallWarning warning) {
            }
        };

        twitterStream.addListener(listener);
        // Starts listening on random sample of all public statuses
        twitterStream.sample();

        countDownLatch.await(5000, TimeUnit.MILLISECONDS);

        List<String> messages = kafkaUnitServer.readMessages(TwitterSourceConstant.KAFKA_TOPIC, NUMBER_OF_MESSAGES);

        twitterStream.shutdown();

    }
}
