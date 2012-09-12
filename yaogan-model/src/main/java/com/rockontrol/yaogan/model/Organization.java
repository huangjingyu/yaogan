package com.rockontrol.yaogan.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Organization extends BaseEntity {

   private String name;
   private String contactEmail;
   private String postCode;
   private String address;
   private List<User> employees;
   private List<Place> places;

   @Column(length = 128)
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Column(name = "contact_email", length = 128)
   public String getContactEmail() {
      return contactEmail;
   }

   public void setContactEmail(String contactEmail) {
      this.contactEmail = contactEmail;
   }

   @Column(name = "post_code", length = 32)
   public String getPostCode() {
      return postCode;
   }

   public void setPostCode(String postCode) {
      this.postCode = postCode;
   }

   @Column(name = "address")
   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = { CascadeType.REMOVE })
   public List<User> getEmployees() {
      return employees;
   }

   public void setEmployees(List<User> employees) {
      this.employees = employees;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = { CascadeType.REMOVE })
   public List<Place> getPlaces() {
      return places;
   }

   public void setPlaces(List<Place> places) {
      this.places = places;
   }

}
