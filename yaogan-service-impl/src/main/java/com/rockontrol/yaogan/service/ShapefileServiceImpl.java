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
   public void addShapefile(Shapefile shapefile) {
      this.save(shapefile);
   }

}
