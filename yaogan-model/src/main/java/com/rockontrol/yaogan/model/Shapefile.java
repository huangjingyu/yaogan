package com.rockontrol.yaogan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "shapefiles")
public class Shapefile {

   public enum Category {
      FILE_REGION_BOUNDARY("边界"), FILE_LAND_TYPE("地类"), FILE_LAND_COLLAPSE("地塌陷"), FILE_LAND_FRACTURE(
            "地裂缝"), FILE_LAND_SOIL("土壤侵蚀");
      private String name;

      private Category(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

   }

   private Long _id;
   private String _fileName;
   private String _WMSURL;
   private String _shootTime;
   private Category _category;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return _id;
   }

   public void setId(Long id) {
      this._id = id;
   }

   @Column(length = 64, unique = true, nullable = false)
   public String getFileName() {
      return _fileName;
   }

   public void setFileName(String fileName) {
      this._fileName = fileName;
   }

   @Column(length = 128)
   public String getWMSURL() {
      return _WMSURL;
   }

   public void setWMSURL(String WMSURL) {
      this._WMSURL = WMSURL;
   }

   @Column(length = 64)
   public String getShootTime() {
      return _shootTime;
   }

   public void set_shootTime(String shootTime) {
      this._shootTime = shootTime;
   }

   @Enumerated(EnumType.STRING)
   public Category getCategory() {
      return _category;
   }

   public void setCategory(Category category) {
      this._category = category;
   }
}
