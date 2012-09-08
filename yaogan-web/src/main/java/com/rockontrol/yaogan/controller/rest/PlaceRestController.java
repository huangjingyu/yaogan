package com.rockontrol.yaogan.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;
import com.rockontrol.yaogan.util.Constants;

@Controller
@RequestMapping(value = "/api")
public class PlaceRestController {

   @Autowired
   private ISecurityManager _secMng;

   @Autowired
   private IYaoganService _service;

   @RequestMapping(value = "place/{placeId}/availableTimes")
   public List<String> availableTimes(@PathVariable("placeId") Long placeId) {
      User user = _secMng.currentUser();
      return _service.getAvailableTimeOptions(user, placeId);
   }

   @RequestMapping(value = "/places")
   public List<Map<String, Object>> availablePlaces(
         @RequestParam(value = "time", required = false) String time) {
      User user = _secMng.currentUser();
      List<Place> list = _service.getPlacesVisibleToUser(user, user.getId(), time);
      List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(list.size());
      for (Place place : list) {
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("id", place.getId());
         map.put("name", place.getName());
         ret.add(map);
      }
      return ret;
   }

   @RequestMapping(value = "place/myAvailableTimes")
   public List<String> avaialbeTimesOfCurrentUser() {
      User user = _secMng.currentUser();
      return _service.getAvailableTimesForUser(user, user.getId());
   }

   @RequestMapping(value = "places/{placeIds}/share/user/{userId}")
   public void sharePlacesToUser(@PathVariable("placeIds") String placeIdsStr,
         @PathVariable("userId") Long userId) {
      User user = _secMng.currentUser();
      String[] arr = Constants.PAT_COMMA.split(placeIdsStr);
      Long[] placeIds = new Long[arr.length];
      for (int i = 0; i < arr.length; i++) {
         placeIds[i] = Long.valueOf(arr[i]);
      }
      _service.sharePlacesToUser(user, userId, placeIds);
   }

   @RequestMapping(value = "places/{placeIds}/unshare/user/{userId}")
   public void unsharePlacesFromUser(@PathVariable("placeIds") String placeIdsStr,
         @PathVariable("userId") Long userId) {
      User user = _secMng.currentUser();
      String[] arr = Constants.PAT_COMMA.split(placeIdsStr);
      Long[] placeIds = new Long[arr.length];
      for (int i = 0; i < arr.length; i++) {
         placeIds[i] = Long.valueOf(arr[i]);
      }
      for (Long placeId : placeIds) {
         _service.unsharePlaceToUser(user, userId, placeId);
      }
   }
}
