package com.rockontrol.yaogan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
public class ListControler {
   @Autowired
   private IYaoganService _service;
   @Autowired
   private ISecurityManager _secMgr;

   @RequestMapping("/shapefile/list")
   public String list(Model model) {
      List<Shapefile> list = null;
      User caller = _secMgr.currentUser();
      List<Place> placeList = _service.getPlacesVisibleToUser(caller, caller.getId());

      // _service.getShapefiles(caller, placeId, time);
      model.addAttribute("shapefiles", list);
      return "/shapefileList";
   }

   @RequestMapping("/shapefile/list")
   public String list(Model model, @RequestParam("place") String placeName,
         @RequestParam("shootTime") String shootTime) {
      Place place = _service.getPlaceByName(placeName);
      if (place != null) {
         _service.getShapefiles(null, place.getId(), shootTime);
      }
      return "/admin/stats/shapefileList";

   }

   public void setSecMgr(ISecurityManager secMgr) {
      this._secMgr = secMgr;
   }

   public void setYaoganService(IYaoganService service) {
      this._service = service;
   }
}
