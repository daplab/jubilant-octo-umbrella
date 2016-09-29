package ch.daplab.stream_processing.spark;

/**
 * Created by bperroud on 22/09/16.
 */
public class TestTest {


    @org.junit.Test
    public void test() throws Exception {

        RunSpark.main(new String[] { "--zk.connect", "daplab-wn-22.fri.lan:2181"});
    }
}