package ch.daplab.yarn;

import com.google.common.util.concurrent.Futures;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.TwillController;
import org.apache.twill.api.TwillRunnerService;
import org.apache.twill.api.logging.PrinterLogHandler;
import org.apache.twill.yarn.YarnTwillRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
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


        final TwillController controller = runnerService.prepare(new CCGeneratorTwillGenerator())
                .addLogHandler(new PrinterLogHandler(new PrintWriter(System.out)))
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

}
