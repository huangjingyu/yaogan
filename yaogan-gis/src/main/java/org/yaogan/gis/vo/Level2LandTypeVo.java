package org.yaogan.gis.vo;

import java.util.Map;

import org.yaogan.gis.util.FactorCaculateConstant;

/**
 * 二级地类vo
 * 
 * @author Administrator
 * 
 */
public class Level2LandTypeVo extends LandTypeInfo {
   private final double youlindi;
   private final double guanmulidi;
   private final double qitalind;
   private final double gaofugaicaodi;
   private final double zhongfugaicaodi;
   private final double difugaicaodi;
   private final double heliu;
   private final double hupo;
   private final double shuijiaodi;
   private final double handi;
   private final double chengzhenyongdi;
   private final double noncunyongdi;
   private final double qitajiansheyongdi;
   private final double shadi;
   private final double yanjiandi;
   private final double luodi;
   private final double luoyanshili;

   public Level2LandTypeVo(Map<String, Double> map) {
      youlindi = map.get(FactorCaculateConstant.LAND_YOULINGDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_YOULINGDI);
      guanmulidi = map.get(FactorCaculateConstant.LAND_GUANMULINDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_GUANMULINDI);
      qitalind = map.get(FactorCaculateConstant.LAND_QITALINDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_QITALINDI);
      _forestArea = youlindi + guanmulidi + qitalind;
      gaofugaicaodi = map.get(FactorCaculateConstant.LAND_GAOFUGAICAODI) == null ? 0
            : map.get(FactorCaculateConstant.LAND_GAOFUGAICAODI);
      zhongfugaicaodi = map.get(FactorCaculateConstant.LAND_ZHONGFUGAICAODI) == null ? 0
            : map.get(FactorCaculateConstant.LAND_ZHONGFUGAICAODI);
      difugaicaodi = map.get(FactorCaculateConstant.LAND_DIFUGAICAODI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_DIFUGAICAODI);
      _lawnArea = gaofugaicaodi + zhongfugaicaodi + difugaicaodi;
      heliu = map.get(FactorCaculateConstant.LAND_HELIU) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_HELIU);
      hupo = map.get(FactorCaculateConstant.LAND_HUPO) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_HUPO);
      _wetLandArea = heliu + hupo;
      shuijiaodi = map.get(FactorCaculateConstant.LAND_SHUIJIAODI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_SHUIJIAODI);
      handi = map.get(FactorCaculateConstant.LAND_HANDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_HANDI);
      _farmArea = shuijiaodi + handi;
      chengzhenyongdi = map.get(FactorCaculateConstant.LAND_CHENGZHENYONGDI) == null ? 0
            : map.get(FactorCaculateConstant.LAND_CHENGZHENYONGDI);
      noncunyongdi = map.get(FactorCaculateConstant.LAND_NONCUNYONGDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_NONCUNYONGDI);
      qitajiansheyongdi = map.get(FactorCaculateConstant.LAND_QITAJIANSHEYONGDI) == null ? 0
            : map.get(FactorCaculateConstant.LAND_QITAJIANSHEYONGDI);
      _constructionArea = chengzhenyongdi + noncunyongdi + qitajiansheyongdi;
      shadi = map.get(FactorCaculateConstant.LAND_SHANDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_SHANDI);
      yanjiandi = map.get(FactorCaculateConstant.LAND_YANJIANDI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_YANJIANDI);
      luodi = map.get(FactorCaculateConstant.LAND_LUODI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_LUODI);
      luoyanshili = map.get(FactorCaculateConstant.LAND_LUOYANSHILI) == null ? 0 : map
            .get(FactorCaculateConstant.LAND_LUOYANSHILI);
      _notUsedArea = shadi + yanjiandi + luodi + luoyanshili;

      _totalArea = _farmArea + _lawnArea + _constructionArea + _notUsedArea
            + _wetLandArea + _forestArea;
   }

   public double getYoulindi() {
      return youlindi;
   }

   public double getGuanmulidi() {
      return guanmulidi;
   }

   public double getQitalind() {
      return qitalind;
   }

   public double getGaofugaicaodi() {
      return gaofugaicaodi;
   }

   public double getZhongfugaicaodi() {
      return zhongfugaicaodi;
   }

   public double getDifugaicaodi() {
      return difugaicaodi;
   }

   public double getHeliu() {
      return heliu;
   }

   public double getHupo() {
      return hupo;
   }

   public double getShuijiaodi() {
      return shuijiaodi;
   }

   public double getHandi() {
      return handi;
   }

   public double getChengzhenyongdi() {
      return chengzhenyongdi;
   }

   public double getNoncunyongdi() {
      return noncunyongdi;
   }

   public double getQitajiansheyongdi() {
      return qitajiansheyongdi;
   }

   public double getShadi() {
      return shadi;
   }

   public double getYanjiandi() {
      return yanjiandi;
   }

   public double getLuodi() {
      return luodi;
   }

   public double getLuoyanshili() {
      return luoyanshili;
   }

   @Override
   public double getFarmArea() {
      return this._farmArea;
   }

   @Override
   public double getLawnArea() {
      return this._lawnArea;
   }

   @Override
   public double getForestArea() {
      return this._forestArea;
   }

   @Override
   public double getWetArea() {
      return this._wetLandArea;
   }

   @Override
   public double getConstructionArea() {
      return this._constructionArea;
   }

   @Override
   public double getNotUsedArea() {
      return this._notUsedArea;
   }

   @Override
   public double getTotalArea() {
      return this._totalArea;
   }

}
