package com.gydz.user.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gydz.user.mapper.MethodLog;
import com.gydz.user.model.QueryParam;
import com.gydz.user.model.chassisList;
import com.gydz.user.model.chassisStatus;
import com.gydz.user.model.sensorrecord;
import com.gydz.user.model.serversip;
import com.gydz.user.service.chassisImpl;
import com.gydz.user.service.chassiswarnsetImpl;
import com.gydz.user.service.englishImpl;
import com.gydz.user.service.ipmiwarnImpl;
import com.gydz.user.service.sensorImpl;
import com.gydz.util.ExcelUtil;


@Controller
@RequestMapping("/chassiswarnrefer")
public class ChassisWarnReferController {

	@Resource
	private chassiswarnsetImpl chassiswarnsetImpl;
	
	@Resource
	private ipmiwarnImpl ipmiwarnImpl;

	@Resource
	private sensorImpl sensorImpl;
	
	@Resource
	private englishImpl englishImpl;
	
	@Resource
	private chassisImpl chassisImpl;
	
	static String IP = "";

	public List<String> getIPname(String IP){
		List<sensorrecord> sensorwarnsetlist = null;
		QueryParam param = new QueryParam();
		param.setIPType(IP);
		sensorwarnsetlist = chassiswarnsetImpl.getIPname(param);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < sensorwarnsetlist.size(); i++) {
			list.add(sensorwarnsetlist.get(i).getIPname());
		}
		return list;
	}
	
	@RequestMapping("/chassiswarnrefer")
	@MethodLog(remark = "进入机箱监控列表页面",openType = "3")
	public String sensorwarnrefer(HttpServletRequest request) {
		HashMap<String, String> map = englishImpl.getmap();
		List<serversip> serversip = null;
		serversip = sensorImpl.getServersip();
		List<chassisList> list = new ArrayList<chassisList>();
		if(!IP.equals("")) {
			list = getinfo("全部",IP);
			List<String> IPnames = 	getIPname(IP);
			request.setAttribute("IPnames", IPnames);
			request.setAttribute("list", list);
		}
		Map<String,String> sysnamemap = sensorImpl.getSysname();
		
		request.setAttribute("list", list);
		request.setAttribute("sysnamemap", sysnamemap);
		request.setAttribute("map", map);
		request.setAttribute("serversip", serversip);
		request.setAttribute("menu", "chassiswarnrefer");
		return "/WEB-INF/ipmiwarn/chassiswarnrefer";
	}
	
	public  List<chassisList> getinfo(String nameType,String IPType){
		QueryParam param = new QueryParam();
		param.setIPType(IPType);
		List<chassisStatus> chassisStatus = null;
		chassisStatus = chassisImpl.getChassiscurrentInfobyParam(param);
		List<chassisList> infolist = new ArrayList<chassisList>();
		infolist = ChassisController.chassisdata(chassisStatus);
		List<chassisList> info = new ArrayList<chassisList>();
		info = ChassisController.chassisquerydata(infolist,nameType);
		return info;
	}
	
	@RequestMapping("/query")
	@MethodLog(remark = "查询机箱监控列表",openType = "3")
	public String query(HttpServletRequest request,String nameType,String IPType) {
		HashMap<String, String> map = englishImpl.getmap();
		List<chassisList> info = new ArrayList<chassisList>();
		List<chassisList> alist = new ArrayList<chassisList>();
		List<String> IPnames = new ArrayList<String>();
		String brand = "";
		if(IPType.contains("=")) {
			brand = IPType.substring(IPType.indexOf("=")+1);
			List<serversip> serversip = null;
			serversip = sensorImpl.getServersip();
			List<String> ipList = new ArrayList<String>();
			for (int i = 0; i < serversip.size(); i++) {
				if(serversip.get(i).getBrand().equals(brand)) {
					ipList.add(serversip.get(i).getHostname());
				}
			}
			for (int i = 0; i < ipList.size(); i++) {
				List<sensorrecord> sensorwarnsetlist = null;
				QueryParam param1 = new QueryParam();
				param1.setIPType(IP);
				sensorwarnsetlist = chassiswarnsetImpl.getIPname(param1);
				for (int k = 0; k < sensorwarnsetlist.size(); k++) {
					IPnames.add(sensorwarnsetlist.get(k).getIPname());
				}
				QueryParam param = new QueryParam();
				param.setIPType(ipList.get(i));
				param.setNameType(nameType);
				List<chassisStatus> chassisStatus = null;
				chassisStatus = chassisImpl.getChassiscurrentInfobyParam(param);
				List<chassisList> infolist = new ArrayList<chassisList>();
				infolist = ChassisController.chassisdata(chassisStatus);
				alist = ChassisController.chassisquerydata(infolist,nameType);
				for (int j = 0; j < alist.size(); j++) {
					info.add(alist.get(j));
				}
			}
			
		}else {
			QueryParam param = new QueryParam();
			param.setIPType(IPType);
			param.setNameType(nameType);
			List<chassisStatus> chassisStatus = null;
			chassisStatus = chassisImpl.getChassiscurrentInfobyParam(param);
			List<chassisList> infolist = new ArrayList<chassisList>();
			infolist = ChassisController.chassisdata(chassisStatus);
			alist = ChassisController.chassisquerydata(infolist,nameType);
			for (int i = 0; i < alist.size(); i++) {
				info.add(alist.get(i));
			}
			IPnames = getIPname(IPType);
		}
		Map<String,String> sysnamemap = sensorImpl.getSysname();
		request.setAttribute("map", map);
		request.setAttribute("IPnames", IPnames);
		request.setAttribute("sysnamemap", sysnamemap);
		request.setAttribute("info", info);
		request.setAttribute("menu", "chassiswarnrefer");
		return "/WEB-INF/ipmiwarn/chassiswarnreferDiv";
	}
	
	/**
     * 导出传感器参考数据
     */
	@RequestMapping("/exprotList")
    @MethodLog(remark = "导出机箱监控列表",openType = "3")
	public void exportExcel(HttpServletRequest request,
    		String datemin, String datemax, String queryType,String IPType,String nameType,
    		HttpServletResponse response){
		HashMap<String, String> stamap = englishImpl.getmap();
    	//int width = 256*14;
		String[] excelHeader = { "时间", "服务器名字", "服务器ip地址", "告警部件名字", "加入告警", "读值"};
		String[] ds_titles = { "start_time", "sysname", "IP", "name","insert","current_num"};
		int[] widths = {256*25, 256*25, 256*25,256*25, 256*25, 156*25};
		int[] ds_format = { 1,2,2,2,2,2};
		try{
			
			List<chassisList> info = new ArrayList<chassisList>();
			List<chassisList> alist = new ArrayList<chassisList>();
			List<String> IPnames = new ArrayList<String>();
			String brand = "";
			if(IPType.contains("=")) {
				brand = IPType.substring(IPType.indexOf("=")+1);
				List<serversip> serversip = null;
				serversip = sensorImpl.getServersip();
				List<String> ipList = new ArrayList<String>();
				for (int i = 0; i < serversip.size(); i++) {
					if(serversip.get(i).getBrand().equals(brand)) {
						ipList.add(serversip.get(i).getHostname());
					}
				}
				for (int i = 0; i < ipList.size(); i++) {
					List<sensorrecord> sensorwarnsetlist = null;
					QueryParam param1 = new QueryParam();
					param1.setIPType(IP);
					sensorwarnsetlist = chassiswarnsetImpl.getIPname(param1);
					for (int k = 0; k < sensorwarnsetlist.size(); k++) {
						IPnames.add(sensorwarnsetlist.get(k).getIPname());
					}
					QueryParam param = new QueryParam();
					param.setIPType(ipList.get(i));
					param.setNameType(nameType);
					List<chassisStatus> chassisStatus = null;
					chassisStatus = chassisImpl.getChassiscurrentInfobyParam(param);
					List<chassisList> infolist = new ArrayList<chassisList>();
					infolist = ChassisController.chassisdata(chassisStatus);
					alist = ChassisController.chassisquerydata(infolist,nameType);
					for (int j = 0; j < alist.size(); j++) {
						info.add(alist.get(j));
					}
				}
				
			}else {
				QueryParam param = new QueryParam();
				param.setIPType(IPType);
				param.setNameType(nameType);
				List<chassisStatus> chassisStatus = null;
				chassisStatus = chassisImpl.getChassiscurrentInfobyParam(param);
				List<chassisList> infolist = new ArrayList<chassisList>();
				infolist = ChassisController.chassisdata(chassisStatus);
				alist = ChassisController.chassisquerydata(infolist,nameType);
				for (int i = 0; i < alist.size(); i++) {
					info.add(alist.get(i));
				}
				IPnames = getIPname(IPType);
			}
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			Map<String,String> sysnamemap = sensorImpl.getSysname();
			if(info != null && info.size() > 0){
				for(chassisList rdata : info){
					Map<String,Object> map = new HashMap<String,Object>();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					map.put("start_time", format.format(rdata.getStart_time()));
					map.put("sysname", sysnamemap.get(rdata.getIP()));
					map.put("IP", rdata.getIP());
					map.put("name", stamap.get(rdata.getName()));
					if(IPnames.contains(rdata.getIP()+rdata.getName())) {
						map.put("insert", "已添加");
					}else {
						map.put("insert", "未添加");
					}
					map.put("current_num", rdata.getState());
					data.add(map);
				}
			}
			ExcelUtil.export("监控列表", "数据列表", excelHeader, ds_titles, ds_format, widths, data, request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//增加服务器
	@RequestMapping("/add")
	@MethodLog(remark = "将机箱监控列表的一条数据加入到实时告警",openType = "0")
    public String add(HttpServletRequest request){
		String IPType = request.getParameterValues("IP")[0];
    	String nameType = request.getParameterValues("name")[0];
    	String brand = "";
		if(IPType.contains("=")) {
			brand = IPType.substring(IPType.indexOf("=")+1);
			List<serversip> serversip = null;
			serversip = sensorImpl.getServersip();
			for (int i = 0; i < serversip.size(); i++) {
				if(serversip.get(i).getBrand().equals(brand)) {
					IP = serversip.get(i).getHostname();
					chassiswarnsetImpl.add(IP,nameType);
				}
			}
			
		}else {
			IP = IPType;
			chassiswarnsetImpl.add(IP,nameType);
		}
		return "redirect:/chassiswarnrefer/chassiswarnrefer";
    }
}	
