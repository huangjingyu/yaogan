package com.rockontrol.yaogan.service;

import com.rockontrol.yaogan.model.User;

public interface IPrintImageService {
   void addShapeLayer(User caller, Long placeId, String category, String time);
}
