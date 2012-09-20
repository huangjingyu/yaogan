package com.rockontrol.yaogan.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
@RequestMapping(value = { "/admin/envstats", "/user/envstats" })
public class EnvStatsController {

   @Autowired
   private ISecurityManager _secMng;

   @Autowired
   private IYaoganService _service;

   @RequestMapping("timeCompare")
   public String timeCompare(Model model,
         @RequestParam(value = "placeId", required = false) Long placeId) {
      User user = _secMng.currentUser();
      List<Place> places = _service.getPlacesVisibleToUser(user, user.getId());
      model.addAttribute("currentUser", user);
      model.addAttribute("places", places);
      if (placeId == null && places.size() > 0) {
         placeId = places.get(0).getId();
      }
      if (placeId != null) {
         model.addAttribute("curPlaceId", placeId);
      }
      return "/share/stats/timeCompare";
   }

   @RequestMapping("placeCompare")
   public String placeCompare(Model model,
         @RequestParam(value = "time", required = false) String time) {
      User user = _secMng.currentUser();
      List<String> times = _service.getAvailableTimesForUser(user, user.getId());
      model.addAttribute("times", times);
      model.addAttribute("currentUser", user);
      if (StringUtils.isEmpty(time) && times.size() > 0) {
         time = times.get(0);
      }
      if (!StringUtils.isEmpty(time)) {
         model.addAttribute("curTime", time);
      }
      return "/share/stats/placeCompare";
   }
}
