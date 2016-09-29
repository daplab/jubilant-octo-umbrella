package ch.daplab.yarn;

import ch.daplab.config.Config;
import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;

public abstract class AbstractKafkaWithTopicAppLauncher extends AbstractAppLauncher {

    public static final String OPTION_KAFKA_CONNECT = "kafka.connect";
    public static final String OPTION_TOPIC = "topic";

    private CuratorFramework curatorFramework;
    private String topic;
    private String brokerList;

    protected String getBrokerList() {
        return brokerList;
    }
    protected String getTopic() { return topic; }


    @Override
    public final int internalRun() throws Exception {

        topic = (String)getOptions().valueOf(OPTION_TOPIC);
        brokerList = (String)getOptions().valueOf(OPTION_KAFKA_CONNECT);

        return internalRunWithKafkaAndTopic(topic, brokerList);

    }


    protected abstract int internalRunWithKafkaAndTopic(String topic, String brokerList) throws Exception;


    /**
     * Override this function to add more options to the command line parser.
     */
    protected void initParser() {

        super.initParser();

        getParser().accepts(OPTION_KAFKA_CONNECT, "List of KAfka host:port brokers, comma-separated.")
                .withRequiredArg().defaultsTo(Config.brokerList);
        getParser().accepts(OPTION_TOPIC, "Topic to publish messages to, must be created before hand")
                .withRequiredArg().defaultsTo(Config.topic);
    }


    /**
     * Override this function to close additional resources prior to closing the curator framework
     *
     * @throws IOException
     */
    protected void internalClose() throws IOException {
        super.internalClose();
    }

}
