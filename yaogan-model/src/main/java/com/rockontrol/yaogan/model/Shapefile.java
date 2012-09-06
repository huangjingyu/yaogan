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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity(name = "shapefiles")
@NamedQueries({
      @NamedQuery(name = "Shapefile.getAvailableTimesOfPlace", query = "select distinct shootTime from com.rockontrol.yaogan.model.Shapefile"
            + " where placeId = :placeId"),
      @NamedQuery(name = "Shapefile.getShapefilesByPlaceAndTime", query = "from com.rockontrol.yaogan.model.Shapefile"
            + " where placeId = :placeId and shootTime = :time") })
public class Shapefile {

   public enum Category {
      FILE_REGION_BOUNDARY("边界"), FILE_LAND_TYPE("土地利用"), FILE_LAND_COLLAPSE("地表塌陷"), FILE_LAND_FRACTURE(
            "地裂缝"), FILE_LAND_SOIL("土壤侵蚀"), FILE_HIG_DEF("高清遥感");
      private final String name;

      private Category(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }
   }

   private Long _id;
   private Long _placeId;
   private String _fileName;
   private String _filePath;
   private String _wmsUrl;
   private String _shootTime;
   private Category _category;
   private Place place;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return _id;
   }

   public void setId(Long id) {
      this._id = id;
   }

   @Column(name = "place_id")
   public Long getPlaceId() {
      return _placeId;
   }

   public void setPlaceId(Long placeId) {
      this._placeId = placeId;
   }

   @Column(length = 512, unique = true, nullable = false)
   public String getFileName() {
      return _fileName;
   }

   public void setFileName(String fileName) {
      this._fileName = fileName;
   }

   @Column(length = 1024)
   public String getFilePath() {
      return _filePath;
   }

   public void setFilePath(String filePath) {
      this._filePath = filePath;
   }

   @Column(length = 128)
   public String getWmsUrl() {
      return _wmsUrl;
   }

   public void setWmsUrl(String wmsUrl) {
      this._wmsUrl = wmsUrl;
   }

   @Column(length = 64)
   public String getShootTime() {
      return _shootTime;
   }

   public void setShootTime(String shootTime) {
      this._shootTime = shootTime;
   }

   @Enumerated(EnumType.STRING)
   public Category getCategory() {
      return _category;
   }

   public void setCategory(Category category) {
      switch (category) {
      case FILE_LAND_COLLAPSE:
         this._category = Category.FILE_LAND_COLLAPSE;
         break;
      case FILE_LAND_SOIL:
         this._category = Category.FILE_LAND_SOIL;
         break;
      case FILE_LAND_FRACTURE:
         this._category = Category.FILE_LAND_FRACTURE;
         break;
      case FILE_LAND_TYPE:
         this._category = Category.FILE_LAND_TYPE;
         break;
      case FILE_REGION_BOUNDARY:
         this._category = Category.FILE_REGION_BOUNDARY;
      }
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "place_id", insertable = false, updatable = false)
   public Place getPlace() {
      return place;
   }

   public void setPlace(Place place) {
      this.place = place;
   }

   // just a placeholder
   public void setTypeString(String str) {
      // nothing to do
   }

   public String getTypeString() {
      switch (this._category) {
      case FILE_LAND_COLLAPSE:
         return "地塌陷";

      case FILE_LAND_SOIL:
         return "土壤侵蚀";

      case FILE_LAND_FRACTURE:
         return "地裂缝";
      case FILE_LAND_TYPE:
         return "地类";
      case FILE_REGION_BOUNDARY:
         return "边界";

      }
      // will never go to here
      return "未知";
   }
}
