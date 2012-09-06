package com.rockontrol.yaogan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rockontrol.yaogan.BaseTest;
import com.rockontrol.yaogan.model.User;

public class BaseDaoTest extends BaseTest {
   @Autowired
   private IUserDao userDao;

   @Before
   // insert 100 objects
   public void prepareData() {
      for (int i = 0; i < 100; i++) {
         User user = new User();
         user.setEmail(i + "@rockontrol.com");
         user.setPassword("123456");
         user.setUserName("user_" + i);
         userDao.save(user);
      }
   }

   @Test
   public void findByPageWithPosition() {
      String hql = "from com.rockontrol.yaogan.model.User  where userName like ?";
      List<User> list = userDao.findByPage(hql, 0, 20, new Object[] { "user_%" });
      Assert.assertEquals(list.size(), 20);
   }

   @Test
   public void findByPageWithNamedParam() {
      String hql = "from com.rockontrol.yaogan.model.User  where userName like :userName";
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("userName", "user_%");
      List<User> list = userDao.findByPage(hql, 0, 20, map);
      Assert.assertEquals(list.size(), 20);
   }
}
