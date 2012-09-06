package com.rockontrol.yaogan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "place_param")
public class PlaceParam {
   // 地下水位下降量
   public static final String GROUND_WATER_DESC = "ground_water_desc";
   private Long id;
   private Long placeId;
   private String paramName;
   private String paramValue;
   private String time;// year
   private Place place;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   public Long getId() {
      return id;
   }

   @Column(name = "place_id")
   public Long getPlaceId() {
      return placeId;
   }

   public void setPlaceId(Long placeId) {
      this.placeId = placeId;
   }

   @Column(name = "param_name", length = 256, nullable = false)
   public String getParamName() {
      return paramName;
   }

   public void setParamName(String paramName) {
      this.paramName = paramName;
   }

   @Column(name = "param_value", length = 512)
   public String getParamValue() {
      return paramValue;
   }

   public void setParamValue(String paramValue) {
      this.paramValue = paramValue;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @Column(name = "time", length = 4, updatable = false, nullable = false)
   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "place_id", insertable = false, updatable = false)
   public Place getPlace() {
      return place;
   }

   public void setPlace(Place place) {
      this.place = place;
   }

}
