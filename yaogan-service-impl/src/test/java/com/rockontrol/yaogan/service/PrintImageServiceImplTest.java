package com.rockontrol.yaogan.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/application-test-config*.xml" })
public class PrintImageServiceImplTest {

   @Autowired
   private IPrintImageService service;

   @Test
   public void testAddShapeLayer() {
      // fail("Not yet implemented");
   }

   @Test
   public void testAddComment() {
      // fail("Not yet implemented");
   }

   @Test
   public void testCopyTemplate() {
      System.out.println(service);
   }

}
