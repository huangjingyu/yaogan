package com.rockontrol.yaogan.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShapefileGroupVo {
   private Long placeId;
   private String placeName;
   private List<ShapefileShootTimeGroupVo> shootTimeGroupList;

   private int count = 0;

   public ShapefileGroupVo() {
      shootTimeGroupList = new ArrayList<ShapefileShootTimeGroupVo>();
   }

   public Long getPlaceId() {
      return placeId;
   }

   public void setPlaceId(Long placeId) {
      this.placeId = placeId;
   }

   public String getPlaceName() {
      return placeName;
   }

   public void setPlaceName(String placeName) {
      this.placeName = placeName;
   }

   public List<ShapefileShootTimeGroupVo> getShootTimeGroupList() {
      Collections.sort(shootTimeGroupList, new Comparator<ShapefileShootTimeGroupVo>() {
         @Override
         public int compare(ShapefileShootTimeGroupVo o1, ShapefileShootTimeGroupVo o2) {
            return 0 - o1.getShootTime().compareTo(o2.getShootTime());
         }
      });
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
