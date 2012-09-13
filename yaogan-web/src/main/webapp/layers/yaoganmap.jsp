<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/includes.jsp"%>
      <div class="rmaplist">
         <div class="map_top">           
         <table>
             <tr>
               <td width="72" style=" font-weight:bold">地图展示</td>
               <td width="130">
                                        地区:<select id="placeSelect">
                  <option value ="">--选择--</option>
                  <c:forEach items="${places}" var="place">
                     <option value ="${place.id}">${place.name}</option>
                  </c:forEach>
               </select>
               </td>
               <td  width="120">
                                            日期:<select id="timeSelect">
                  <option value ="">--选择--</option>
               </select>
               </td>
               <td>
                   <a href="#"><img id="queryMap" src="<%=request.getContextPath()%>/static/img/butt_search.gif" alt="查询" width="50" height="24" style="display:block; margin:0 auto"/></a>
               </td>
             </tr>
           </table></div>
           
         <div class="rmapnr">      
         <div style="margin-left:10px">
         <ul id="mapSelect">
            <li style="margin-right:30px">
               <input type="radio" value="td" name="baseType" checked="checked"/>二维图
               <input type="radio" value="yg" name="baseType"/>遥感图
            </li>
            <li>
               <input type="checkbox" value="kq"/>矿区
               <input type="checkbox" value="tdly"/>土地利用
               <input type="checkbox" value="dbtx"/>地表塌陷
               <input type="checkbox" value="dlf"/>地裂缝
               <input type="checkbox" value="trqs"/>土壤侵蚀
               <input type="checkbox" value="gqyg"/>高清遥感图
            </li>
         </ul> 
      </div>
      <div id="map"></div>
      </div>
      
      <div class="rmbutt"><a href="#"  id="thematicMapLink"><img src="<%=request.getContextPath()%>/static/img/butt_creatimg.jpg" width="132" height="39" /></a></div>
    </div>
   <div id="rightd">      
   <div id="right">
    <div class="right_top">地图信息</div>
    <div id="placeData" class="message">
     <h2>整个地区</h2>
     <ul>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_1.gif" />植被覆盖指数：<span id="plczbfg"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_2.gif" />生物丰度指数：<span id="plcswfd"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_3.gif" />地质环境指数：<span id="plcdzhj"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_4.gif" />土地退化指数：<span id="plctdth"></span></li>
     </ul>    
    </div>
    <div id="areaData" class="message">
     <h2>所选地区</h2>
     <ul>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_1.gif" />植被覆盖指数：<span id="areazbfg"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_2.gif" />生物丰度指数：<span id="areaswfd"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_3.gif" />地质环境指数：<span id="areadzhj"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_4.gif" />土地退化指数：<span id="areatdth"></span></li>
     </ul>    
    </div>
   </div>
   </div>