package com.rockontrol.yaogan.service.util;


public interface GeoClientTask {

   public <T>T doTask(GeoServerClient client, Object...objects);
}
