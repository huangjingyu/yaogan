<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/includes.jsp"%>
<script type="text/javascript">
function print(){
	var url="<%=request.getContextPath()%>/user/print?placeId=1&time=&category=";
	window.location.href = url;
}
</script>
   <!-- 左边 -->
      <div id="leftDiv">
        <div class="map_top">
           <form id="form2" name="form2" method="post" action="">
           <table  border="0" cellpadding="0" cellspacing="0">
             <tr>
               <td width="72" style=" font-weight:bold">地图展示</td>
               <td>
                                        地区:<select id="placeSelect">
                  <option value ="">--选择--</option>
                  <c:forEach items="${places}" var="place">
                     <option value ="${place.id}">${place.name}</option>
                  </c:forEach>
               </select>
               </td>
               <td>
                                            日期:<select id="timeSelect">
                  <option value ="">--选择--</option>
               </select>
               </td>
               <td>
                   <a href="#"><img id="queryMap" src="<%=request.getContextPath()%>/static/img/butt_search.gif" alt="查询" width="50" height="24" style="display:block; margin:0 auto"/></a>
               </td>
             </tr>
           </table>
           </form>
         </div>
      <!-- 地图选择 -->
      <div>
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
               <div id="wrapper">
         <!-- 地理位置信息 -->
         <div id="location">位置</div>
         <!-- 比例尺信息 -->
         <div id="scale">Scale</div> 
       </div>
       <!-- 区域选择的类型 -->
       <div id="selectOp">
       <input type="radio" value="nav" checked="checked" name="op"/>导航
       <input type="radio" value="box" name="op"/>区域绘制
       <input type="radio" value="pyn" name="op"/>多边形绘制
       <input type="radio" value="select" name="op"/>区块选择
       <input type="button" value="清除所选" name="rm" id="rm"/>
       <input type="button" value="清除全部" name="rmAll" id="rmAll"/>
       </div>
      <div class="map_bottom" style=""><img src="<%=request.getContextPath()%>/static/img/butt_creatimg.jpg" width="132" height="39" onclick="print()"/></div>       
    </div>
    <!-- 右边 -->
     <div id="rightDiv">
     <div class="right_top">指数查询</div>
     <div id="queryType" style="margin-left:10px"> 
     <table><tr><td>
        <input type="checkbox" name="swfd" checked="checked"/>生物丰度
        <input type="checkbox" name="zbfg" checked="checked"/>植被覆盖<br/>
        <input type="checkbox" name="tdth" checked="checked"/>土地退回
        <input type="checkbox" name="dzhj" checked="checked"/>地质环境
        </td></tr><tr><td>
        <a href="#"><img id="queryData" src="<%=request.getContextPath()%>/static/img/butt_search.gif" alt="查询" width="50" height="24" style="display:block; margin:0 auto;"/></a>
     </td></tr></table>
     </div>
    <div id="placeData" class="message">
     <h2>矿区指数</h2>
     <ul>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_1.gif" />植被覆盖指数：<span id="plczbfg"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_2.gif" />生物丰度指数：<span id="plcswfd"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_3.gif" />地质环境指数：<span id="plcdzhj"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_4.gif" />土地退化指数：<span id="plctdth"></span></li>
     </ul>    
    </div>
    <div id="areaData" class="message">
     <h2>矿区所选区域指数</h2>
     <ul>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_1.gif" />植被覆盖指数：<span id="areazbfg"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_2.gif" />生物丰度指数：<span id="areaswfd"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_3.gif" />地质环境指数：<span id="areadzhj"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_4.gif" />土地退化指数：<span id="areatdth"></span></li>
     </ul>    
    </div>
     </div>