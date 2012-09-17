package com.rockontrol.yaogan.service.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.rockontrol.yaogan.model.Shapefile;

public class GeoServiceUtil {
   
   /**
    * 用于标识文件属性
    */
   /*shapefile*/
   public static final String SF_ATTR = "SF";
   /*高清遥感*/
   public static final String HD_ATTR = "HD";
   
   public static final Log log = LogFactory.getLog(GeoServiceUtil.class);
   /**存放每种地图的样式名称 名称在geoserver中添加*/
   private static Map<Shapefile.Category, String> map = new HashMap<Shapefile.Category, String>();
   
   static {
      /**矿区*/
      map.put(Shapefile.Category.FILE_REGION_BOUNDARY, "kuangqu");
      /**土地利用*/
      map.put(Shapefile.Category.FILE_LAND_TYPE, "tudiliyong");
      /**地表塌陷*/
      map.put(Shapefile.Category.FILE_LAND_COLLAPSE, "ditaxian");
      /**地裂缝*/
      map.put(Shapefile.Category.FILE_LAND_FRACTURE, "diliefeng");
      /**土壤侵蚀*/
      map.put(Shapefile.Category.FILE_LAND_SOIL, "turangqinshi");
      /**高清遥感*/
      map.put(Shapefile.Category.FILE_HIG_DEF, "TM");
   }

   /**
    * 得到地图对应的样式
    * @param category
    * @return
    */
   public static String getStyle(Shapefile.Category category) {
      return map.get(category);
   }
   /**
    * 生成一个uuid标识
    * @return
    */
   public static String getUUID() {
      String s = UUID.randomUUID().toString(); 
      //去掉“-”符号 
      return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
   }
   
   /**
    * 从object处反向查找ch字符的位置
    * @param html
    * @param from
    * @param ch
    * @return
    */
   public static int reverseSearch(String html, Object from, char ch) {
      int fromPos = 0;
      if(from instanceof Integer) {
         fromPos = (Integer) from;
      } else {
         fromPos = html.indexOf((String) from);
         if(fromPos == -1) {
            log.debug("html:" + html + "\nfrom字符串搜索失败," + from);
            return -1;
         }
      }
      while(fromPos-- >= 0) {
         if(html.charAt(fromPos) == ch) {
            return fromPos;
         }
      }
      return -1;
   }
   /**
    * 从html中搜索字符串 从from 处开始找 找内容在start和end之间的
    * @param html
    * @param from
    * @param start
    * @param end
    * @return
    */
   public static String search(String html, Object from, Object start, Integer startOffset, String end) {
      int fromPos = 0;
      if(from != null) {
         if(from instanceof Integer) {
            fromPos = (Integer) from;
         } else {
            fromPos = html.indexOf((String) from);
            if(fromPos == -1) {
               log.debug("html:" + html + "from字符串搜索失败," + from);
               return null;
            }
         }
      }
      int startPos;
      if(start instanceof Integer) {
         startPos = (Integer) start;
      } else {
            startPos = html.indexOf((String) start, fromPos);
            if(startPos == -1) {
               log.debug("html:" + html + "start字符串搜索失败," + start);
               return null;
            }
      }
      if(startOffset == null) {
         if(start instanceof String) {
            startPos += ((String) start).length();
         } 
      } else {
         startPos += startOffset;
      }
      int endPos = html.indexOf(end, startPos);
      if(endPos == -1) {
         log.debug("html:" + html + "end字符串搜索失败," + end);
         return null;
      }
      return html.substring(startPos, endPos);
   }
   /**
    * 得到http响应的信息
    * @param response
    * @return
    */
   public static String getContent(HttpResponse response) {
      try {
         return EntityUtils.toString(response.getEntity());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * 得到http跳转的地址
    * 
    * @param response
    * @return
    */
   public static String getLocation(HttpResponse response) {
      Header[] locations = response.getHeaders("Location");
      if (locations != null && locations.length > 0) {
         return locations[0].getValue();
      }
      return null;
   }
   
   /**
    * 将shapeFile拷贝到工作目录下
    * 
    * @param shapeFile
    */
   public static void copyShapeFile(File shapeFile, String rename, String workDir) {
      String name = shapeFile.getName();
      int pos = name.indexOf(".");
      String prefix = name.substring(0, pos);
      String prePart = shapeFile.getParent() + "/" + prefix + ".";
      copyOne(new File(prePart + "dbf"), rename, workDir);
      copyOne(new File(prePart + "prj"), rename, workDir);
      copyOne(new File(prePart + "qix"), rename, workDir);
      copyOne(new File(prePart + "sbn"), rename, workDir);
      copyOne(new File(prePart + "sbx"), rename, workDir);
      copyOne(new File(prePart + "shp"), rename, workDir);
      copyOne(new File(prePart + "shp.xml"), rename, workDir);
      copyOne(new File(prePart + "shx"), rename, workDir);
      copyOne(new File(prePart + "evf"), rename, workDir);

   }
   /**
    * 将高清遥感图拷贝到工作目录下
    * @param hdFile
    * @param rename
    */
   public static void copyHdFile(File hdFile, String rename, String workDir) {
      copyOne(hdFile, rename, workDir);
   }

   /**
    * 拷贝一个文件
    * 
    * @param source
    * @param rename
    */
   private static void copyOne(File source, String rename, String workDir) {
      if (!source.exists()) {
         return;
      }
      String name = source.getName();
      int pos = name.indexOf(".");
      String suffix = name.substring(pos + 1);
      File file = new File(workDir);
      if(! file.exists()) {
         file.mkdirs();
      }
      File dest = new File(workDir + rename + "." + suffix);
      try {
         FileUtils.copyFile(source, dest);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

}
