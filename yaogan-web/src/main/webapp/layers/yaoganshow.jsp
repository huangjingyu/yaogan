<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/includes.jsp"%>
 <c:set var="ctx" value="<%=request.getContextPath()%>" />
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
      <div style="float:left">
      <div id="map"></div><div style="clear:both"></div>
      </div>
      <div id="layer">
         <ul id="mapSelect">
            <li>
               <h4>专题图层</h4>
               <ul>
                  <li id="kq"><input type="checkbox" value="kq"/>矿区<div></div></li>
                  <li id="tdly"><input type="checkbox" value="tdly"/>土地利用<div></div></li>
                  <li id="dbtx"><input type="checkbox" value="dbtx"/>地表塌陷<div></div></li>
                  <li id="dlf"><input type="checkbox" value="dlf"/>地裂缝<div></div></li>
                  <li id="trqs"><input type="checkbox" value="trqs"/>土壤侵蚀<div></div></li>
                  <li id="gqyg"><input type="checkbox" value="gqyg"/>高清遥感<div></div></li>
               </ul>
            </li>
            <li>
               <h4>基础图层</h4>
               <input type="radio" value="td" name="baseType" checked="checked"/>二维图<br/>
               <input type="radio" value="yg" name="baseType"/>遥感图
            </li>            
         </ul> 
      </div>
      </div>
      <div class="rmbutt"><a href="#"  id="thematicMapLink"><img src="<%=request.getContextPath()%>/static/img/butt_creatimg.jpg" width="132" height="39" /></a></div>
    </div>
   <div id="rightd">      
   <div id="right">
    <div class="right_top">地图信息</div>
    <div id="placeData" class="message">
     <h2>整个地区</h2>
     <ul>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_1.gif" />植被覆盖指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="指评价区域内林地、草地、 耕地、 建设用地和未利用地五种类型的面积占评价区域面积的比重，用于反映评价区域植被覆盖的程度"/></a>：<span id="plczbfg"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_2.gif" />生物丰度指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="指通过单位面积上不同生态系统类型在生物物种数量上的差异，间接地反映评价区域内生物丰度的丰贫程度"/></a>：<span id="plcswfd"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_3.gif" />地质环境指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="表征评价区域内地表塌陷、地裂缝、地下水水位下降等地质破坏状况，用于反映评价区域地质破坏程度"/></a>：<span id="plcdzhj"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_4.gif" />土地退化指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="指评价区域内风蚀、水蚀、重力侵蚀、冻融侵蚀和工程侵蚀的面积占评价区域面积的比重，用于反映评价区域内土地退化程度"/></a>：<span id="plctdth"></span></li>
     </ul>    
    </div>
    <div id="areaData" class="message">
     <h2>所选地区</h2>
     <ul>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_1.gif" />植被覆盖指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="指评价区域内林地、草地、 耕地、 建设用地和未利用地五种类型的面积占评价区域面积的比重，用于反映评价区域植被覆盖的程度"/></a>：<span id="areazbfg"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_2.gif" />生物丰度指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="指通过单位面积上不同生态系统类型在生物物种数量上的差异，间接地反映评价区域内生物丰度的丰贫程度"/></a>：<span id="areaswfd"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_3.gif" />地质环境指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="表征评价区域内地表塌陷、地裂缝、地下水水位下降等地质破坏状况，用于反映评价区域地质破坏程度"/></a>：<span id="areadzhj"></span></li>
      <li><img src="<%=request.getContextPath()%>/static/img/message_icon_4.gif" />土地退化指数<a href="#"><img src="<%=request.getContextPath()%>/static/img/icon_help.gif" alt="帮助" title="指评价区域内风蚀、水蚀、重力侵蚀、冻融侵蚀和工程侵蚀的面积占评价区域面积的比重，用于反映评价区域内土地退化程度"/></a>：<span id="areatdth"></span></li>
     </ul>    
    </div>
   </div>
   </div>
   
   <!-- 打印图片框 -->
<div id="print-image-dialog" style="display: none" align="center">
		<form name="printForm" id="printForm" action="" method="post">
			<table>
				<tr>
					<td> 请输入标题：</td>
					<td>
					<input type="text" name="comment" id="comment" size="30" />
					</td>
				</tr>
				<tr>
				<td colspan="2" align="center"><img id="printImage" alt="加载中。。。" src="" height="423" width="328"></td>
				</tr>
			</table>
					<input type="hidden" name="mapPath"  value="" />
		</form>
	</div>