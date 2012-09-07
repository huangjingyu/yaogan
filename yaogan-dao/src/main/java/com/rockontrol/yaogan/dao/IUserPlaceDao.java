package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.UserPlace;

public interface IUserPlaceDao extends IBaseDao<UserPlace> {

   public List<Place> getPlacesVisibleToUser(Long userId);

   public Long getIdByUserIdPlaceId(Long userId, Long placeId);
}
