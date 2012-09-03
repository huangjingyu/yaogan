package com.rockontrol.yaogan.service;

import org.she.mvc.db.hibernate.service.DefaultService;
import org.springframework.stereotype.Service;

import com.rockontrol.yaogan.model.Place;

@Service("placeService")
public class PlaceServiceImpl extends DefaultService implements IPlaceService {

   @Override
   public Place findPlaceByName(String region) {
      Place palce = this.findOne(Place.class, "name", region);
      return palce;
   }

}
