package com.gydz.user.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gydz.user.mapper.MethodLog;
import com.gydz.user.model.QueryParam;
import com.gydz.user.model.chassisList;
import com.gydz.user.model.chassisStatus;
import com.gydz.user.model.sensorrecord;
import com.gydz.user.model.snmp;
import com.gydz.user.model.serversip;
import com.gydz.user.service.chassisImpl;
import com.gydz.user.service.chassiswarnsetImpl;
import com.gydz.user.service.englishImpl;
import com.gydz.user.service.ipmiwarnImpl;
import com.gydz.user.service.resourcewarnsetImpl;
import com.gydz.user.service.sensorImpl;
import com.gydz.user.service.sensorwarnsetImpl;
import com.gydz.user.service.warnhistoryImpl;

@Controller
@RequestMapping("/warncurrent")
public class WarnCurrentController {
	
	@Resource
	private resourcewarnsetImpl resourcewarnsetImpl;

	@Resource
	private chassiswarnsetImpl chassiswarnsetImpl;
	
	@Resource
	private sensorwarnsetImpl sensorwarnsetImpl;
	
	@Resource
	private ipmiwarnImpl ipmiwarnImpl;

	@Resource
	private sensorImpl sensorImpl;
	
	@Resource
	private warnhistoryImpl warnhistoryImpl;
	
	@Resource
	private englishImpl englishImpl;
	
	@Resource
	private chassisImpl chassisImpl;
	
	static String IP = "";
	
	static int warnlevel;
	
	public List<String> getwarnnotok(){
		List<sensorrecord> list = warnhistoryImpl.getwarnnotok();
		List<String> strlist = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			strlist.add(list.get(i).getIPnamelevel());
		}
		return strlist;
	}
	
	
	//获取告警信息的数量
	public List<Integer> getLevelSum(){
		List<sensorrecord> list = warnhistoryImpl.getwarnnotok();
		List<Integer> intlist = new ArrayList<Integer>();
		int all = 0;
		int slight = 0;
		int serious = 0;
		int exigence = 0;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getWarnlevel().equals("轻微告警")) {
				slight += 1;
			}else if(list.get(i).getWarnlevel().equals("严重告警")) {
				serious += 1;
			}else if(list.get(i).getWarnlevel().equals("紧急告警")) {
				exigence += 1;
			}
			all = slight + serious + exigence;
		}
		intlist.add(all);
		intlist.add(slight);
		intlist.add(serious);
		intlist.add(exigence);
		return intlist;
	}
	
	@RequestMapping("/refresh")
	@MethodLog(remark = "刷新实时告警信息",openType = "3")
	public String refresh(HttpServletRequest request) {
		List<sensorrecord> warnsetlist = null;
		QueryParam param = new QueryParam();
		warnsetlist = resourcewarnsetImpl.getresourcewarnsetbyparam(param);
		for (int i = 0; i < warnsetlist.size(); i++) {
			String nameip = warnsetlist.get(i).getIPname();
			String emptyresource = warnsetlist.get(i).getEmptyresource();
			List<sensorrecord> addlist = null;
			QueryParam addparam = new QueryParam();
			addparam.setIPType(nameip.substring(nameip.indexOf('1')));
			addparam.setEntity_id(nameip.substring(0, nameip.indexOf('1')));
			addlist = sensorImpl.getcurrentInfobyParam(addparam);
			String emptyname = dealEmptyName(addlist);
			if(!emptyname.equals(emptyresource)) {
				resourcewarnsetImpl.refresh(nameip,emptyname);
			}
		}
		sensorImpl.updateIsOnline();
		return "redirect:/warncurrent/warncurrentall";
	}
	
	@RequestMapping("/jtopo")
	@MethodLog(remark = "查看拓扑图",openType = "3")
	public String jtopo(HttpServletRequest request) {
		Set<String> buildingSet = new LinkedHashSet<String>();
		buildingSet.add("红楼");
		buildingSet.add("黄楼");
		buildingSet.add("绿楼");
		int buildingNum = 3;
		Set<String> houseSet = new LinkedHashSet<String>();
		houseSet.add("701");
		houseSet.add("702");
		houseSet.add("703");
		houseSet.add("704");
		houseSet.add("705");
		houseSet.add("706");
		int houseNum = 6;
		Map<String,Integer> mapHouse = new HashMap<String, Integer>();
		for (int i = 0; i < 3; i++) {
			mapHouse.put("建筑"+i, 2);
		}
		Map<String,Integer> mapCab = new HashMap<String, Integer>();
		for (int i = 0; i < 6; i++) {
			mapCab.put("房间"+i, 25);
		}
		Map<String,Integer> mapCol = new HashMap<String, Integer>();
		int column = 0;
		for (int i = 0; i < houseNum; i++) {
			int cabNum = mapCab.get("房间"+i)/20 + 1;
			mapCol.put("房间"+i, cabNum);
			column += cabNum;
		}
		Map<String,Integer> mapBuildingCol = new HashMap<String, Integer>();
		mapBuildingCol.put("建筑0", 4);
		mapBuildingCol.put("建筑1", 4);
		mapBuildingCol.put("建筑2", 4);
		/*buildingNum为建筑物的数量
		 *column为服务器列的数量
		 *mapCol为房间号与房间中服务器列的键值对
		 *mapCab为房间号与房间中服务器数量的键值对
		 *mapHouse为建筑物号与建筑中房间数量的键值对
		 *mapBuildingCol为建筑物号与房间中服务器列的键值对
		 * */
		request.setAttribute("buildingNum", buildingNum);
		request.setAttribute("column", column);
		request.setAttribute("mapCol", mapCol);
		request.setAttribute("mapCab", mapCab);
		request.setAttribute("mapHouse", mapHouse);
		request.setAttribute("mapBuildingCol", mapBuildingCol);
		return "/WEB-INF/ipmiwarn/jtopo";
	}
	
	@RequestMapping("/warncurrent")
	@MethodLog(remark = "进入实时告警页面",openType = "3")
	public String remotesysinfo(HttpServletRequest request) {
		warnlevel = 1;
		String menu = "warncurrent";
		String warnType = "告警";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrent";
	}
	
	@RequestMapping("/warncurrentall")
	@MethodLog(remark = "进入全部告警页面",openType = "3")
	public String warncurrentall(HttpServletRequest request) {
		warnlevel = 1;
		String menu = "warncurrentall";
		String warnType = "告警";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentall";
	}
	
	@RequestMapping("/warncurrentallDiv")
	public String warncurrentallDiv(HttpServletRequest request) {
		warnlevel = 1;
		String menu = "warncurrentall";
		String warnType = "告警";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentallDiv";
	}
	
	// 告警信息页面
	@RequestMapping("/warncurrentserious")
	@MethodLog(remark = "进入严重告警页面",openType = "3")
	public String warncurrentserious(HttpServletRequest request) {
		warnlevel = 3;
		String menu = "warncurrentall";
		String warnType = "严重";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentserious";
	}
	
	@RequestMapping("/warncurrentseriousDiv")
	public String warncurrentseriousDiv(HttpServletRequest request) {
		warnlevel = 3;
		String menu = "warncurrentall";
		String warnType = "严重";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentseriousDiv";
	}
	
	@RequestMapping("/warncurrentslight")
	@MethodLog(remark = "进入轻微告警页面",openType = "3")
	public String warncurrentslight(HttpServletRequest request) {
		warnlevel = 2;
		String menu = "warncurrentall";
		String warnType = "轻微";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentslight";
	}
	
	@RequestMapping("/warncurrentslightDiv")
	public String warncurrentslightDiv(HttpServletRequest request) {
		warnlevel = 2;
		String menu = "warncurrentall";
		String warnType = "轻微";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentslightDiv";
	}
	
	@RequestMapping("/warncurrentexigence")
	@MethodLog(remark = "进入紧急告警页面",openType = "3")
	public String warncurrentexigence(HttpServletRequest request) {
		warnlevel = 4;
		String menu = "warncurrentall";
		String warnType = "紧急";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentexigence";
	}
	
	@RequestMapping("/warncurrentexigenceDiv")
	public String warncurrentexigenceDiv(HttpServletRequest request) {
		warnlevel = 4;
		String menu = "warncurrentall";
		String warnType = "紧急";
		warnCurrent(request,menu,warnType);
		return "/WEB-INF/ipmiwarn/warncurrentexigenceDiv";
	}
	
	public void warnCurrent(HttpServletRequest request,String menu,String warnType) {
		HashMap<String, String> map = englishImpl.getmap();
		List<serversip> serversIPList = null;
		serversIPList = sensorImpl.getServersip();
		List<sensorrecord> warnList = getWarn();
		List<String> warnNotOkList = getwarnnotok();
		List<String> middleList = new ArrayList<String>();
		List<sensorrecord> requestList = new ArrayList<sensorrecord>();
		for (int i = 0; i < warnList.size(); i++) {
			middleList.add(warnList.get(i).getIPnamelevel());
			if(!warnNotOkList.contains(warnList.get(i).getIPnamelevel())) {
				warnhistoryImpl.add(warnList.get(i));
			}
			if(warnList.get(i).getWarnlevel().contains(warnType)) {
				requestList.add(warnList.get(i));
			}
		}
		for (int i = 0; i < warnNotOkList.size(); i++) {
			if(!middleList.contains(warnNotOkList.get(i))) {
				Date date = new Date();
				SimpleDateFormat dts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String format = dts.format(date);
				QueryParam param = new QueryParam();
				param.setNameType(warnNotOkList.get(i));
				param.setEnd_time(format);
				warnhistoryImpl.update(param);
			}
		}
		Map<String,String> sysNameMap = sensorImpl.getSysname();
		List<Integer> intList = getLevelSum();
		request.setAttribute("warnlevel", warnlevel);
		request.setAttribute("intList", intList);
		request.setAttribute("map", map);
		request.setAttribute("sysNameMap", sysNameMap);
		request.setAttribute("requestList", requestList);
		request.setAttribute("warnLevelList", warnList);
		request.setAttribute("serversIPList", serversIPList);
		request.setAttribute("menu", menu);
	}
	
	
	//机箱告警信息	
	public List<sensorrecord> getChassisWarn(){
		List<sensorrecord> warnlist = new ArrayList<sensorrecord>();
		List<sensorrecord> setlist = null;
		QueryParam param = new QueryParam();
		setlist = chassiswarnsetImpl.getIPname(param);
		List<String> strlist = new ArrayList<String>();
		for (int i = 0; i < setlist.size(); i++) {
			strlist.add(setlist.get(i).getIPname());
		}
		List<chassisList> list = getInfo("全部","全部");
		for (int i = 0; i < list.size(); i++) {
			String IPname = list.get(i).getIP() + list.get(i).getName();
			Date date = list.get(i).getStart_time();
			if(strlist.contains(IPname)) {
				sensorrecord sensorrecord = new sensorrecord();
				if(list.get(i).getName().equals("is_power_on")){
					if(list.get(i).getState().equals("false")) {
						sensorrecord.setIP(list.get(i).getIP());
						sensorrecord.setName("is_power_on");
						sensorrecord.setStart_time(date);
						sensorrecord.setWarncontent("电源已关闭");
						sensorrecord.setWarnlevel("严重告警");
						sensorrecord.setSensor_type("ChassisState");
						sensorrecord.setIPnamelevel(IPname + "严重告警");
						warnlist.add(sensorrecord);
					}
				}else if(list.get(i).getState().equals("true")){
					sensorrecord.setIP(list.get(i).getIP());
					sensorrecord.setName("is_power_on");
					sensorrecord.setStart_time(date);
					sensorrecord.setWarncontent("出故障了");
					sensorrecord.setWarnlevel("严重告警");
					sensorrecord.setSensor_type("ChassisState");
					sensorrecord.setIPnamelevel(IPname + "严重告警");
					warnlist.add(sensorrecord);
				}
			}
		}
		
		return warnlist;
	}
	
	public  List<chassisList> getInfo(String nameType,String IPType){
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
	
	//服务器无法连接告警
	public List<sensorrecord> getIsOnlineWarn(){
		Date date = new Date();
		List<sensorrecord> warnlist = new ArrayList<sensorrecord>();
		List<serversip> ipIsOnline = sensorImpl.selectIsOnline();
		for (int i = 0; i < ipIsOnline.size(); i++) {
			serversip serversip = ipIsOnline.get(i);
			String IPName = serversip.getName()+serversip.getHostname();
			sensorrecord sensorrecord = new sensorrecord();
			sensorrecord.setName("ipIsOnline");
			sensorrecord.setIP(serversip.getHostname());
			sensorrecord.setStart_time(date);
			sensorrecord.setWarncontent(IPName+"无法连接");
			sensorrecord.setWarnlevel("紧急告警");
			sensorrecord.setSensor_type("ipIsOnline");
			sensorrecord.setIPnamelevel(IPName + "严重告警");
			warnlist.add(sensorrecord);
		}
		return warnlist;
	}
	
	//资源缺失告警
	public List<sensorrecord> getResourceWarn(){
		List<sensorrecord> warnlist = new ArrayList<sensorrecord>();
		List<sensorrecord> warnsetlist = null;
		QueryParam param = new QueryParam();
		warnsetlist = resourcewarnsetImpl.getresourcewarnsetbyparam(param);
		for (int i = 0; i < warnsetlist.size(); i++) {
			String nameip = warnsetlist.get(i).getIPname();
			String emptyresource = warnsetlist.get(i).getEmptyresource();
			List<sensorrecord> addlist = null;
			QueryParam addparam = new QueryParam();
			addparam.setIPType(nameip.substring(nameip.indexOf('1')));
			addparam.setEntity_id(nameip.substring(0, nameip.indexOf('1')));
			addlist = sensorImpl.getcurrentInfobyParam(addparam);
			String emptyname = dealEmptyName(addlist);
			if(!emptyname.equals(emptyresource)) {
				for (int j = 0; j < addlist.size(); j++) {
					if(addlist.get(j).getStatesAsserted() != null && addlist.get(j).getStatesAsserted().isEmpty()) {
						String name = addlist.get(j).getName();
						if(!emptyresource.contains(name)) {
							sensorrecord sensorrecord = addlist.get(j);
							sensorrecord.setWarncontent(name+"缺失");
							sensorrecord.setWarnlevel("严重告警");
							sensorrecord.setSensor_type("ResourceState");
							sensorrecord.setIPnamelevel(nameip + "严重告警");
							warnlist.add(sensorrecord);
						}else {
							emptyresource = emptyresource.replace(name, "");
						}
					}
				}
				if(emptyresource.length() > 1) {
					sensorrecord sensorrecord = new sensorrecord();
					sensorrecord.setIP(nameip.substring(nameip.indexOf('1')));
					sensorrecord.setName(emptyresource);
					sensorrecord.setWarncontent("增加了"+emptyresource);
					sensorrecord.setWarnlevel("严重告警");
					sensorrecord.setSensor_type("ResourceState");
					sensorrecord.setIPnamelevel(nameip + "严重告警");
					warnlist.add(sensorrecord);
				}
			}
		}
		return warnlist;
	}
	
	
	// 获取传感器告警信息
	public List<sensorrecord> getSensorWarn(){
		List<sensorrecord> warnlist = new ArrayList<sensorrecord>();
		List<sensorrecord> list = sensorImpl.getIPnamenum();
		List<String> strlist = getIPname();
		for (int i = 0; i < list.size(); i++) {
			String IPname = list.get(i).getIP() + list.get(i).getName();
			
			if(strlist.contains(IPname)) {
				List<sensorrecord> sensorwarnsetlist = null;
				QueryParam param = new QueryParam();
				param.setIPType(list.get(i).getIP());
				param.setNameType(list.get(i).getName());
				sensorwarnsetlist = sensorwarnsetImpl.getsensorwarnsetbyparam(param);
				sensorrecord sensorrecord = sensorwarnsetlist.get(0);
				double d = list.get(i).getCurrent_num();
				Date date = list.get(i).getStart_time();
				if(d < sensorrecord.getLower_non_recoverable_threshold()
						|| d > sensorrecord.getUpper_non_recoverable_threshold()) {
					sensorrecord.setStart_time(date);
					sensorrecord.setWarncontent(d+"");
					sensorrecord.setWarnlevel("紧急告警");
					sensorrecord.setIPnamelevel(IPname + "紧急告警");
					warnlist.add(sensorrecord);
				}else if(d < sensorrecord.getLower_critical_threshold()
						|| d > sensorrecord.getUpper_critical_threshold()) {
					sensorrecord.setStart_time(date);
					sensorrecord.setWarncontent(d+"");
					sensorrecord.setWarnlevel("严重告警");
					sensorrecord.setIPnamelevel(IPname + "严重告警");
					warnlist.add(sensorrecord);
				}else if(d < sensorrecord.getLower_non_critical_threshold()
						|| d > sensorrecord.getUpper_non_critical_threshold()) {
					sensorrecord.setStart_time(date);
					sensorrecord.setWarncontent(d+"");
					sensorrecord.setWarnlevel("轻微告警");
					sensorrecord.setIPnamelevel(IPname + "轻微告警");
					warnlist.add(sensorrecord);
				}
			}
		}
		return warnlist;
	}
	
	 public String dealEmptyName(List<sensorrecord> list){
    	String emptyname = "";
    	for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getStatesAsserted() != null && list.get(i).getStatesAsserted().isEmpty()) {
				emptyname += list.get(i).getName();
			}
		}
		return emptyname;
    }
	
	 //收集交换机的告警信息
	 /**
	  * 逻辑，获取交换机各个参数的实时数据，与设定的阈值进行比较，将超出
	  * 阈值范围的数据加入告警列表
	  * 
	  * */
	 
	//收集DXC的告警信息
	 
	//收集TAP的告警信息
	 
	//收集网关服务器的告警信息
	 
	 //收集所有的告警信息
	public List<sensorrecord> getWarn(){
		List<sensorrecord> returnList = new ArrayList<sensorrecord>();
		List<sensorrecord> sensorWarnList = getSensorWarn();
		for (int j = 0; j < sensorWarnList.size(); j++) {
			returnList.add(sensorWarnList.get(j));
		}
		List<sensorrecord> chassisWarnList = getChassisWarn();
		for (int j = 0; j < chassisWarnList.size(); j++) {
			returnList.add(chassisWarnList.get(j));
		}
		List<sensorrecord> resourceWarnList = getResourceWarn();
		for (int i = 0; i < resourceWarnList.size(); i++) {
			returnList.add(resourceWarnList.get(i));
		}
		List<sensorrecord> isOnlineWarnList = getIsOnlineWarn();
		for (int i = 0; i < isOnlineWarnList.size(); i++) {
			returnList.add(isOnlineWarnList.get(i));
		}
		return returnList;
	}
	
	public List<String> getIPname(){
		List<sensorrecord> sensorwarnsetlist = null;
		sensorwarnsetlist = sensorwarnsetImpl.getIPname();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < sensorwarnsetlist.size(); i++) {
			list.add(sensorwarnsetlist.get(i).getIPname());
		}
		return list;
	}
}	
