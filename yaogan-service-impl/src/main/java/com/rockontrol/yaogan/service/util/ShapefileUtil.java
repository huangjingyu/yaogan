package com.rockontrol.yaogan.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.vo.ShapefileGroupVo;
import com.rockontrol.yaogan.vo.ShapefileVo;

public class ShapefileUtil {
   public static List<ShapefileGroupVo> groupShapefiles(List<Shapefile> list) {
      Map<String, ShapefileGroupVo> groupVoMap = new HashMap<String, ShapefileGroupVo>();
      for (Shapefile shpfile : list) {
         String placeName = shpfile.getPlace().getName();
         ShapefileGroupVo vo = groupVoMap.get(placeName);
         if (vo == null) {
            vo = new ShapefileGroupVo();
            vo.setPlaceName(placeName);
            groupVoMap.put(placeName, vo);
         }
         ShapefileVo shpvo = toShapefileVo(shpfile);
         vo.addShapefileVo(shpvo);

      }
      return new ArrayList(groupVoMap.values());
   }

   public static ShapefileVo toShapefileVo(Shapefile shpfile) {
      ShapefileVo vo = new ShapefileVo();
      vo.setFileName(shpfile.getFileName());
      vo.setPlace(shpfile.getPlace().getName());
      vo.setShootTime(shpfile.getShootTime());
      vo.setUploadTime(shpfile.getUploadTime());
      vo.setTypeString(shpfile.getTypeString());
      return vo;
   }
}
