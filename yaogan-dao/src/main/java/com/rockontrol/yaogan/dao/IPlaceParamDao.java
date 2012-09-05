package com.rockontrol.yaogan.dao;

import java.util.List;

import com.rockontrol.yaogan.model.PlaceParam;

public interface IPlaceParamDao extends IBaseDao<PlaceParam> {
   public List<PlaceParam> getPlaceParams(Long placeId, String time);

   public PlaceParam getPlaceParam(Long placeId, String time, String paramName);
}
