package com.rockontrol.yaogan.model;

import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;

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
   private List<PlaceParam> params;
   private List<UserPlace> userPlaces;
   private List<Shapefile> shapeFiles;

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

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "place", cascade = { CascadeType.REMOVE })
   public List<PlaceParam> getParams() {
      return params;
   }

   public void setParams(List<PlaceParam> params) {
      this.params = params;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "place", cascade = { CascadeType.REMOVE })
   public List<UserPlace> getUserPlaces() {
      return userPlaces;
   }

   public void setUserPlaces(List<UserPlace> userPlaces) {
      this.userPlaces = userPlaces;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "place", cascade = { CascadeType.REMOVE })
   public List<Shapefile> getShapeFiles() {
      return shapeFiles;
   }

   public void setShapeFiles(List<Shapefile> shapeFiles) {
      this.shapeFiles = shapeFiles;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Place other = (Place) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      return true;
   }

}
