package com.rockontrol.yaogan.service;

import java.io.File;

import com.rockontrol.yaogan.model.Shapefile;

public interface GeoService {
  
   public String publishGeoFile(Shapefile.Category type, File shapeFile);
   
}
   
