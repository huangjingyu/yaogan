package com.rockontrol.yaogan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/share/envstats")
public class EnvStatsController {

   @RequestMapping("timeCompare")
   public String timeCompare(Model model) {
      return "/share/stats/timeCompare";
   }
}
