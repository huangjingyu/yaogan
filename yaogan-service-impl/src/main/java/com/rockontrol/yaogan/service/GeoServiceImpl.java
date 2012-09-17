package com.rockontrol.yaogan.service;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import com.rockontrol.yaogan.model.Shapefile;
import com.rockontrol.yaogan.service.util.CallContext;
import com.rockontrol.yaogan.service.util.GeoServerClient;
import com.rockontrol.yaogan.service.util.GeoServiceUtil;
import com.rockontrol.yaogan.service.util.GisHttpUtil;

public class GeoServiceImpl implements GeoService {

   public static final String SERVER_BASE = "http://localhost:8888/geoserver";


   /**
    * geoserver启动的地址
    */
   private String serverBase = SERVER_BASE;
   
   private String userName = "admin";
   
   private String password = "geoserver";
   
   
   
   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getServerBase() {
      return serverBase;
   }

   public void setServerBase(String serverBase) {
      this.serverBase = serverBase;
   }

   
   public static final Log log = LogFactory.getLog(GeoServiceImpl.class);
   
   public String publishGeoFile(Shapefile.Category type, File geoFile) {
      HttpClient client = null;
      CallContext context = new CallContext();
      context.category = type;
      context.file = geoFile;
      if(! geoFile.exists()) {
         throw new RuntimeException("文件:" + geoFile.getAbsolutePath() + "不存在");
      }
      try {
         String fileName = geoFile.getName();
         context.fileName = fileName;
         context.storeName = fileName;
         log.info(fileName + ":" + context.storeName);
         /**根据文件后缀判断文件属性*/
         if(fileName.endsWith(".shp")) {
            context.fileAttr = GeoServiceUtil.SF_ATTR;
            //GeoServiceUtil.copyShapeFile(geoFile, context.storeName, workDir);
            context.newFileName = context.storeName + ".shp";
         } else {
            context.fileAttr = GeoServiceUtil.HD_ATTR;
            //GeoServiceUtil.copyHdFile(geoFile, context.storeName, workDir);
            context.newFileName = context.storeName + ".tif";
         }
        
         context.workspaceName = GeoServerClient.WORKSPACE_NAME;
         client = GisHttpUtil.createClient();
         HttpResponse response;
         String html;
         String location;
         GeoServerClient geoClient = new GeoServerClient(client, context, serverBase);
         /** 登陆 */
         response = geoClient.loginGeoServer(userName, password);
         location = GeoServiceUtil.getLocation(response);
         /** 如果location中有error说明认证失败 */
         if (location == null || location.contains("error")) {
            throw new RuntimeException("登陆失败");
         }

         /** 打开添加存储页面 */
         response = geoClient.openStorePage();

         html = GeoServiceUtil.getContent(response);
         context.location = geoClient.getWebLocation(getStoreSubmitFormAction(html));
         String workSpaceId = getWorkspaceId(html);
         context.put(GeoServerClient.WS_KEY, workSpaceId);
         context.lastHtml = html;
         /** 添加存储 */
         geoClient.addStore();

         /** 打开发布页面 */
         response = geoClient.openPublishLayerPage();
         html = GeoServiceUtil.getContent(response);
         context.lastHtml = html;
         context.location = geoClient.getWebLocation(GeoServiceUtil.search(html, "<form", "?wicket:interface", 0, "\""));
         /** 添加layer */
         geoClient.addLayer();
         /**发布layer 主要是添加样式*/
         if(GeoServiceUtil.getStyle(type) != null) {
            response = geoClient.publlishLayer();
         }
      } finally {
         if (client != null) {
            client.getConnectionManager().shutdown();
         }
      }
      String[] bounds = (String[]) context.get(GeoServerClient.NATIVE_KEY);
      String bound = bounds[0].replace(",", "") + "," + bounds[1].replace(",", "")  + "," + bounds[2].replace(",", "")  + "," + bounds[3].replace(",", "");
      String wmsInfo = GeoServerClient.WORKSPACE_NAME + ":" + context.storeName + "?" + bound;
      log.info("wmsInfo:" + wmsInfo);
      return wmsInfo;
   }


   /**
    * 在存储发布页面中找到工作空间的ID标识
    * 
    * @param html
    * @return
    */
   private String getWorkspaceId(String html) {
      int idStart = GeoServiceUtil.reverseSearch(html, GeoServerClient.WORKSPACE_NAME, '=');
      if(idStart == -1) {
         throw new RuntimeException("请先创建名称为:" + GeoServerClient.WORKSPACE_NAME + "的工作空间");
      }
      return GeoServiceUtil.search(html, null, idStart, 2, "\"");
   }


   /**
    * 得到存储提交页面post地址
    * 
    * @param html
    * @return
    */
   private String getStoreSubmitFormAction(String html) {
      int formStart = html.indexOf("<form");
      String location = GeoServiceUtil.search(html.substring(formStart), "<div class=\"button-group", "?wicket:interface", 0, "\'");
      return location.replace("&amp;", "&");
   }
}
