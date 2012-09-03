package com.rockontrol.yaogan.service;

import java.util.List;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;

public interface IYaoganService {

   public List<Place> getAllPlaces(User caller);

   public List<Place> getPlacesVisibleToUser(User caller, Long userId);

   public List<String> getAvailableTimeOptions(User caller, Long placeId);

   public List<User> getAllUsersOfOrg(User caller, Long orgId);

   public void sharePlacesToUser(User caller, Long userId, Long[] placeId);

   public void unsharePlaceToUser(User caller, Long userId, Long placeId);
}
