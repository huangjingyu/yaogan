package com.rockontrol.yaogan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.service.IYaoganService;

@Controller
public class ListControler {
   @Autowired
   private IYaoganService _service;

   @RequestMapping("/shapefile/list")
   public String list(Model model) {
      List<Shapefile> list = null; // TODO
      model.addAttribute("shapefiles", list);
      return "/shapefileList";
   }
}
