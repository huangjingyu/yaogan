package com.rockontrol.yaogan.service;

import org.junit.Before;
import org.junit.Test;
import org.yaogan.gis.mgr.SimpleDataStoreManagerImpl;

public class EcoFactorComputeServiceTest {
   private final EcoFactorComputeServiceImpl service = new EcoFactorComputeServiceImpl();

   @Before
   public void prepare() {
      System.setProperty("yaogan.gis.shapefile.home", "f:/shapeFile");
      service.setDataStoreManager(new SimpleDataStoreManagerImpl());
   }

   @Test
   public void testComputeAbioWholeRegion() {
      // try {
      // double result_2010 = service.computeAbio("平朔", 2010);
      // double result_2011 = service.computeAbio("平朔", 2011);
      // System.out.println(result_2010);
      // System.out.println(result_2011);
      // } catch (IOException e) {
      // e.printStackTrace();
      // }
   }

   @Test
   public void testComputeAvegWholeRegion() {
      // try {
      // double result_2010 = service.computeAveg("平朔", 2010);
      // double result_2011 = service.computeAveg("平朔", 2011);
      // System.out.println(result_2010);
      // System.out.println(result_2011);
      // } catch (IOException e) {
      // e.printStackTrace();
      // }
   }

   @Test
   public void testComputeAeroWholeRegion() {
      // try {
      // double result_2010 = service.computeAero("平朔", 2010);
      // System.out.println(result_2010);
      // } catch (IOException e) {
      // e.printStackTrace();
      // }
   }
}
