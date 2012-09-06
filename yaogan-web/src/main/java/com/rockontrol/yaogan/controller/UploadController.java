package com.rockontrol.yaogan.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.yaogan.gis.mgr.DataFileType;

import com.rockontrol.yaogan.model.Shapefile.Category;
import com.rockontrol.yaogan.service.IYaoganService;
import com.rockontrol.yaogan.util.CompressUtil;
import com.rockontrol.yaogan.util.GlobalConfig;

@Controller
@RequestMapping("upload")
public class UploadController {

   @Autowired
   private IYaoganService yaoganService;

   @RequestMapping(value = "/gis")
   public String handleFormUpload(@RequestParam("region") String region,
         @RequestParam("shootTime") String year,
         @RequestParam("landType") MultipartFile landTypeFile,
         @RequestParam("landSoil") MultipartFile landSoilFile,
         @RequestParam("boundary") MultipartFile boundaryFile,
         @RequestParam("collapse") MultipartFile collapseFile,
         @RequestParam("fracture") MultipartFile fractureFile) {
      try {
         // region/type/year/filename
         this.processUploadFile(landTypeFile, DataFileType.FILE_LAND_TYPE, year, region);
         this.processUploadFile(landSoilFile, DataFileType.FILE_LAND_SOIL, year, region);
         this.processUploadFile(boundaryFile, DataFileType.FILE_REGION_BOUNDARY, year,
               region);
         this.processUploadFile(collapseFile, DataFileType.FILE_LAND_COLLAPSE, year,
               region);
         this.processUploadFile(fractureFile, DataFileType.FILE_LAND_FRACTURE, year,
               region);
      } catch (IOException e) {
         e.printStackTrace();
         return "/failed";
      }

      return "/success";

   }

   private void checkPath(String path) {
      File file = new File(path);
      if (!file.exists())
         file.mkdirs();
   }

   private void unZip(File file) throws IOException {
      CompressUtil.unZip(file, file.getParentFile().getAbsolutePath(),
            Charset.forName("gbk"));
   }

   private void processUploadFile(MultipartFile file, String type, String year,
         String region) throws IllegalStateException, IOException {
      String shapeFileHome = (String) GlobalConfig.getProperties().getProperty(
            "yaogan.gis.shapefile.home");
      String landTypePath = shapeFileHome + File.separator + region + File.separator
            + type + File.separator + year;
      checkPath(landTypePath);
      File dLandTypeFile = new File(landTypePath + File.separator + region + "_" + type
            + "_" + year + ".zip");
      File dLandTypeShpfile = new File(landTypePath + File.separator + region + "_"
            + type + "_" + year + ".shp");
      file.transferTo(dLandTypeFile);
      unZip(dLandTypeFile);
      Category category = null;
      if (type.equals(DataFileType.FILE_LAND_TYPE))
         category = Category.FILE_LAND_TYPE;
      if (type.equals(DataFileType.FILE_LAND_COLLAPSE))
         category = Category.FILE_LAND_COLLAPSE;
      if (type.equals(DataFileType.FILE_LAND_FRACTURE))
         category = Category.FILE_LAND_FRACTURE;
      if (type.equals(DataFileType.FILE_LAND_SOIL))
         category = Category.FILE_LAND_SOIL;
      if (type.equals(DataFileType.FILE_REGION_BOUNDARY))
         category = Category.FILE_REGION_BOUNDARY;
      yaoganService.saveShapefile(region, category, dLandTypeShpfile, year);
      dLandTypeFile.delete();
   }
}
