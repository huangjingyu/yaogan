package com.rockontrol.yaogan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.service.IShapefileService;

@Controller
public class ListControler {
   @Autowired
   private IShapefileService _service;

   @RequestMapping("/shapefile/list")
   public String list(Model model) {
      List<Shapefile> list = _service.findAll(Shapefile.class, "id");
      model.addAttribute("shapefiles", list);
      return "/shapefileList";
   }
}
