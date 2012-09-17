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

public class AddLayerTask  implements GeoClientTask{

   @Override
   public <T> T doTask(GeoServerClient geoClient, Object... objects) {
      CallContext context = geoClient.getContext();
      HttpClient client = geoClient.getClient();
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
      String[] nativeBound = getNativeBound(geoClient, context, params);
      context.put(GeoServerClient.NATIVE_KEY, nativeBound);
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minX", nativeBound[0]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:minY", nativeBound[1]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxX", nativeBound[2]));
      params.add(new BasicNameValuePair(
            "tabs:panel:theList:0:content:referencingForm:nativeBoundingBox:maxY", nativeBound[3]));
      String[] llBound = getLLBound(geoClient, context, params);
      context.put(GeoServerClient.LL_KEY, llBound);
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
      return (T) response;
   }
   
   /**
    * 计算本地边界值 点击compute from data按钮
    * @param client
    * @param context
    * @param params
    * @return
    */
   private String[] getNativeBound(GeoServerClient geoClient, CallContext context, List<NameValuePair> params) {
      String content;
      String html = context.lastHtml;
      int startPos = html.indexOf("nativeBoundingBox");
      /**如果是shapefile格式的 则需要发起请求*/
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         String link = geoClient.getWebLocation(GeoServiceUtil.search(html, startPos, "?", 0, "'"));
         HttpPost post = new HttpPost(link);
         post.addHeader("Wicket-Ajax", "true");
         post.addHeader("Accept", "text/xml");
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:computeNative", "1"));
         HttpResponse response = GisHttpUtil.execute(params, geoClient.getClient(), post);
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
   private String[] getLLBound(GeoServerClient geoClient, CallContext context, List<NameValuePair> params) {
      String html = context.lastHtml;
      int startPos = html.indexOf("Lat/Lon Bounding Box");
      String content;
      if(GeoServiceUtil.SF_ATTR.equals(context.fileAttr)) {
         int linkStart = html.indexOf("?", startPos);
         int linkEnd = html.indexOf("'", linkStart);
         String link = geoClient.getWebLocation(html.substring(linkStart, linkEnd));
         HttpPost post = new HttpPost(link);
         post.addHeader("Wicket-Ajax", "true");
         post.addHeader("Accept", "text/xml");
         params.add(new BasicNameValuePair("tabs:panel:theList:0:content:referencingForm:computeLatLon", "1"));
         HttpResponse response = GisHttpUtil.execute(params, geoClient.getClient(), post);
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


}
