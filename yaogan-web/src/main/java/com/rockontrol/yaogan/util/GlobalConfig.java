package com.rockontrol.yaogan.util;

import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class GlobalConfig {
   private static final String _CONFIG_FILE = "config/yaogan.properties";

   private PropertiesConfiguration _properties;

   private static GlobalConfig INSTANCE = new GlobalConfig();

   private GlobalConfig() {
      URL url = Thread.currentThread().getContextClassLoader().getResource(_CONFIG_FILE);
      try {
         _properties = new PropertiesConfiguration(url);
      } catch (ConfigurationException ex) {
         ex.printStackTrace();
      }
   }

   public static PropertiesConfiguration getProperties() {
      return INSTANCE._properties;
   }
}
