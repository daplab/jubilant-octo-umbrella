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
public abstract class AbstractAppLauncher implements Tool, Closeable {

    protected static final String OPTION_HELP = "help";

    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    private final OptionParser parser = new OptionParser();

    private OptionSet options;
    private Configuration conf;


    protected final OptionSet getOptions() {
        return options;
    }

    protected final OptionParser getParser() {
        return parser;
    }


    @Override
    public final int run(String[] args) throws Exception {

        privateInitParser();

        boolean invalidOptions = false;
        try {
            options = getParser().parse(args);
        } catch (OptionException e) {
            invalidOptions = true;
            System.err.println("Invalid argument: " + e.getMessage());
            System.err.println("Run with --" + OPTION_HELP + " for help.");
        }

        if (invalidOptions || options.has(OPTION_HELP)) {
            getParser().printHelpOn(System.out);
            return ReturnCode.HELP;
        }

        internalRun();

        return ReturnCode.ALL_GOOD;
    }


    protected abstract int internalRun() throws Exception;


    private void privateInitParser() {
        initParser();
        getParser().accepts(OPTION_HELP, "Print this help").isForHelp();
    }


    /**
     * Override this function to add more options to the command line parser.
     */
    protected void initParser() {
    }


    @Override
    public final Configuration getConf() {
        return conf;
    }

    @Override
    public final void setConf(Configuration configuration) {
        this.conf = configuration;
    }

    @Override
    public final void close() throws IOException {
        internalClose();
    }


    /**
     * Override this function to close additional resources prior to closing the curator framework
     *
     * @throws IOException
     */
    protected void internalClose() throws IOException {
    }


    protected class ReturnCode {
        public static final int ALL_GOOD = 0;
        public static final int HELP = 1;
        public static final int CANNOT_CONNECT_TO_ZK = 2;
        public static final int WRONG_ZK_CONFIG = 10;
    }
}
