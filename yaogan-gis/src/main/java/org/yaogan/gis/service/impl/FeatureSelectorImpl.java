package org.yaogan.gis.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.yaogan.gis.service.IFeatureSelector;

import com.vividsolutions.jts.geom.Geometry;

public class FeatureSelectorImpl implements IFeatureSelector {

   @Override
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
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

   @Override
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         Geometry boundary) throws IOException {
      FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
      Filter filter = ff.within(ff.property("the_geom"), ff.literal(boundary));
      SimpleFeatureCollection featureCollection = source.getFeatures(filter);
      return featureCollection;
   }

   @Override
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         double maxX, double maxY, double minX, double minY) throws IOException {
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

   @Override
   public SimpleFeatureCollection selectFeatures(SimpleFeatureSource source,
         String cqlString) throws Exception {
      Filter filter = CQL.toFilter(cqlString);
      SimpleFeatureCollection featureCollection = source.getFeatures(filter);
      return featureCollection;
   }

}
