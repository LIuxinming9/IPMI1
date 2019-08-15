package com.gydz.user.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gydz.user.mapper.MethodLog;
import com.gydz.user.model.QueryParam;
import com.gydz.user.model.resource_name;
import com.gydz.user.model.sensorrecord;
import com.gydz.user.model.serversip;
import com.gydz.user.service.englishImpl;
import com.gydz.user.service.ipmiwarnImpl;
import com.gydz.user.service.resourcewarnsetImpl;
import com.gydz.user.service.sensorImpl;
import com.gydz.util.ExcelUtil;
import com.gydz.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/resourcewarnrefer")
public class ResourceWarnReferController {

	@Resource
	private resourcewarnsetImpl resourcewarnsetImpl;
	
	@Resource
	private ipmiwarnImpl ipmiwarnImpl;

	@Resource
	private sensorImpl sensorImpl;
	
	@Resource
	private englishImpl englishImpl;
	
	static String IP = "";

	public List<String> getIPname(){
		List<sensorrecord> IPnamelist = null;
    	QueryParam param = new QueryParam();
    	IPnamelist = resourcewarnsetImpl.getresourcewarnsetbyparam(param);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < IPnamelist.size(); i++) {
			list.add(IPnamelist.get(i).getIPname());
		}
		return list;
	}
	
	@RequestMapping("/resourcewarnrefer")
	@MethodLog(remark = "进入资源监控列表页面",openType = "3")
	public String sensorwarnrefer(HttpServletRequest request) {
		HashMap<String, String> map = englishImpl.getmap();
		List<resource_name> resource_name = new ArrayList<resource_name>();
		List<serversip> serversip = new ArrayList<serversip>();
		serversip = sensorImpl.getServersip();
		if(!IP.equals("")) {
			List<sensorrecord> list = new ArrayList<sensorrecord>();
	    	QueryParam param = new QueryParam();
	    	param.setIPType(IP);
	    	list = sensorImpl.getcurrentInfobyParam(param);
	    	resource_name = dealres(list,"全部");
			List<String> IPnames = 	getIPname();
			request.setAttribute("IPnames", IPnames);
			request.setAttribute("resource_name", resource_name);
		}
		Map<String,String> sysnamemap = sensorImpl.getSysname();
		request.setAttribute("resource_name", resource_name);
		request.setAttribute("sysnamemap", sysnamemap);
		request.setAttribute("map", map);
		request.setAttribute("serversip", serversip);
		request.setAttribute("menu", "resourcewarnrefer");
		return "/WEB-INF/ipmiwarn/resourcewarnrefer";
	}
	
	@RequestMapping("/changename")
	@ResponseBody
	public void changename(HttpServletRequest request,HttpServletResponse response) throws Exception{
  	HashMap<String, String> map = englishImpl.getmap();
	JSONArray jsonArray=new JSONArray();
	JSONObject jsonObject=new JSONObject();
	String IPType = request.getParameter("IPType");
	
	List<sensorrecord> sensorwarnreferlist = getValue(IPType,null);
	
	Set<String> set = new LinkedHashSet<String>();
	for (int i = 0; i < sensorwarnreferlist.size(); i++) {
		set.add(sensorwarnreferlist.get(i).getEntity_id());
	}
	List<String> list = new ArrayList<String>();
	Iterator<String> it = set.iterator();
	while(it.hasNext()) {
		list.add(it.next());
	}
	for(int i=0;i<list.size();i++)
	{
		jsonObject.put("id", i);
		jsonObject.put("name", list.get(i));
		jsonObject.put("mapname", map.get(list.get(i)));
		jsonArray.add(jsonObject);
		
	}
	ResponseUtil.write(response, jsonArray);
	}
	
	public List<sensorrecord> getValue(String IPType,String nameType){
		List<sensorrecord> list = new ArrayList<sensorrecord>();
		List<sensorrecord> alist = new ArrayList<sensorrecord>();
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
				param.setIPType(ipList.get(i));
				param.setEntity_id(nameType);
				alist =  sensorImpl.getcurrentInfobyParam(param);
				for (int j = 0; j < alist.size(); j++) {
					list.add(alist.get(j));
				}
			}
			
		}else {
			QueryParam param = new QueryParam();
			param.setIPType(IPType);
			param.setEntity_id(nameType);
			alist =  sensorImpl.getcurrentInfobyParam(param);
			for (int i = 0; i < alist.size(); i++) {
				list.add(alist.get(i));
			}
		}
		return list;
	}
	
	@RequestMapping("/query")
	@MethodLog(remark = "查询资源监控列表",openType = "3")
	public String query(HttpServletRequest request,String nameType,String IPType) {
		HashMap<String, String> map = englishImpl.getmap();
		List<resource_name> resource_name = new ArrayList<resource_name>();
		List<sensorrecord> list = getValue(IPType,nameType);
    	resource_name = dealres(list,nameType);
		Map<String,String> sysnamemap = sensorImpl.getSysname();
		List<String> IPnames = 	getIPname();
		request.setAttribute("map", map);
		request.setAttribute("IPnames", IPnames);
		request.setAttribute("sysnamemap", sysnamemap);
		request.setAttribute("resource_name", resource_name);
		request.setAttribute("menu", "resourcewarnrefer");
		return "/WEB-INF/ipmiwarn/resourcewarnreferDiv";
	}
	
	/**
     * 导出资源监控列表
     */
	@RequestMapping("/exprotList")
    @MethodLog(remark = "导出资源监控列表",openType = "3")
	public void exportExcel(HttpServletRequest request,
    		String datemin, String datemax, String queryType,String IPType,String nameType,
    		HttpServletResponse response){
		HashMap<String, String> stamap = englishImpl.getmap();
    	//int width = 256*14;
		String[] excelHeader = { "时间", "服务器用途", "服务器ip地址", "告警部件名字", "加入告警"};
		String[] ds_titles = { "start_time", "sysname", "IP", "name","insert"};
		int[] widths = {256*25, 256*25, 256*25,256*25,256*25};
		int[] ds_format = { 1,2,2,2,2};
		try{
			List<String> IPnames = 	getIPname();
			List<resource_name> resource_name = null;
			List<sensorrecord> list = getValue(IPType,nameType);
	    	resource_name = dealres(list,nameType);
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			Map<String,String> sysnamemap = sensorImpl.getSysname();
			if(resource_name != null && resource_name.size() > 0){
				for(resource_name rdata : resource_name){
					Map<String,Object> map = new HashMap<String,Object>();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					map.put("start_time", format.format(rdata.getStart_time()));
					map.put("sysname", sysnamemap.get(rdata.getIP()));
					map.put("IP", rdata.getIP());
					map.put("name", stamap.get(rdata.getEntity_id()));
					if(IPnames.contains(rdata.getEntity_id()+rdata.getIP())) {
						map.put("insert", "已添加");
					}else {
						map.put("insert", "未添加");
					}
					data.add(map);
				}
			}
			ExcelUtil.export("资源监控列表", "数据列表", excelHeader, ds_titles, ds_format, widths, data, request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	//资源加入告警
	@RequestMapping("/add")
	@MethodLog(remark = "资源加入告警",openType = "0")
    public String add(HttpServletRequest request){
		String IPType = request.getParameterValues("IP")[0];
    	String nameType = request.getParameterValues("entity_id")[0];
		List<sensorrecord> addlist = new ArrayList<sensorrecord>();
		List<sensorrecord> alist = new ArrayList<sensorrecord>();
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
				IP = ipList.get(i);
				QueryParam param = new QueryParam();
				param.setIPType(ipList.get(i));
				param.setEntity_id(nameType);
				alist = sensorImpl.getcurrentInfobyParam(param);
				for (int j = 0; j < alist.size(); j++) {
					addlist.add(alist.get(j));
				}
				String emptyname = dealemptyname(addlist);
				addlist.get(0).setEmptyresource(emptyname);
				addlist.get(0).setIPname(nameType+ipList.get(i));
				resourcewarnsetImpl.add(addlist.get(0));
			}
			
		}else {
			QueryParam param = new QueryParam();
			param.setIPType(IPType);
			param.setEntity_id(nameType);
			alist = sensorImpl.getcurrentInfobyParam(param);
			for (int i = 0; i < alist.size(); i++) {
				addlist.add(alist.get(i));
			}
			String emptyname = dealemptyname(addlist);
			addlist.get(0).setEmptyresource(emptyname);
			addlist.get(0).setIPname(nameType+IPType);
			resourcewarnsetImpl.add(addlist.get(0));
			IP = IPType;
		}
		
		
		return "redirect:/resourcewarnrefer/resourcewarnrefer";
    }
	
	 public String dealemptyname(List<sensorrecord> list){
	    	String emptyname = "";
	    	for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getStatesAsserted() != null && list.get(i).getStatesAsserted().isEmpty()) {
					emptyname += list.get(i).getName();
				}
			}
			return emptyname;
	    }
	
	public List<resource_name> dealres(List<sensorrecord> list,String nameType){
		
    	Set<String> entityidset = new LinkedHashSet<String>();
  		for (int i = 0; i < list.size(); i++) {
  			entityidset.add(list.get(i).getEntity_id()+list.get(i).getIP());
		}
  		List<resource_name> resourcebyname = new ArrayList<resource_name>();
  		Iterator<String> Iterator = entityidset.iterator();
  		
		if(nameType==null||nameType==""||nameType.equals("全部")) {
			while(Iterator.hasNext()) {
				int cardnum = 0;
				int hardnum = 0;
				String next = Iterator.next();
				for (int j = 0; j < list.size(); j++) {
					if(next.equals(list.get(j).getEntity_id()+list.get(j).getIP())) {
						cardnum++;
						if(list.get(j).getStatesAsserted()==null) {
							hardnum++;
						}else if(!list.get(j).getStatesAsserted().isEmpty()) {
							hardnum++;
						}
					}
				}
				resource_name resource_name = new resource_name();
				resource_name.setStart_time(list.get(0).getStart_time());
				resource_name.setIP(next.substring(next.indexOf('1')));
				resource_name.setEntity_id(next.substring(0, next.indexOf('1')));
				resource_name.setCardnum(cardnum);
				resource_name.setHardnum(hardnum);
				resource_name.setEmptynum(cardnum - hardnum);
				resourcebyname.add(resource_name);
	  		}
		}else {
			while(Iterator.hasNext()) {
				int cardnum = 0;
				int hardnum = 0;
				String next = Iterator.next();
				if(next.equals(nameType+list.get(0).getIP())){
					for (int j = 0; j < list.size(); j++) {
						if(next.equals(list.get(j).getEntity_id()+list.get(j).getIP())) {
							cardnum++;
							if(list.get(j).getStatesAsserted()==null) {
								hardnum++;
							}else if(!list.get(j).getStatesAsserted().isEmpty()) {
								hardnum++;
							}
						}
					}
					resource_name resource_name = new resource_name();
					resource_name.setStart_time(list.get(0).getStart_time());
					resource_name.setIP(list.get(0).getIP());
					resource_name.setEntity_id(next.substring(0,next.indexOf('1')));
					resource_name.setCardnum(cardnum);
					resource_name.setHardnum(hardnum);
					resource_name.setEmptynum(cardnum - hardnum);
					resourcebyname.add(resource_name);
				}
				
	  		}
		}
		return resourcebyname;
    }
}	
