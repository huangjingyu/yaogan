package com.rockontrol.yaogan.service.util;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class GeoServiceUtil {
   
   public static final Log log = LogFactory.getLog(GeoServiceUtil.class);

   /**
    * 生成一个uuid标识
    * @return
    */
   public static String getUUID() {
      String s = UUID.randomUUID().toString(); 
      //去掉“-”符号 
      return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
   }
   
   /**
    * 从object处反向查找ch字符的位置
    * @param html
    * @param from
    * @param ch
    * @return
    */
   public static int reverseSearch(String html, Object from, char ch) {
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
   public static String search(String html, Object from, Object start, Integer startOffset, String end) {
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
    * 得到http响应的信息
    * @param response
    * @return
    */
   public static String getContent(HttpResponse response) {
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
   public static String getLocation(HttpResponse response) {
      Header[] locations = response.getHeaders("Location");
      if (locations != null && locations.length > 0) {
         return locations[0].getValue();
      }
      return null;
   }
}
