package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.Shapefile;

public interface IShapefileDao extends IBaseDao<Shapefile> {
   public List<String> getAvailableTimesOfPlace(Long placeId);

   public List<Shapefile> getShapefiles(Long placeId, String time);

   public Shapefile getShapefile(Long placeId, String time, String category);

   public List<Shapefile> getAvailableFilesOfUser(Long userId);
}
