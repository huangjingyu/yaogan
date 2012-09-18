package com.rockontrol.yaogan;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.service.GeoService;

public class GeoServiceTest {

   private static ApplicationContext context;
   static {

      context = new ClassPathXmlApplicationContext(
            "classpath:config/applicationContext-httpclient.xml");
   }

   @Test
   public void testLogin() {
      GeoService service = context.getBean(GeoService.class);

      service.publishGeoFile(Shapefile.Category.FILE_REGION_BOUNDARY, new File(
            "D:/test/玉亮测试/边界/2028/a1bcd.shp"));
   }
}
