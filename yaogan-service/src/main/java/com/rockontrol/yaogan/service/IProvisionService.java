package com.rockontrol.yaogan.service;

import com.rockontrol.yaogan.model.Organization;
import com.rockontrol.yaogan.model.User;

public interface IProvisionService {
   public void createOrg(Organization org);

   public void updateOrg(Long orgId, Organization org);

   public void deleteOrg(Long orgId);

   public void createUser(Long orgId, User user);

   public void updateUser(Long orgId, Long userId, User user);

   public void deleteUser(Long orgId, Long userId);
}
