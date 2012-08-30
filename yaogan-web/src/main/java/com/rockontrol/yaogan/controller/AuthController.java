package com.rockontrol.yaogan.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

   @RequestMapping(value = "/auth/login")
   public String login() {
      return "redirect:/home";
   }

   @RequestMapping(value = "/doLogout")
   public void logout(HttpServletRequest request, HttpServletResponse response) {
      String casLogout = "https://sso.rkcloud.cn/cas/logout";

      String protocal = request.getScheme();
      String server = request.getServerName();
      int port = request.getServerPort();
      String contextPath = request.getContextPath();
      String service = protocal + ":" + "//" + server;
      if (port != 80) {
         service += ":" + port;
      }
      service += contextPath;

      casLogout += "?service=" + service;

      request.getSession().invalidate();
      try {
         response.sendRedirect(casLogout);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
