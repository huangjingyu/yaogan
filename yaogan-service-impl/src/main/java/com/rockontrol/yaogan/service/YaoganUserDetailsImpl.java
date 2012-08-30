package com.rockontrol.yaogan.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class YaoganUserDetailsImpl extends User implements IYaoganUserDetails {

   private static final long serialVersionUID = 1L;
   private Long userId;

   private Long tenantId;

   public YaoganUserDetailsImpl(Long userId, Long tenantId, String username,
         String password, boolean enabled, boolean accountNonExpired,
         boolean credentialsNonExpired, boolean accountNonLocked,
         Collection<? extends GrantedAuthority> authorities) {
      super(username, password, enabled, accountNonExpired, credentialsNonExpired,
            accountNonLocked, authorities);
      this.userId = userId;
      this.tenantId = tenantId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public void setTenantId(Long tenantId) {
      this.tenantId = tenantId;
   }

   public Long getUserId() {
      return this.userId;
   }

   public Long getTenantId() {
      return this.tenantId;
   }

}
