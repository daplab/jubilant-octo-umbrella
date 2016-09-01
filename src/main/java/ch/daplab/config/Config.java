package ch.daplab.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Config {

    public static String zkConnect = "daplab-wn-22.fri.lan:2181,daplab-wn-25.fri.lan:2181,daplab-wn-33.fri.lan:2181";
    public static String brokerList = "daplab-rt-11.fri.lan:6667,daplab-rt-12.fri.lan:6667,daplab-rt-13.fri.lan:6667,daplab-rt-14.fri.lan:6667";
    public static String topic = "cc-tx";

}
