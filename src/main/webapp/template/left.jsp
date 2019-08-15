<%@ page import="java.sql.*" language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
	<nav class='' id='main-nav'>
		<div class='navigation'>
			<ul class='nav nav-stacked'>
				<li class="${(menu=='sensorwarnset' or menu=='sensorwarnrefer'    or menu=='resourcewarnset'
				or menu=='chassiswarnset' or menu=='chassiswarnrefer'  or menu=='resourcewarnrefer'
				or menu=='warncurrentall' or menu=='warnhistory')?'active':''}">
					<a class='dropdown-collapse' href='#'> <i class='icon-link'></i>
						<span>告警管理</span> <i class='icon-angle-down angle-down'></i>
					</a>
					<ul class="nav nav-stacked ${(menu=='sensorwarnset' or menu=='sensorwarnrefer'   or menu=='resourcewarnset'
					or menu=='chassiswarnset' or menu=='chassiswarnrefer'  or menu=='resourcewarnrefer'
					or menu=='warncurrentall' or menu=='warnhistory')?'in':''}">
					<li class="${(menu=='warncurrentall')?'active':''}">
			    		<a href='${pageContext.request.contextPath}/warncurrent/warncurrentall' >
			        	<i class='icon-map-marker'></i><span>实时告警</span>
			    		</a>
					</li>
					<li class="${(menu=='warnhistory')?'active':''}">
			    		<a href='${pageContext.request.contextPath}/warnhistory/warnhistory' >
			        	<i class='icon-map-marker'></i><span>历史告警</span>
			    		</a>
					</li>
					<shiro:hasPermission name="/delete">
					<li class="${(menu=='sensorwarnset' or menu=='sensorwarnrefer' 
					or menu=='chassiswarnset' or menu=='chassiswarnrefer'  or menu=='resourcewarnset')?'in':''}">
					     <a class='dropdown-collapse ' href='#'>
					        <i class='icon-map-marker'></i>
					        <span>告警列表</span><i class='icon-angle-down angle-down'></i>
					    </a>	 
						<ul class="nav nav-stacked ${(menu=='sensorwarnset' or menu=='chassiswarnset'
						  or menu=='resourcewarnset')?'in':''}">
							<li class="${(menu=='sensorwarnset')?'active':''}">
					    		<a href='${pageContext.request.contextPath}/sensorwarnset/sensorwarnset' >
					        	<i class='icon-caret-right'></i><span>传感器告警列表</span>
					    		</a>
							</li>
							<li class="${(menu=='chassiswarnset')?'active':''}">
					    		<a href='${pageContext.request.contextPath}/chassiswarnset/chassiswarnset' >
					        	<i class='icon-caret-right'></i><span>机箱告警列表</span>
					    		</a>
							</li>
							<li class="${(menu=='resourcewarnset')?'active':''}">
					    		<a href='${pageContext.request.contextPath}/resourcewarnset/resourcewarnset' >
					        	<i class='icon-caret-right'></i><span>资源告警列表</span>
					    		</a>
							</li>	 
						</ul>
					</li>
					<li class="${(menu=='chassiswarnrefer' or menu=='sensorwarnrefer'
					 or menu=='resourcewarnrefer')?'in':''}">
					     <a class='dropdown-collapse ' href='#'>
					        <i class='icon-map-marker'></i>
					        <span>监控列表</span><i class='icon-angle-down angle-down'></i>
					    </a>
						<ul class="nav nav-stacked ${(menu=='chassiswarnrefer' or menu=='sensorwarnrefer'
						 or menu=='resourcewarnrefer')?'in':''}">
							<li class="${(menu=='sensorwarnrefer')?'active':''}">
					    		<a href='${pageContext.request.contextPath}/sensorwarnrefer/sensorwarnrefer' >
					        	<i class='icon-caret-right'></i><span>传感器监控列表</span>
					    		</a>
							</li>
							<li class="${(menu=='chassiswarnrefer')?'active':''}">
					    		<a href='${pageContext.request.contextPath}/chassiswarnrefer/chassiswarnrefer' >
					        	<i class='icon-caret-right'></i><span>机箱监控列表</span>
					    		</a>
							</li>
							<li class="${(menu=='resourcewarnrefer')?'active':''}">
					    		<a href='${pageContext.request.contextPath}/resourcewarnrefer/resourcewarnrefer' >
					        	<i class='icon-caret-right'></i><span>资源监控列表</span>
					    		</a>
							</li>	 
						</ul>
					</li>
					</shiro:hasPermission>
					</ul>	 
				</li>
				
				<li class="${((menu=='sensorteinfo' or menu=='sensorfainfo' or menu=='sensorvoinfo')
				or (menu=='chassisinfo') or (menu=='fruchassisinfo' or menu=='fruboardinfo' or menu=='fruproductinfo')
				or (menu=='selinfo') or menu=='resourceinfo' or (menu=='snmp')
				 or menu=='snmp' or menu=='snmpMem' or menu=='snmpCpu')?'active':''}">
					<a class='dropdown-collapse' href='#'> <i class='icon-link'></i>
						<span>信息统计</span> <i class='icon-angle-down angle-down'></i>
					</a>
				<ul class="nav nav-stacked ${((menu=='sensorteinfo' or menu=='sensorfainfo' or menu=='sensorvoinfo')
				or (menu=='chassisinfo') or (menu=='fruchassisinfo' or menu=='fruboardinfo' or menu=='fruproductinfo')
				or (menu=='selinfo') or menu=='resourceinfo' or menu=='snmp'
				 or menu=='snmp' or menu=='snmpMem' or menu=='snmpCpu')?'in':''}">
					<li class="${(menu=='sensorteinfo' or menu=='sensorfainfo' or menu=='sensorvoinfo'
					 or menu=='snmp' or menu=='snmpMem' or menu=='snmpCpu')?'in':''}">
					     <a class='dropdown-collapse ' href='#'>
					        <i class='icon-map-marker'></i>
					        <span>传感器记录</span><i class='icon-angle-down angle-down'></i>
					    </a>
					    <ul
						class="nav nav-stacked ${(menu=='sensorteinfo' or menu=='sensorfainfo' or menu=='sensorvoinfo'
						 or menu=='snmp' or menu=='snmpMem' or menu=='snmpCpu')?'in':''}">
						<li class="${(menu=='sensorteinfo')?'active':''}"><a
							href='${pageContext.request.contextPath}/sensor/sensorteinfo' >
								<i class='icon-caret-right'></i> <span>温度</span>
						</a></li>
						<li class="${(menu=='sensorfainfo')?'active':''}"><a
							href='${pageContext.request.contextPath}/sensor/sensorfainfo' >
								<i class='icon-caret-right'></i> <span>风扇</span>
						</a></li>
						<li class="${(menu=='sensorvoinfo')?'active':''}"><a
							href='${pageContext.request.contextPath}/sensor/sensorvoinfo' >
								<i class='icon-caret-right'></i> <span>电压</span>
						</a></li>
						<%-- <li class="${(menu=='snmp')?'active':''}"><a
							href='${pageContext.request.contextPath}/snmp/snmp' >
								<i class='icon-caret-right'></i> <span>硬盘</span>
						</a></li>
						<li class="${(menu=='snmpMem')?'active':''}"><a
							href='${pageContext.request.contextPath}/snmpMem/snmpMem' >
								<i class='icon-caret-right'></i> <span>内存</span>
						</a></li>
						<li class="${(menu=='snmpCpu')?'active':''}"><a
							href='${pageContext.request.contextPath}/snmpCpu/snmpCpu' >
								<i class='icon-caret-right'></i> <span>CPU</span>
						</a></li> --%>
					</ul>
					 </li>
					<li class="${(menu=='fruchassisinfo' or menu=='fruboardinfo' or menu=='fruproductinfo')?'active':''}">
					     <a class='dropdown-collapse ' href='#'>
					        <i class='icon-map-marker'></i>
					        <span>FRU信息</span><i class='icon-angle-down angle-down'></i>
					    </a>
					    <ul
						class="nav nav-stacked ${(menu=='fruchassisinfo' or menu=='fruboardinfo' or menu=='fruproductinfo')?'in':''}">
						<li class="${(menu=='fruchassisinfo')?'active':''}"><a
							href='${pageContext.request.contextPath}/fru/fruchassisinfo' >
								<i class='icon-caret-right'></i> <span>机箱信息</span>
						</a></li>
						<li class="${(menu=='fruboardinfo')?'active':''}"><a
							href='${pageContext.request.contextPath}/fru/fruboardinfo' >
								<i class='icon-caret-right'></i> <span>板信息</span>
						</a></li>
						<li class="${(menu=='fruproductinfo')?'active':''}"><a
							href='${pageContext.request.contextPath}/fru/fruproductinfo' >
								<i class='icon-caret-right'></i> <span>产品信息</span>
						</a></li>
					</ul>
					</li>
					<li class="${(menu=='chassisinfo')?'active':''}">
				    <a href='${pageContext.request.contextPath}/chassis/chassisinfo' >
				        <i class='icon-map-marker'></i>
				        <span>机箱状态</span>
				    </a>
					</li>
					<li class="${(menu=='resourceinfo')?'active':''}">
					    <a href='${pageContext.request.contextPath}/resource/resourceinfo' >
					        <i class='icon-map-marker'></i>
					        <span>资源管理</span>
					    </a>
					</li>
					<li class="${(menu=='selinfo')?'active':''}">
				    <a href='${pageContext.request.contextPath}/sel/selinfo' >
				        <i class='icon-map-marker'></i>
				        <span>服务器日志</span>
				    </a>
					</li>
				</ul>
				</li>
				<shiro:hasPermission name="/delete">
				<li class="${(menu=='power')?'active':''}">
				    <a href='${pageContext.request.contextPath}/power/power' >
				        <i class='icon-link'></i>
				        <span>应急处置</span>
				    </a>
				</li>
				</shiro:hasPermission>
				<li
					class="${(menu=='user' or menu=='updateSysIP' or menu=='chinese' or menu=='log'  
					or menu=='snmpSet' or menu=='setValueByOid' or menu=='snmpMan')?'active':''}">
					<a class='dropdown-collapse ' href='#'> <i class='icon-link'></i>
						<span>系统管理</span> <i class='icon-angle-down angle-down'></i>
					</a>
					<ul
						class="nav nav-stacked ${(menu=='user' or menu=='updateSysIP' or menu=='chinese' or menu=='log'
						  or menu=='snmpSet' or menu=='setValueByOid' or menu=='snmpMan')?'in':''}">
						<shiro:hasRole name="admin">
						<li class="${(menu=='user')?'active':''}"><a
							href='${pageContext.request.contextPath}/user/list'
							> <i class='icon-map-marker'></i> <span>用户管理</span>
						</a></li>
						</shiro:hasRole>
						<shiro:hasPermission name="/delete">
						<%-- <li class="${(menu=='snmpSet')?'active':''}"><a
							href='${pageContext.request.contextPath}/snmpSet/list' >
								<i class='icon-map-marker'></i> <span>OID设置</span>
						</a></li>
						<li class="${(menu=='snmpMan')?'active':''}"><a
							href='${pageContext.request.contextPath}/snmpMan/snmpMan' >
								<i class='icon-map-marker'></i> <span>手动添加</span>
						</a></li> --%>
						<%-- <li class="${(menu=='setValueByOid')?'active':''}"><a
							href='${pageContext.request.contextPath}/setValueByOid/list' >
								<i class='icon-map-marker'></i> <span>SNMP-SET</span>
						</a></li> --%>
						
						<li class="${(menu=='updateSysIP')?'active':''}"><a
							href='${pageContext.request.contextPath}/remotesys/updateSysIP'
							> <i class='icon-map-marker'></i> <span>监控对象</span>
						</a></li>
						</shiro:hasPermission>
					<li class="${(menu=='log')?'active':''}"><a
							href='${pageContext.request.contextPath}/user/log' >
								<i class='icon-map-marker'></i> <span>系统日志</span>
						</a></li>
					<shiro:hasPermission name="/delete">
					<li class="${(menu=='chinese')?'active':''}"><a
						href='${pageContext.request.contextPath}/chinese/chinese' >
							<i class='icon-map-marker'></i> <span>英语翻译</span>
					</a></li>
					</shiro:hasPermission>
					</ul>
				</li>
			</ul>
		</div>
	</nav>