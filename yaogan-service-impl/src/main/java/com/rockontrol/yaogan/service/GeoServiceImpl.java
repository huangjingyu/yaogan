package com.rockontrol.yaogan.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class GeoServiceImpl implements GeoService {
   
   public static final String SERVER_BASE = "http://localhost:8888/geoserver";
   
   /**
    * shapeFile工作目录
    */
   public static final String WORK_DIR = "D:/pf/apache-tomcat-7.0.29/webapps/geoserver/data/yaogandata/";
   /**
    * geoserver中手工创建的工作空间名称
    */
   public static final String WORKSPACE_NAME = "yaogan";
   
   public static final String FOLDER_NAME = "yaogandata";
   
   public static final String WS_KEY = "WS";
   public static final String SHP_FILE_KEY = "SHP_FILE";
   public static final Log log = LogFactory.getLog(GeoServiceImpl.class);

   /**
    * @param area 矿区标识
    * @param type 类型:KQ 矿区 DL:地类
    * @param time 时间:如2010
    * @param shapeFile 文件名称
    */
   @Override
   public void publishShapeFile(String area, String type, String time, File shapeFile) {
      HttpClient client = null;
      try {
         CallContext context = new CallContext();
         if("KQ".equals(type)) {
            context.storeName = area + "_" + type;
         } else {
            context.storeName = area + "_" + type + "_" + time;
         }
         copyFile(shapeFile, context.storeName);
         context.workspaceName = WORKSPACE_NAME;
         client = createClient();
         HttpResponse response;
         String html;
         String location;
         /**登陆*/
         response = loginGeoServer(client, context);
         location = getLocation(response);
         /**如果location中有error说明认证失败*/
         if(location == null || location.contains("error")) {
            throw new RuntimeException("登陆失败");
         }
         
         /**打开添加存储页面*/
         response = openStorePage(client, context);
         
         html = getContent(response);
         context.location = SERVER_BASE + "/web/" + getStoreSubmitFormAction(html);
         String workSpaceId = getWorkspaceId(html);
         context.put(WS_KEY , workSpaceId);
         context.lastHtml = html;
         /**添加存储*/
         addStore(client, context);
         
         /**打开发布页面*/
         response = openPublishLayerPage(client, context);
         html = getContent(response);
         context.location = SERVER_BASE + "/web/" + getLayerSubmitFormAction(html);
         /**添加layer*/
         addLayer(client, context);
         
      } finally {
         if(client != null) {
            client.getConnectionManager().shutdown();
         }
      }
   }
   
   /**
    * 将shapeFile拷贝到工作目录下
    * @param shapeFile
    */
   private void copyFile(File shapeFile, String rename) {
      String name = shapeFile.getName();
      int pos = name.indexOf(".");
      String prefix = name.substring(0, pos);
      String prePart = shapeFile.getParent() + "/" + prefix + ".";
      copyOne(new File(prePart + "dbf"), rename);
      copyOne(new File(prePart + "prj"), rename);
      copyOne(new File(prePart + "qix"), rename);
      copyOne(new File(prePart + "sbn"), rename);
      copyOne(new File(prePart + "sbx"), rename);
      copyOne(new File(prePart + "shp"), rename);
      copyOne(new File(prePart + "shp.xml"), rename);
      copyOne(new File(prePart + "shx"), rename);
      copyOne(new File(prePart + "evf"), rename);

   }
   /**
    * 拷贝一个文件
    * @param source
    * @param rename
    */
   private void copyOne(File source, String rename) {
      if(! source.exists()) {
         return;
      }
      String name = source.getName();
      int pos = name.indexOf(".");
      String suffix = name.substring(pos + 1);
      File dest = new File(WORK_DIR + rename + "." + suffix);
      try {
         FileUtils.copyFile(source, dest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
   }
   /**
    * 在存储发布页面中找到工作空间的ID标识
    * @param html
    * @return
    */
   private String getWorkspaceId(String html) {
      int yaoganLabel = html.indexOf(WORKSPACE_NAME);
      while(yaoganLabel-- > 0) {
         if(html.charAt(yaoganLabel) == '=') {
            break;
         }
      }
      int idStart = yaoganLabel + 2;
      int idEnd = html.indexOf("\"", idStart);
      String id = html.substring(idStart, idEnd);
      return id;
   }
   
   /**
    * 创建一个http客户端
    * */
   private HttpClient createClient() {
      ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
      manager.setDefaultMaxPerRoute(10);
      DefaultHttpClient client = new DefaultHttpClient(manager);
     // HttpHost proxy = new HttpHost("127.0.0.1", 8080);    
     // client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);    
      HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);    
      return client;
   }
   
   /**
    * 登陆到服务器
    * @param client
    * @return
    */
   private HttpResponse loginGeoServer(HttpClient client, CallContext context) {
      HttpGet get = new HttpGet(SERVER_BASE + "/web/");
      execute(client, get);
      
      HttpPost httpPost = new HttpPost(SERVER_BASE + "/j_spring_security_check");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("username", "admin"));
      params.add(new BasicNameValuePair("password", "geoserver"));
      HttpResponse response =execute(params, client, httpPost);
      return response;
   }
   
   /**
    * 打开存储选择页面
    * @param client
    * @param context
    */
   public HttpResponse openStorePage(HttpClient client, CallContext context) {
      HttpGet httpGet = new HttpGet(SERVER_BASE + "/web/?wicket:bookmarkablePage=:org.geoserver.web.data.store.NewDataPage");
      HttpResponse response = execute(client, httpGet);
      String html = getContent(response);   
      /**发送post请求到shapefile页面*/
      HttpPost post = new HttpPost(SERVER_BASE + "/web/" + getStorePageFormAction(html));
      response = execute(Arrays.asList(new BasicNameValuePair("vectorResources:4:resourcelink", "x")), client, post);
      String location = getLocation(response);
      
      /**跳转到返回的shapefile页面*/
      httpGet = new HttpGet(location);
      response = execute(client, httpGet);
      return response;
   }
   
   /**得到存储页面post地址
    * 部分格式为 <form id="id141" method="post" action="?wicket:interface=:19:storeForm::IFormSubmitListener::"> 
    * 得到?wicket:interface=:19:storeForm::IFormSubmitListener::
    * @param html
    * @return
    */
   private String getStorePageFormAction(String html) {
      int formStart = html.indexOf("<form");
      int actionStart = html.indexOf("action=", formStart) + 8;
      int actionEnd = html.indexOf("\"", actionStart);
      return html.substring(actionStart, actionEnd);
   }
   /**
    * 得到存储提交页面post地址
    * @param html
    * @return
    */
   private String getStoreSubmitFormAction(String html) {
      int formStart = html.indexOf("<form");
      int buttonStart = html.indexOf("<div class=\"button-group", formStart);
      int actionStart = html.indexOf("?wicket:interface", buttonStart);
      int actionEnd = html.indexOf("\'", actionStart);
      String location = html.substring(actionStart, actionEnd);
      return location.replace("&amp;", "&");
   }
   
   /**
    * 得到layer提交页面post地址
    * @param html
    * @return
    */
   private String getLayerSubmitFormAction(String html) {
      int formStart = html.indexOf("<form");
      int actionStart = html.indexOf("?wicket:interface", formStart);
      int actionEnd = html.indexOf("\"", actionStart);
      return html.substring(actionStart, actionEnd);
   }
   
   /**
    * 提交shapefile表单
    * @param client
    * @param context
    */
   private HttpResponse addStore(HttpClient client, CallContext context) {
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /**工作空间test*/
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue", (String) context.get(WS_KEY)));
      /**存储名称*/
      params.add(new BasicNameValuePair("dataStoreNamePanel:border:paramValue", context.storeName));   
      /**存储描述*/
      params.add(new BasicNameValuePair("dataStoreDescriptionPanel:border:paramValue", ""));   
      /**启用标志*/
      params.add(new BasicNameValuePair("dataStoreEnabledPanel:paramValue", "on")); 
      
      /**得到文件选择的地址*/
      String html = context.lastHtml;
      int browIndex = html.indexOf("Browse...");
      while(browIndex-- > 0) {
         if(html.charAt(browIndex) == '?') {
            break;
         }
      }
      int start = browIndex;
      int end = html.indexOf("'", start);
      String action = html.substring(start, end);
      context.location = SERVER_BASE + "/web/" + action;
      selectFile(client, context);
      /**存储目录*/
      params.add(new BasicNameValuePair("parametersPanel:url:border:paramValue", "file:yaogandata/" + context.storeName + ".shp"));   
      params.add(new BasicNameValuePair("parametersPanel:url:dialog:content:roots", "0"));   
      /**字符集GBK*/
      params.add(new BasicNameValuePair("parametersPanel:charset:border:paramValue", "6"));   
      /**index*/
      params.add(new BasicNameValuePair("parametersPanel:spatialIndex:paramValue", "on"));   
      /**cache memory*/
      params.add(new BasicNameValuePair("parametersPanel:cacheMemoryMaps:paramValue", "on"));  
      
      params.add(new BasicNameValuePair("save", "1"));   
      httpPost.addHeader("Wicket-Ajax", "true");
      httpPost.addHeader("Accept", "text/xml");
      HttpResponse response = execute(params, client, httpPost);
      return response;
   }
   
   private HttpResponse selectFile(HttpClient client, CallContext context) {
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /**工作空间test*/
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue", (String) context.get(WS_KEY)));
      /**存储名称*/
      params.add(new BasicNameValuePair("dataStoreNamePanel:border:paramValue", context.storeName));   
      /**存储目录*/
      params.add(new BasicNameValuePair("parametersPanel:url:border:paramValue", "file:data/example.extension"));   
      
      params.add(new BasicNameValuePair("parametersPanel:url:border:chooser", "1")); 
      httpPost.addHeader("Wicket-Ajax", "true");
      httpPost.addHeader("Accept", "text/xml");
      /**打开文件对话框*/
      HttpResponse response = execute(params, client, httpPost);
       response = execute(params, client, httpPost);
      String html = getContent(response);
      int pos = html.indexOf(FOLDER_NAME);
      while(pos-- > 0) {
         if(html.charAt(pos) == '?') {
            break;
         }
      }
      int start = pos;
      int end = html.indexOf("\'", start);
      String href = html.substring(start, end);
      /**点击yaogandata*/
      HttpGet get = new HttpGet(SERVER_BASE + "/web/" + href);     
      response = execute(client, get);
      html = getContent(response);      
      pos = html.indexOf(context.storeName + ".shp");
      while(pos-- > 0) {
         if(html.charAt(pos) == '?') {
            break;
         }
      }
      start = pos;
      end = html.indexOf("\'", start);
      href = html.substring(start, end);
      /**点击文件*/
      get = new HttpGet(SERVER_BASE + "/web/" + href);
      execute(client, get);
      return response;
   }
   
   private HttpResponse addLayer(HttpClient client, CallContext context) {
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:name", context.storeName));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:title", context.storeName));   
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:abstract", ""));   
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:newKeyword", "")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:lang", "")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:vocab", "")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeSRS:srs", "UNKNOWN")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:declaredSRS:srs", "EPSG:2413")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:declaredSRS:popup:content:table:filterForm:filter", "1954")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "FORCE_DECLARED"));   
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minX", "37,610,716.932526596"));   
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minY", "4,365,977.2120092"));   
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxX", "37,625,766.36042764"));   
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxY", "4,385,308.8299839"));  
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minX", "112.28619667632907"));  
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minY", "39.41804968685204"));  
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxX", "112.46456147932551")); 
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxY", "39.59418969944382"));  
      
      params.add(new BasicNameValuePair("save", "x"));   
      HttpResponse response = execute(params, client, httpPost);
      return response;
   }
   
   /**
    * 打开发布页面 为了独立性 不是在存储后点击发布 而是在layer页面中选择发布
    * @param client
    * @param context
    * @return
    */
   public HttpResponse openPublishLayerPage(HttpClient client, CallContext context) {
      HttpGet httpGet = new HttpGet(SERVER_BASE + "/web/?wicket:bookmarkablePage=:org.geoserver.web.data.layer.NewLayerPage");
      HttpResponse response = execute(client, httpGet);
      String html = getContent(response);   
      /**得到刚加入的存储*/
      int storeStart = html.indexOf(context.workspaceName + ":" + context.storeName);
      while(storeStart-- > 0) {
         if(html.charAt(storeStart) == '=') {
            break;
         }
      }
      int idStart = storeStart + 2;
      int idEnd = html.indexOf("\"", idStart);
     
      String id = html.substring(idStart, idEnd);
      /**得到form表单地址*/
      int formStart = html.indexOf("<select name=\"storesDropDown\"");
      int actionStart = html.indexOf("?", formStart);
      int actionEnd = html.indexOf( "\'", actionStart);
      String action = html.substring(actionStart, actionEnd);
      HttpPost httpPost = new HttpPost(SERVER_BASE + "/web/" + action);
      response = execute(Arrays.asList(new BasicNameValuePair("storesDropDown", id)), client, httpPost);
      html = getContent(response);
      int labelStart = html.indexOf("Publish<");
      while(labelStart-- > 0) {
         if(html.charAt(labelStart) == '?') {
            break;
         }
      }
      int linkStart = labelStart;
      int linkEnd = html.indexOf("'", linkStart);
      String link = html.substring(linkStart, linkEnd);
      /**点击发布链接*/
      httpGet = new HttpGet(SERVER_BASE + "/web/" + link);
      response = execute(client, httpGet);
      return response;
   }
   
   private HttpResponse execute(HttpClient client, HttpGet get) {
      try {
         return client.execute(get);
      } catch (Exception e) {
         throw new RuntimeException("Get调用异常:" + get.getURI(), e);
      }
   }
   
   private HttpResponse execute(List<? extends NameValuePair> params,HttpClient client, HttpPost post) {
      try {
         post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("Post 调用异常:" + post.getURI(), e);
      }
     return  execute(client, post);
   }
   
   private HttpResponse execute(HttpClient client, HttpPost post) {
      try {
         return client.execute(post);
      } catch (Exception e) {
         throw new RuntimeException("Get调用异常:" + post.getURI(), e);
      }
   }
   
   private String getContent(HttpResponse response) {
      try {
         return EntityUtils.toString(response.getEntity());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
   }
   
   /**
    * 得到http跳转的地址
    * @param response
    * @return
    */
   private String getLocation(HttpResponse response) {
      Header[] locations = response.getHeaders("Location");
      if(locations != null && locations.length > 0) {
         return locations[0].getValue();
      }
      return null;
   }
   
   /**
    * 用户存储调用的上下文
    * @author liyuliang
    *
    */
   private static class CallContext extends HashMap<String, Object>{
      
      private String location;
      
      private String cookie;
      /**
       * 工作空间的名字
       */
      private String workspaceName;
      /**
       * 存储的名字 即输入的name
       */
      private String storeName;
      
      private String lastHtml;
      
      public CallContext() {
         
      }
   }

}
