package org.yaogan.gis.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * simple util for feature retrival
 * 
 * @author Administrator
 * 
 */
public class FeatureSelector {

   public static SimpleFeatureCollection selectFeatureWithinBoundary(Geometry boundary,
         SimpleFeatureSource source) throws IOException {
      FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

      Filter filter = ff.within(ff.property("the_geom"), ff.literal(boundary));
      SimpleFeatureCollection collection = source.getFeatures(filter);
      return collection;
   }

   public static SimpleFeatureCollection selectFeatureWithinBBox(
         SimpleFeatureSource source, double maxX, double maxY, double minX, double minY)
         throws IOException {
      FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
      SimpleFeatureType schema = source.getSchema();
      String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
      CoordinateReferenceSystem targetCRS = schema.getGeometryDescriptor()
            .getCoordinateReferenceSystem();
      ReferencedEnvelope bbox = new ReferencedEnvelope(maxX, maxY, minX, minY, targetCRS);
      Filter filter = ff.bbox(ff.property(geometryPropertyName), bbox);
      SimpleFeatureCollection featureCollection = source.getFeatures(filter);
      return featureCollection;
   }

   public static SimpleFeature selectTheFeature(SimpleFeatureSource source,
         String geom_str) throws IOException, CQLException {

      Filter filter = ECQL.toFilter("EQUALS(the_geom," + geom_str + ")");
      SimpleFeatureCollection collection = source.getFeatures(filter);
      SimpleFeatureIterator iterator = collection.features();
      if (iterator.hasNext())
         return iterator.next();
      return null;
   }

   public static SimpleFeatureCollection selectFeatureByIDS(SimpleFeatureSource source,
         List<String> ids) throws IOException {
      FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
      Set<FeatureId> fids = new HashSet<FeatureId>();
      for (String id : ids) {
         FeatureId featureId = ff.featureId(id);
         fids.add(featureId);
      }
      Filter filter = ff.id(fids);
      SimpleFeatureCollection featureCollection = source.getFeatures(filter);
      return featureCollection;
   }

}
