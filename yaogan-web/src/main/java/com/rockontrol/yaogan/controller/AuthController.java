package com.rockontrol.yaogan.controller;

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
   }
}
