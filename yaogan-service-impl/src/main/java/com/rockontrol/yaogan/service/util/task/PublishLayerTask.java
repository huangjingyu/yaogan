package com.rockontrol.yaogan.service.util.task;

import java.util.ArrayList;
import java.util.List;

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

public class PublishLayerTask implements GeoClientTask{
   
   public static final Log log = LogFactory.getLog(PublishLayerTask.class);

   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
      /**打开图层页面*/
      String location = "?wicket:bookmarkablePage=:org.geoserver.web.data.resource.ResourceConfigurationPage&name=" + context.storeName + "&wsName=" + GeoServerClient.WORKSPACE_NAME;
      HttpGet get = new HttpGet(geoClient.getWebLocation(location));
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
      String[] nativeBound = (String[]) context.get(GeoServerClient.NATIVE_KEY);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minX", nativeBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minY", nativeBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxX", nativeBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxY", nativeBound[3]));
      String[] llBound = (String[]) context.get(GeoServerClient.LL_KEY);
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
      HttpPost post = new HttpPost(geoClient.getWebLocation(action));
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
      post = new HttpPost(geoClient.getWebLocation(styleAction));
      post.addHeader("Wicket-Ajax", "true");
      post.addHeader("Accept", "text/xml");
      response = GisHttpUtil.execute(styleParams, client, post);
      String content = GeoServiceUtil.getContent(response);
      HttpGet picGet = new HttpGet(geoClient.getServerBase() + "/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&STRICT=false&style=kuangqu");
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
      post = new HttpPost(geoClient.getWebLocation(action));
      response = GisHttpUtil.execute(params, client, post);
      return (T)response;
   }
}
