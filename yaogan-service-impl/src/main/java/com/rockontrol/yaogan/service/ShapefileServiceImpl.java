/**
 * 
 */
package com.rockontrol.yaogan.service;

import org.she.mvc.db.hibernate.service.DefaultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rockontrol.yaogan.model.Shapefile;

@Service("shapeFileService")
public class ShapefileServiceImpl extends DefaultService implements IShapefileService {

   @Override
   @Transactional
   public void saveShapefile(Shapefile shapefile) {
      this.save(shapefile);
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
