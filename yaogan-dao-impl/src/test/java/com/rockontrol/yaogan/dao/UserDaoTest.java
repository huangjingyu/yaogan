package com.rockontrol.yaogan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.rockontrol.yaogan.BaseTest;
import com.rockontrol.yaogan.model.User;

public class UserDaoTest extends BaseTest {

   @Before
   public void setUp() {
      super.setUp();
   }

   @Test
   public void testGetUserByName() throws Exception {
      User dbUser = userDao.getUserByName(user.getUserName());
      assertNotNull(dbUser);
      assertEquals(user.getId(), dbUser.getId());
      assertEquals(user.getUserName(), dbUser.getUserName());
   }
}
