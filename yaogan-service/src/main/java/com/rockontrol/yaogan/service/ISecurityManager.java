package com.rockontrol.yaogan.service;

import com.rockontrol.yaogan.model.User;

public interface ISecurityManager {
   IYaoganUserDetails userContext();

   User currentUser();
}
