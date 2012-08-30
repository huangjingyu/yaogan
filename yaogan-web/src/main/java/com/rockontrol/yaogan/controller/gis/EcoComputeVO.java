package com.rockontrol.yaogan.controller.gis;

public class EcoComputeVO {
   private String region;
   private int year;
   private String boundary;
   private String bbox;

   public String getRegion() {
      return region;
   }

   public void setRegion(String region) {
      this.region = region;
   }

   public int getYear() {
      return year;
   }

   public void setYear(int year) {
      this.year = year;
   }

   public String getBoundary() {
      return boundary;
   }

   public void setBoundary(String boundary) {
      this.boundary = boundary;
   }

   public String getBbox() {
      return bbox;
   }

   public void setBbox(String bbox) {
      this.bbox = bbox;
   }

}
