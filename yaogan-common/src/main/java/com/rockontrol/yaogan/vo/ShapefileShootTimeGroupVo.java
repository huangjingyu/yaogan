package com.rockontrol.yaogan.vo;

import java.util.ArrayList;
import java.util.List;

public class ShapefileShootTimeGroupVo {
   private String shootTime;
   private List<ShapefileVo> fileList;

   public ShapefileShootTimeGroupVo() {
      fileList = new ArrayList<ShapefileVo>();
   }

   public String getShootTime() {
      return shootTime;
   }

   public void setShootTime(String shootTime) {
      this.shootTime = shootTime;
   }

   public List<ShapefileVo> getFileList() {
      return fileList;
   }

   public void setFileList(List<ShapefileVo> fileList) {
      this.fileList = fileList;
   }

   public int getCount() {
      return this.fileList.size();
   }

   public void addShapefileVo(ShapefileVo vo) {
      fileList.add(vo);
   }

}
