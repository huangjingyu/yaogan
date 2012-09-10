package com.rockontrol.yaogan.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.UserPlace;

@Repository("userPlaceDaoImpl")
public class UserPlaceDaoImpl extends BaseDaoImpl<UserPlace> implements IUserPlaceDao {

   @Override
   public List<Place> getPlacesVisibleToUser(Long userId) {
      List<Place> li = new ArrayList<Place>();
      Query query = getSession().createQuery(
            "select up.placeId from com.rockontrol.yaogan.model.UserPlace as up where up.userId="
                  + userId);
      List list = query.list();
      for (int i = 0; i < list.size(); i++) {
         Long uId = (Long) list.get(i);
         Place pl = (Place) getSession().get(Place.class, uId);
         li.add(pl);
      }
      return li;
   }

   @Override
   public Long getIdByUserIdPlaceId(Long userId, Long placeId) {
      Query query = getSession().createQuery(
            "select up.id from com.rockontrol.yaogan.model.UserPlace as up where up.userId="
                  + userId + " and up.placeId=" + placeId);
      Long i = (Long) query.uniqueResult();
      return i;
   }

}
