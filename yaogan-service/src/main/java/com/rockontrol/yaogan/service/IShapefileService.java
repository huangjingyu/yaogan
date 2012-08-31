package com.rockontrol.yaogan.service;

import org.she.mvc.db.IBaseService;

import com.rockontrol.yaogan.model.Shapefile;

public interface IShapefileService extends IBaseService {
   public void addShapefile(Shapefile shapefile);
}
