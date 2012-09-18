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

public class OpenPublishLayerPageTask  implements GeoClientTask{

   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      HttpGet httpGet = new HttpGet(geoClient.getWebLocation("?wicket:bookmarkablePage=:org.geoserver.web.data.layer.NewLayerPage"));
      HttpResponse response = GisHttpUtil.execute(client, httpGet);
      String html = GeoServiceUtil.getContent(response);
      /** 得到刚加入的存储 */
      int idStart = GeoServiceUtil.reverseSearch(html, context.workspaceName + ":" + context.storeName, '=');
      String id = GeoServiceUtil.search(html, null, idStart, 2, "\"");
      /** 得到form表单地址 */
      String action = GeoServiceUtil.search(html, "<select name=\"storesDropDown\"", "?", 0, "\'");
      HttpPost httpPost = new HttpPost(geoClient.getWebLocation(action));
      response = GisHttpUtil.execute(Arrays.asList(new BasicNameValuePair("storesDropDown", id)),
            client, httpPost);
      html = GeoServiceUtil.getContent(response);
      int linkStart = GeoServiceUtil.reverseSearch(html, "Publish<", '?');
      String link = GeoServiceUtil.search(html, null, linkStart, 0, "'");
      /** 点击发布链接 */
      httpGet = new HttpGet(geoClient.getWebLocation(link));
      response = GisHttpUtil.execute(client, httpGet);
      return (T) response;
   }

}
