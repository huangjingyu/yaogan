package org.yaogan.gis.mgr;

import org.geotools.data.FileDataStore;

public interface IDataStoreManager {
   public FileDataStore getFileDataStore(int year, String data_type);

   public FileDataStore getFileDataStore(String region, int year, String data_type);
}
