package com.rockontrol.yaogan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
@RequestMapping("/share/envstats")
public class EnvStatsController {

   @Autowired
   private ISecurityManager _secMng;

   @Autowired
   private IYaoganService _service;

   @RequestMapping("timeCompare")
   public String timeCompare(Model model) {
      User user = _secMng.currentUser();
      List<Place> places = null;
      if (user.isAdmin()) {
         places = _service.getPlacesOfOrg(user, user.getOrgId());
      } else {
         places = _service.getPlacesVisibleToUser(user, user.getId());
      }
      model.addAttribute("places", places);
      return "/share/stats/timeCompare";
   }
}
