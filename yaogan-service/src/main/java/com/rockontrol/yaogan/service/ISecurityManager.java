package com.rockontrol.yaogan.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.rockontrol.yaogan.model.User;

public interface ISecurityManager {
   UserDetails userContext();

   User currentUser();
}
