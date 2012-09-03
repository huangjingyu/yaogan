package com.rockontrol.yaogan.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class YaoganUserDetailsImpl extends User implements IYaoganUserDetails {

   private static final long serialVersionUID = 1L;
   private Long _userId;

   private Long _orgId;

   public YaoganUserDetailsImpl(Long userId, Long orgId, String username,
         String password, boolean enabled, boolean accountNonExpired,
         boolean credentialsNonExpired, boolean accountNonLocked,
         Collection<? extends GrantedAuthority> authorities) {
      super(username, password, enabled, accountNonExpired, credentialsNonExpired,
            accountNonLocked, authorities);
      this._userId = userId;
      this._orgId = orgId;
   }

   public void setUserId(Long userId) {
      this._userId = userId;
   }

   public void setOrgId(Long orgId) {
      this._orgId = orgId;
   }

   public Long getUserId() {
      return this._userId;
   }

   public Long getOrgId() {
      return this._orgId;
   }

}
