package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;

public interface IShapefileDao extends IBaseDao<Shapefile> {
   public List<String> getAvailableTimesOfPlace(Long placeId);

   public List<Place> getAvailablePlacesOfUser(Long userId, String time);

   public List<Place> getAvailablePlacesOfOrg(Long orgId, String time);

   public List<String> getAvailableTimesOfOrg(Long orgId);

   public List<String> getAvailableTimesOfUser(Long userId);

   public List<Shapefile> getShapefiles(Long placeId, String time);

   public List<Shapefile> getShapefilesOfOrg(Long orgId);

   public List<Shapefile> getShapefilesOfOrg(Long orgId, Long placeId, String time);

   public Shapefile getShapefile(Long placeId, String time, String category);

   public List<Shapefile> getAvailableFilesOfUser(Long userId);

   public List<Shapefile> filter(Long placeId, String time, int startIndex, int maxCount);

   public long getCount(Long placeId, String time);

}
