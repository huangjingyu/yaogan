package com.rockontrol.yaogan.service.util.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.rockontrol.yaogan.service.util.CallContext;
import com.rockontrol.yaogan.service.util.GeoClientTask;
import com.rockontrol.yaogan.service.util.GeoServerClient;
import com.rockontrol.yaogan.service.util.GeoServiceUtil;
import com.rockontrol.yaogan.service.util.GisHttpUtil;

public class SelectFileTask implements GeoClientTask{

   public static final Log log = LogFactory.getLog(SelectFileTask.class);
   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      File file = (File) objects[0];
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /** 工作空间test */
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue",
            (String) context.get(GeoServerClient.WS_KEY)));
      /** 存储名称 */
      params.add(new BasicNameValuePair("dataStoreNamePanel:border:paramValue",
            context.storeName));
      /** 存储目录 */
      params.add(new BasicNameValuePair("parametersPanel:url:border:paramValue",
            "file:data/example.extension"));

      params.add(new BasicNameValuePair("parametersPanel:url:border:chooser", "1"));
      httpPost.addHeader("Wicket-Ajax", "true");
      httpPost.addHeader("Accept", "text/xml");
      /** 打开文件对话框 */
      HttpResponse response = GisHttpUtil.execute(params, client, httpPost);
      String html = GeoServiceUtil.getContent(response);
      
      /**选择根目录*/
      String rootHref = GeoServiceUtil.search(html, "select", "?", 0, "'");
      String rootValue = getRootValue(html, file);
      httpPost = new HttpPost(geoClient.getWebLocation(rootHref));
      params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("parametersPanel:url:dialog:content:roots", rootValue));
      response = GisHttpUtil.execute(params, client, httpPost);
      context.put(GeoServerClient.SELECT_ROOT_KEY, rootValue);
      Stack<String> stack = new Stack<String>();
      File f = file;
      log.info("filePath:" + f.getAbsolutePath());
      while(f != null) {
         stack.push(f.getName());
         log.info("fileName:" + f.getName());
         f = f.getParentFile();
      }
      stack.pop();
      while(! stack.isEmpty()) {
         String name = stack.pop();
         html = GeoServiceUtil.getContent(response);
         log.info("html:" + html);
         int start = GeoServiceUtil.reverseSearch(html, name, '?');
         int end = html.indexOf("\'", start);
         log.info(start + " " + end);
         String href = html.substring(start, end);
         HttpGet get = new HttpGet(geoClient.getWebLocation(href));
         response = GisHttpUtil.execute(client, get);
      }
      return (T) response;
   }

   /**
    * 得到根目录在html中的value值
    * @param html
    * @param f
    * @return
    */
   private static String getRootValue(String html, File file) {
      String path = file.getAbsolutePath();
      int posEnd;
      if(path.startsWith("/")) {
         posEnd = html.indexOf("/<option>");
      } else {
         int rootEnd = path.indexOf(':');
         String root = path.substring(0, rootEnd + 1);
         int posStart = html.indexOf("<option");
         posEnd = html.indexOf(root.toUpperCase(), posStart);
      }
      int posStart = GeoServiceUtil.reverseSearch(html, posEnd, '=');
      String value = GeoServiceUtil.search(html, null, posStart, 2, "\"");
      return value;        
   }
   
}
