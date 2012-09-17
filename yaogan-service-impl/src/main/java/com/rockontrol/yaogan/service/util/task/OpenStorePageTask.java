package com.rockontrol.yaogan.service.util.task;

import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.rockontrol.yaogan.service.util.CallContext;
import com.rockontrol.yaogan.service.util.GeoClientTask;
import com.rockontrol.yaogan.service.util.GeoServerClient;
import com.rockontrol.yaogan.service.util.GeoServiceUtil;
import com.rockontrol.yaogan.service.util.GisHttpUtil;

public class OpenStorePageTask implements GeoClientTask{

   /**
    * 打开存储选择页面
    */
   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      HttpGet httpGet = new HttpGet(geoClient.getWebLocation("?wicket:bookmarkablePage=:org.geoserver.web.data.store.NewDataPage"));
      HttpResponse response = GisHttpUtil.execute(client, httpGet);
      String html = GeoServiceUtil.getContent(response);
      /** 发送post请求到shapefile页面 */
      HttpPost post = new HttpPost(geoClient.getWebLocation(GeoServiceUtil.search(html, "<form", "action=", 8, "\"")));
      String key;
      /**根据文件属性选择打开shapefile格式还是tif格式链接*/
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         key = "vectorResources:4:resourcelink";
      } else {
         key = "rasterResources:1:resourcelink";
      }
      
      response = GisHttpUtil.execute(Arrays.asList(new BasicNameValuePair(key, "x")),client, post);
      String location = GeoServiceUtil.getLocation(response);

      /** 跳转到返回的shapefile页面 */
      httpGet = new HttpGet(location);
      response = GisHttpUtil.execute(client, httpGet);
      return (T) response;
   }

}
