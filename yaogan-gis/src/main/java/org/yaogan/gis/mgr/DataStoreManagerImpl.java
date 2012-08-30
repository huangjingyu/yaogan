package org.yaogan.gis.mgr;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;

public class DataStoreManagerImpl implements IDataStoreManager {
   private final DataFileManager _fileManager = new DataFileManager();

   @Override
   public FileDataStore getFileDataStore(int year, String data_type) {
      File file = _fileManager.getShapefile(year, data_type);
      FileDataStore fds = null;
      if (file != null) {
         try {
            fds = new ShapefileDataStore(file.toURI().toURL(), false,
                  Charset.forName("gbk"));
         } catch (MalformedURLException e) {
            e.printStackTrace();
         }
      }
      return fds;
   }

   @Override
   public FileDataStore getFileDataStore(String region, int year, String data_type) {
      File file = _fileManager.getShapefile(region, year, data_type);
      FileDataStore fds = null;
      if (file != null) {
         try {
            fds = new ShapefileDataStore(file.toURI().toURL(), false,
                  Charset.forName("gbk"));
         } catch (MalformedURLException e) {
            e.printStackTrace();
         }
      }
      return fds;
   }

}
