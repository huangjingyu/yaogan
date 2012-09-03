package com.rockontrol.yaogan.service;

import com.rockontrol.yaogan.model.Shapefile;

public interface IShapefileService {
   public void saveShapefile(Shapefile shapefile);

   public Shapefile getShapeFile(String areaName, String type, String year);
}
