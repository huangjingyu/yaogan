package org.yaogan.gis.mgr;

import java.io.IOException;

import org.geotools.data.FileDataStore;

/**
 * 
 * @author Administrator
 * 
 */
public interface IDataStoreManager {

   public FileDataStore getDataStore(String shapeFilePath) throws IOException;

   public void releaseDataStore(FileDataStore store);

}
