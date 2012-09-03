package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.Shapefile;

public interface IShapefileDao extends IBaseDao<Shapefile> {
   public List<String> getAvailableTimesOfPlace(Long placeId);
}
