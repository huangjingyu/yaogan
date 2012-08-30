package org.yaogan.gis.mgr;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class DagaFileManagerTest {
   private final DataFileManager _manager = new DataFileManager();

   @Before
   public void configHome() {
      _manager.setShapeFileHome("F:/shapeFile");
   }

   @Test
   public void testGetShapefile() {
      File file = _manager.getShapefile("平朔", 2011, "边界");
      Assert.assertNotNull(file);
      file = _manager.getShapefile(2011, "地类");
      Assert.assertNotNull(file);
   }
}
