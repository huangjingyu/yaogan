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
}
