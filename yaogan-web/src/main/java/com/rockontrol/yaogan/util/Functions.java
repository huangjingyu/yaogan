package com.rockontrol.yaogan.util;

import org.springframework.util.AntPathMatcher;

/**
 * Supplies functions for the whole application.
 * 
 * @author Weiyong Huang
 * @version 1.0 2012/5/7
 * 
 */
public class Functions {
   /**
    * Compares a path with ant path pattern.
    * 
    * @param pattern
    *           ant path pattern
    * @param path
    *           path information
    * @return
    */
   public static boolean matchAntPath(String pattern, String path) {
      AntPathMatcher matcher = new AntPathMatcher();
      return matcher.match(pattern, path);
   }

}
