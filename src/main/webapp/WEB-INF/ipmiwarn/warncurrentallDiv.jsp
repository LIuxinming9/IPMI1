<%@ page import="java.sql.*" language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<div id="resultDiv" class="mt-20" style="margin-top:-20px;">
		<div class="cl pd-5 bg-1 bk-gray"> 
         		<ul class="l pull-right">
         				<li style="color:black;background-color: OrangeRed;border: 5px solid OrangeRed;list-style-type:circle;">
          				<a href='${pageContext.request.contextPath}/warncurrent/warncurrentexigence' >
        				<span>紧急告警：${intList.get(3)}</span></a></li></ul>
          		<ul class="l pull-right">
          			<li style="color:black;background-color: Lime;border: 5px solid Lime;list-style-type:circle;">
          				<a href='${pageContext.request.contextPath}/warncurrent/warncurrentserious' >
        				<span>严重告警：${intList.get(2)}</span></a></li></ul>
         			<ul class="l pull-right">
         				<li style="color:black;background-color: DeepSkyBlue;border: 5px solid DeepSkyBlue;list-style-type:circle;">
          				<a href='${pageContext.request.contextPath}/warncurrent/warncurrentslight' >
	        			<span>轻微告警：${intList.get(1)}</span></a></li></ul>
         			<ul class="l pull-right">
         				<li style="color:black;background-color: LightGray ;border: 5px solid LightGray ;list-style-type:disc;">
          				<a href='${pageContext.request.contextPath}/warncurrent/warncurrentall' >
	        			<span>全部告警：${intList.get(0)}</span></a></li></ul>
	        		<ul class="l pull-right">
           				<li style="color:black;background-color:Cyan ;border: 5px solid Cyan ;list-style-type:circle;">
            				<a href='${pageContext.request.contextPath}/warncurrent/refresh' >
			        			<span>刷新</span></a></li></ul>
      		</div>
	<table class="table table-border table-bordered table-hover table-bg table-sort" style="table-layout:fixed;">
		<thead>
			<tr class="text-c">
			   <th width="50px">序号</th>
                  <th width="200px">时间</th>
                  <th width="100px">服务器名字</th>
                  <th width="100px">服务器ip地址</th>
                  <th width="100px">告警信息类别</th>
                  <th width="200px">告警部件名字</th>
                  <th width="200px">告警内容</th>
                  <th width="100px">告警级别</th>
               </tr>
           </thead>
           <tbody>
		<c:forEach items="${requestList}" var="item" varStatus="status">
        		<tr class="text-c">
        			<td class="center">${status.index+1}</td>
                   <td class="center"><fmt:formatDate value="${item.start_time}" pattern="yyyy年MM月dd日  HH:mm:ss" /></td>
                   <td class="center">${sysNameMap.get(item.IP)}</td>
                   <td class="center">${item.IP}</td>
                   <td class="center">${map.get(item.sensor_type)}</td>
                   <td class="center">${map.get(item.name)}</td>
                   <td class="center">${item.warncontent}</td>
                   <td class="center">${item.warnlevel}</td>
                   <c:choose>
			    <c:when test="${item.warnlevel=='告警'}">
			    	<audio autoplay>
					  <source src="<%=basePath%>/js/警告1.m4a" type="audio/mpeg">
					</audio> 
			    </c:when>
			     <c:when test="${item.warnlevel=='严重告警'}">
			    	<audio autoplay>
					  <source src="<%=basePath%>/js/严重警告1.m4a" type="audio/mpeg">
					</audio>
			    </c:when>
			     <c:when test="${item.warnlevel=='故障告警'}">
			    	<audio autoplay>
					  <source src="<%=basePath%>/js/故障警告1.m4a" type="audio/mpeg">
					</audio>
			    </c:when>
			</c:choose>
               </tr>
           </c:forEach>
       </tbody>
   </table>
</div>					
<script type="text/javascript">

$('.table-sort').dataTable({
	"aaSorting": [[ 0, "asc" ]],//默认第几个排序
	"pagingType": "full_numbers",
	"bStateSave": false,//状态保存
	"pading":false,
	"aoColumnDefs": [
		  //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
		  {"orderable":false,"aTargets":[0]}// 制定列不参与排序
	],
	"bLengthChange": true,
	"bFilter": false,
	"bPaginate": true,
	"bInfo":true,
	"aLengthMenu": [[10, 25, 50, -1], ["10条", "25条", "50条", "全部"]],
	"oLanguage": {
		"sLengthMenu": "每页显示 _MENU_ 记录",
		"sZeroRecords": "没有找到符合条件的数据",
        "sProcessing": "加载中，请稍候...",
        "sInfo": "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
        "sInfoEmpty": "没有记录",
        "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "前一页",
            "sNext": "后一页",
            "sLast": "尾页"
        }
    },
    "bSort": true,
    "bAutoWidth": true,
    "sScrollX": "90%",
    "sScrollXInner": "100%",
    "bScrollCollapse": true
});
function showAll() 
{
	$.ajax({
        type:"POST",
        url:"${pageContext.request.contextPath}/warncurrent/warncurrentallDiv",
        data:{},
        success : function(msg) {
        	$('#resultDiv').html(msg);
        },
        error: function (data) {
            console.info("error: " + data.responseText);
        }
    });
}


setTimeout('showAll()',3000);
</script>
</body>
</html>