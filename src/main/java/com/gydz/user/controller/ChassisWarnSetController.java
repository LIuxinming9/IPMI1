package com.gydz.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gydz.user.mapper.MethodLog;
import com.gydz.user.model.QueryParam;
import com.gydz.user.model.sensorrecord;
import com.gydz.user.model.serversip;
import com.gydz.user.service.chassiswarnsetImpl;
import com.gydz.user.service.englishImpl;
import com.gydz.user.service.sensorImpl;
import com.gydz.util.ExcelUtil;
import com.gydz.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/chassiswarnset")
public class ChassisWarnSetController {

	@Resource
	private chassiswarnsetImpl chassiswarnsetImpl;

	@Resource
	private sensorImpl sensorImpl;
	
	static String IP = "";
	
	@Resource
	private englishImpl englishImpl;
	
	@RequestMapping("/chassiswarnset")
	@MethodLog(remark = "进入机箱告警列表页面",openType = "3")
	public String sensorwarnrefer(HttpServletRequest request) {
		HashMap<String, String> map = englishImpl.getmap();
		List<serversip> serversip = null;
		serversip = sensorImpl.getServersip();
		if(!IP.equals("")) {
			List<sensorrecord> setlist = null;
			QueryParam param = new QueryParam();
			param.setIPType(IP);
			setlist = chassiswarnsetImpl.getIPname(param);
			request.setAttribute("setlist", setlist);
		}
		Map<String,String> sysnamemap = sensorImpl.getSysname();
		request.setAttribute("map", map);
		request.setAttribute("sysnamemap", sysnamemap);
		request.setAttribute("serversip", serversip);
		request.setAttribute("menu", "chassiswarnset");
		return "/WEB-INF/ipmiwarn/chassiswarnset";
	}
	
	@RequestMapping("/changeInfo")
	@ResponseBody
	public void changeInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
	JSONArray jsonArray=new JSONArray();
	JSONObject jsonObject=new JSONObject();
	String infoType = request.getParameter("infoType");
	List<serversip> serversip = null;
	serversip = sensorImpl.getServersip();
	List<String> list = new ArrayList<String>();
	List<String> valueIdList = new ArrayList<String>();
	if(infoType.equals("name")) {
		for (int i = 0; i < serversip.size(); i++) {
			list.add(serversip.get(i).getName());
			valueIdList.add(serversip.get(i).getHostname());
		}
	}else if(infoType.equals("brand")){
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < serversip.size(); i++) {
			set.add(serversip.get(i).getBrand());
		}
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String next = it.next();
			list.add(next);
			valueIdList.add("brand="+next);
		}
	}else if(infoType.equals("hostname")) {
		for (int i = 0; i < serversip.size(); i++) {
			list.add(serversip.get(i).getHostname());
			valueIdList.add(serversip.get(i).getHostname());
		}
	}
	for(int i=0;i<list.size();i++)
	{
		jsonObject.put("id", i);
		jsonObject.put("value", list.get(i));
		jsonObject.put("valueId", valueIdList.get(i));
		jsonArray.add(jsonObject);
		
	}
	ResponseUtil.write(response, jsonArray);
	}
	
	@RequestMapping("/query")
	@MethodLog(remark = "查询机箱告警列表",openType = "3")
	public String query(HttpServletRequest request,String queryType,String nameType,String IPType) {
		HashMap<String, String> map = englishImpl.getmap();
		List<sensorrecord> setlist = new ArrayList<sensorrecord>();
		List<sensorrecord> alist = null;
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
				QueryParam param = new QueryParam();
				param.setQueryType(queryType);
				param.setIPType(ipList.get(i));
				param.setNameType(nameType);
				alist = chassiswarnsetImpl.getIPname(param);
				for (int j = 0; j < alist.size(); j++) {
					setlist.add(alist.get(j));
				}
			}
			
		}else {
			QueryParam param = new QueryParam();
			param.setQueryType(queryType);
			param.setIPType(IPType);
			param.setNameType(nameType);
			alist = chassiswarnsetImpl.getIPname(param);
			for (int i = 0; i < alist.size(); i++) {
				setlist.add(alist.get(i));
			}
		}
		
		Map<String,String> sysnamemap = sensorImpl.getSysname();
		request.setAttribute("map", map);
		request.setAttribute("sysnamemap", sysnamemap);
		request.setAttribute("setlist", setlist);
		request.setAttribute("menu", "chassiswarnset");
		return "/WEB-INF/ipmiwarn/chassiswarnsetDiv";
	}
	
	@RequestMapping("/exprotList")
    @MethodLog(remark = "导出机箱告警列表",openType = "3")
	public void exportExcel(HttpServletRequest request,
    		String datemin, String datemax, String queryType,String IPType,String nameType,
    		HttpServletResponse response){
		HashMap<String, String> stamap = englishImpl.getmap();
    	//int width = 256*14;
		String[] excelHeader = {"服务器名字", "服务器ip地址", "告警部件名字"};
		String[] ds_titles = {"sysname", "IP","name"};
		int[] widths = {256*25,256*25,256*25};
		int[] ds_format = {2,2,2};
		try{
			
			List<sensorrecord> setlist = new ArrayList<sensorrecord>();
			List<sensorrecord> alist = null;
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
					QueryParam param = new QueryParam();
					param.setQueryType(queryType);
					param.setIPType(ipList.get(i));
					param.setNameType(nameType);
					alist = chassiswarnsetImpl.getIPname(param);
					for (int j = 0; j < alist.size(); j++) {
						setlist.add(alist.get(j));
					}
				}
				
			}else {
				QueryParam param = new QueryParam();
				param.setQueryType(queryType);
				param.setIPType(IPType);
				param.setNameType(nameType);
				alist = chassiswarnsetImpl.getIPname(param);
				for (int i = 0; i < alist.size(); i++) {
					setlist.add(alist.get(i));
				}
			}
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			Map<String,String> sysnamemap = sensorImpl.getSysname();
			if(setlist != null && setlist.size() > 0){
				for(sensorrecord rdata : setlist){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("sysname", sysnamemap.get(rdata.getIP()));
					map.put("IP", rdata.getIP());
					map.put("name", stamap.get(rdata.getName()));
					data.add(map);
				}
			}
			ExcelUtil.export("机箱告警列表", "数据列表", excelHeader, ds_titles, ds_format, widths, data, request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	//删除用户
    @RequestMapping(value = "/del")
    @MethodLog(remark = "删除一项机箱告警",openType = "1")
    public String delSysIP(HttpServletRequest request){
        try {
        	String IPType = request.getParameterValues("IP")[0];
        	String name = request.getParameterValues("name")[0];
        	IP = IPType;
        	QueryParam param = new QueryParam();
    		param.setIPType(IPType);
    		param.setNameType(name);
        	chassiswarnsetImpl.del(param);
        }catch (Exception ex){
        	System.out.println("____________删除失败——————————————————————");
        }
        request.setAttribute("menu", "chassiswarnset");
        return "redirect:/chassiswarnset/chassiswarnset";
    }
    
    // 保存服务器信息页面
 	@RequestMapping(value = "/save", method = RequestMethod.POST)
 	@MethodLog(remark = "修改机箱告警信息",openType = "2")
 	public String saveSysIP(HttpServletRequest request, Model model) {

 		List<sensorrecord> list = new ArrayList<sensorrecord>();
 		String[] IPs = request.getParameterValues("IP");
 		IP = IPs[0];
 		String[] names = request.getParameterValues("name");
 		for (int i = 0; i < IPs.length; i++) {
 			sensorrecord sensorrecord = new sensorrecord();
 			sensorrecord.setIP(IPs[i]);
 			sensorrecord.setName(names[i]);
 			sensorrecord.setIPname(IPs[i]+names[i]);
 			list.add(sensorrecord);
 		}
 		for (int i = 0; i < IPs.length; i++) {
 			chassiswarnsetImpl.update(list.get(i));
 		}
 		request.setAttribute("menu", "chassiswarnset");
 		return "redirect:/chassiswarnset/chassiswarnset";
 	}
}	
