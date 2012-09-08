package com.rockontrol.yaogan.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.model.Place;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
@RequestMapping("/admin/user")
public class UserManageController {

   @Autowired
   private ISecurityManager _secMng;

   @Autowired
   private IYaoganService _service;

   @RequestMapping(value = { "", "list" })
   public String list(Model model) {
      User user = _secMng.currentUser();
      List<User> list = _service.getAllUsersOfOrg(user, user.getOrgId());
      Collections.sort(list, new Comparator<User>() {
         @Override
         public int compare(User u1, User u2) {
            if (u1.getRole().equals(u2.getRole()))
               return 0;
            if (u1.getIsAdmin())
               return -1;
            else
               return 1;
         }
      });
      model.addAttribute("users", list);
      return "/admin/usermng/list";
   }

   @RequestMapping(value = "{userId}/sharePlacesForm")
   public String sharePalcesForm(@PathVariable("userId") Long userId, Model model) {
      User user = _secMng.currentUser();
      List<Place> sharedPlaces = _service.getPlacesVisibleToUser(user, userId);
      List<Place> allPlacesOfOrg = _service.getPlacesOfOrg(user, user.getOrgId());
      if (allPlacesOfOrg != null) {
         allPlacesOfOrg.removeAll(sharedPlaces);
      }
      List<Place> toSharePlaces = allPlacesOfOrg;
      model.addAttribute("sharedPlaces", sharedPlaces);
      model.addAttribute("toSharePlaces", toSharePlaces);
      model.addAttribute("editingUser", _service.getUser(user, userId));
      return "/admin/usermng/sharePlacesForm";
   }
}
