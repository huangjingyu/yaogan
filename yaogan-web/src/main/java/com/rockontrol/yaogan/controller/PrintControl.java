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
         // TODO
         String webRootPath = this.getWebRootPath();

         File img = service.copyTemplate(webRootPath + "WEB-INF/template/template1.jpg",
               webRootPath + "public/themeImage/temp.jpg");
         service.addComment(img, comment);
         service.addShapeLayer(placeId, time, category, img);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return "redirect:/print.jsp";
   }

   /**
    * 带"/"
    * 
    * @return
    * @throws Exception
    */
   private String getWebRootPath() throws Exception {
      URI uri = PrintControl.class.getResource("/").toURI();
      String webRootPath = uri.toURL().toString();
      System.out.println(webRootPath);
      String flag = "WEB-INF";
      int begin = webRootPath.indexOf("/") + 1;
      int end = webRootPath.indexOf(flag);
      webRootPath = webRootPath.substring(begin, end);
      return webRootPath;
   }
}
