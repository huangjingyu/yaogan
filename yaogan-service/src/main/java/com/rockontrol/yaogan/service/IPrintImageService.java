package com.rockontrol.yaogan.service;

import java.io.File;

public interface IPrintImageService {
   File addShapeLayer(File template, File image) throws Exception;

   File addComment(File img, String comment) throws Exception;

   File copyTemplate(String templatePath, String imagePath) throws Exception;

   File getMap(Long placeId, String time, String category, String tempPath)
         throws Exception;
}
