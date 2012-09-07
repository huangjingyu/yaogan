package com.rockontrol.yaogan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.rockontrol.yaogan.BaseTest;
import com.rockontrol.yaogan.model.Organization;
import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;

public class PlaceDaoTest extends BaseTest {
   @Before
   public void setUp() {
     // super.setUp();
     // mockOrg();
      Place place=new Place();
     // place.setId(100l);
      place.setName("rock");
      place.setOrgId(200l);
      Organization organi = new Organization();
      org.setName(UUID.randomUUID().toString().substring(18));
      place.setOrganization(organi);
   }

   @Test
   public void testGetUserByName() throws Exception {
      User dbUser = userDao.getUserByName(user.getUserName());
     // Place place=
      assertNotNull(dbUser);
      assertEquals(user.getId(), dbUser.getId());
      assertEquals(user.getUserName(), dbUser.getUserName());
   }

}
