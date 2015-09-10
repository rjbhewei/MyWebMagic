package com.hewei.csdn.inits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:58
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnInit {

    private static final Logger logger = LoggerFactory.getLogger(CsdnInit.class);

    private static final String filePath = "/csdn.properties";

    private static Properties properties = new Properties();

    static {
        init();
    }

    public static void init() {
        try (InputStream in = CsdnInit.class.getResourceAsStream(filePath)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("LogInit properties:" + properties);
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

}