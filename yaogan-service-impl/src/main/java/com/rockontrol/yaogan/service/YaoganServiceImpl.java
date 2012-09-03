package com.rockontrol.yaogan.service;

import java.util.List;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.vo.EnvStats;

public class YaoganServiceImpl implements IYaoganService {

   @Override
   public List<Place> getAllPlaces(User caller) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Place> getPlacesVisibleToUser(User caller, Long userId) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<String> getAvailableTimeOptions(User caller, Long placeId) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<User> getAllUsersOfOrg(User caller, Long orgId) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void sharePlacesToUser(User caller, Long userId, Long[] placeIds) {
      // TODO Auto-generated method stub

   }

   @Override
   public void unsharePlaceToUser(User caller, Long userId, Long placeId) {
      // TODO Auto-generated method stub

   }

   @Override
   public EnvStats getEnvStats(User caller, Long placeId, String time) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public EnvStats computeEnvStats(User caller, Long placeId, String time) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public EnvStats computeEnvStats(User caller, Long placeId, String time,
         String geom_string) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Shapefile> getShapefiles(User caller, Long placeId, String time) {
      // TODO Auto-generated method stub
      return null;
   }

}
