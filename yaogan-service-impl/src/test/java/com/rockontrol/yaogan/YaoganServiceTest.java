package com.rockontrol.yaogan;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.transaction.annotation.Transactional;

import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.GeoService;
import com.rockontrol.yaogan.service.YaoganServiceImpl;

//@RunWith(value = SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:config/application-test-config*.xml" })
//@Transactional
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class YaoganServiceTest {
//   private static  ApplicationContext context;
//   static {
//      context = new ClassPathXmlApplicationContext("classpath:config/applicationContext-test-config.xml");
//   }
//   
   @Test
   public void testLogin() {
      System.out.println("进来了");
    //  ApplicationContext context=new ClassPathXmlApplicationContext("config/applicationContext-test-config.xml");
     SessionFactory sessionFac=new AnnotationConfiguration().configure().buildSessionFactory();
      System.out.println("下来了");
      Long[] a=new Long[2];
      a[0]=1l;
      a[1]=2l;
      
      YaoganServiceImpl   yaogan=new YaoganServiceImpl();
  
     yaogan.sharePlacesToUser(new User(), 1l, a);
   }

}
