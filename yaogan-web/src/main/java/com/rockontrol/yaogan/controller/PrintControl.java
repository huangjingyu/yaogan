package com.rockontrol.yaogan.controller;

import java.io.File;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rockontrol.yaogan.service.IPrintImageService;

@Controller
public class PrintControl {

   @Autowired
   private IPrintImageService service;

   @RequestMapping("/shapefile/print")
   public String print(@RequestParam("comment") String comment,
         @RequestParam("placeId") Long placeId, @RequestParam("time") String time,
         @RequestParam("category") String category) {
      try {

         URI uri = PrintControl.class.getResource("/").toURI();
         String classPathRoot = uri.toURL().toString();
         System.out.println(classPathRoot);

         File img = service.copyTemplate("..//" + classPathRoot
               + "//template//template1.jpg", "..//..//themeImage//temp.jpg");
         service.addComment(img, comment);
         service.addShapeLayer(placeId, time, category, img);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return "redirect:print.jsp";
   }
}
