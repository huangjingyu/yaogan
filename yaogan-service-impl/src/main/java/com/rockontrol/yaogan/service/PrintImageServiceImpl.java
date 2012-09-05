package com.rockontrol.yaogan.service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.model.User;

@Service
public class PrintImageServiceImpl implements IPrintImageService {

   @Autowired
   private IYaoganService yaoganService;

   @Override
   public void addShapeLayer(User caller, Long placeId, String category, String time) {

      Shapefile shapefile = yaoganService.getShapefile(caller, placeId, category, time);
      File file = new File(shapefile.getFilePath());

      // File sldFile = new File("C:\\data\\" + name + ".sld");

      try {
         FileDataStore store = new ShapefileDataStore(file.toURI().toURL(), false,
               Charset.forName("GBK"));
      } catch (MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // FeatureSource featureSource = store.getFeatureSource();
      //
      // Configuration config = new SLDConfiguration();
      //
      // Parser parser = new Parser(config);
      //
      // InputStream sld = new FileInputStream(sldFile);
      //
      // StyledLayerDescriptor styleSLD = (StyledLayerDescriptor)
      // parser.parse(sld);
      //
      // Style style = SLD.defaultStyle(styleSLD);
      //
      // map.addLayer(featureSource, style);

   }
}
