package com.rockontrol.yaogan.service;

import org.she.mvc.db.IBaseService;

import com.rockontrol.yaogan.model.Shapefile;

public interface IShapefileService extends IBaseService {
   public void saveShapefile(Shapefile shapefile);

   public Shapefile getShapeFile(String areaName, String type, String year);
}
