package org.yaogan.gis.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.yaogan.gis.vo.LandDeterioratedInfo;
import org.yaogan.gis.vo.LandTypeInfo;
import org.yaogan.gis.vo.Level2LandTypeVo;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * 生态功能指数计算工具类
 */
public class EcoFactorCaculator {
   // 生物丰度计算参数 begin
   private static final double RATE_ABIO_FOREST = 0.35;
   private static final double RATE_ABIO_LAWN = 0.21;
   private static final double RATE_ABIO_WET = 0.28;
   private static final double RATE_ABIO_FARM = 0.11;
   private static final double RATE_ABIO_CONS = 0.04;
   private static final double RATE_ABIO_NOT_USED = 0.01;

   // 暂未使用
   // private static final double RATE_ABIO_YOULINDI = 0.6;
   // private static final double RATE_ABIO_GUANMULINDI = 0.25;
   // private static final double RATE_ABIO_QITALINDI = 0.15;
   // private static final double RATE_ABIO_GAOFUGAICAODI = 0.6;
   // private static final double RATE_ABIO_ZHONGFUGAICAODI = 0.3;
   // private static final double RATE_ABIO_DIFUGAOCAODI = 0.1;
   // private static final double RATE_ABIO_HELIU = 0.6;
   // private static final double RATE_ABIO_HUPO = 0.4;
   // private static final double RATE_ABIO_SHUIJIAODI = 0.6;
   // private static final double RATE_ABIO_HANDI = 0.4;
   // private static final double RATE_ABIO_CHENGZHENYONGDI = 0.2;
   // private static final double RATE_ABIO_NONGCUNYONGDI = 0.5;
   // private static final double RATE_ABIO_QITAYONGDI = 0.3;
   // private static final double RATE_ABIO_SHADI = 0.2;
   // private static final double RATE_ABIO_YANJIANDI = 0.3;
   // private static final double RATE_ABIO_LUODI = 0.3;
   // private static final double RATE_ABIO_LUOYANSHILI = 0.2;
   // end

   // 植被覆盖指数参数begin
   private static final double RATE_AVEG_FOREST = 0.38;
   private static final double RATE_AVEG_LAWN = 0.34;
   private static final double RATE_AVEG_FARM = 0.19;
   private static final double RATE_AVEG_CONS = 0.07;
   private static final double RATE_AVEG_NOT_USED = 0.02;
   // 暂未使用
   // private static final double RATE_AVEG_YOULINDI = 0.6;
   // private static final double RATE_AVEG_GUANMULINDI = 0.25;
   // private static final double RATE_AVEG_QITALINDI = 0.15;
   // private static final double RATE_AVEG_GAOFUGAICAODI = 0.6;
   // private static final double RATE_AVEG_ZHONGFUGAICAODI = 0.3;
   // private static final double RATE_AVEG_DIFUGAOCAODI = 0.1;
   // private static final double RATE_AVEG_SHUIJIAODI = 0.7;
   // private static final double RATE_AVEG_HANDI = 0.3;
   // private static final double RATE_AVEG_CHENGZHENYONGDI = 0.2;
   // private static final double RATE_AVEG_NONGCUNYONGDI = 0.5;
   // private static final double RATE_AVEG_QITAYONGDI = 0.3;
   // private static final double RATE_AVEG_SHADI = 0.2;
   // private static final double RATE_AVEG_YANJIANDI = 0.3;
   // private static final double RATE_AVEG_LUODI = 0.3;
   // private static final double RATE_AVEG_LUOYANSHILI = 0.2;
   // end

   // 土地退化指数参数begin
   private static final double RATE_AERO_SLIGHT = 0.05;
   private static final double RATE_AERO_PART = 0.25;
   private static final double RATE_AERO_SERIOUS = 0.7;

   // end

   // 土地环境指数
   private static final double RATE_ASUS = 0.35;
   private static final double RATE_ASUC = 0.35;
   private static final double RATE_UWC = 0.3;

   /**
    * 
    * @param forest_area
    *           林地面积
    * @param lawn_are
    *           草地面积
    * @param wetland_area
    *           湿地面积
    * @param farm_land_area
    *           耕地面积
    * @param construcion_area
    *           建设用地面积
    * @param not_used_area
    *           未使用面积
    * @return
    */
   public static double computeAbio(double forest_area, double lawn_area,
         double wetland_area, double farm_land_area, double construcion_area,
         double not_used_area) {
      double total_area = forest_area + lawn_area + wetland_area + farm_land_area
            + construcion_area + not_used_area;
      double result = FactorCaculateConstant.ABIO
            * (RATE_ABIO_FOREST * forest_area + RATE_ABIO_LAWN * lawn_area
                  + RATE_ABIO_WET * wetland_area + RATE_ABIO_FARM * farm_land_area
                  + RATE_ABIO_CONS * construcion_area + RATE_ABIO_NOT_USED
                  * not_used_area) / total_area;
      return result;
   }

   private static double computeAbio(LandTypeInfo areaInfo) {
      double forest_area = areaInfo.getForestArea();
      double lawn_area = areaInfo.getLawnArea();
      double farm_land_area = areaInfo.getFarmArea();
      double wetland_area = areaInfo.getWetArea();
      double construcion_area = areaInfo.getConstructionArea();
      double not_used_area = areaInfo.getNotUsedArea();
      double total_area = areaInfo.getTotalArea();
      double result = FactorCaculateConstant.ABIO
            * (RATE_ABIO_FOREST * forest_area + RATE_ABIO_LAWN * lawn_area
                  + RATE_ABIO_WET * wetland_area + RATE_ABIO_FARM * farm_land_area
                  + RATE_ABIO_CONS * construcion_area + RATE_ABIO_NOT_USED
                  * not_used_area) / total_area;
      return result;
   }

   /**
    * 
    * @param forest_area
    *           林地面积
    * @param lawn_area
    *           草地面积
    * @param farm_land_area
    *           耕地面积
    * @param construction_area
    *           建设用地面积
    * @param not_used_area
    *           未使用面积
    * @return
    */
   public static double computeAveg(double forest_area, double lawn_area,
         double farm_land_area, double construction_area, double not_used_area) {
      double total_area = forest_area + lawn_area + farm_land_area + construction_area
            + not_used_area;
      double result = FactorCaculateConstant.AVEG
            * (RATE_AVEG_FOREST * forest_area + RATE_AVEG_LAWN * lawn_area
                  + RATE_AVEG_FARM * farm_land_area + RATE_AVEG_CONS * construction_area + RATE_AVEG_NOT_USED
                  * not_used_area) / total_area;
      return result;
   }

   private static double computeAveg(LandTypeInfo areaInfo) {
      double forest_area = areaInfo.getForestArea();
      double lawn_area = areaInfo.getLawnArea();
      double farm_land_area = areaInfo.getFarmArea();
      double construction_area = areaInfo.getConstructionArea();
      double not_used_area = areaInfo.getNotUsedArea();
      double total_area = areaInfo.getTotalArea();
      double result = FactorCaculateConstant.AVEG
            * (RATE_AVEG_FOREST * forest_area + RATE_AVEG_LAWN * lawn_area
                  + RATE_AVEG_FARM * farm_land_area + RATE_AVEG_CONS * construction_area + RATE_AVEG_NOT_USED
                  * not_used_area) / total_area;
      return result;
   }

   private static double computeAbio(Level2LandTypeVo vo) {
      return computeAbio(vo.getForestArea(), vo.getLawnArea(), vo.getWetArea(),
            vo.getFarmArea(), vo.getConstructionArea(), vo.getNotUsedArea());
   }

   public static double computeAveg(Level2LandTypeVo vo) {
      return computeAveg(vo.getForestArea(), vo.getLawnArea(), vo.getFarmArea(),
            vo.getConstructionArea(), vo.getNotUsedArea());
   }

   /**
    * 
    * @param slight_area
    *           轻度腐蚀面积
    * @param part_area
    *           中度腐蚀面积
    * @param serious_area
    *           重度腐蚀面积
    * @return
    */
   public static double computeAero(double slight_area, double part_area,
         double serious_area) {
      double total_area = slight_area + part_area + serious_area;
      double result = 100
            - FactorCaculateConstant.AERO
            * (RATE_AERO_SLIGHT * slight_area + RATE_AERO_PART * part_area + RATE_AERO_SERIOUS
                  * serious_area) / total_area;
      return result;
   }

   private static double computeAero(LandDeterioratedInfo landInfo) {
      double slight_area = landInfo.getSlightArea();
      double part_area = landInfo.getPartArea();
      double serious_area = landInfo.getSeriousArea();
      double total_area = landInfo.getTotalArea();
      double result = 100
            - FactorCaculateConstant.AERO
            * (RATE_AERO_SLIGHT * slight_area + RATE_AERO_PART * part_area + RATE_AERO_SERIOUS
                  * serious_area) / total_area;
      return result;
   }

   public static double computeAbio(SimpleFeatureSource source, Geometry boundary)
         throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBoundary(
            boundary, source);
      LandTypeInfo typyInfo = getLandTypeInfo(collection);
      return computeAbio(typyInfo);
   }

   public static double computeAbio(SimpleFeatureSource source, String boundary)
         throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBoundary(
            boundary, source);
      LandTypeInfo typyInfo = getLandTypeInfo(collection);
      return computeAbio(typyInfo);
   }

   public static double computeAbio(SimpleFeatureSource source) throws IOException {
      LandTypeInfo typyInfo = getLandTypeInfo(source.getFeatures());
      return computeAbio(typyInfo);
   }

   public static double computeAbio(SimpleFeatureSource source, double maxX,
         double maxY, double minX, double minY) throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBBox(
            source, maxX, maxY, minX, minY);
      LandTypeInfo landInfo = getLandTypeInfo(collection);
      return computeAbio(landInfo);
   }

   public static double computeAveg(SimpleFeatureSource source, Geometry boundary)
         throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBoundary(
            boundary, source);
      LandTypeInfo typyInfo = getLandTypeInfo(collection);
      return computeAveg(typyInfo);
   }

   public static double computeAveg(SimpleFeatureSource source, String boundary)
         throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBoundary(
            boundary, source);
      LandTypeInfo typyInfo = getLandTypeInfo(collection);
      return computeAveg(typyInfo);
   }

   public static double computeAveg(SimpleFeatureSource source) throws IOException {
      LandTypeInfo typyInfo = getLandTypeInfo(source.getFeatures());
      return computeAveg(typyInfo);
   }

   public static double computeAveg(SimpleFeatureSource source, double maxX,
         double maxY, double minX, double minY) throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBBox(
            source, maxX, maxY, minX, minY);
      LandTypeInfo landInfo = getLandTypeInfo(collection);
      return computeAveg(landInfo);
   }

   public static double computeAero(SimpleFeatureSource source, double maxX,
         double maxY, double minX, double minY) throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBBox(
            source, maxX, maxY, minX, minY);
      LandDeterioratedInfo info = getDeterioratedInfo(collection);
      return computeAero(info);
   }

   public static double computeAero(SimpleFeatureSource source, Geometry boundary)
         throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBoundary(
            boundary, source);
      LandDeterioratedInfo info = getDeterioratedInfo(collection);
      return computeAero(info);
   }

   public static double computeAero(SimpleFeatureSource source, String boundary)
         throws IOException {
      SimpleFeatureCollection collection = FeatureSelector.selectFeatureWithinBoundary(
            boundary, source);
      LandDeterioratedInfo info = getDeterioratedInfo(collection);
      return computeAero(info);
   }

   public static double computeAero(SimpleFeatureSource source) throws IOException {
      LandDeterioratedInfo info = getDeterioratedInfo(source.getFeatures());
      return computeAero(info);
   }

   /**
    * 
    * @param fracture_length
    *           地裂缝长度
    * @param collapse_area
    *           地表塌陷面积
    * @param water_descrement
    *           地下水下降量
    * @param total_area
    *           区域总面积
    * @return
    */
   private static double computeAsus(double fracture_length, double collapse_area,
         double water_descrement, double total_area) {
      double result = RATE_ASUS
            * (100 - FactorCaculateConstant.ASUS * collapse_area / total_area)
            + RATE_ASUC
            * (100 - FactorCaculateConstant.ASUC * fracture_length / total_area)
            + RATE_UWC * (100 - FactorCaculateConstant.AUWC * water_descrement);
      return result;
   }

   public static double computeAsus(SimpleFeatureSource fractureSource,
         SimpleFeatureSource collapseSource, SimpleFeatureSource boundarySource,
         double water_descrement) throws IOException {
      SimpleFeatureIterator iterator = fractureSource.getFeatures().features();
      SimpleFeatureIterator collIterator = collapseSource.getFeatures().features();
      SimpleFeatureIterator boundIterator = boundarySource.getFeatures().features();
      double fracLength = 0;
      double collapsArea = 0;
      double totalArea = 0;
      while (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         fracLength += geom.getLength();
      }

      while (collIterator.hasNext()) {
         SimpleFeature feature = collIterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         collapsArea += geom.getArea();
      }

      while (boundIterator.hasNext()) {
         SimpleFeature feature = boundIterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         totalArea += geom.getArea();
      }
      return computeAsus(fracLength, collapsArea, water_descrement, totalArea);
   }

   /**
    * 
    * @param fractureSource
    *           地裂缝数据源
    * @param collapseSource
    *           地表塌陷数据源
    * @param boundarySource
    *           边界数据源
    * @param geom_string
    *           选择区域字符串
    * @param water_descrement
    *           地下水下降量
    * @return
    * @throws IOException
    */
   public static double computeAsus(SimpleFeatureSource fractureSource,
         SimpleFeatureSource collapseSource, SimpleFeatureSource boundarySource,
         String geom_string, double water_descrement) throws IOException {
      SimpleFeatureIterator iterator = FeatureSelector.selectFeatureWithinBoundary(
            geom_string, fractureSource).features();
      SimpleFeatureIterator collIterator = FeatureSelector.selectFeatureWithinBoundary(
            geom_string, collapseSource).features();
      SimpleFeatureIterator boundIterator = FeatureSelector.selectFeatureWithinBoundary(
            geom_string, boundarySource).features();
      double fracLength = 0;
      double collapsArea = 0;
      double totalArea = 0;
      while (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         fracLength += geom.getLength();
      }

      while (collIterator.hasNext()) {
         SimpleFeature feature = collIterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         collapsArea += geom.getArea();
      }

      while (boundIterator.hasNext()) {
         SimpleFeature feature = boundIterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         totalArea += geom.getArea();
      }
      return computeAsus(fracLength, collapsArea, water_descrement, totalArea);
   }

   public static double computeAsus(SimpleFeatureSource fractureSource,
         SimpleFeatureSource collapseSource, double water_descrement, double maxX,
         double maxY, double minX, double minY) throws IOException {
      SimpleFeatureIterator iterator = FeatureSelector.selectFeatureWithinBBox(
            fractureSource, maxX, maxY, minX, minY).features();
      SimpleFeatureIterator collIterator = FeatureSelector.selectFeatureWithinBBox(
            collapseSource, maxX, maxY, minX, minY).features();
      // SimpleFeatureIterator boundIterator =
      // FeatureSelector.selectFeatureWithinBoundary(
      // geom_string, boundarySource).features();
      double fracLength = 0;
      double collapsArea = 0;
      double totalArea = (maxX - minX) * (maxY - minY);
      while (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         fracLength += geom.getLength();
      }

      while (collIterator.hasNext()) {
         SimpleFeature feature = collIterator.next();
         Geometry geom = (Geometry) feature.getDefaultGeometry();
         collapsArea += geom.getArea();
      }

      return computeAsus(fracLength, collapsArea, water_descrement, totalArea);
   }

   private static LandTypeInfo getLandTypeInfo(SimpleFeatureCollection collection)
         throws IOException {

      if (isLevel2LandType(collection))
         return getLevel2LandTypeVo(collection);

      SimpleFeatureIterator iterator = collection.features();
      Map<String, Double> areaMap = new HashMap<String, Double>();
      while (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         Object value = feature.getAttribute("一级地类");
         if (value != null) {
            // Geometry geom = (Geometry) feature.getDefaultGeometry();
            Double area = (Double) feature.getAttribute("面积");
            Double totalarea = areaMap.get(value);
            if (totalarea == null)
               areaMap.put((String) value, area);
            else {
               totalarea += area;
               areaMap.put((String) value, area);
            }
         }
      }
      LandTypeInfo typeInfo = new LandTypeInfo(areaMap);
      return typeInfo;
   }

   private static boolean isLevel2LandType(SimpleFeatureCollection collection) {
      SimpleFeatureIterator iterator = collection.features();
      Object value1 = null;
      Object value2 = null;
      if (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         value1 = feature.getAttribute("一级地类");
         value2 = feature.getAttribute("QDLMC");
      }
      if (value1 == null && value2 != null)
         return true;
      throw new RuntimeException("未知的土地利用文件类型!");
   }

   private static Level2LandTypeVo getLevel2LandTypeVo(SimpleFeatureCollection collection) {
      SimpleFeatureIterator iterator = collection.features();
      Map<String, Double> areaMap = new HashMap<String, Double>();
      while (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         Object value = feature.getAttribute("QDLMC");// 二级地类key
         if (value != null) {
            Double area = (Double) feature.getAttribute("MJ");// 面积key
            Double totalarea = areaMap.get(value);
            if (totalarea == null)
               areaMap.put((String) value, area);
            else {
               totalarea += area;
               areaMap.put((String) value, area);
            }
         }
      }
      Level2LandTypeVo vo = new Level2LandTypeVo(areaMap);
      return vo;
   }

   private static LandDeterioratedInfo getDeterioratedInfo(
         SimpleFeatureCollection collection) throws IOException {
      SimpleFeatureIterator iterator = collection.features();
      Map<String, Double> areaMap = new HashMap<String, Double>();
      while (iterator.hasNext()) {
         SimpleFeature feature = iterator.next();
         Object value = feature.getAttribute("QSQD");
         if (value != null) {
            // Geometry geom = (Geometry) feature.getDefaultGeometry();
            Double area = (Double) feature.getAttribute("MJ");
            Double totalarea = areaMap.get(value);
            if (totalarea == null)
               areaMap.put((String) value, area);
            else {
               totalarea += area;
               areaMap.put((String) value, area);
            }
         }
      }
      LandDeterioratedInfo typeInfo = new LandDeterioratedInfo(areaMap);
      return typeInfo;
   }

}
