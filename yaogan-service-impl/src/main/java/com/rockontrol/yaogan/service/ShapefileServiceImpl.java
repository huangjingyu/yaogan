/**
 * 
 */
package com.rockontrol.yaogan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rockontrol.yaogan.dao.IShapefileDao;
import com.rockontrol.yaogan.model.Shapefile;

@Service("shapeFileService")
public class ShapefileServiceImpl implements IShapefileService {

   @Autowired
   protected IShapefileDao dao;

   @Override
   @Transactional
   public void saveShapefile(Shapefile shapefile) {
      dao.save(shapefile);
      // Shapefile shapefile = new Shapefile();
      // setCategory(shapefile, type);
      // shapefile.setFileName(file.getName());
      // shapefile.setShootTime(year);
   }

   @Override
   public Shapefile getShapeFile(String areaName, String type, String year) {
      // TODO Auto-generated method stub
      return null;
   }

}
