package org.yaogan.gis.mgr;

import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.junit.Test;
import org.yaogan.gis.util.EcoFactorCaculator;

public class EcoFactorCaculatorTest {

   @Test
   public void testComputeAbio() throws IOException {
      SimpleDataStoreManagerImpl manager = new SimpleDataStoreManagerImpl();
      FileDataStore store = manager.getDataStore("F:/shapeFile/数据2/landuse2010.shp");

      double abio = EcoFactorCaculator.computeAbio(store.getFeatureSource());
      double aveg = EcoFactorCaculator.computeAveg(store.getFeatureSource());
      System.out.println(abio + "  " + aveg);
   }
}
