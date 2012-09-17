package com.rockontrol.yaogan.service.util.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.rockontrol.yaogan.service.util.CallContext;
import com.rockontrol.yaogan.service.util.GeoClientTask;
import com.rockontrol.yaogan.service.util.GeoServerClient;
import com.rockontrol.yaogan.service.util.GisHttpUtil;

public class LoginTask implements GeoClientTask{

   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      String  serverBase = (String) objects[0];
      String userName = (String) objects[1];
      String password = (String) objects[2];
      HttpGet get = new HttpGet(serverBase + "/web/");
      GisHttpUtil.execute(client, get);

      HttpPost httpPost = new HttpPost(serverBase + "/j_spring_security_check");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("username", userName));
      params.add(new BasicNameValuePair("password", password));
      HttpResponse response = GisHttpUtil.execute(params, client, httpPost);
      return (T) response;
   }

}
