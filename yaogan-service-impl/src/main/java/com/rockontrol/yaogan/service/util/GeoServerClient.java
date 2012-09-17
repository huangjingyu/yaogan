package com.rockontrol.yaogan.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import com.rockontrol.yaogan.service.util.task.AddLayerTask;
import com.rockontrol.yaogan.service.util.task.AddStoreTask;
import com.rockontrol.yaogan.service.util.task.LoginTask;
import com.rockontrol.yaogan.service.util.task.OpenPublishLayerPageTask;
import com.rockontrol.yaogan.service.util.task.OpenStorePageTask;
import com.rockontrol.yaogan.service.util.task.PublishLayerTask;

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
   
   private GeoClientTask loginTask =  new LoginTask();
   
   private GeoClientTask openStorePageTask = new OpenStorePageTask();
   
   private GeoClientTask addStoreTask = new AddStoreTask();
   
   private GeoClientTask openPublishLayerPageTask = new OpenPublishLayerPageTask();
   
   private GeoClientTask addLayerTask = new AddLayerTask();
   
   private GeoClientTask publishLayerTask = new PublishLayerTask();
   
   public String getServerBase() {
      return serverBase;
   }

   public void setServerBase(String serverBase) {
      this.serverBase = serverBase;
   }

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
      HttpResponse response = loginTask.doTask(this, serverBase, userName, password);
      return response;
   }
   
   /**
    * 打开存储选择页面
    * 
    * @param client
    * @param context
    */
   public HttpResponse openStorePage() {
      HttpResponse response = openStorePageTask.doTask(this);
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
      HttpResponse response = addStoreTask.doTask(this);
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
      HttpResponse response = openPublishLayerPageTask.doTask(this);
      return response;
   }
   
   public HttpResponse addLayer() {
      HttpResponse response = addLayerTask.doTask(this);
      return response;
   }
   

   /**
    * 发布图层 主要是添加样式
    * @param client
    * @param context
    * @return
    */
   public HttpResponse publlishLayer() {
      HttpResponse response = publishLayerTask.doTask(this);
      return response;
   }

   


}
