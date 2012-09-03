package com.rockontrol.yaogan.service;

import org.she.mvc.db.IBaseService;

import com.rockontrol.yaogan.model.Place;

public interface IPlaceService extends IBaseService {
   public Place findPlaceByName(String region);
}
