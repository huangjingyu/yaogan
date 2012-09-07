package com.rockontrol.yaogan.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;

@Repository("shapefileDao")
public class ShapefileDaoImpl extends BaseDaoImpl<Shapefile> implements IShapefileDao {

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getAvailableTimesOfPlace(Long placeId) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailableTimesOfPlace");
      query.setLong("placeId", placeId);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Place> getAvailablePlacesOfUser(Long userId, String time) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailablePlacesOfUser");
      query.setLong("userId", userId);
      query.setString("time", time);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Place> getAvailablePlacesOfOrg(Long orgId, String time) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailablePlacesOfOrg");
      query.setLong("orgId", orgId);
      query.setString("time", time);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getAvailableTimesOfOrg(Long orgId) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailableTimesOfOrg");
      query.setLong("orgId", orgId);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getAvailableTimesOfUser(Long userId) {
      Query query = getSession().getNamedQuery("Shapefile.getAvailableTimesOfUser");
      query.setLong("userId", userId);
      return query.list();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Shapefile> getShapefiles(Long placeId, String time) {
      Query query = getSession().getNamedQuery("Shapefile.getShapefilesByPlaceAndTime");
      query.setLong("placeId", placeId);
      query.setString("time", time);
      return query.list();
   }

}
