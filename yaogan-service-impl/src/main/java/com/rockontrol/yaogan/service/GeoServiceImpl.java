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

import com.rockontrol.yaogan.model.Shapefile;

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
   /**
    * geoserver启动的地址
    */
   private String serverBase = SERVER_BASE;
   /**
    * geoserver的工作目录
    */
   private String workDir = WORK_DIR;
   
   
   
   public String getServerBase() {
      return serverBase;
   }

   public void setServerBase(String serverBase) {
      this.serverBase = serverBase;
   }

   public String getWorkDir() {
      return workDir;
   }

   public void setWorkDir(String workDir) {
      this.workDir = workDir;
   }

   /**
    * 用于标识文件属性
    */
   /*shapefile*/
   public static final String SF_ATTR = "SF";
   /*高清遥感*/
   public static final String HD_ATTR = "HD";
   
   public static final Log log = LogFactory.getLog(GeoServiceImpl.class);
   
   public String publishGeoFile(Shapefile.Category type, File geoFile) {
      System.out.println(serverBase);
      if(log != null) {
         return "";
      }
      HttpClient client = null;
      CallContext context = new CallContext();
      try {
         String fileName = geoFile.getName();
         context.fileName = fileName;
         int pos = fileName.lastIndexOf('.');
         context.storeName = fileName.substring(0, pos);
         /**根据文件后缀判断文件属性*/
         if(fileName.endsWith(".shp")) {
            context.fileAttr = SF_ATTR;
            copyShapeFile(geoFile, context.storeName);
         } else {
            context.fileAttr = HD_ATTR;
            copyHdFile(geoFile, context.storeName);
         }
        
         context.workspaceName = WORKSPACE_NAME;
         client = createClient();
         HttpResponse response;
         String html;
         String location;
         /** 登陆 */
         response = loginGeoServer(client, context);
         location = getLocation(response);
         /** 如果location中有error说明认证失败 */
         if (location == null || location.contains("error")) {
            throw new RuntimeException("登陆失败");
         }

         /** 打开添加存储页面 */
         response = openStorePage(client, context);

         html = getContent(response);
         context.location = getWebLocation(getStoreSubmitFormAction(html));
         String workSpaceId = getWorkspaceId(html);
         context.put(WS_KEY, workSpaceId);
         context.lastHtml = html;
         /** 添加存储 */
         addStore(client, context);

         /** 打开发布页面 */
         response = openPublishLayerPage(client, context);
         html = getContent(response);
         context.lastHtml = html;
         context.location = getWebLocation(search(html, "<form", "?wicket:interface", 0, "\""));
         /** 添加layer */
         addLayer(client, context);

      } finally {
         if (client != null) {
            client.getConnectionManager().shutdown();
         }
      }
     return context.storeName;
   }

   /**
    * 将shapeFile拷贝到工作目录下
    * 
    * @param shapeFile
    */
   private void copyShapeFile(File shapeFile, String rename) {
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
    * 将高清遥感图拷贝到工作目录下
    * @param hdFile
    * @param rename
    */
   private void copyHdFile(File hdFile, String rename) {
      copyOne(hdFile, rename);
   }

   /**
    * 拷贝一个文件
    * 
    * @param source
    * @param rename
    */
   private void copyOne(File source, String rename) {
      if (!source.exists()) {
         return;
      }
      String name = source.getName();
      int pos = name.indexOf(".");
      String suffix = name.substring(pos + 1);
      File file = new File(workDir);
      if(! file.exists()) {
         file.mkdirs();
      }
      File dest = new File(workDir + rename + "." + suffix);
      try {
         FileUtils.copyFile(source, dest);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * 在存储发布页面中找到工作空间的ID标识
    * 
    * @param html
    * @return
    */
   private String getWorkspaceId(String html) {
      int idStart = reverseSearch(html, WORKSPACE_NAME, '=');
      return search(html, null, idStart, 2, "\"");
   }

   /**
    * 创建一个http客户端
    * */
   private HttpClient createClient() {
      ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
      manager.setDefaultMaxPerRoute(20);
      DefaultHttpClient client = new DefaultHttpClient(manager);
      HttpHost proxy = new HttpHost("127.0.0.1", 8080);
      client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
      HttpClientParams.setCookiePolicy(client.getParams(),
            CookiePolicy.BROWSER_COMPATIBILITY);
      return client;
   }

   /**
    * 登陆到服务器
    * 
    * @param client
    * @return
    */
   private HttpResponse loginGeoServer(HttpClient client, CallContext context) {
      HttpGet get = new HttpGet(serverBase + "/web/");
      execute(client, get);

      HttpPost httpPost = new HttpPost(serverBase + "/j_spring_security_check");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("username", "admin"));
      params.add(new BasicNameValuePair("password", "geoserver"));
      HttpResponse response = execute(params, client, httpPost);
      return response;
   }

   /**
    * 打开存储选择页面
    * 
    * @param client
    * @param context
    */
   public HttpResponse openStorePage(HttpClient client, CallContext context) {
      HttpGet httpGet = new HttpGet(getWebLocation("?wicket:bookmarkablePage=:org.geoserver.web.data.store.NewDataPage"));
      HttpResponse response = execute(client, httpGet);
      String html = getContent(response);
      /** 发送post请求到shapefile页面 */
      HttpPost post = new HttpPost(getWebLocation(getStorePageFormAction(html)));
      String key;
      /**根据文件属性选择打开shapefile格式还是tif格式链接*/
      if(SF_ATTR.equals(context.fileAttr)) {
         key = "vectorResources:4:resourcelink";
      } else {
         key = "rasterResources:1:resourcelink";
      }
      
      response = execute(Arrays.asList(new BasicNameValuePair(key, "x")),client, post);
      String location = getLocation(response);

      /** 跳转到返回的shapefile页面 */
      httpGet = new HttpGet(location);
      response = execute(client, httpGet);
      return response;
   }

   /**
    * 得到存储页面post地址 部分格式为 <form id="id141" method="post"
    * action="?wicket:interface=:19:storeForm::IFormSubmitListener::">
    * 得到?wicket:interface=:19:storeForm::IFormSubmitListener::
    * 
    * @param html
    * @return
    */
   private String getStorePageFormAction(String html) {
      return search(html, "<form", "action=", 8, "\"");
   }

   /**
    * 得到存储提交页面post地址
    * 
    * @param html
    * @return
    */
   private String getStoreSubmitFormAction(String html) {
      int formStart = html.indexOf("<form");
      String location = search(html.substring(formStart), "<div class=\"button-group", "?wicket:interface", 0, "\'");
      return location.replace("&amp;", "&");
   }



   /**
    * 提交shapefile表单
    * 
    * @param client
    * @param context
    */
   private HttpResponse addStore(HttpClient client, CallContext context) {
      
      String html = context.lastHtml;
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /** 工作空间yaogan */
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue",(String) context.get(WS_KEY)));
      String wsAction = search(html, "<select", "?", 0, "'");
      /**点击下拉的工作空间*/
      HttpPost wsPost = new HttpPost(getWebLocation(wsAction));
      execute(params, client, wsPost);
      
      /** 存储名称 */
      String storeNameKey;
      /** 存储描述 */
      String descKey;   
      String enableKey;
      if(SF_ATTR.equals(context.fileAttr)) {
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
      int start = reverseSearch(html, "Browse...", '?');
      int end = html.indexOf("'", start);
      String action = html.substring(start, end);
      context.location = getWebLocation(action);
      selectFile(client, context);
      /** 存储目录 */
      params.add(new BasicNameValuePair("parametersPanel:url:border:paramValue",
            "file:yaogandata/" + context.fileName));
      params.add(new BasicNameValuePair("parametersPanel:url:dialog:content:roots", "0"));
      if(SF_ATTR.equals(context.fileAttr)) {
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
      HttpResponse response = execute(params, client, httpPost);
      return response;
   }

   private HttpResponse selectFile(HttpClient client, CallContext context) {
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /** 工作空间test */
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue",
            (String) context.get(WS_KEY)));
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
      HttpResponse response = execute(params, client, httpPost);
      String html = getContent(response);
      
      int start = reverseSearch(html, FOLDER_NAME, '?');
      int end = html.indexOf("\'", start);
      String href = html.substring(start, end);
      /** 点击yaogandata */
      HttpGet get = new HttpGet(getWebLocation(href));
      response = execute(client, get);
      html = getContent(response);
      start = reverseSearch(html, context.fileName, '?');
      end = html.indexOf("\'", start);
      href = html.substring(start, end);
      /** 点击文件 */
      get = new HttpGet(getWebLocation(href));
      execute(client, get);
      return response;
   }

   private HttpResponse addLayer(HttpClient client, CallContext context) {
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:name",
            context.storeName));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:title",
            context.storeName));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:abstract", ""));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:keywords:newKeyword", ""));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:lang", ""));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:vocab",
            ""));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeSRS:srs", "UNKNOWN"));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:declaredSRS:srs", "EPSG:4326"));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:declaredSRS:popup:content:table:filterForm:filter",
            "4326"));
      if(SF_ATTR.equals(context.fileAttr)) {
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "FORCE_DECLARED"));
      } else {
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "REPROJECT_TO_DECLARED"));
      }
      String[] nativeBound = getNativeBound(client, context, params);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minX", nativeBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minY", nativeBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxX", nativeBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxY", nativeBound[3]));
      String[] llBound = getLLBound(client, context, params);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minX", llBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minY", llBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxX", llBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxY", llBound[3]));
      
      if(HD_ATTR.equals(context.fileAttr)) {
         params.add(new BasicNameValuePair("tabs:panel:theList:1:content:parameters:0:parameterPanel:border:paramValue", ""));
         String html = context.lastHtml;
         int pos = reverseSearch(html, "name=\"tabs:panel:theList:1:content:parameters:1:parameterPanel:border:paramValue", '=');
         String value = search(html, null, pos, 2, "\"");
         params.add(new BasicNameValuePair("tabs:panel:theList:1:content:parameters:1:parameterPanel:border:paramValue", value));
      }
      
      params.add(new BasicNameValuePair("save", "x"));
      HttpResponse response = execute(params, client, httpPost);
      return response;
   }
   
   /**
    * 计算本地边界值 点击compute from data按钮
    * @param client
    * @param context
    * @param params
    * @return
    */
   private String[] getNativeBound(HttpClient client, CallContext context, List<NameValuePair> params) {
      String content;
      String html = context.lastHtml;
      int startPos = html.indexOf("nativeBoundingBox");
      /**如果是shapefile格式的 则需要发起请求*/
      if(SF_ATTR.equals(context.fileAttr)) {
         String link = getWebLocation(search(html, startPos, "?", 0, "'"));
         HttpPost post = new HttpPost(link);
         post.addHeader("Wicket-Ajax", "true");
         post.addHeader("Accept", "text/xml");
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:computeNative", "1"));
         HttpResponse response = execute(params, client, post);
         params.remove(params.size() - 1);
         content = getContent(response);
         /**tif格式的可以从页面直接得到*/
      } else {
         content = html.substring(startPos);
      }
      int minXStart = content.indexOf("value=") + 7;
      int minXEnd = content.indexOf("\"", minXStart);
      String minX = content.substring(minXStart, minXEnd);

      int minYStart = content.indexOf("value=", minXEnd) + 7;
      int minYEnd = content.indexOf("\"", minYStart);
      String minY = content.substring(minYStart, minYEnd);
      
      int maxXStart = content.indexOf("value=", minYEnd) + 7;
      int maxXEnd = content.indexOf("\"", maxXStart);
      String maxX = content.substring(maxXStart, maxXEnd);
      int maxYStart = content.indexOf("value=", maxXEnd) + 7;
      int maxYEnd = content.indexOf("\"", maxYStart);
      String maxY = content.substring(maxYStart, maxYEnd);
      return new String[]{minX, minY, maxX, maxY};
   }
   
   /**
    * 计算经纬度边界值 点击compute from data按钮
    * @param client
    * @param context
    * @param params
    * @return
    */
   private String[] getLLBound(HttpClient client, CallContext context, List<NameValuePair> params) {
      String html = context.lastHtml;
      int startPos = html.indexOf("Lat/Lon Bounding Box");
      String content;
      if(SF_ATTR.equals(context.fileAttr)) {
         int linkStart = html.indexOf("?", startPos);
         int linkEnd = html.indexOf("'", linkStart);
         String link = getWebLocation(html.substring(linkStart, linkEnd));
         HttpPost post = new HttpPost(link);
         post.addHeader("Wicket-Ajax", "true");
         post.addHeader("Accept", "text/xml");
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:computeLatLon", "1"));
         HttpResponse response = execute(params, client, post);
         content = getContent(response);
         params.remove(params.size() - 1);
      } else {
         content = html.substring(startPos);
      }
      int minXStart = content.indexOf("value=") + 7;
      int minXEnd = content.indexOf("\"", minXStart);
      String minX = content.substring(minXStart, minXEnd);

      int minYStart = content.indexOf("value=", minXEnd) + 7;
      int minYEnd = content.indexOf("\"", minYStart);
      String minY = content.substring(minYStart, minYEnd);
      
      int maxXStart = content.indexOf("value=", minYEnd) + 7;
      int maxXEnd = content.indexOf("\"", maxXStart);
      String maxX = content.substring(maxXStart, maxXEnd);
      int maxYStart = content.indexOf("value=", maxXEnd) + 7;
      int maxYEnd = content.indexOf("\"", maxYStart);
      String maxY = content.substring(maxYStart, maxYEnd);
      return new String[]{minX, minY, maxX, maxY};
   }


   /**
    * 打开发布页面 为了独立性 不是在存储后点击发布 而是在layer页面中选择发布
    * 
    * @param client
    * @param context
    * @return
    */
   public HttpResponse openPublishLayerPage(HttpClient client, CallContext context) {
      HttpGet httpGet = new HttpGet(getWebLocation("?wicket:bookmarkablePage=:org.geoserver.web.data.layer.NewLayerPage"));
      HttpResponse response = execute(client, httpGet);
      String html = getContent(response);
      /** 得到刚加入的存储 */
      int storeStart = html.indexOf(context.workspaceName + ":" + context.storeName);
      while (storeStart-- > 0) {
         if (html.charAt(storeStart) == '=') {
            break;
         }
      }
      int idStart = reverseSearch(html, context.workspaceName + ":" + context.storeName, '=');
      String id = search(html, null, idStart, 2, "\"");
      /** 得到form表单地址 */
      String action = search(html, "<select name=\"storesDropDown\"", "?", 0, "\'");
      HttpPost httpPost = new HttpPost(getWebLocation(action));
      response = execute(Arrays.asList(new BasicNameValuePair("storesDropDown", id)),
            client, httpPost);
      html = getContent(response);
      int linkStart = reverseSearch(html, "Publish<", '?');
      String link = search(html, null, linkStart, 0, "'");
      /** 点击发布链接 */
      httpGet = new HttpGet(getWebLocation(link));
      response = execute(client, httpGet);
      return response;
   }

   private HttpResponse execute(HttpClient client, HttpGet get) {
      try {
         HttpResponse response = client.execute(get);
         return response;
      } catch (Exception e) {
         throw new RuntimeException("Get调用异常:" + get.getURI(), e);
      }
   }

   private HttpResponse execute(List<? extends NameValuePair> params, HttpClient client,
         HttpPost post) {
      try {
         post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("Post 调用异常:" + post.getURI(), e);
      }
      return execute(client, post);
   }

   private HttpResponse execute(HttpClient client, HttpPost post) {
      try {
         return client.execute(post);
      } catch (Exception e) {
         throw new RuntimeException("Get调用异常:" + post.getURI(), e);
      }
   }

   private static String getContent(HttpResponse response) {
      try {
         return EntityUtils.toString(response.getEntity());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * 得到http跳转的地址
    * 
    * @param response
    * @return
    */
   private static String getLocation(HttpResponse response) {
      Header[] locations = response.getHeaders("Location");
      if (locations != null && locations.length > 0) {
         return locations[0].getValue();
      }
      return null;
   }
   
   /**
    * geoserver地址+web + suffix拼接
    * @param suffix
    * @return
    */
   private static String getWebLocation(String suffix) {
      return SERVER_BASE + "/web/" + suffix;
   }
   
   /**
    * 从object处反向查找ch字符的位置
    * @param html
    * @param from
    * @param ch
    * @return
    */
   private static int reverseSearch(String html, Object from, char ch) {
      int fromPos = 0;
      if(from instanceof Integer) {
         fromPos = (Integer) from;
      } else {
         fromPos = html.indexOf((String) from);
         if(fromPos == -1) {
            log.info("html:" + html);
            throw new RuntimeException("from字符串搜索失败," + from);
         }
      }
      while(fromPos-- >= 0) {
         if(html.charAt(fromPos) == ch) {
            return fromPos;
         }
      }
      return -1;
   }
   /**
    * 从html中搜索字符串 从from 处开始找 找内容在start和end之间的
    * @param html
    * @param from
    * @param start
    * @param end
    * @return
    */
   private static String search(String html, Object from, Object start, Integer startOffset, String end) {
      int fromPos = 0;
      if(from != null) {
         if(from instanceof Integer) {
            fromPos = (Integer) from;
         } else {
            fromPos = html.indexOf((String) from);
            if(fromPos == -1) {
               log.info("html:" + html);
               throw new RuntimeException("from字符串搜索失败," + from);
            }
         }
      }
      int startPos;
      if(start instanceof Integer) {
         startPos = (Integer) start;
      } else {
            startPos = html.indexOf((String) start, fromPos);
            if(startPos == -1) {
               log.info("html:" + html);
               throw new RuntimeException("start字符串搜索失败," + start);
            }
      }
      if(startOffset == null) {
         if(start instanceof String) {
            startPos += ((String) start).length();
         } 
      } else {
         startPos += startOffset;
      }
      int endPos = html.indexOf(end, startPos);
      if(endPos == -1) {
         throw new RuntimeException("end字符串搜索失败," + end);
      }
      return html.substring(startPos, endPos);
   }

   /**
    * 用户存储调用的上下文
    * 
    * @author liyuliang
    * 
    */
   private static class CallContext extends HashMap<String, Object> {

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
      /**
       * 文件属性
       * */
      private String fileAttr;
      /**
       * 文件类型
       */
      private String fileType;
      /**
       * 文件名称
       * */
      private String fileName;

      public CallContext() {

      }
   }

}
