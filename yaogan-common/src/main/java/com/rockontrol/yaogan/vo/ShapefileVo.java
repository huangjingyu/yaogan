package com.rockontrol.yaogan.vo;

import java.util.Date;

public class ShapefileVo {
   private String fileName;
   private String place;
   private String typeString;
   private String shootTime;
   private Date uploadTime;

   public String getFileName() {
      return fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public String getPlace() {
      return place;
   }

   public void setPlace(String place) {
      this.place = place;
   }

   public String getShootTime() {
      return shootTime;
   }

   public void setShootTime(String shootTime) {
      this.shootTime = shootTime;
   }

   public Date getUploadTime() {
      return uploadTime;
   }

   public void setUploadTime(Date uploadTime) {
      this.uploadTime = uploadTime;
   }

   public String getTypeString() {
      return typeString;
   }

   public void setTypeString(String typeString) {
      this.typeString = typeString;
   }
}
