package com.rockontrol.yaogan.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rockontrol.yaogan.service.IPrintImageService;

@Controller
@RequestMapping(value = { "user", "admin" })
public class PrintController {

   @Autowired
   private IPrintImageService service;

   @RequestMapping(value = "/print.json")
   @ResponseBody
   public Map print(@RequestParam("placeId") Long placeId,
         @RequestParam("time") String time, @RequestParam("category") String category) {
      File map = null;
      UUID uuid = UUID.randomUUID();
      try {
         map = service.getMap(placeId, time, category, this.getWebRootPath()
               + "public/temp/" + uuid.hashCode() + ".jpg");// TODO
      } catch (Exception e) {
         e.printStackTrace();
      }
      Map<String, String> json = new HashMap<String, String>();
      json.put("mapPath",map.getAbsolutePath());
      json.put("mapName",uuid.hashCode() + ".jpg");
      
      return json;
   }

   @RequestMapping("/save")
   public void preview(HttpServletResponse response, @RequestParam("comment") String comment,
         @RequestParam("mapPath") String mapPath) {
      String fileName = UUID.randomUUID().hashCode() + ".jpg";
      File themeImage  = null;
      try {
         String webRootPath = this.getWebRootPath();
         File template = service.copyTemplate(webRootPath
               + "public/template/template1.jpg", webRootPath + "public/temp/"
               + fileName); // TODO
         service.addComment(template, comment);
         File image = new File(mapPath);

        themeImage  = service.addShapeLayer(template, image);
      } catch (Exception e) {
         e.printStackTrace();
      }
      response.setContentType("image/jpeg");
      try {
		response.addHeader("content-disposition", "attachment;filename=" +(new String(comment.getBytes("UTF-8"),"ISO8859-1"))+".jpg");
		response.setContentLength((int)themeImage.length());
		ImageIO.write(ImageIO.read(themeImage), "image/jpeg", response.getOutputStream());
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}catch (IOException e) {
		e.printStackTrace();
	}      
   }

   /**
    * "yaogan-web/"
    * 
    * @return
    * @throws Exception
    */
   private String getWebRootPath() throws Exception {
      URI uri = PrintController.class.getResource("/").toURI();
      String filePath = uri.toURL().getFile();
      String flag = "WEB-INF";
      String webRootPath = filePath.substring(0, filePath.indexOf(flag));
      return webRootPath;
   }

}
