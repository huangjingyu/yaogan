package com.rockontrol.yaogan.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IYaoganUserDetails extends UserDetails {

   Long getUserId();

   Long getTenantId();

}
