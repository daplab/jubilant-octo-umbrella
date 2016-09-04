package ch.daplab.yarn;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.ClassAcceptor;
import org.apache.twill.api.TwillController;
import org.apache.twill.api.TwillRunnerService;
import org.apache.twill.api.logging.PrinterLogHandler;
import org.apache.twill.yarn.YarnTwillRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CCGeneratorCli extends AbstractAppLauncher {

    private static final Logger LOG = LoggerFactory.getLogger(CCGeneratorCli.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CCGeneratorCli(), args);
        System.exit(res);
    }

    @Override
    protected int internalRun() throws Exception {

        TwillRunnerService runnerService = new YarnTwillRunnerService(
                new YarnConfiguration(), getZkConnect());
        runnerService.start();


        YarnConfiguration yarnConfiguration = new YarnConfiguration(getConf());
        String yarnClasspath =
                yarnConfiguration.get(YarnConfiguration.YARN_APPLICATION_CLASSPATH,
                        Joiner.on(",").join(YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH));
        List<String> applicationClassPaths = Lists.newArrayList();
        Iterables.addAll(applicationClassPaths, Splitter.on(",").split(yarnClasspath));

        final TwillController controller = runnerService.prepare(new CCGeneratorTwillGenerator())
                .addLogHandler(new PrinterLogHandler(new PrintWriter(System.out)))
                .withApplicationClassPaths(applicationClassPaths)
                .withBundlerClassAcceptor(new HadoopClassExcluder())
                .start();


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Futures.getUnchecked(controller.terminate());
                } finally {
                    runnerService.stop();
                }
            }
        });

        try {
            controller.awaitTerminated();
        } catch (ExecutionException e) {
            LOG.error("Error", e);
        }

        // Good, let it simply run on his own.
        return ReturnCode.ALL_GOOD;
    }

    static class HadoopClassExcluder extends ClassAcceptor {
        @Override
        public boolean accept(String className, URL classUrl, URL classPathUrl) {
            // exclude hadoop but not hbase package
            System.out.println(className + ", classUrl=" + className.toString() + ", classPathUrl=" + classPathUrl.toString());
            return !(className.startsWith("org.apache.hadoop") && !className.startsWith("org.apache.hadoop.hbase"));
        }
    }

}
