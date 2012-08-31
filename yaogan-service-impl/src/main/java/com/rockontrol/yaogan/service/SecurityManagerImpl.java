package com.rockontrol.yaogan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rockontrol.yaogan.dao.IUserDao;
import com.rockontrol.yaogan.model.User;

@Service("yaoganSecurityManager")
public class SecurityManagerImpl implements ISecurityManager {
   @Autowired
   private IUserDao userDao;

   @Override
   public IYaoganUserDetails userContext() {
      if (SecurityContextHolder.getContext() == null
            || SecurityContextHolder.getContext().getAuthentication() == null)
         return null;
      Object userObj = SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
      if (userObj == null)
         return null;
      if (userObj instanceof IYaoganUserDetails) {
         return (IYaoganUserDetails) userObj;
      } else {
         return null;
      }
   }

   @Override
   public User currentUser() {
      IYaoganUserDetails ctx = userContext();
      if (ctx == null)
         return null;
      return userDao.getUserByName(ctx.getUsername());
   }

}
