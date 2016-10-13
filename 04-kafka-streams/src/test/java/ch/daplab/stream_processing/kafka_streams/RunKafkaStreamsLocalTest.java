package ch.daplab.stream_processing.kafka_streams;

import ch.daplab.config.Config;
import org.junit.Test;

public class RunKafkaStreamsLocalTest {

    @Test
    public void runKafkaStreams() throws Exception {

        String[] args = new String[] {
                "--" + RunKafkaStreams.OPTION_KAFKA_CONNECT, Config.local_brokerList,
                "--" + RunKafkaStreams.OPTION_ZK_CONNECT, Config.local_zkConnect,
        };

        RunKafkaStreams.main(args);

    }
}
