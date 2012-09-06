package com.rockontrol.yaogan.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.ISecurityManager;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
@RequestMapping(value = "/api/place")
public class PlaceRestController {

   @Autowired
   private ISecurityManager _secMng;

   @Autowired
   private IYaoganService _service;
   
   @RequestMapping(value = "{placeId}/availableTimes")
   public List<String> availableTimes(@PathVariable("placeId") Long placeId) {
      User user = _secMng.currentUser();
      return _service.getAvailableTimeOptions(user, placeId);
   }
}
