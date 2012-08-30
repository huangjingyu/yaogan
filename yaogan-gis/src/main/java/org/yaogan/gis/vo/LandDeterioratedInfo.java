package org.yaogan.gis.vo;

import java.util.Map;

import org.yaogan.gis.service.FactorCaculateConstant;

public class LandDeterioratedInfo {
   private final double _slightArea;
   private final double _partArea;
   private final double _seriousArea;
   private final double _totalArea;

   public LandDeterioratedInfo(Map<String, Double> infoMap) {
      _slightArea = infoMap.get(FactorCaculateConstant.LAND_DETERIORATED_SLIGHT) == null ? 0
            : infoMap.get(FactorCaculateConstant.LAND_DETERIORATED_SLIGHT);
      _partArea = infoMap.get(FactorCaculateConstant.LAND_DETERIORATED_PART) == null ? 0
            : infoMap.get(FactorCaculateConstant.LAND_DETERIORATED_PART);
      _seriousArea = infoMap.get(FactorCaculateConstant.LAND_DETERIORATED_SERIOUS) == null ? 0
            : infoMap.get(FactorCaculateConstant.LAND_DETERIORATED_SERIOUS);
      _totalArea = _slightArea + _partArea + _seriousArea;
   }

   public double getSlightArea() {
      return this._slightArea;
   }

   public double getPartArea() {
      return this._partArea;
   }

   public double getSeriousArea() {
      return this._seriousArea;
   }

   public double getTotalArea() {
      return this._totalArea;
   }
}
