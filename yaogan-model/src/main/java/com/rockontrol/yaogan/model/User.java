package com.rockontrol.yaogan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name = "users")
public class User {
   public enum Role {
      ROLE_ADMIN, ROLE_USER
   }

   private Long _id;
   private Long _orgId;
   private String _userName;
   private String _password;
   private String _email;
   private Role _role;
   private Organization _organization;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return _id;
   }

   public void setId(Long id) {
      this._id = id;
   }

   @Column(name = "org_id")
   public Long getOrgId() {
      return _orgId;
   }

   public void setOrgId(Long orgId) {
      this._orgId = orgId;
   }

   @Column(length = 128, unique = true, nullable = false)
   public String getUserName() {
      return _userName;
   }

   public void setUserName(String userName) {
      this._userName = userName;
   }

   @Column(name = "password", length = 128)
   public String getPassword() {
      return _password;
   }

   public void setPassword(String password) {
      this._password = password;
   }

   @Column(name = "email", length = 128, unique = true)
   public String getEmail() {
      return _email;
   }

   public void setEmail(String email) {
      this._email = email;
   }

   @Enumerated(EnumType.STRING)
   @Column(name = "role", length = 32)
   public Role getRole() {
      return _role;
   }

   public void setRole(Role role) {
      this._role = role;
   }

   @ManyToOne(fetch = FetchType.LAZY, optional = true)
   @JoinColumn(name = "org_id", insertable = false, updatable = false)
   public Organization getOrganization() {
      return _organization;
   }

   public void setOrganization(Organization organization) {
      this._organization = organization;
   }

   @Transient
   public boolean isAdmin() {
      return Role.ROLE_ADMIN.equals(_role);
   }
}
