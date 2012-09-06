package com.rockontrol.yaogan.service.util;

import java.util.HashMap;

/**
 * GeoService调用时的上下文
 * @author liyuliang
 *
 */
public class CallContext extends HashMap<String, Object> {

   public String location;

   /**
    * 工作空间的名字
    */
   public String workspaceName;
   /**
    * 存储的名字 即输入的name
    */
   public String storeName;

   public String lastHtml;
   /**
    * 文件属性
    * */
   public String fileAttr;
   /**
    * 文件类型
    */
   public String fileType;
   /**
    * 文件名称
    * */
   public String fileName;
   /**
    * 新文件名称
    */
   public String newFileName;

   public CallContext() {

   }
}

