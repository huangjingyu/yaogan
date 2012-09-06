package com.rockontrol.yaogan.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rockontrol.yaogan.model.Organization;
import com.rockontrol.yaogan.model.User;
import com.rockontrol.yaogan.service.IProvisionService;

@Controller
@RequestMapping("/api/provision")
public class ProvisionRestController {

   @Autowired
   private IProvisionService _service;

   @RequestMapping(value = "orgs", method = RequestMethod.POST)
   @ResponseBody
   public void createOrg(@RequestBody Organization org) {
      _service.createOrg(org);
   }

   @RequestMapping(value = "org/{orgId}", method = RequestMethod.PUT)
   @ResponseBody
   public void updateOrg(@PathVariable("orgId") Long orgId, @RequestBody Organization org) {
      _service.updateOrg(orgId, org);
   }

   @RequestMapping(value = "org/{orgId}", method = RequestMethod.DELETE)
   @ResponseBody
   public void deleteOrg(@PathVariable("orgId") Long orgId) {
      _service.deleteOrg(orgId);
   }

   @RequestMapping(value = "org/{orgId}/users", method = RequestMethod.POST)
   @ResponseBody
   public void createUser(@PathVariable("orgId") Long orgId, @RequestBody User user) {
      _service.createUser(orgId, user);
   }

   @RequestMapping(value = "org/{orgId}/user/{userId}", method = RequestMethod.PUT)
   @ResponseBody
   public void updateUser(@PathVariable("orgId") Long orgId,
         @PathVariable("userId") Long userId, @RequestBody User user) {
      _service.updateUser(orgId, userId, user);
   }

   @RequestMapping(value = "org/{orgId}/user/{userId}", method = RequestMethod.DELETE)
   @ResponseBody
   public void deleteUser(@PathVariable("orgId") Long orgId,
         @PathVariable("userId") Long userId) {
      _service.deleteUser(orgId, userId);
   }
}
