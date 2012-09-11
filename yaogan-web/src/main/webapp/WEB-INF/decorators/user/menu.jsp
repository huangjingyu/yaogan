<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/tag.jsp"%>
<%
String servletPath=request.getServletPath();
//System.out.println("servletPath=" + servletPath);
String[] strArrays=servletPath.split("/");
int len=strArrays.length;
//search menu jsp file location by url sections
String menuPath=servletPath;
java.net.URL fileURL = null;
String menuPage=null;
for(int i=1;i<len-1;i++){
    int rightSlashIndex=menuPath.lastIndexOf("/");
    menuPath=menuPath.substring(0, rightSlashIndex);
    //System.out.println("menuPath=" + menuPath);
    menuPage=menuPath+"/_menu.jsp";
    fileURL = pageContext.getServletContext().getResource(menuPage);
    if(fileURL!=null){
        break;
    }
}
%>
<%if(fileURL!=null){%>
<jsp:include page='<%=menuPage%>'/>
<%}%>