package org.yaogan.gis.vo;

import java.util.Map;

import org.yaogan.gis.util.FactorCaculateConstant;

public class LandTypeInfo {
   // private String _areaName;
   private final double _farmArea;
   private final double _lawnArea;
   private final double _constructionArea;
   private final double _wetLandArea;
   private final double _notUsedArea;
   private final double _forestArea;
   private final double _totalArea;

   public LandTypeInfo(Map<String, Double> landInfoMap) {
      _forestArea = landInfoMap.get(FactorCaculateConstant.LAND_TYPE_FORST) == null ? 0
            : landInfoMap.get(FactorCaculateConstant.LAND_TYPE_FORST);
      _lawnArea = landInfoMap.get(FactorCaculateConstant.LAND_TYPE_LAWN) == null ? 0
            : landInfoMap.get(FactorCaculateConstant.LAND_TYPE_LAWN);
      _farmArea = landInfoMap.get(FactorCaculateConstant.LAND_TYPE_FARM) == null ? 0
            : landInfoMap.get(FactorCaculateConstant.LAND_TYPE_FARM);
      _wetLandArea = landInfoMap.get(FactorCaculateConstant.LAND_TYPE_WET) == null ? 0
            : landInfoMap.get(FactorCaculateConstant.LAND_TYPE_WET);
      _constructionArea = landInfoMap.get(FactorCaculateConstant.LAND_TYPE_CONSTRUCTION) == null ? 0
            : landInfoMap.get(FactorCaculateConstant.LAND_TYPE_CONSTRUCTION);
      _notUsedArea = landInfoMap.get(FactorCaculateConstant.LAND_TYPE_NOT_USED) == null ? 0
            : landInfoMap.get(FactorCaculateConstant.LAND_TYPE_NOT_USED);
      _totalArea = _forestArea + _lawnArea + _wetLandArea + _farmArea
            + _constructionArea + _notUsedArea;
   }

   public double getFarmArea() {
      return this._farmArea;
   }

   public double getLawnArea() {
      return this._lawnArea;
   }

   public double getForestArea() {
      return this._forestArea;
   }

   public double getWetArea() {
      return this._wetLandArea;
   }

   public double getConstructionArea() {
      return this._constructionArea;
   }

   public double getNotUsedArea() {
      return this._notUsedArea;
   }

   public double getTotalArea() {
      return this._totalArea;
   }
}
