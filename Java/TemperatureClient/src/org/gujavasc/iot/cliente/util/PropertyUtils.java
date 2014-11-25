/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gujavasc.iot.cliente.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.gujavasc.iot.client.App;

/**
 *
 * @author avila
 */
public class PropertyUtils {
    static final Properties properties = new Properties();
    
    static {
        InputStream is = null;
        try {
            File configFile = new File("config.properties");

            if (configFile.exists()) {
                System.out.println("Using custom properties: " + configFile.getAbsoluteFile());
                is = new FileInputStream(configFile.getAbsoluteFile());
            } else {
                System.out.println("Using default properties: /resources/config.properties");
                is = App.class.getResourceAsStream("/resources/config.properties");
            }
            properties.load(is);
        } catch (IOException ex) {
            System.out.println("Error loading properties. " + ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                System.out.println("Error closing stream: " + ex);
            }
        }
    }
    
    public static String getPropertyString(String key) {
        return properties.getProperty(key);
    }
}
