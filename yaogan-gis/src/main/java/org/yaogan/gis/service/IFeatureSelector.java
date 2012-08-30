package org.yaogan.gis.service;

import java.io.IOException;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;

import com.vividsolutions.jts.geom.Geometry;

public interface IFeatureSelector {

   /**
    * select features by ids(id in shapefile)
    * 
    * @param source
    * @param ids
    * @return
    * @throws IOException
    */
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         List<String> ids) throws IOException;

   /**
    * select feature by boundary(all features within boundary)
    * 
    * @param source
    * @param boundary
    * @return
    * @throws IOException
    */
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         Geometry boundary) throws IOException;

   /**
    * select feature by boundary box
    * 
    * @param source
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         double maxX, double maxY, double minX, double minY) throws IOException;

   /**
    * select feature by cql
    * 
    * @param source
    * @param cqlString
    * @return
    * @throws Exception
    */
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         String cqlString) throws Exception;
}
