package org.yaogan.gis.mgr;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;

//no cache datastore implemention

public class SimpleDataStoreManagerImpl implements IDataStoreManager {
   private final DataFileManager _fileManager = new DataFileManager();

   @Override
   public FileDataStore getDataStore(String shapeFilePath) throws IOException {
      FileDataStore fds = new ShapefileDataStore(
            new File(shapeFilePath).toURI().toURL(), false, Charset.forName("gbk"));
      return fds;
   }

   @Override
   public void releaseDataStore(FileDataStore store) {
      store.dispose();
   }
}
