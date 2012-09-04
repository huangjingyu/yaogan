package com.rockontrol.yaogan.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rockontrol.yaogan.dao.IOrganizationDao;
import com.rockontrol.yaogan.dao.IPlaceDao;
import com.rockontrol.yaogan.dao.IShapefileDao;
import com.rockontrol.yaogan.model.Organization;
import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.Shapefile.Category;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.vo.EnvStats;

@Service
public class YaoganServiceImpl implements IYaoganService {

   @Autowired
   protected IPlaceDao placeDao;

   @Autowired
   protected IShapefileDao shapefileDao;

   @Autowired
   protected IOrganizationDao orgDao;

   @Override
   public List<Place> getAllPlaces(User caller) {
      return placeDao.findAll();
   }

   @Override
   public Place findPlaceByName(String placeName) {
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
      return shapefileDao.getAvailableTimesOfPlace(placeId);
   }

   @Override
   public List<User> getAllUsersOfOrg(User caller, Long orgId) {
      Organization org = orgDao.get(orgId);
      return org == null ? null : org.getEmployees();
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
      return shapefileDao.getShapefiles(placeId, time);
   }

   @Override
   public void saveShapefile(String placeName, Category type, File file, String time) {
      Shapefile shapefile = new Shapefile();
      shapefile.setCategory(type);
      shapefile.setFileName(file.getName());
      shapefile.setShootTime(time);
      Place place = findPlaceByName(placeName);
      if (place == null) {
         place = new Place();
         place.setName(placeName);
         placeDao.save(place);
      }
      shapefile.setPlaceId(place.getId());
      // shapefile.setWmsUrl(geoService.publishShapeFile(file));
      shapefile.setWmsUrl(null);
      shapefileDao.save(shapefile);
   }

}
