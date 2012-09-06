package com.rockontrol.yaogan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rockontrol.yaogan.BaseTest;
import com.rockontrol.yaogan.model.Organization;
import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;

public class ShapefileDaoTest extends BaseTest{
   private Shapefile shap;
   @Before
   public void setUp() {
     // super.setUp();
    shap=new Shapefile();
      shap.setFileName("shapefile");
      shap.setFilePath("rock");
      shap.setPlaceId(12l);
      shap.setWmsUrl("luokejiahua");
      
//      Place place=new Place();
//      place.setName("taiyuan");
//      Organization orga=new Organization();
//      orga.setName("guowuyaun");
//      User user1=new User();
//     user1.setUserName("pa");
//     user1.setEmail("1226734725@qq.com");
//     user1.setPassword("123456");
//     List<User> list1=new ArrayList<User>();
//     
//      orga.setEmployees(list1);
//      
//      place.setOrganization(orga);
      //shap.setPlace(place);
      shap.setShootTime("2007");
   }

   @Test
   public void testGetUserByName() throws Exception {
//      User dbUser = userDao.getUserByName(user.getUserName());
//      assertNotNull(dbUser);
//      assertEquals(user.getId(), dbUser.getId());
//      assertEquals(user.getUserName(), dbUser.getUserName());
      ShapefileDaoImpl shapefiledaoimpl=new ShapefileDaoImpl();
      //shapefiledaoimpl.getShapefiles(placeId, time)
     List<String> s= shapefiledaoimpl.getAvailableTimesOfPlace(12l);
     
   }

}
