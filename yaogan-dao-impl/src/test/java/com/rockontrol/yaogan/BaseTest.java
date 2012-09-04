package com.rockontrol.yaogan;

import java.util.UUID;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.rockontrol.yaogan.dao.IOrganizationDao;
import com.rockontrol.yaogan.dao.IPlaceDao;
import com.rockontrol.yaogan.dao.IShapefileDao;
import com.rockontrol.yaogan.dao.IUserDao;
import com.rockontrol.yaogan.dao.IUserPlaceDao;
import com.rockontrol.yaogan.model.Organization;
import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/application-test-config*.xml" })
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {

   @Autowired
   protected IUserDao userDao;

   @Autowired
   protected IOrganizationDao orgDao;

   @Autowired
   protected IPlaceDao placeDao;

   @Autowired
   protected IShapefileDao shapefileDao;

   @Autowired
   protected IUserPlaceDao upDao;

   protected User user;
   protected Organization org;
   protected Place place;

   protected void setUp() {
      org = mockOrg();
      orgDao.save(org);
      user = mockUser(org.getId());
      userDao.save(user);
      place = mockPlace(org.getId());
      placeDao.save(place);
   }

   protected Organization mockOrg() {
      Organization org = new Organization();
      org.setName(UUID.randomUUID().toString().substring(18));
      return org;
   }

   protected User mockUser(Long orgId) {
      User user = new User();
      user.setUserName(UUID.randomUUID().toString().substring(18));
      user.setPassword("password");
      user.setEmail(UUID.randomUUID().toString().substring(8) + "@rockontrol.com");
      return user;
   }

   protected Place mockPlace(Long orgId) {
      Place place = new Place();
      place.setName(UUID.randomUUID().toString());
      place.setOrgId(orgId);
      return place;
   }
}
