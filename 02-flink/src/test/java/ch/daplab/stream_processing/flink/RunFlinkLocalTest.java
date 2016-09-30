package ch.daplab.stream_processing.flink;

import ch.daplab.config.Config;
import org.junit.Test;

/**
 * Created by bperroud on 30/09/16.
 */
public class RunFlinkLocalTest {

    @Test
    public void runFlink() throws Exception {

        String[] args = new String[] {
                "--" + RunFlink.OPTION_KAFKA_CONNECT, Config.local_brokerList,
                "--" + RunFlink.OPTION_ZK_CONNECT, Config.local_zkConnect,
        };

        RunFlink.main(args);

    }
}
