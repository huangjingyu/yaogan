package com.rockontrol.yaogan.service;

import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.springframework.stereotype.Service;
import org.yaogan.gis.mgr.IDataStoreManager;
import org.yaogan.gis.util.EcoFactorCaculator;

@Service
public class EcoFactorComputeServiceImpl implements EcoFactorComputeService {
   private IDataStoreManager dataStoreManager;

   /**
    * 
    * DataStore可能需要cache 如果频繁调用，可以考虑使用实现了缓存功能的 DataStoreManager
    * 
    */
   @Override
   public double computeAbio(String shapeFilePath, String geom_string)
         throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAbio(source, geom_string);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }

   }

   @Override
   public double computeAbio(String shapeFilePath, double maxX, double maxY,
         double minX, double minY) throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAbio(source, maxX, maxY, minX, minY);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }
   }

   @Override
   public double computeAero(String shapeFilePath, double maxX, double maxY,
         double minX, double minY) throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAero(source, maxX, maxY, minX, minY);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }
   }

   @Override
   public double computeAero(String shapeFilePath, String geom_string)
         throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAero(source, geom_string);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }

   }

   @Override
   public double computeAveg(String shapeFilePath, String geom_string)
         throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAveg(source, geom_string);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }

   }

   @Override
   public double computeAveg(String shapeFilePath, double maxX, double maxY,
         double minX, double minY) throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAveg(source, maxX, maxY, minX, minY);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }
   }

   public void setDataStoreManager(IDataStoreManager manager) {
      this.dataStoreManager = manager;
   }

   public IDataStoreManager getDataStoreManager() {
      return this.dataStoreManager;
   }

   @Override
   public double computeAbio(String shapeFilePath) throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAbio(source);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }

   }

   @Override
   public double computeAero(String shapeFilePath) throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAero(source);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }

   }

   @Override
   public double computeAveg(String shapeFilePath) throws IOException {
      FileDataStore store = dataStoreManager.getDataStore(shapeFilePath);
      double ret = 0;
      try {
         SimpleFeatureSource source = store.getFeatureSource();
         ret = EcoFactorCaculator.computeAveg(source);
         return ret;
      } catch (IOException e) {
         throw e;
      } finally {
         dataStoreManager.releaseDataStore(store);
      }
   }
}
