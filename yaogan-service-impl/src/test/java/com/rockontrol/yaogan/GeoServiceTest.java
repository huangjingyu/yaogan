package com.rockontrol.yaogan;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rockontrol.yaogan.service.GeoService;

public class GeoServiceTest {

   private static ApplicationContext context;
   static {
      context = new ClassPathXmlApplicationContext(
            "classpath:config/applicationContext-test-config.xml");
   }

   @Test
   public void testLogin() {
      GeoService service = context.getBean(GeoService.class);
      service.publishShapeFile(new File("d:\\东露天矿.shp"));
   }

}
