package com.rockontrol.yaogan.service;

import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Service;
import org.yaogan.gis.mgr.DataFileType;
import org.yaogan.gis.mgr.IDataStoreManager;
import org.yaogan.gis.util.EcoFactorCaculator;
import org.yaogan.gis.util.FeatureSelector;

import com.vividsolutions.jts.geom.Geometry;

@Service
public class EcoFactorComputeServiceImpl implements EcoFactorComputeService {
   // private IFeatureSelector _featureSelector;
   private IDataStoreManager _dataStoreManager;

   @Override
   public double computeAbio(int year, String geom_string) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      Geometry boundary = null;
      try {
         boundary = getTheBoundaryGeom(year, geom_string);
      } catch (CQLException e) {
         e.printStackTrace();
      }

      return EcoFactorCaculator.computeAbio(source, boundary);
   }

   @Override
   public double computeAveg(int year, String geom_string) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      Geometry boundary = null;
      try {
         boundary = getTheBoundaryGeom(year, geom_string);
      } catch (CQLException e) {
         e.printStackTrace();
      }
      return EcoFactorCaculator.computeAveg(source, boundary);
   }

   @Override
   public double computeAero(int year, String geom_string) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_LAND_SOIL);
      SimpleFeatureSource source = store.getFeatureSource();
      Geometry boundary = null;
      try {
         boundary = getTheBoundaryGeom(year, geom_string);
      } catch (CQLException e) {
         e.printStackTrace();
      }
      return EcoFactorCaculator.computeAero(source, boundary);
   }

   @Override
   public double computeAbio(int year, double maxX, double maxY, double minX, double minY)
         throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAbio(source, maxX, maxY, minX, minY);
   }

   @Override
   public double computeAveg(int year, double maxX, double maxY, double minX, double minY)
         throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAveg(source, maxX, maxY, minX, minY);
   }

   @Override
   public double computeAero(int year, double maxX, double maxY, double minX, double minY)
         throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_LAND_SOIL);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAero(source, maxX, maxY, minX, minY);
   }

   public void setDataStoreManager(IDataStoreManager manger) {
      _dataStoreManager = manger;
   }

   private Geometry getTheBoundaryGeom(int year, String geom_str) throws CQLException,
         IOException {
      FileDataStore store2 = _dataStoreManager.getFileDataStore(year,
            DataFileType.FILE_REGION_BOUNDARY);
      SimpleFeature feature = FeatureSelector.selectTheFeature(
            store2.getFeatureSource(), geom_str);
      Geometry geom = null;
      if (feature != null)
         geom = (Geometry) feature.getDefaultGeometry();
      return geom;
   }

   private Geometry getTheBoundaryGeom(String region, int year, String geom_str)
         throws CQLException, IOException {
      FileDataStore store2 = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_REGION_BOUNDARY);
      SimpleFeature feature = FeatureSelector.selectTheFeature(
            store2.getFeatureSource(), geom_str);
      Geometry geom = null;
      if (feature != null)
         geom = (Geometry) feature.getDefaultGeometry();
      return geom;
   }

   @Override
   public double computeAbio(String region, int year, String geom_string)
         throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      Geometry boundary = null;
      try {
         boundary = getTheBoundaryGeom(region, year, geom_string);

      } catch (CQLException e) {
         e.printStackTrace();
      }
      return EcoFactorCaculator.computeAbio(source, boundary);
   }

   @Override
   public double computeAbio(String region, int year) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAbio(source);
   }

   @Override
   public double computeAveg(String region, int year, String geom_string)
         throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      Geometry boundary = null;
      try {
         boundary = getTheBoundaryGeom(region, year, geom_string);
      } catch (CQLException e) {
         e.printStackTrace();
      }
      return EcoFactorCaculator.computeAveg(source, boundary);
   }

   @Override
   public double computeAveg(String region, int year) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAveg(source);
   }

   @Override
   public double computeAero(String region, int year, String geom_string)
         throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_SOIL);
      SimpleFeatureSource source = store.getFeatureSource();
      Geometry boundary = null;
      try {
         boundary = getTheBoundaryGeom(year, geom_string);
      } catch (CQLException e) {
         e.printStackTrace();
      }
      return EcoFactorCaculator.computeAero(source, boundary);
   }

   @Override
   public double computeAero(String region, int year) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_SOIL);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAero(source);
   }

   @Override
   public double computeAbio(String region, int year, double maxX, double maxY,
         double minX, double minY) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();

      return EcoFactorCaculator.computeAbio(source, maxX, maxY, minX, minY);
   }

   @Override
   public double computeAveg(String region, int year, double maxX, double maxY,
         double minX, double minY) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_TYPE);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAveg(source, maxX, maxY, minX, minY);
   }

   @Override
   public double computeAero(String region, int year, double maxX, double maxY,
         double minX, double minY) throws IOException {
      FileDataStore store = _dataStoreManager.getFileDataStore(region, year,
            DataFileType.FILE_LAND_SOIL);
      SimpleFeatureSource source = store.getFeatureSource();
      return EcoFactorCaculator.computeAero(source, maxX, maxY, minX, minY);
   }

   @Override
   public double computeAbio(long regionId, int year) throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAbio(long regionId, int year, String geom_string)
         throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAveg(long regionId, int year, String geom_string)
         throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAveg(long regionId, int year) throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAero(long regionId, int year, String geom_string)
         throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAero(long regionId, int year) throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAbio(long regionId, int year, double maxX, double maxY,
         double minX, double minY) throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAveg(long regionId, int year, double maxX, double maxY,
         double minX, double minY) throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public double computeAero(long regionId, int year, double maxX, double maxY,
         double minX, double minY) throws IOException {
      // TODO Auto-generated method stub
      return 0;
   }
}
