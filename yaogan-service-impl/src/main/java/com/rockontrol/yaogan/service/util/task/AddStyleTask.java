package com.rockontrol.yaogan.service.util.task;

import org.apache.http.client.HttpClient;

import com.rockontrol.yaogan.service.util.CallContext;
import com.rockontrol.yaogan.service.util.GeoClientTask;
import com.rockontrol.yaogan.service.util.GeoServerClient;

public class AddStyleTask implements GeoClientTask{
   

   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      return null;
   }

}
