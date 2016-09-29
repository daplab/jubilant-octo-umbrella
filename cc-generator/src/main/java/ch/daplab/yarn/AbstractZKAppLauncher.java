package ch.daplab.yarn;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * public static void main(String[] args) throws Exception {
 *   int res = ToolRunner.run(new Configuration(), new Yourclass extends AbstractAppLauncher, args);
 *   System.exit(res);
 * }
 * </pre>
 */
public abstract class AbstractZKAppLauncher extends AbstractAppLauncher {

    public static final String OPTION_ZK_CONNECT = "zk.connect";


    private CuratorFramework curatorFramework;
    private String zkConnect;

    protected String getZkConnect() {
        return zkConnect;
    }


    protected final CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    @Override
    public final int internalRun() throws Exception {

        zkConnect = (String) getOptions().valueOf(OPTION_ZK_CONNECT);

        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkConnect)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();


        boolean zkConnectionOk = getCuratorFramework().blockUntilConnected(5, TimeUnit.SECONDS);
        if (!zkConnectionOk) {
            System.err.println("Can't connect to zookeeper. Please check the --" + OPTION_ZK_CONNECT + " and retry");
            return ReturnCode.CANNOT_CONNECT_TO_ZK;
        }

        return internalRunWithZk(zkConnect);

    }

    protected abstract int internalRunWithZk(String zkConnect) throws Exception;



    private void privateInitParser() {
        getParser().accepts(OPTION_ZK_CONNECT, "List of ZK host:port hosts, comma-separated.")
                .withRequiredArg().required();

        initParser();

        getParser().accepts(OPTION_HELP, "Print this help").isForHelp();
    }

    /**
     * Override this function to add more options to the command line parser.
     */
    protected void initParser() {
        super.initParser();
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
