package com.rockontrol.yaogan.service.util.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.rockontrol.yaogan.service.util.CallContext;
import com.rockontrol.yaogan.service.util.GeoClientTask;
import com.rockontrol.yaogan.service.util.GeoServerClient;
import com.rockontrol.yaogan.service.util.GeoServiceUtil;
import com.rockontrol.yaogan.service.util.GisHttpUtil;

public class AddStoreTask implements GeoClientTask{
   
   private GeoClientTask selectFileTask = new SelectFileTask();

   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      String html = context.lastHtml;
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /** 工作空间yaogan */
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue",(String) context.get(GeoServerClient.WS_KEY)));
      String wsAction = GeoServiceUtil.search(html, "<select", "?", 0, "'");
      /**点击下拉的工作空间*/
      HttpPost wsPost = new HttpPost(geoClient.getWebLocation(wsAction));
      GisHttpUtil.execute(params, client, wsPost);
      
      /** 存储名称 */
      String storeNameKey;
      /** 存储描述 */
      String descKey;   
      String enableKey;
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         storeNameKey = "dataStoreNamePanel:border:paramValue";
         descKey = "dataStoreDescriptionPanel:border:paramValue";
         enableKey = "dataStoreEnabledPanel:paramValue";
      } else {
         storeNameKey = "namePanel:border:paramValue";
         descKey = "descriptionPanel:border:paramValue";
         enableKey = "enabledPanel:paramValue";
      }
      params.add(new BasicNameValuePair(storeNameKey, context.storeName));
      params.add(new BasicNameValuePair(descKey,""));
      params.add(new BasicNameValuePair(enableKey, "on"));

      /** 得到文件选择的地址 */
      int start = GeoServiceUtil.reverseSearch(html, "Browse...", '?');
      int end = html.indexOf("'", start);
      String action = html.substring(start, end);
      context.location = geoClient.getWebLocation(action);
      /**选择文件*/
      HttpResponse response = selectFileTask.doTask(geoClient, context.file);
      html = GeoServiceUtil.getContent(response);
      String path = GeoServiceUtil.search(html, null, "value=", 7, "\"");
      /** 存储目录 */
      params.add(new BasicNameValuePair("parametersPanel:url:border:paramValue", path));
      String root = (String) context.get(GeoServerClient.SELECT_ROOT_KEY);
      params.add(new BasicNameValuePair("parametersPanel:url:dialog:content:roots", root));
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         /** 字符集GBK */
         params.add(new BasicNameValuePair("parametersPanel:charset:border:paramValue", "6"));
         /** index */
         params.add(new BasicNameValuePair("parametersPanel:spatialIndex:paramValue", "on"));
         /** cache memory */
         params.add(new BasicNameValuePair("parametersPanel:cacheMemoryMaps:paramValue",
               "on"));
      }
      params.add(new BasicNameValuePair("save", "1"));
      httpPost.addHeader("Wicket-Ajax", "true");
      httpPost.addHeader("Accept", "text/xml");
      response = GisHttpUtil.execute(params, client, httpPost);
      return (T) response;
   }

}
