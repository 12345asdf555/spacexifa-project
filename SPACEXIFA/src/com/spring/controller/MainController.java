package com.spring.controller;

import com.github.pagehelper.PageInfo;
import com.spring.model.*;
import com.spring.page.Page;
import com.spring.service.EmailService;
import com.spring.service.InsframeworkService;
import com.spring.service.ResourceService;
import com.spring.service.UserService;
import com.spring.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping(value = "/hierarchy", produces = { "text/json;charset=UTF-8" })
public class MainController {
	@Autowired
	private UserService user;
	@Autowired
	private InsframeworkService is;
	@Autowired
	private ResourceService rs;
	@Autowired
	private EmailService es;

	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	IsnullUtil iutil = new IsnullUtil();
	
	/**
	 * 跳转index页面进行分层
	 * @return
	 */
	@RequestMapping("/goIndex")
	@ResponseBody
	public String goGather(HttpServletRequest request){
		String hierarchy = request.getSession().getServletContext().getInitParameter("hierarchy");
		request.setAttribute("hierarchy", hierarchy);
		JSONObject obj = new JSONObject();
		obj.put("hierarchy", hierarchy);
		return obj.toString();
	}
	
	@RequestMapping("/getUserInsframework")
	@ResponseBody
	public String getUserInsframework(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(object==null){
			obj.put("uname", "请登录");
			obj.put("insframework", "无");
			return obj.toString();
		}
		MyUser u = (MyUser)object;
		User list = user.getUserInsframework(new BigInteger(u.getId()+""));
		List<String> name = rs.getAuthName((int)u.getId());
		List<Resources> menu = null;
		boolean flag = true;
		for(int i=0;i<name.size();i++){
			if(name.get(i).equalsIgnoreCase("ROLE_admin")){
				flag = false;
				menu = rs.getResourceByAdmin();
				break;
			}
		}
		if(flag){
			menu = rs.getResourceByUserid((int)u.getId());
		}
		for(int i=0;i<menu.size();i++){
			json.put("resource", menu.get(i).getResourceAddress().substring(1));
			ary.add(json);
		}
		//获取服务器ip地址
		String ipurl = request.getSession().getServletContext().getInitParameter("ipurl");
		StringBuffer requestURL = request.getRequestURL();
		if (requestURL.indexOf("10.110.11.3") != -1){
			ipurl = "http://10.110.11.3:9090/";
		}
		obj.put("ipurl", ipurl);
		obj.put("uname", list.getUserName());
		obj.put("insframework", list.getInsname());
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getHierarchy")
	@ResponseBody
	public String getHierarchy(){
		JSONObject obj = new JSONObject();
		JSONArray ary1 = new JSONArray();
		JSONArray ary2 = new JSONArray();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		List<Insframework> company = is.getConmpany();
		for(Insframework i:company){
			json1.put("companyid", i.getId());
			json1.put("companyname", i.getName());
			ary1.add(json1);
			List<Insframework> caust = is.getCause(i.getId());
			for(Insframework j:caust){
				json2.put("companyid", i.getId());
				json2.put("caustid", j.getId());
				json2.put("caustname", j.getName());
				ary2.add(json2);
			}
		}
		obj.put("ary1", ary1);
		obj.put("ary2", ary2);
		return obj.toString();
	}
	
	/**
	 * 获取隐藏菜单
	 * @return
	 */
	@RequestMapping("/getHiddenMenu")
	@ResponseBody
	public String getHiddenMenu(){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		List<User> menu = user.getHiddenMenu();
		for(int i=0;i<menu.size();i++){
			json.put("name", menu.get(i).getMenuName());
			ary.add(json);
		}
		obj.put("ary", ary);
		return obj.toString();
	}

	/**
	 * 跳转邮件管理页面
	 * @return
	 */
	@RequestMapping("/goEmail")
	public String goEmail(HttpServletRequest request){
		return "email/email";
	}

	/**
	 * 跳转邮件记录查询
	 * @return
	 */
	@RequestMapping("/goEmailHistory")
	public String goEmailHistory(HttpServletRequest request){
		return "email/emailhistory";
	}

	@RequestMapping("/getEmailList")
	@ResponseBody
	public String getEmailList(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<Email> list = es.getEmailAll(page,str);
		long total = 0;

		if(list != null){
			PageInfo<Email> pageinfo = new PageInfo<Email>(list);
			total = pageinfo.getTotal();
		}
		try{
			for(int i=0;i<list.size();i++){
				json.put("id", list.get(i).getFid());
				json.put("femailname", list.get(i).getFemailname());
				json.put("femailaddress", list.get(i).getFemailaddress());
				json.put("femailtype", list.get(i).getFemailtype());
				String[] emailstr = list.get(i).getFemailtype().split(",");
				String typestr = "";
				for(int j=0;j<emailstr.length;j++){
					if(Integer.parseInt(emailstr[j]) == 1){
						typestr += "员工入职半年提醒,";
					}
					if(Integer.parseInt(emailstr[j]) == 2){
						typestr += "员工IC卡有效期提醒,";
					}
					if(Integer.parseInt(emailstr[j]) == 3){
						typestr += "员工长时间未工作提醒,";
					}
					if(Integer.parseInt(emailstr[j]) == 4){
						typestr += "焊机校验提醒,";
					}
					if(Integer.parseInt(emailstr[j]) == 5){
						typestr += "焊机保养提醒,";
					}
				}
				if(typestr.length()!=0){
					typestr = typestr.substring(0, typestr.length()-1);
				}
				json.put("typestr", typestr);
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}

	@RequestMapping("/addEmail")
	@ResponseBody
	public String addEmail(@ModelAttribute("email")Email email){
		JSONObject obj = new JSONObject();
		try{
			String[] emailstr = email.getFemailtype().split(",");
			for(int i=0;i<emailstr.length;i++){
				email.setFemailtype(emailstr[i]);
				es.addEmail(email);
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("msg", e.getMessage());
		}
		return obj.toString();
	}

	@RequestMapping("/editEmail")
	@ResponseBody
	public String editEmail(HttpServletRequest request,@ModelAttribute("email")Email email){
		JSONObject obj = new JSONObject();
		try{
			es.deleteEmail(request.getParameter("address"));
			String[] emailstr = email.getFemailtype().split(",");
			for(int i=0;i<emailstr.length;i++){
				email.setFemailtype(emailstr[i]);
				es.addEmail(email);
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("msg", e.getMessage());
		}
		return obj.toString();
	}

	@RequestMapping("/deleteEmail")
	@ResponseBody
	public String deleteEmail(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			es.deleteEmail(request.getParameter("femailaddress"));
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("msg", e.getMessage());
		}
		return obj.toString();
	}
}
