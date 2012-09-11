package com.rockontrol.yaogan.service.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class GisHttpUtil {
   
   /**
    * 创建一个http客户端
    * */
   public static HttpClient createClient() {
      ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
      manager.setDefaultMaxPerRoute(20);
      DefaultHttpClient client = new DefaultHttpClient(manager);
      //HttpHost proxy = new HttpHost("127.0.0.1", 8080);
      //client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
      HttpClientParams.setCookiePolicy(client.getParams(),
            CookiePolicy.BROWSER_COMPATIBILITY);
      return client;
   }

   public static HttpResponse execute(HttpClient client, HttpGet get) {
      try {
         HttpResponse response = client.execute(get);
         return response;
      } catch (Exception e) {
         throw new RuntimeException("Get调用异常:" + get.getURI(), e);
      }
   }

   public static HttpResponse execute(List<? extends NameValuePair> params, HttpClient client,
         HttpPost post) {
      try {
         post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("Post 调用异常:" + post.getURI(), e);
      }
      return execute(client, post);
   }

   public static HttpResponse execute(HttpClient client, HttpPost post) {
      try {
         return client.execute(post);
      } catch (Exception e) {
         throw new RuntimeException("Get调用异常:" + post.getURI(), e);
      }
   }
}
