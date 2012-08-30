package com.rockontrol.yaogan.servimpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("yaoganUserDetailsService")
public class YaoganUserDetailsService implements UserDetailsService {

   public UserDetails loadUserByUsername(String username)
         throws UsernameNotFoundException {
      // TODO Auto-generated method stub
      return null;
   }

}
