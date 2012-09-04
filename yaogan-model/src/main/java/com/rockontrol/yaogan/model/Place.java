package com.rockontrol.yaogan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity(name = "place")
@NamedQueries({
      @NamedQuery(name = "Place.getByName", query = "from com.rockontrol.yaogan.model.Place"
            + " where name = :placeName"),
      @NamedQuery(name = "Place.getByOrgId", query = "from com.rockontrol.yaogan.model.Place"
            + " where orgId = :orgId") })
public class Place {
   private Long id;
   private String name;
   private Long orgId;
   private Organization organization;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @Column(name = "name", length = 256, unique = true, nullable = false)
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Column(name = "org_id")
   public Long getOrgId() {
      return orgId;
   }

   public void setOrgId(Long orgId) {
      this.orgId = orgId;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "org_id", insertable = false, updatable = false)
   public Organization getOrganization() {
      return organization;
   }

   public void setOrganization(Organization organization) {
      this.organization = organization;
   }

}
