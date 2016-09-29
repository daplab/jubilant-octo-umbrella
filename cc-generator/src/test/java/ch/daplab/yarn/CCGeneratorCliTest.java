package ch.daplab.yarn;

import ch.daplab.config.Config;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bperroud on 22/09/16.
 */
public class CCGeneratorCliTest {

    @Test
    public void generateMessages() throws Exception {

        ToolRunner.run(new Configuration(), new CCGeneratorCli(), new String[] {});

    }

}