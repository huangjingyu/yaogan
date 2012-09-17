package com.rockontrol.yaogan.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.rockontrol.yaogan.service.util.task.SelectFileTask;

public class GeoServerClient {
   
   public static final Log log = LogFactory.getLog(GeoServerClient.class);
   
   public static final String WS_KEY = "WS";
   public static final String SHP_FILE_KEY = "SHP_FILE";
   public static final String LL_KEY = "LL";
   public static final String NATIVE_KEY = "NATIVE";
   public static final String SELECT_ROOT_KEY = "SELECT_ROOT";
   
   /**
    * geoserver中手工创建的工作空间名称
    */
   public static final String WORKSPACE_NAME = "yaogan";
   
   public static final String FOLDER_NAME = "yaogandata";
   
   private HttpClient client;
   
   private CallContext context;
   
   private String serverBase;
   
   private GeoClientTask selectFileTask = new SelectFileTask();
   
   
   
   public HttpClient getClient() {
      return client;
   }

   public void setClient(HttpClient client) {
      this.client = client;
   }

   public CallContext getContext() {
      return context;
   }

   public void setContext(CallContext context) {
      this.context = context;
   }

   public GeoServerClient(HttpClient client, CallContext context, String serverBase) {
      this.client = client;
      this.context = context;
      this.serverBase = serverBase;
   }
   
   /**
    * 登陆到服务器
    * 
    * @param client
    * @return
    */
   public HttpResponse loginGeoServer(String userName, String password) {
      HttpGet get = new HttpGet(serverBase + "/web/");
      GisHttpUtil.execute(client, get);

      HttpPost httpPost = new HttpPost(serverBase + "/j_spring_security_check");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("username", userName));
      params.add(new BasicNameValuePair("password", password));
      HttpResponse response = GisHttpUtil.execute(params, client, httpPost);
      return response;
   }
   
   /**
    * 打开存储选择页面
    * 
    * @param client
    * @param context
    */
   public HttpResponse openStorePage() {
      HttpGet httpGet = new HttpGet(getWebLocation("?wicket:bookmarkablePage=:org.geoserver.web.data.store.NewDataPage"));
      HttpResponse response = GisHttpUtil.execute(client, httpGet);
      String html = GeoServiceUtil.getContent(response);
      /** 发送post请求到shapefile页面 */
      HttpPost post = new HttpPost(getWebLocation(GeoServiceUtil.search(html, "<form", "action=", 8, "\"")));
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
      return response;
   }
   
   /**
    * geoserver地址+web + suffix拼接
    * @param suffix
    * @return
    */
   public String getWebLocation(String suffix) {
      return serverBase + "/web/" + suffix;
   }
   
   /**
    * 提交shapefile表单
    * 
    * @param client
    * @param context
    */
   public HttpResponse addStore() {
      
      String html = context.lastHtml;
      HttpPost httpPost = new HttpPost(context.location);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      /** 工作空间yaogan */
      params.add(new BasicNameValuePair("workspacePanel:border:paramValue",(String) context.get(WS_KEY)));
      String wsAction = GeoServiceUtil.search(html, "<select", "?", 0, "'");
      /**点击下拉的工作空间*/
      HttpPost wsPost = new HttpPost(getWebLocation(wsAction));
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
      context.location = getWebLocation(action);
      /**选择文件*/
      HttpResponse response = selectFileTask.doTask(this, context.file);
      html = GeoServiceUtil.getContent(response);
      String path = GeoServiceUtil.search(html, null, "value=", 7, "\"");
      /** 存储目录 */
      params.add(new BasicNameValuePair("parametersPanel:url:border:paramValue", path));
      String root = (String) context.get(SELECT_ROOT_KEY);
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
      return response;
   }
   

   private HttpResponse selectFile() {
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
      HttpResponse response = GisHttpUtil.execute(params, client, httpPost);
      String html = GeoServiceUtil.getContent(response);
      
      int start = GeoServiceUtil.reverseSearch(html, FOLDER_NAME, '?');
      int end = html.indexOf("\'", start);
      String href = html.substring(start, end);
      /** 点击yaogandata */
      HttpGet get = new HttpGet(getWebLocation(href));
      response = GisHttpUtil.execute(client, get);
      html = GeoServiceUtil.getContent(response);
      start = GeoServiceUtil.reverseSearch(html, context.newFileName, '?');
      end = html.indexOf("\'", start);
      href = html.substring(start, end);
      /** 点击文件 */
      get = new HttpGet(getWebLocation(href));
      GisHttpUtil.execute(client, get);
      return response;
   }
   
   /**
    * 打开发布页面 为了独立性 不是在存储后点击发布 而是在layer页面中选择发布
    * 
    * @param client
    * @param context
    * @return
    */
   public HttpResponse openPublishLayerPage() {
      HttpGet httpGet = new HttpGet(getWebLocation("?wicket:bookmarkablePage=:org.geoserver.web.data.layer.NewLayerPage"));
      HttpResponse response = GisHttpUtil.execute(client, httpGet);
      String html = GeoServiceUtil.getContent(response);
      /** 得到刚加入的存储 */
      int storeStart = html.indexOf(context.workspaceName + ":" + context.storeName);
      while (storeStart-- > 0) {
         if (html.charAt(storeStart) == '=') {
            break;
         }
      }
      int idStart = GeoServiceUtil.reverseSearch(html, context.workspaceName + ":" + context.storeName, '=');
      String id = GeoServiceUtil.search(html, null, idStart, 2, "\"");
      /** 得到form表单地址 */
      String action = GeoServiceUtil.search(html, "<select name=\"storesDropDown\"", "?", 0, "\'");
      HttpPost httpPost = new HttpPost(getWebLocation(action));
      response = GisHttpUtil.execute(Arrays.asList(new BasicNameValuePair("storesDropDown", id)),
            client, httpPost);
      html = GeoServiceUtil.getContent(response);
      int linkStart = GeoServiceUtil.reverseSearch(html, "Publish<", '?');
      String link = GeoServiceUtil.search(html, null, linkStart, 0, "'");
      /** 点击发布链接 */
      httpGet = new HttpGet(getWebLocation(link));
      response = GisHttpUtil.execute(client, httpGet);
      return response;
   }
   
   public HttpResponse addLayer() {
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
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "FORCE_DECLARED"));
      } else {
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "REPROJECT_TO_DECLARED"));
      }
      String[] nativeBound = getNativeBound(params);
      context.put(NATIVE_KEY, nativeBound);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minX", nativeBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minY", nativeBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxX", nativeBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxY", nativeBound[3]));
      String[] llBound = getLLBound(params);
      context.put(LL_KEY, llBound);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minX", llBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minY", llBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxX", llBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxY", llBound[3]));
      
      if(GeoServiceUtil.HD_ATTR.equals(context.fileAttr)) {
         params.add(new BasicNameValuePair("tabs:panel:theList:1:content:parameters:0:parameterPanel:border:paramValue", ""));
         String html = context.lastHtml;
         int pos = GeoServiceUtil.reverseSearch(html, "name=\"tabs:panel:theList:1:content:parameters:1:parameterPanel:border:paramValue", '=');
         String value = GeoServiceUtil.search(html, null, pos, 2, "\"");
         params.add(new BasicNameValuePair("tabs:panel:theList:1:content:parameters:1:parameterPanel:border:paramValue", value));
      }
      
      params.add(new BasicNameValuePair("save", "x"));
      HttpResponse response = GisHttpUtil.execute(params, client, httpPost);
      return response;
   }
   
   /**
    * 计算本地边界值 点击compute from data按钮
    * @param client
    * @param context
    * @param params
    * @return
    */
   private String[] getNativeBound(List<NameValuePair> params) {
      String content;
      String html = context.lastHtml;
      int startPos = html.indexOf("nativeBoundingBox");
      /**如果是shapefile格式的 则需要发起请求*/
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         String link = getWebLocation(GeoServiceUtil.search(html, startPos, "?", 0, "'"));
         HttpPost post = new HttpPost(link);
         post.addHeader("Wicket-Ajax", "true");
         post.addHeader("Accept", "text/xml");
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:computeNative", "1"));
         HttpResponse response = GisHttpUtil.execute(params, client, post);
         params.remove(params.size() - 1);
         content = GeoServiceUtil.getContent(response);
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
   private String[] getLLBound(List<NameValuePair> params) {
      String html = context.lastHtml;
      int startPos = html.indexOf("Lat/Lon Bounding Box");
      String content;
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         int linkStart = html.indexOf("?", startPos);
         int linkEnd = html.indexOf("'", linkStart);
         String link = getWebLocation(html.substring(linkStart, linkEnd));
         HttpPost post = new HttpPost(link);
         post.addHeader("Wicket-Ajax", "true");
         post.addHeader("Accept", "text/xml");
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:computeLatLon", "1"));
         HttpResponse response = GisHttpUtil.execute(params, client, post);
         content = GeoServiceUtil.getContent(response);
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
    * 发布图层 主要是添加样式
    * @param client
    * @param context
    * @return
    */
   public HttpResponse publlishLayer() {
      /**打开图层页面*/
      String location = "?wicket:bookmarkablePage=:org.geoserver.web.data.resource.ResourceConfigurationPage&name=" + context.storeName + "&wsName=" + WORKSPACE_NAME;
      HttpGet get = new HttpGet(getWebLocation(location));
      HttpResponse response;
      response = GisHttpUtil.execute(client, get);
      String html = GeoServiceUtil.getContent(response);
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      BasicNameValuePair linkType = new BasicNameValuePair("tabs:tabs-container:tabs:1:link", "x");
      params.add(linkType);
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:name", context.storeName));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:title", context.storeName));
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeSRS:srs", "UNKNOWN"));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "FORCE_DECLARED"));
      } else {
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:nativeSRS:srs", "EPSG:4326"));   
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "REPROJECT_TO_DECLARED")); 
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:abstract", "")); 
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:newKeyword", "")); 
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:lang", "")); 
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:keywords:vocab", "")); 
         params.add(new BasicNameValuePair("tabs:panel:theList:1:content:parameters:0:parameterPanel:border:paramValue", ""));
      }
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:declaredSRS:srs", "EPSG:4326"));
      String[] nativeBound = (String[]) context.get(NATIVE_KEY);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minX", nativeBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minY", nativeBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxX", nativeBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxY", nativeBound[3]));
      String[] llBound = (String[]) context.get(LL_KEY);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minX", llBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:minY", llBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxX", llBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:latLonBoundingBox:maxY", llBound[3]));
      String action = GeoServiceUtil.search(html, "<form", "?", 0, "\"");
      /**点击publish 得到一个重定向地址*/
      HttpPost post = new HttpPost(getWebLocation(action));
      response = GisHttpUtil.execute(params, client, post);
      location = GeoServiceUtil.getLocation(response);
      /**请求重定向地址 得到页面html*/
      get = new HttpGet(location);
      response = GisHttpUtil.execute(client, get);
      html = GeoServiceUtil.getContent(response);
      
      String style = GeoServiceUtil.getStyle(context.category);
      if(style == null) {
         log.info("未配置样式");
         return null;
      }

      int startPos = GeoServiceUtil.reverseSearch(html, ">" + style + "</option>", '=');
      if(startPos == -1) {
         log.info("未找到匹配样式");
         return null;
      }
      String styleId = GeoServiceUtil.search(html, null, startPos, 2, "\"");
      List<NameValuePair> styleParams = new ArrayList<NameValuePair>();
      styleParams.add(new BasicNameValuePair("tabs:panel:theList:3:content:styles:defaultStyle", styleId));
      String styleAction = GeoServiceUtil.search(html, "tabs:panel:theList:3:content:styles:defaultStyle", "?", 0, "'");
      post = new HttpPost(getWebLocation(styleAction));
      post.addHeader("Wicket-Ajax", "true");
      post.addHeader("Accept", "text/xml");
      response = GisHttpUtil.execute(styleParams, client, post);
      String content = GeoServiceUtil.getContent(response);
      HttpGet picGet = new HttpGet(this.serverBase + "/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&STRICT=false&style=kuangqu");
      GisHttpUtil.execute(client, picGet);
      params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("save", "x"));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:enabled", "on"));
      params.add(new BasicNameValuePair("tabs:panel:theList:0:content:advertised", "on"));
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         params.add(new BasicNameValuePair("tabs:panel:theList:2:content:perReqFeaturesBorder:perReqFeatureLimit", "0"));
         params.add(new BasicNameValuePair("tabs:panel:theList:2:content:maxDecimalsBorder:maxDecimals", "0"));
      } else {
         params.add(new BasicNameValuePair("tabs:panel:theList:2:content:interpolationMethods:recorder", "nearest+neighbor,bilinear,bicubic"));
         params.add(new BasicNameValuePair("tabs:panel:theList:2:content:formatPalette:recorder", "Gtopo30,GIF,PNG,JPEG,TIFF,ImageMosaic,GEOTIFF,ArcGrid"));
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:srsHandling", "REPROJECT_TO_DECLARED"));
      }
      params.add(new BasicNameValuePair("tabs:panel:theList:3:content:queryableEnabled", "on"));
      params.add(new BasicNameValuePair("tabs:panel:theList:3:content:styles:defaultStyle", styleId));
      params.add(new BasicNameValuePair("tabs:panel:theList:4:content:wms.attribution.width", "0"));
      params.add(new BasicNameValuePair("tabs:panel:theList:4:content:wms.attribution.height", "0"));
      action = GeoServiceUtil.search(html, "<form", "?", 0, "\"");
      post = new HttpPost(getWebLocation(action));
      response = GisHttpUtil.execute(params, client, post);
      return response;
   }

   


}
