package com.rockontrol.yaogan.service;

import java.io.File;

public interface IPrintImageService {
   File addShapeLayer(Long placeId, String time, String category, File img)
         throws Exception;

   File addComment(File img, String comment) throws Exception;

   File copyTemplate(String templatePath, String imagePath) throws Exception;
}
