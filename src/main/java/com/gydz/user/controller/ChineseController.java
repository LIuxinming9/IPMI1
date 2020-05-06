package com.gydz.user.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gydz.user.mapper.MethodLog;
import com.gydz.user.model.QueryParam;
import com.gydz.user.model.User;
import com.gydz.user.model.english;
import com.gydz.user.model.selrecord;
import com.gydz.user.model.sensorrecord;
import com.gydz.user.model.serversip;
import com.gydz.user.service.englishImpl;
import com.gydz.user.service.selImpl;
import com.gydz.user.service.sensorImpl;
import com.gydz.util.ipmiutil;

@Controller
@RequestMapping("/chinese")
public class ChineseController {

	@Resource
	private sensorImpl sensorImpl;
	
	@Resource
	private englishImpl englishImpl;
	
	@Resource
	private selImpl selImpl;
	
	public List<String> getallenglish(){
		List<String> list = new ArrayList<String>();
		QueryParam param = new QueryParam();
		List<sensorrecord> senlist = sensorImpl.getcurrentInfobyParam(param);
		Set<String> idset = new LinkedHashSet<String>();
		Set<String> typeset = new LinkedHashSet<String>();
		Set<String> unitset = new LinkedHashSet<String>();
		List<selrecord> sensorwarnreferlist = null;
		sensorwarnreferlist = selImpl.getInfobyParam(param);
		Set<String> seltypeset = new LinkedHashSet<String>();
		Set<String> selnameset = new LinkedHashSet<String>();
		for (int i = 0; i < sensorwarnreferlist.size(); i++) {
			selnameset.add(sensorwarnreferlist.get(i).getEvent());
		}
		for (int i = 0; i < sensorwarnreferlist.size(); i++) {
			seltypeset.add(sensorwarnreferlist.get(i).getSensorType());
		}
		for (int i = 0; i < senlist.size(); i++) {
			idset.add(senlist.get(i).getEntity_id());
			typeset.add(senlist.get(i).getSensor_type());
			list.add(senlist.get(i).getName());
			unitset.add(senlist.get(i).getSensor_base_unit());
		}
		Iterator<String> idit = idset.iterator();
		while(idit.hasNext()) {
			list.add(idit.next());
		}
		Iterator<String> typeit = typeset.iterator();
		while(typeit.hasNext()) {
			list.add(typeit.next());
		}
		Iterator<String> unitit = unitset.iterator();
		while(unitit.hasNext()) {
			list.add(unitit.next());
		}
		Iterator<String> seltypeit = seltypeset.iterator();
		while(seltypeit.hasNext()) {
			list.add(seltypeit.next());
		}
		Iterator<String> selnameit = selnameset.iterator();
		while(selnameit.hasNext()) {
			list.add(selnameit.next());
		}
		return list;
	}
	 
	@RequestMapping("/chinese")
	@MethodLog(remark = "进入英语翻译页面",openType = "3")
	public String sensorwarnrefer(HttpServletRequest request) {
		List<String> alist = getallenglish();
		List<String> elist = englishImpl.getlist();
		for (int i = 0; i < alist.size(); i++) {
			String english = alist.get(i);
			if(english!=null&&english!="") {
				if(!elist.contains(english.trim())) {
					englishImpl.add(english.trim());
				}
			}
		}
		List<english> list = englishImpl.getinfo();
		request.setAttribute("list", list);
		request.setAttribute("menu", "chinese");
		return "/WEB-INF/english/chinese";
	}
	
	@RequestMapping("/query")
    @MethodLog(remark = "查询英语翻译",openType = "3")
    public String list(HttpServletRequest request, String keyword){
    	List<english> list = new ArrayList<english>();
    	List<english> elist = englishImpl.getinfo();
    	if(StringUtils.isBlank(keyword)){
    		list = elist;
    	}else{
    		for (int i = 0; i < elist.size(); i++) {
				if(elist.get(i).getEnglish().equals(keyword)||elist.get(i).getChinese().equals(keyword)) {
					english english = new english();
					english.setEnglish(elist.get(i).getEnglish());
					english.setChinese(elist.get(i).getChinese());
					list.add(english);
				}
			}
    	}
    	request.getSession().setAttribute("list",list);
    	request.setAttribute("menu","chinese");
    	request.setAttribute("keyword",keyword);
        return "/WEB-INF/english/chinese";
    }
    
	//增加服务器
	@RequestMapping("/add")
	@MethodLog(remark = "增加一个英语翻译",openType = "0")
    public String add(HttpServletRequest request){
		englishImpl.addid();
		request.setAttribute("menu", "chinese");
        return "redirect:/chinese/chinese";
    }
	
	// 保存服务器信息页面
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@MethodLog(remark = "修改英语翻译",openType = "2")
	public String saveSysIP(HttpServletRequest request, Model model) {

		List<english> englishlist = null;
		englishlist = new ArrayList<english>();
		String[] englishs = request.getParameterValues("english");
		String[] chineses = request.getParameterValues("chinese");
		for (int i = 0; i < englishs.length; i++) {
			english english = new english();
			english.setEnglish(englishs[i]);
			english.setChinese(chineses[i]);
			englishlist.add(english);
		}
		for (int i = 0; i < englishs.length; i++) {
			englishImpl.update(englishlist.get(i));
		}
		request.setAttribute("menu", "chinese");
		return "redirect:/chinese/chinese";
	}
	
	//删除用户
    @RequestMapping(value = "/del")
    @MethodLog(remark = "删除一个英语翻译",openType = "1")
    public String delSysIP(HttpServletRequest request){
        try {
        	String english = request.getParameterValues("english")[0];
        	englishImpl.del(english);
        }catch (Exception ex){
        	System.out.println("____________删除失败——————————————————————");
        }
        request.setAttribute("menu", "chinese");
        return "redirect:/chinese/chinese";
    }

}	
