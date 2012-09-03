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
import com.rockontrol.yaogan.service.IPlaceService;
import com.rockontrol.yaogan.service.IShapefileService;
import com.rockontrol.yaogan.util.CompressUtil;
import com.rockontrol.yaogan.util.GlobalConfig;

@Controller
@RequestMapping("upload")
public class UploadController {

   @Autowired
   private IPlaceService placeService;
   @Autowired
   private IShapefileService shapefileService;

   @RequestMapping(value = "/gis")
   public String handleFormUpload(@RequestParam("region") String region,
         @RequestParam("shootTime") String year,
         @RequestParam("landType") MultipartFile landTypeFile,
         @RequestParam("landSoil") MultipartFile landSoilFile,
         @RequestParam("boundary") MultipartFile boundaryFile) {
      // String tempDir = (String) GlobalConfig.getProperties().getProperty(
      // "yaogan.upload.tempdir");
      String shapeFileHome = (String) GlobalConfig.getProperties().getProperty(
            "yaogan.gis.shapefile.home");
      try {
         // region/type/year/filename
         String landTypePath = shapeFileHome + File.separator + region + File.separator
               + DataFileType.FILE_LAND_TYPE + File.separator + year;
         String landSoilPath = shapeFileHome + File.separator + region + File.separator
               + DataFileType.FILE_LAND_SOIL + File.separator + year;
         String boundaryPath = shapeFileHome + File.separator + region + File.separator
               + DataFileType.FILE_REGION_BOUNDARY + File.separator + year;
         checkPath(landTypePath);
         checkPath(landSoilPath);
         checkPath(boundaryPath);

         File dLandTypeFile = new File(landTypePath + File.separator + region + "_"
               + DataFileType.FILE_LAND_TYPE + "_" + year + ".zip");
         File dLandTypeShpfile = new File(landTypePath + File.separator + region + "_"
               + DataFileType.FILE_LAND_TYPE + "_" + year + ".shp");
         File dSoilFile = new File(landSoilPath + File.separator + region + "_"
               + DataFileType.FILE_LAND_SOIL + "_" + year + ".zip");
         File dSoilShpFile = new File(landSoilPath + File.separator + region + "_"
               + DataFileType.FILE_LAND_SOIL + "_" + year + ".shp");
         File dBoundaryFile = new File(boundaryPath + File.separator + region + "_"
               + DataFileType.FILE_REGION_BOUNDARY + "_" + year + ".zip");
         File dBoundaryShpFile = new File(boundaryPath + File.separator + region + "_"
               + DataFileType.FILE_REGION_BOUNDARY + "_" + year + ".shp");
         landTypeFile.transferTo(dLandTypeFile);
         landSoilFile.transferTo(dSoilFile);
         boundaryFile.transferTo(dBoundaryFile);
         unZip(dLandTypeFile);
         unZip(dSoilFile);
         unZip(dBoundaryFile);

         shapefileService.saveUploadFile(region, Category.FILE_LAND_TYPE,
               dLandTypeShpfile, year);
         shapefileService.saveUploadFile(region, Category.FILE_LAND_SOIL, dSoilShpFile,
               year);
         shapefileService.saveUploadFile(region, Category.FILE_REGION_BOUNDARY,
               dBoundaryShpFile, year);

         dLandTypeFile.delete();
         dSoilFile.delete();
         dBoundaryFile.delete();
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
}
