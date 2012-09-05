package com.rockontrol.yaogan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rockontrol.yaogan.dao.IUserDao;
import com.rockontrol.yaogan.model.User;

@Service("yaoganSecurityManager")
public class SecurityManagerImpl implements ISecurityManager {
   @Autowired
   private IUserDao userDao;

   @Override
   public UserDetails userContext() {
      if (SecurityContextHolder.getContext() == null
            || SecurityContextHolder.getContext().getAuthentication() == null)
         return null;
      Object userObj = SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
      return (UserDetails) userObj;
   }

   @Override
   public User currentUser() {
      UserDetails ctx = userContext();
      if (ctx == null)
         return null;
      return userDao.getUserByName(ctx.getUsername());
   }

}
