package com.rockontrol.yaogan.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;
import com.rockontrol.yaogan.vo.ShapefileGroupVo;
import com.rockontrol.yaogan.vo.ShapefileVo;

@Controller
@RequestMapping("admin/place")
public class ListController {
   @Autowired
   private IYaoganService _service;
   @Autowired
   private ISecurityManager _secMgr;

   @RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
   public String list(Model model) {
      User caller = _secMgr.currentUser();
      List<Shapefile> list = new ArrayList();
      if (caller.getIsAdmin())
         list = _service.getShapefilesOfOrg(caller.getOrgId());
      else
         list = _service.getShapefiles(caller);
      // model.addAttribute("shapefiles", list);
      List<ShapefileGroupVo> groupVoList = groupShapefiles(list);
      model.addAttribute("shapefiles", groupVoList);
      model.addAttribute("places",
            _service.getPlacesVisibleToUser(caller, caller.getId()));
      return "/admin/stats/shapefileList";
   }

   @RequestMapping("/fileList")
   public String list(Model model, @RequestParam("placeId") Long placeId,
         @RequestParam("shootTime") String shootTime) {
      if (placeId == null) {
         return "redirect:/admin/place";
      }
      Place place = _service.getPlaceById(placeId);
      User caller = _secMgr.currentUser();
      List<Shapefile> list = new ArrayList<Shapefile>();
      if (place != null && shootTime != null) {
         if (caller.getIsAdmin()) {
            list = _service.getShapefileOfOrg(caller.getOrgId(), place.getId(),
                  shootTime);
         } else {
            list = _service.getShapefiles(caller, place.getId(), shootTime);
         }
      }
      // model.addAttribute("shapefiles", list);
      List<ShapefileGroupVo> groupVoList = groupShapefiles(list);
      model.addAttribute("shapefiles", groupVoList);
      model.addAttribute("places",
            _service.getPlacesVisibleToUser(caller, caller.getId()));
      model.addAttribute("curPlaceId", placeId);
      model.addAttribute("curShootTime", shootTime);
      model.addAttribute("shootTimes", _service.getAvailableTimeOptions(caller, placeId));
      return "/admin/stats/shapefileList";

   }

   public void setSecMgr(ISecurityManager secMgr) {
      this._secMgr = secMgr;
   }

   public void setYaoganService(IYaoganService service) {
      this._service = service;
   }

   private List<ShapefileGroupVo> groupShapefiles(List<Shapefile> list) {
      Map<String, ShapefileGroupVo> groupVoMap = new HashMap<String, ShapefileGroupVo>();
      for (Shapefile shpfile : list) {
         String placeName = shpfile.getPlace().getName();
         ShapefileGroupVo vo = groupVoMap.get(placeName);
         if (vo == null) {
            vo = new ShapefileGroupVo();
            vo.setPlaceName(placeName);
            groupVoMap.put(placeName, vo);
         }
         ShapefileVo shpvo = toShapefileVo(shpfile);
         vo.addShapefileVo(shpvo);

      }
      return new ArrayList(groupVoMap.values());
   }

   private ShapefileVo toShapefileVo(Shapefile shpfile) {
      ShapefileVo vo = new ShapefileVo();
      vo.setFileName(shpfile.getFileName());
      vo.setPlace(shpfile.getPlace().getName());
      vo.setShootTime(shpfile.getShootTime());
      vo.setUploadTime(shpfile.getUploadTime());
      vo.setTypeString(shpfile.getTypeString());
      return vo;
   }
}
