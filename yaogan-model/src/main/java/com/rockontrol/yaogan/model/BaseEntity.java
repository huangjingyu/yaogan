package com.rockontrol.yaogan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
public class BaseEntity implements Serializable {
   protected Long id;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }
}
