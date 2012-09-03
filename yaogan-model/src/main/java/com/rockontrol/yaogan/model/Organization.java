package com.rockontrol.yaogan.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Organization {

   private Long _id;
   private String _name;
   private List<User> _employees;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return _id;
   }

   public void setId(Long id) {
      this._id = id;
   }

   @Column(length = 128)
   public String getName() {
      return _name;
   }

   public void setName(String name) {
      this._name = name;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
   public List<User> getEmployees() {
      return _employees;
   }

   public void setEmployees(List<User> employees) {
      this._employees = employees;
   }

}
