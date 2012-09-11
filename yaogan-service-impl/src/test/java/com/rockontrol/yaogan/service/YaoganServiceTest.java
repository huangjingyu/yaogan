package com.rockontrol.yaogan.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.vo.EnvStats;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/application-test-config*.xml" })
// @Transactional
// @TransactionConfiguration(transactionManager = "transactionManager",
// defaultRollback = true)
public class YaoganServiceTest {

   @Autowired
   private IYaoganService service;

   @Test
   public void testComputeEnvStats() {
      User user = new User();
      user.setId(1l);
      user.setEmail("cloudsa@rockontrol.com");

      EnvStats stats = service.computeEnvStats(user, 10l, "2010");
      System.out.println(stats.getAbio() + " : " + stats.getAero() + " : "
            + stats.getAveg() + " : " + stats.getAsus());
   }
}
