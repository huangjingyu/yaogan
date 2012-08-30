package com.rockontrol.yaogan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rockontrol.yaogan.dao.IUserDao;
import com.rockontrol.yaogan.model.User;

@Service("yaoganUserDetailsService")
public class YaoganUserDetailsService implements UserDetailsService {

   @Autowired
   private IUserDao userDao;

   @Transactional
   public UserDetails loadUserByUsername(String username)
         throws UsernameNotFoundException {
      User user = userDao.getUserByName(username);

      if (user == null) {
         throw new UsernameNotFoundException(
               "Security::Error in retrieving user(username=" + username + ")");
      }

      Long tenantId = null;
      if (user.getTenant() != null) {
         tenantId = user.getTenant().getId();
      }
      boolean enabled = true;
      boolean acountNonExpired = true;
      boolean credentialsNonExpired = true;
      boolean accountNonLock = true;
      /*
       * if (user.getUserState() != null) { enabled = UserState.DISABLED !=
       * user.getUserState(); accountNonLock = UserState.LOCKED !=
       * user.getUserState(); }
       */
      IYaoganUserDetails yaoganUserDetails = new YaoganUserDetailsImpl(user.getId(),
            tenantId, user.getUserName(), user.getPassword(), enabled, acountNonExpired,
            credentialsNonExpired, accountNonLock, null);
      return yaoganUserDetails;

   }
}