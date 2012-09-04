package com.rockontrol.yaogan.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.Place;

@Repository("placeDao")
public class PlaceDaoImpl extends BaseDaoImpl<Place> implements IPlaceDao {

   @SuppressWarnings("unchecked")
   @Override
   public List<Place> getPlacesOfOrg(Long orgId) {
      Query query = getSession().getNamedQuery("Place.getByOrgId");
      query.setLong("orgId", orgId);
      return query.list();
   }

   @Override
   public Place getPlaceByName(String placeName) {
      Query query = getSession().getNamedQuery("Place.getByName");
      query.setString("placeName", placeName);
      return (Place) query.uniqueResult();
   }

}
