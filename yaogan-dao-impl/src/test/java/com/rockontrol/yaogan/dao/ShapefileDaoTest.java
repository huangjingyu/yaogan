package com.rockontrol.yaogan.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rockontrol.yaogan.BaseTest;
import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;

public class ShapefileDaoTest extends BaseTest {

   private Shapefile sf1;
   private Shapefile sf2;
   private Shapefile sf3;

   @Before
   public void setUp() {
      super.setUp();
      sf1 = mockShapefile(place.getId(), "2010");
      shapefileDao.save(sf1);
      sf2 = mockShapefile(place.getId(), "2010");
      shapefileDao.save(sf2);
      sf3 = mockShapefile(place.getId(), "2011");
      shapefileDao.save(sf3);
   }

   @Test
   public void testGetAvailableTimesOfPlace() throws Exception {
      List<String> list = shapefileDao.getAvailableTimesOfPlace(place.getId());
      assertEquals(2, list.size());
      assertEquals("2010", list.get(0));
      assertEquals("2011", list.get(1));
   }

   @Test
   public void testGetAvailablePlacesForTime() throws Exception {
      List<Place> list = shapefileDao.getAvailablePlacesOfOrg(place.getOrgId(), "2010");
      assertEquals(1, list.size());
      assertEquals(place.getId(), list.get(0).getId());
   }

   @Test
   public void testGetAvailableTimesOfOrg() throws Exception {
      List<String> list = shapefileDao.getAvailableTimesOfOrg(org.getId());
      assertEquals(2, list.size());
      assertEquals("2011", list.get(0));
      assertEquals("2010", list.get(1));
   }

}
