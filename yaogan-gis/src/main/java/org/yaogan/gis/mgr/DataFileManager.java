package org.yaogan.gis.mgr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * 
 * 
 * ShapeFile store :$ROOT/type/year FileName: region_type_year或region_type
 * 
 * @author Administrator
 * 
 */
public class DataFileManager {
   private String _shapeFileHome;

   public DataFileManager() {
      initHomePath();
   }

   public File getShapefile(int year, String type) {

      String fileName = type + "_" + year + ".shp";
      String typeHome = _shapeFileHome + File.separator + type;
      String fullPath = typeHome + File.separator + year + File.separator + fileName;
      File file = new File(fullPath);
      if (!file.exists()) {
         fileName = type + ".shp";
         fullPath = typeHome + File.separator + fileName;
         file = new File(fullPath);
      }

      if (!file.exists())
         file = null;
      return file;
   }

   /**
    * 
    * @param region
    * @param year
    * @param type
    * @return 地区_类型_year.shp
    */
   public File getShapefile(String region, int year, String type) {

      String fileName = region + "_" + type + "_" + year + ".shp";
      String typeHome = _shapeFileHome + File.separator + type;
      String fullPath = typeHome + File.separator + year + File.separator + fileName;
      File file = new File(fullPath);
      if (!file.exists()) {
         fileName = type + "_" + year + ".shp";
         fullPath = typeHome + File.separator + year + File.separator + fileName;
         file = new File(fullPath);
      }
      if (!file.exists()) {
         fileName = region + "_" + type + ".shp";
         fullPath = typeHome + File.separator + fileName;
         file = new File(fullPath);
      }

      if (!file.exists())
         file = null;
      return file;
   }

   public void setShapeFileHome(String home) {
      _shapeFileHome = home.trim();
      char ch = _shapeFileHome.charAt(_shapeFileHome.length() - 1);
      if (ch == '\\' || ch == '/')
         _shapeFileHome = _shapeFileHome.substring(0, _shapeFileHome.length() - 1);
   }

   public String getShapeFileHome() {
      return this._shapeFileHome;

   }

   private void initHomePath() {
      InputStream is = DataFileManager.class
            .getResourceAsStream("/ShapeFileStore.properties");
      Properties props = new Properties();
      try {
         props.load(is);
         _shapeFileHome = props.getProperty("yaogan.gis.shapefile.home");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
