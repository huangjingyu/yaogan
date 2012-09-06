package com.rockontrol.yaogan;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.service.GeoService;
import com.rockontrol.yaogan.service.GeoServiceImpl;


public class GeoServiceTest {
   
   private static  ApplicationContext context;
   static {
      context = new ClassPathXmlApplicationContext("classpath:config/applicationContext-httpclient.xml");
   }
   
   @Test
   public void testLogin() {
      GeoService service = context.getBean(GeoService.class);
      service.publishGeoFile(Shapefile.Category.FILE_LAND_COLLAPSE, new File("E:/testfile/groundcrack2011.shp"));
   }
}
