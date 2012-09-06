package com.rockontrol.yaogan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/admin", "/user"})
public class LayersController {

   /**
    * 地图图层展示页面
    * @return
    */
   @RequestMapping("/layers")
   public String layers(Model model) {
      model.addAttribute("", "");
      return "/layers/indexlayers3";
   }
}
