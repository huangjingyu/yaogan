package com.rockontrol.yaogan.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.PlaceParam;

@Repository("placeParamDao")
public class PlaceParamDaoImpl extends BaseDaoImpl<PlaceParam> implements IPlaceParamDao {

   @Override
   public List<PlaceParam> getPlaceParams(Long placeId, String time) {
      Query query = getSession()
            .createQuery(
                  "from com.rockontrol.yaogan.model.PlaceParam where placeId=:placeId and time=:time");
      query.setLong("placeId", placeId);
      query.setString("time", time);
      return query.list();
   }

   @Override
   public PlaceParam getPlaceParam(Long placeId, String time, String paramName) {
      Query query = getSession()
            .createQuery(
                  "from com.rockontrol.yaogan.model.PlaceParam where placeId=:placeId and time=:time and paramName=:paramName");
      query.setLong("placeId", placeId);
      query.setString("time", time);
      query.setString("paramName", paramName);
      return (PlaceParam) query.uniqueResult();
   }

}
