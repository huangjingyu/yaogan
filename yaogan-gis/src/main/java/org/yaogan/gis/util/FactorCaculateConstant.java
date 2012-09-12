package org.yaogan.gis.util;

public interface FactorCaculateConstant {
   public static final double AF_MAX = Math.sqrt(35.0);
   // 生物丰度参数
   public static final double ABIO = 1; /* 100 / Math.sqrt(35.0); */

   public static final double AVEG = 1; /* 100 / Math.sqrt(38); */
   public static final double AERO_MAX = (100 + Math.sqrt(100 * 100 - 20)) / 2;
   public static final double AERO = 1;/* 100 / AERO_MAX; */

   // 土地环境指数
   public static final double ASUS = 1;
   public static final double ASUC = 1;
   public static final double AUWC = 1;

   public static final String LAND_TYPE_FORST = "林地";
   public static final String LAND_TYPE_LAWN = "草地";
   public static final String LAND_TYPE_FARM = "耕地";
   public static final String LAND_TYPE_WET = "水域湿地";
   public static final String LAND_TYPE_CONSTRUCTION = "建设用地";
   public static final String LAND_TYPE_NOT_USED = "未利用地";

   public static final String LAND_DETERIORATED_SLIGHT = "轻度侵蚀";
   public static final String LAND_DETERIORATED_PART = "中度侵蚀";
   public static final String LAND_DETERIORATED_SERIOUS = "重度侵蚀";

   // 二级地类
   public static final String LAND_YOULINGDI = "有林地";
   public static final String LAND_GUANMULINDI = "灌木林地";
   public static final String LAND_QITALINDI = "疏林地和其它林地";
   public static final String LAND_GAOFUGAICAODI = "高覆盖草地";
   public static final String LAND_ZHONGFUGAICAODI = "中覆盖草地";
   public static final String LAND_DIFUGAICAODI = "地覆盖草地";
   public static final String LAND_HELIU = "河流";
   public static final String LAND_HUPO = "湖泊(库)";
   public static final String LAND_SHUIJIAODI = "水浇地";
   public static final String LAND_HANDI = "旱地";
   public static final String LAND_CHENGZHENYONGDI = "城镇建设用地";
   public static final String LAND_NONCUNYONGDI = "农村居民点";
   public static final String LAND_QITAJIANSHEYONGDI = "其他建设用地";
   public static final String LAND_SHANDI = "沙地";
   public static final String LAND_YANJIANDI = "盐碱地";
   public static final String LAND_LUODI = "裸土地";
   public static final String LAND_LUOYANSHILI = "裸岩石砾";

}
