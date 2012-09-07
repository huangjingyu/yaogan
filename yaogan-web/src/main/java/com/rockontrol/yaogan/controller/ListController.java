package com.rockontrol.yaogan.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
@RequestMapping("admin/place")
public class ListController {
   @Autowired
   private IYaoganService _service;
   @Autowired
   private ISecurityManager _secMgr;

   @RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
   public String list(Model model) {
      List<Shapefile> list = null;
      // User caller = _secMgr.currentUser();
      // List<Place> placeList = _service.getPlacesVisibleToUser(caller,
      // caller.getId());
      model.addAttribute("shapefiles", mockList());
      return "/admin/stats/shapefileList";
   }

   @RequestMapping("/fileList")
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

   private List<Shapefile> mockList() {
      List<Shapefile> list = new ArrayList<Shapefile>();
      Place place = new Place();
      place.setName("平朔");
      String names[] = new String[] { "平朔_地类_2010.shp", "平朔_边界_2010.shp",
            "平朔_地塌陷_2010.shp", "平朔_地裂缝_2010.shp", "平朔_土壤侵蚀_2010.shp" };
      Shapefile.Category cas[] = new Shapefile.Category[] {
            Shapefile.Category.FILE_LAND_TYPE, Shapefile.Category.FILE_REGION_BOUNDARY,
            Shapefile.Category.FILE_LAND_COLLAPSE,
            Shapefile.Category.FILE_LAND_FRACTURE, Shapefile.Category.FILE_LAND_SOIL };
      for (int i = 0; i < names.length; i++) {
         Shapefile file = new Shapefile();
         file.setFileName(names[i]);
         file.setShootTime("2010");
         file.setPlace(place);
         file.setCategory(cas[i]);
         file.setUploadTime(new Date());
         list.add(file);
      }
      return list;
   }
}
