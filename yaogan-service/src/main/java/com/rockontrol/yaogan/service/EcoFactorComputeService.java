package com.rockontrol.yaogan.service;

import java.io.IOException;

public interface EcoFactorComputeService {
   /**
    * 生物丰度指数
    * 
    * @param shapeFilePath
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAbio(String shapeFilePath, String geom_string)
         throws IOException;

   /**
    * 生物丰度指数
    * 
    * @param shapeFilePath
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAbio(String shapeFilePath) throws IOException;

   /**
    * 生物丰度指数
    * 
    * @param shapeFilePath
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public double computeAbio(String shapeFilePath, double maxX, double maxY,
         double minX, double minY) throws IOException;

   /**
    * 土地侵蚀指数
    * 
    * @param shapeFilePath
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public double computeAero(String shapeFilePath, double maxX, double maxY,
         double minX, double minY) throws IOException;

   /**
    * 土地侵蚀指数
    * 
    * @param shapeFilePath
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public double computeAero(String shapeFilePath) throws IOException;

   /**
    * 土地侵蚀指数
    * 
    * @param shapeFilePath
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAero(String shapeFilePath, String geom_string)
         throws IOException;

   /**
    * 土地退化指数
    * 
    * @param shapeFilePath
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAveg(String shapeFilePath, String geom_string)
         throws IOException;

   /**
    * 土地退化指数
    * 
    * @param shapeFilePath
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAveg(String shapeFilePath) throws IOException;

   /**
    * 土地退化指数
    * 
    * @param shapeFilePath
    * @param maxX
    * @param m
    * @return
    * @throws IOException
    */
   public double computeAveg(String shapeFilePath, double maxX, double maxY,
         double minX, double minY) throws IOException;

   /**
    * 土地环境指数
    * 
    * @param fractureFilePath
    *           地裂缝文件
    * @param collapseFilePath
    *           土地塌陷文件
    * @param boundaryFilePath
    *           边界文件
    * @param geom_string
    *           选择区域字符串
    * @param water_descrement
    *           地下水位下降量
    * @return
    * @throws IOException
    */
   public double computeAsus(String fractureFilePath, String collapseFilePath,
         String boundaryFilePath, String geom_string, double water_descrement)
         throws IOException;

   public double computeAsus(String fractureFilePath, String collapseFilePath,
         String boundaryFilePath, double water_descrement) throws IOException;

   public double computeAsus(String fractureFilePath, String collapseFilePath,
         String boundaryFilePath, double water_descrement, double maxX, double maxY,
         double minX, double minY) throws IOException;

}
