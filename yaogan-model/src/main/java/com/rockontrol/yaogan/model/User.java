package com.rockontrol.yaogan.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity(name = "users")
public class User {
   public enum Role {
      ROLE_ADMIN, ROLE_USER
   }

   private Long id;
   private Long orgId;
   private String userName;
   private String password;
   private String email;
   private Role role;
   private String realName;
   private String mobile;
   private Organization organization;
   private List<UserPlace> userPlaces;

   @Id
   @Column(name = "id")
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @Column(name = "org_id")
   public Long getOrgId() {
      return orgId;
   }

   public void setOrgId(Long orgId) {
      this.orgId = orgId;
   }

   @Column(length = 128, unique = true, nullable = false)
   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   @Column(name = "password", length = 128)
   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   @Column(name = "email", length = 128, unique = true)
   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   @Enumerated(EnumType.STRING)
   @Column(name = "role", length = 32)
   public Role getRole() {
      return role;
   }

   public void setRole(Role role) {
      this.role = role;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "org_id", insertable = false, updatable = false)
   public Organization getOrganization() {
      return organization;
   }

   public void setOrganization(Organization organization) {
      this.organization = organization;
   }

   public String getRealName() {
      return realName;
   }

   public void setRealName(String realName) {
      this.realName = realName;
   }

   public String getMobile() {
      return mobile;
   }

   public void setMobile(String mobile) {
      this.mobile = mobile;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = { CascadeType.REMOVE })
   public List<UserPlace> getUserPlaces() {
      return userPlaces;
   }

   public void setUserPlaces(List<UserPlace> userPlaces) {
      this.userPlaces = userPlaces;
   }

   @Transient
   public Boolean getIsAdmin() {
      return Role.ROLE_ADMIN.equals(role);
   }

   public void setIsAdmin(Boolean isAdmin) {
      if (Boolean.TRUE.equals(isAdmin))
         this.role = Role.ROLE_ADMIN;
      else
         this.role = Role.ROLE_USER;
   }
}
