package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.PlaceParam;

public interface IPlaceParamDao extends IBaseDao<PlaceParam> {
   public List<PlaceParam> getPlaceParams(Long placeId, String time);

   public PlaceParam getPlaceParam(Long placeId, String time, String paramName);

   public PlaceParam getPlaceParam(String placeName, String time, String paramName);

   public void deleteParam(String placeName, String time, String paramName);
}
