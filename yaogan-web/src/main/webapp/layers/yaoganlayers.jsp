<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/includes.jsp"%>
   <!-- 左边 -->
      <div id="leftDiv">
      <div>
      <!-- 矿区选择 -->
         <ul id="kqSelect">
            <li>
                                        地区:<select id="placeSelect">
                  <option value ="">--选择--</option>
                  <c:forEach items="${places}" var="place">
                     <option value ="${place.id}">${place.name}</option>
                  </c:forEach>
               </select>
            </li>
            <li>
                                            日期:<select id="timeSelect">
                  <option value ="">--选择--</option>
               </select>
            </li>
            <li style="float:none"><input id="queryMap" type="button" value="地图查询"></input></li>
         </ul>
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
    </div>
    <!-- 右边 -->
     <div id="rightDiv">
     <div id="queryType"> 
        <input type="checkbox" name="swfd" checked="checked"/>生物丰度
        <input type="checkbox" name="zbfg" checked="checked"/>植被覆盖
        <input type="checkbox" name="tdth" checked="checked"/>土地退回
     </div>
     <div>
        <input id="queryData" type="button" value="查询指数"/><br/>
      查询结果：<br/>
      </div>
      <div id="content"></div>
     </div>