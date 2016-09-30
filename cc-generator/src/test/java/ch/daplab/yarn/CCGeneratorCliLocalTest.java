package ch.daplab.yarn;

import ch.daplab.config.Config;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

/**
 * Please run StartLocalKafka before running this one
 */
public class CCGeneratorCliLocalTest {

    @Test
    public void generateMessages() throws Exception {

        String[] args = new String[] {
                "--" + CCGeneratorCli.OPTION_KAFKA_CONNECT, Config.local_brokerList
        };

        ToolRunner.run(new Configuration(), new CCGeneratorCli(), args);

    }

}