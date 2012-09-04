package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.Place;

public interface IPlaceDao extends IBaseDao<Place> {
   public List<Place> getPlacesOfOrg(Long orgId);

   public Place getPlaceByName(String placeName);
}
