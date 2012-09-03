package com.rockontrol.yaogan.service;

import java.io.File;

import org.she.mvc.db.IBaseService;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.Shapefile.Category;

public interface IShapefileService extends IBaseService {
   public void saveUploadFile(String region, Category type, File file, String year);

   public Shapefile getShapeFile(String areaName, String type, String year);
}
