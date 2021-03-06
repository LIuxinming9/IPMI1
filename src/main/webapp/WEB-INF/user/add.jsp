<%@ page import="java.sql.*" language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
    <title>服务器通用带外网管软件系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport' />   
    <jsp:include page="/template/link.jsp" />
    
</head>
<body class='contrast-blue '>
<header>
    <jsp:include page="/template/header.jsp" />
</header>
<div id='wrapper'>
	<jsp:include page="/template/left.jsp" />
	<script type="text/javascript">
		function clearShowResult()
		{
			$("#showResult").html("");
		}
		function checkIsExist() 
		{
		    var account = $.trim($("#account").val());
		    if(account.length > 0){
		    	$.ajax({
			        type:"POST",   
			        url:"${pageContext.request.contextPath}/user/isExist", 
			        data:"account="+account, 
			        success : function(msg) {
			        	if(msg=="success"){
			    		 	$("#showResult").html("<img src='../images/validform/iconpic-right.png'><font color='green'>登录账号可用</font>");
			    		 	$('#saveUser').attr("disabled",false);
						} else {
							$("#showResult").html("<img src='../images/validform/iconpic-error.png'><font color='red'>登录账号已存在</font>");
							$('#saveUser').attr("disabled",true);
						} 
			       } 
			    });
		    }
		    
		}
	</script>
	<section id='content'>
	    <div class='container-fluid'>
	        <div class='row-fluid' id='content-wrapper'>
	            <div class='span12'>
	            	<nav class="breadcrumb">
						<i class="Hui-iconfont">&#xe67f;</i> 系统管理 <span class="c-gray en">&gt;</span> 用户管理 
					</nav>
	            	<div class='row-fluid' style="margin-top:20px;">
	                    <div class='span12 box'>
	                        <div class='box-header blue-background'>
	                            <div class='title'>创建用户</div>
	                            <div class='actions'>
	                                
	                            </div>
	                        </div>
	                        <shiro:hasRole name="admin">
	                        <div class='box-content'>
	                            <form class='form form-horizontal validate-form' style='margin-bottom: 0;' 
	                            	action="${pageContext.request.contextPath}/user/save" method="post" modelAttribute="user" >
	                                <div class='control-group'>
	                                    <label class='control-label' for='username'>用户名</label>
	                                    <div class='controls'>
	                                        <input data-rule-minlength='2' data-rule-required='true' style="height:30px;"
	                                        	id='username' name='username' placeholder='请输入用户名...' type='text' />
	                                    </div>
	                                </div>
	                                <div class='control-group'>
	                                    <label class='control-label' for='account'>登录账号</label>
	                                    <div class='controls'>
	                                        <input data-rule-minlength='2' data-rule-required='true' onblur="checkIsExist();" onfocus="clearShowResult();" 
	                                        	id='account' name='account' placeholder='请输入admin,user或monitor...' type='text' /><span id="showResult"></span>
	                                    </div>
	                                </div>
	                                <div class='control-group'>
	                                    <label class='control-label' for='password'>密码</label>
	                                    <div class='controls'>
	                                        <input data-rule-minlength='6' data-rule-password='true' data-rule-required='true' 
	                                        	id='password' name='password' placeholder='请输入密码...' type='password' />
	                                    </div>
	                                </div>
	                                <div class='control-group'>
	                                    <label class='control-label' for='password_confirmation'>确认密码</label>
	                                    <div class='controls'>
	                                        <input data-rule-equalto='#password' data-rule-required='true' 
	                                        	id='password_confirmation' name='password_confirmation' placeholder='请再次输入密码...' type='password' />
	                                    </div>
	                                </div>
	                                <div class='form-actions' style='margin-bottom:0'>
	                                    <button id="saveUser" class='btn btn-primary' type='submit'>
	                                        <i class='icon-save'></i> 保存
	                                    </button>
	                                    <span style="padding-left:20px;"></span>
	                                    <a href="${pageContext.request.contextPath}/user/list" class="btn btn-warning"><i class='icon-reply'></i> 取消</a>
	                                </div>
	                            </form>
	                        </div>
	                        </shiro:hasRole>
	                    </div>
                	</div>
	            </div>
	        </div>
	    </div>
	</section>
</div>

</body>
</html>
