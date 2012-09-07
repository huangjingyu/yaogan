package com.rockontrol.yaogan.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rockontrol.yaogan.util.GlobalConfig;

@Controller
public class AuthController {

   @RequestMapping(value = "/auth/login")
   public String login() {
      return "redirect:/";
   }

   @RequestMapping(value = "/doLogout")
   public void logout(HttpServletRequest request, HttpServletResponse response) {
      String casLogout = (String) GlobalConfig.getProperties().getProperty(
            "cas.logout.url");

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
