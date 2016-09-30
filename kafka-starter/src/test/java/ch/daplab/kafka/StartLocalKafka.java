package ch.daplab.kafka;

import info.batey.kafka.unit.KafkaUnit;

import java.io.IOException;

/**
 * Kafka test class: instantiate embedded ZK and Kafka clusters
 * Instantiate consumer group, and start writing
 */
public class StartLocalKafka {

    public static int ZK_PORT = 28389;
    public static int KAFKA_PORT = 28387;

    private static KafkaUnit kafkaUnitServer;

    public static void main(String[] args) throws IOException {
        kafkaUnitServer = new KafkaUnit(ZK_PORT, KAFKA_PORT);
        kafkaUnitServer.startup();

        // wait for an external signal to shutdown
        System.in.read();

        if (kafkaUnitServer != null) {
            kafkaUnitServer.shutdown();
        }

    }
}
