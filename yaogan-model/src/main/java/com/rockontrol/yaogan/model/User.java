package com.rockontrol.yaogan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "users")
public class User {

   private Long _id;
   private Long tenantId;
   private String _userName;
   private String _password;
   private String _email;
   private Tenant _tenant;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return _id;
   }

   public void setId(Long id) {
      this._id = id;
   }

   @Column(name = "tenant_id")
   public Long getTenantId() {
      return tenantId;
   }

   public void setTenantId(Long tenantId) {
      this.tenantId = tenantId;
   }

   @Column(length = 64, unique = true, nullable = false)
   public String getUserName() {
      return _userName;
   }

   public void setUserName(String userName) {
      this._userName = userName;
   }

   @Column(length = 128)
   public String getPassword() {
      return _password;
   }

   public void setPassword(String password) {
      this._password = password;
   }

   @Column(length = 64, unique = true)
   public String getEmail() {
      return _email;
   }

   public void setEmail(String email) {
      this._email = email;
   }

   @ManyToOne(fetch = FetchType.LAZY, optional = true)
   @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
   public Tenant getTenant() {
      return _tenant;
   }

   public void setTenant(Tenant tenant) {
      this._tenant = tenant;
   }
}
