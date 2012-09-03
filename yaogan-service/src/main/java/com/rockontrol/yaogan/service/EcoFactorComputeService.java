package com.rockontrol.yaogan.service;

import java.io.IOException;

public interface EcoFactorComputeService {
   /**
    * 指定年份特定地区的生物丰度指数
    * 
    * @param year
    *           年份
    * @param geom_string
    *           地区边界geom字符串
    * @return
    */
   public double computeAbio(int year, String geom_string) throws IOException;

   /**
    * 每个地区一个shapefile时使用,计算指定区域的指数
    * 
    * @param regionId
    * @param year
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAbio(Long regionId, int year, String geom_string)
         throws IOException;

   /**
    * 每个地区一个shapefile时使用，计算整个地区的指数
    * 
    * @param regionId
    * @param year
    * @return
    * @throws IOException
    */
   public double computeAbio(Long regionId, int year) throws IOException;

   /**
    * 指定年份特定地区的植被覆盖指数
    * 
    * @param year
    *           年份
    * @param geom_string
    *           地区边界geom字符串
    * @return
    */
   public double computeAveg(int year, String geom_string) throws IOException;

   /**
    * 每个地区一个shapefile时使用,计算指定区域的指数
    * 
    * @param regionId
    * @param year
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAveg(Long regionId, int year, String geom_string)
         throws IOException;

   /**
    * 每个地区一个shapefile时使用,计算整个地区的指数
    * 
    * @param regionId
    * @param year
    * @return
    * @throws IOException
    */
   public double computeAveg(Long regionId, int year) throws IOException;

   /**
    * 指定年份特定地区的土地退化指数
    * 
    * @param year
    *           年份
    * @param geom_string
    *           地区边界geom字符串
    * @return
    */
   public double computeAero(int year, String geom_string) throws IOException;

   /**
    * 每个地区一个shapefile时使用, 计算指定区域的指数
    * 
    * @param regionId
    * @param year
    * @param geom_string
    * @return
    * @throws IOException
    */
   public double computeAero(Long regionId, int year, String geom_string)
         throws IOException;

   /**
    * 每个地区一个shapefile时使用 计算整个地区的指数
    * 
    * @param regionId
    * @param year
    * @return
    * @throws IOException
    */
   public double computeAero(Long regionId, int year) throws IOException;

   /**
    * 选择区域生物丰度指数
    * 
    * @param year
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    */
   public double computeAbio(int year, double maxX, double maxY, double minX, double minY)
         throws IOException;

   /**
    * 每个地区一个shapefile时使用
    * 
    * @param regionId
    * @param year
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public double computeAbio(Long regionId, int year, double maxX, double maxY,
         double minX, double minY) throws IOException;

   /**
    * 选择区域植被覆盖指数
    * 
    * @param year
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    */
   public double computeAveg(int year, double maxX, double maxY, double minX, double minY)
         throws IOException;

   /**
    * 每个地区一个shapefile时使用
    * 
    * @param regionId
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public double computeAveg(Long regionId, int year, double maxX, double maxY,
         double minX, double minY) throws IOException;

   /**
    * 所有地区在同一shapefile时使用 选择区域土地退化指数
    * 
    * @param year
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    */
   public double computeAero(int year, double maxX, double maxY, double minX, double minY)
         throws IOException;

   /**
    * 地区分文件时使用
    * 
    * @param regionId
    * @param year
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @return
    * @throws IOException
    */
   public double computeAero(Long regionId, int year, double maxX, double maxY,
         double minX, double minY) throws IOException;

}
