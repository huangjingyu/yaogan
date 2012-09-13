package com.rockontrol.yaogan.vo;

import java.util.ArrayList;
import java.util.List;

public class ShapefileGroupVo {
   private String placeName;
   private List<ShapefileShootTimeGroupVo> shootTimeGroupList;

   private int count = 0;

   public ShapefileGroupVo() {
      shootTimeGroupList = new ArrayList<ShapefileShootTimeGroupVo>();
   }

   public String getPlaceName() {
      return placeName;
   }

   public void setPlaceName(String placeName) {
      this.placeName = placeName;
   }

   public List<ShapefileShootTimeGroupVo> getShootTimeGroupList() {
      return shootTimeGroupList;
   }

   public void setShootTimeGroupList(List<ShapefileShootTimeGroupVo> shootTimeGroup) {
      this.shootTimeGroupList = shootTimeGroup;
   }

   public int getCount() {
      return count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public void addShapefileVo(ShapefileVo vo) {
      String shootTime = vo.getShootTime();
      this.count++;
      for (ShapefileShootTimeGroupVo group : shootTimeGroupList) {
         if (shootTime.equals(group.getShootTime())) {
            group.addShapefileVo(vo);
            return;
         }
      }
      ShapefileShootTimeGroupVo group = new ShapefileShootTimeGroupVo();
      group.addShapefileVo(vo);
      group.setShootTime(shootTime);
      shootTimeGroupList.add(group);

   }

}
