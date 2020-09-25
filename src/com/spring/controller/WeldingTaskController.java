package com.spring.controller;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.cxf.endpoint.Client;
import javax.xml.namespace.QName;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.MyUser;
import com.spring.model.Person;
import com.spring.model.WeldingMachine;
import com.spring.model.Wps;
import com.spring.model.Dictionarys;
import com.spring.model.Insframework;
import com.spring.model.WeldedJunction;
import com.spring.model.Welder;
import com.spring.page.Page;
import com.spring.service.InsframeworkService;
import com.spring.service.LiveDataService;
import com.spring.service.PersonService;
import com.spring.service.WeldedJunctionService;
import com.spring.util.IsnullUtil;
import com.spring.service.DictionaryService;
import com.spring.service.WeldingMachineService;
import com.spring.service.WpsService;
import com.spring.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/weldtask", produces = { "text/json;charset=UTF-8" })
public class WeldingTaskController {

	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;

	@Autowired
	private WeldedJunctionService wjm;
	@Autowired
	private InsframeworkService insm;
	@Autowired
	private LiveDataService lm;
	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private DictionaryService dm;
	@Autowired
	private UserService fuser;
	@Autowired
	private PersonService ps;
	@Autowired
	private WpsService wps;
	
	IsnullUtil iutil = new IsnullUtil();

	@RequestMapping("/goWeldTask")
	public String goWeldTask(HttpServletRequest request){
//		String serach="";
//		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
//		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
//		int bz=0;
//		if(instype==20){
//			
//		}else if(instype==23){
//			serach = "tb_welder.Fowner="+userinsid;
//		}else{
//			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
//			for(Insframework inns : ls ){
//				if(bz==0){
//					serach=serach+"(tb_welder.Fowner="+inns.getId();
//				}else{
//					serach=serach+" or tb_welder.Fowner="+inns.getId();
//				}
//				bz++;
//			}
//			serach=serach+" or tb_welder.Fowner="+userinsid+")";
//		}
//		request.setAttribute("userinsall",serach );
		return "weldingtask/TrackingCard";
	}
	
	@RequestMapping("/goTaskResult")
	public String goTaskResult(HttpServletRequest request){
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		if(instype==20){
			
		}else if(instype==23){
			serach = "and w.Fowner="+userinsid;
		}else{
			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
			for(Insframework inns : ls ){
				if(bz==0){
					serach=serach+"and (w.Fowner="+inns.getId();
				}else{
					serach=serach+" or w.Fowner="+inns.getId();
				}
				bz++;
			}
			serach=serach+" or w.Fowner="+userinsid+")";
		}
		request.setAttribute("userid",serach );
		request.setAttribute("userinsframework", fuser.getUserInsframework(new BigInteger(String.valueOf(user.getId()))).getInsname());
		return "weldingtask/taskresult";
	}
	@RequestMapping("/goTaskEvaluate")
	public String goTaskEvaluate(HttpServletRequest request){
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		request.setAttribute("userinsframework", fuser.getUserInsframework(new BigInteger(String.valueOf(user.getId()))).getInsname());
		return "weldingtask/taskevaluate";
	}
	@RequestMapping("/getWeldTaskList")
	@ResponseBody
	public String getWeldTaskList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		if(instype==20){
			
		}else if(instype==23){
			serach = "j.fitemId="+userinsid;
		}else{
			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
			for(Insframework inns : ls ){
				if(bz==0){
					serach=serach+"(j.fitemId="+inns.getId();
				}else{
					serach=serach+" or j.fitemId="+inns.getId();
				}
				bz++;
			}
			serach=serach+" or j.fitemId="+userinsid+")";
		}
		if(request.getParameter("searchStr")!=null&&serach!=null&&serach!=""){
			serach=serach+" and "+request.getParameter("searchStr");
		}
		if(request.getParameter("searchStr")!=null&&(serach==null||serach=="")){
			serach=serach+request.getParameter("searchStr");
		}
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getWeldedJunctionAll(page, serach);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("weldedJunctionno", w.getWeldedJunctionno());
				json.put("startTime", w.getStartTime());
				json.put("fengineering_symbol", w.getFengineering_symbol());
				json.put("fweld_method", w.getFweld_method());
				json.put("fweld_position", w.getFweld_position());
				json.put("fbase_material_type", w.getFbase_material_type());
				json.put("fweld_material_model", w.getFweld_material_model());
				json.put("ftechnological_design", w.getFtechnological_design());
				json.put("fwarm_up_requirement", w.getFwarm_up_requirement());
				json.put("finter_channel_temperature", w.getFinter_channel_temperature());
				json.put("fcarbon_requirement", w.getFcarbon_requirement());
				json.put("fpost_heat_requirement", w.getFpost_heat_requirement());
				json.put("fannealed_weld", w.getFannealed_weld());
				json.put("fassembly_clearance", w.getFassembly_clearance());
				json.put("fcarbon_depth", w.getFcarbon_depth());
				json.put("fcarbon_width", w.getFcarbon_width());
				json.put("fpost_heat_temperature", w.getFpost_heat_temperature());
				json.put("fafter_hot_time", w.getFafter_hot_time());
				json.put("fwps_lib_name", w.getFwps_lib_name());
				json.put("fwelder_name", w.getFwelder_name());
				json.put("fwelder_id", w.getFwelder_id());
				json.put("fwps_lib_name", w.getFwps_lib_name());
				json.put("fwpslib_id", w.getFwpslib_id());
				json.put("iname", w.getIname());
				json.put("iid", w.getIid());
				if(w.getFstatus()==null){
					json.put("status", 2);
				}else if(Integer.valueOf(w.getFstatus())==1){
					json.put("status", 1);
				}else{
					json.put("status", 0);
				}
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getWeldTask")
	@ResponseBody
	public String getWeldTask(HttpServletRequest request){
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		if(instype==20){
			
		}else if(instype==23){
			serach = "j.fitemId="+userinsid;
		}else{
			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
			for(Insframework inns : ls ){
				if(bz==0){
					serach=serach+"(j.fitemId="+inns.getId();
				}else{
					serach=serach+" or j.fitemId="+inns.getId();
				}
				bz++;
			}
			serach=serach+" or j.fitemId="+userinsid+")";
		}
		if(request.getParameter("searchStr")!=null&&serach!=null&&serach!=""){
			serach=serach+" and "+request.getParameter("searchStr");
		}
		if(request.getParameter("searchStr")!=null&&(serach==null||serach=="")){
			serach=serach+request.getParameter("searchStr");
		}
		List<WeldedJunction> list = wjm.getWeldedJunction(serach);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("weldedJunctionno", w.getWeldedJunctionno());
				json.put("serialNo", w.getSerialNo());
				json.put("pipelineNo", w.getPipelineNo());
				json.put("roomNo", w.getRoomNo());
				json.put("levelid", w.getSystems());
				json.put("levelname", w.getArea());
				json.put("realwelder", w.getNext_material());
				json.put("itemname", w.getIname());
				json.put("itemid", w.getIid());
				if(w.getMaterial()==null){
					json.put("status", 2);
				}else if(Integer.valueOf(w.getMaterial())==1){
					json.put("status", 1);
				}else{
					json.put("status", 0);
				}
				json.put("welderid", w.getDyne());
				json.put("quali", w.getExternalDiameter());
				json.put("dtoTime1",w.getStartTime());
				json.put("dtoTime2", w.getEndTime());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/goTrackCard")
	public String goTrackCard(HttpServletRequest request){
		String fid = request.getParameter("fid");
		String fwelded_junction_no = request.getParameter("fwelded_junction_no");
		String status = request.getParameter("status");
		String fitemName = request.getParameter("fitemName");
		request.setAttribute("fid", fid);
		request.setAttribute("fwelded_junction_no", fwelded_junction_no);
		request.setAttribute("status", status);
		request.setAttribute("fitemName", fitemName);
		String symbol = "0";
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		List<String> ls = fuser.getAuthoritiesByUsername(myuser.getUsername());
		for(int i=0;i<ls.size();i++) {
			if(ls.get(i).equals("ROLE_审核员") || ls.get(i).equals("ROLE_admin")) {
				symbol = "1";
				break;
			}
		}
		request.setAttribute("symbol", symbol);
		return "weldingtask/cardprocess";
	}
	
	@RequestMapping("/getTaskResultList")
	@ResponseBody
	public String getTaskResultList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		String parent = request.getParameter("parent");
		String serach = "";
		page = new Page(pageIndex,pageSize,total);
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		if(iutil.isNull(parent)){
			serach = parent;
		}else{
			int bz=0;
			if(instype==20){
				serach="";
			}else if(instype==23){
				serach = "j.fitemId="+userinsid;
			}else{
				List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+userinsid+")";
			}
		}

		if(iutil.isNull(str)){
			if(iutil.isNull(serach)){
				serach += " and "+str;
			}else{
				serach = str;
			}
		}
		List<WeldedJunction> list = wjm.getTaskResultAll(page, serach);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("taskid", w.getCounts());
/*				json.put("welderid", w.getInsfid());
				json.put("machineid", w.getMachid());*/
				json.put("operateid",w.getDyne());
				json.put("result", w.getPipelineNo());
				json.put("resultid", w.getUpdatecount());
				json.put("taskNo",w.getWeldedJunctionno());
/*				json.put("welderNo", w.getSerialNo());
				json.put("machineNo", w.getMachine_num());*/
				json.put("resultName", w.getRoomNo());
				json.put("getdatatime", w.getUpdateTime());
				json.put("starttime", w.getStartTime());
				json.put("endtime", w.getEndTime());
				json.put("fitemid", w.getArea());
				json.put("user", w.getMaterial());
				json.put("itemname", w.getIname());
				json.put("itemid", w.getIid());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getRealWelder")
	@ResponseBody
	public String getRealWelder(HttpServletRequest request){
		String str = request.getParameter("searchStr");
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getRealWelder(page,new BigInteger(str));
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("taskid", w.getCreater());
				json.put("taskno", w.getWeldedJunctionno());
				json.put("welderid", w.getIid());
				json.put("welderno", w.getRoomNo());
				json.put("weldername", w.getIname());
				json.put("machid", w.getMachid());
				json.put("machno", w.getMachine_num());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getJunctionByWelder")
	@ResponseBody
	public String getJunctionByWelder(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String welder = request.getParameter("welder");
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getJunctionByWelder(page, welder, dto);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("weldedJunctionno", w.getWeldedJunctionno().substring(2, 8));
				json.put("maxElectricity", w.getMaxElectricity());
				json.put("minElectricity", w.getMinElectricity());
				json.put("maxValtage", w.getMaxValtage());
				json.put("minValtage", w.getMinValtage());
				json.put("itemname", w.getIname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}

	/**
	 * 显示焊机列表
	 * @return
	 */
	@RequestMapping("/getWedlingMachineList")
	@ResponseBody
	public String getWedlingMachineList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String searchStr = request.getParameter("searchStr");
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		request.getSession().setAttribute("searchStr", searchStr);
		page = new Page(pageIndex,pageSize,total);
		List<WeldingMachine> list = wmm.getWeldingMachineAll(page,parent,searchStr);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldingMachine> pageinfo = new PageInfo<WeldingMachine>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldingMachine wm:list){
				json.put("id", wm.getId());
				json.put("ip", wm.getIp());
				json.put("equipmentNo", wm.getEquipmentNo());
				json.put("position", wm.getPosition());
				json.put("gatherId", wm.getGatherId());
				if(wm.getIsnetworking()==0){
					json.put("isnetworking", "是");
				}else{
					json.put("isnetworking", "否");
				}
				json.put("isnetworkingId", wm.getIsnetworking());
				json.put("joinTime", wm.getJoinTime());
				json.put("typeName",wm.getTypename());
				json.put("typeId", wm.getTypeId());
				json.put("statusName", wm.getStatusname());
				json.put("statusId", wm.getStatusId());
				json.put("manufacturerName", wm.getMvaluename());
				json.put("manuno", wm.getMvalueid());
				if( wm.getInsframeworkId()!=null && !"".equals(wm.getInsframeworkId())){
					json.put("insframeworkName", wm.getInsframeworkId().getName());
					json.put("iId", wm.getInsframeworkId().getId());
				}
				json.put("model",wm.getModel());
				if(wm.getGatherId()!=null && !("").equals(wm.getGatherId())){
					json.put("gatherId", wm.getGatherId().getGatherNo());
					json.put("gid", wm.getGatherId().getId());
				}else{
					json.put("gatherId", null);
					json.put("gid", null);
				}
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}

	/**
	 * 获取评价等级
	 * @return
	 */
	@RequestMapping("/getStatusAll")
	@ResponseBody
	public String getStatusAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(16);
			for(Dictionarys d:dictionary){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("ary", ary);
		return obj.toString();
	}

	@RequestMapping("/addWeldTask")
	@ResponseBody
	public String addWeldTask(HttpServletRequest request,@ModelAttribute WeldedJunction wj){
		JSONObject obj = new JSONObject();
		try{
			wj.setWeldedJunctionno(request.getParameter("weldedJunctionno"));
			wj.setStartTime(request.getParameter("startTime"));
			wj.setFwpslib_id(new BigInteger(request.getParameter("fwpslib_id")));
			wj.setFwelder_id(new BigInteger(request.getParameter("fwelder_id")));
			wj.setIid(new BigInteger(ps.getInsidByFid(new BigInteger(request.getParameter("fwelder_id")))));
			wjm.addTask(wj);
			obj.put("success", true);
			//客户端执行操作
//			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
//			iutil.Authority(client);
//			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"addJunction\"}";
//			String obj2 = "{\"JUNCTIONNO\":\""+request.getParameter("weldedJunctionno")+"\",\"DYNE\":\""+request.getParameter("fwelderid")+"\",\"TASKLEVEL\":\""+request.getParameter("tasklevel")+"\","+
//					"\"INSFID\":\""+request.getParameter("fitemid")+"\",\"STARTTIME\":\""+request.getParameter("dtoTime1")+"\",\"ENDTIME\":\""+request.getParameter("dtoTime2")+"\",\"EXTERNALDIAMETER\":\""+request.getParameter("quali")+"\"}";
//			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
//			if(objects[0].toString().equals("true")){
//				obj.put("success", true);
//			}else if(!objects[0].toString().equals("false")){
//				obj.put("success", true);
//				obj.put("msg", objects[0].toString());
//			}else{
//				obj.put("success", false);
//				obj.put("errorMsg", "操作失败！");
//			}
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}

	@RequestMapping("/editWeldTask")
	@ResponseBody
	public String editWeldTask(HttpServletRequest request,@ModelAttribute WeldedJunction wj){
		JSONObject obj = new JSONObject();
		try{
			wj.setId(new BigInteger(request.getParameter("id")));
			wj.setWeldedJunctionno(request.getParameter("weldedJunctionno"));
			wj.setStartTime(request.getParameter("startTime"));
			wj.setFwpslib_id(new BigInteger(request.getParameter("fwpslib_id")));
			wj.setFwelder_id(new BigInteger(request.getParameter("fwelder_id")));
			wj.setIid(new BigInteger(ps.getInsidByFid(new BigInteger(request.getParameter("fwelder_id")))));
			wjm.updateTaskByFid(wj);
			obj.put("success", true);
			//客户端执行操作
//			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
//			iutil.Authority(client);
//			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"updateJunction\"}";
//			String obj2 = "{\"ID\":\""+request.getParameter("id")+"\",\"JUNCTIONNO\":\""+request.getParameter("weldedJunctionno")+"\",\"DYNE\":\""+request.getParameter("fwelderid")+"\",\"TASKLEVEL\":\""+request.getParameter("tasklevel")+"\","+
//					"\"INSFID\":\""+request.getParameter("fitemid")+"\",\"STARTTIME\":\""+request.getParameter("dtoTime1")+"\",\"ENDTIME\":\""+request.getParameter("dtoTime2")+"\",\"EXTERNALDIAMETER\":\""+request.getParameter("quali")+"\"}";
//			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
//			if(objects[0].toString().equals("true")){
//				obj.put("success", true);
//			}else if(!objects[0].toString().equals("false")){
//				obj.put("success", true);
//				obj.put("msg", objects[0].toString());
//			}else{
//				obj.put("success", false);
//				obj.put("errorMsg", "操作失败！");
//			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}

	@RequestMapping("/removeWeldTask")
	@ResponseBody
	public String removeWeldTask(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			String id = request.getParameter("id");
			wjm.deleteJunction(new BigInteger(id));
			obj.put("success", true);
			//客户端执行操作
//			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
//			iutil.Authority(client);
//			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"deleteJunction\"}";
//			String obj2 = "{\"ID\":\""+request.getParameter("id")+"\",\"INSFID\":\""+request.getParameter("insfid")+"\"}";
//			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
//			if(objects[0].toString().equals("true")){
//				obj.put("success", true);
//			}else if(!objects[0].toString().equals("false")){
//				obj.put("success", true);
//				obj.put("msg", objects[0].toString());
//			}else{
//				obj.put("success", false);
//				obj.put("errorMsg", "操作失败！");
//			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}
	
	@RequestMapping("/wjNoValidate")
	@ResponseBody
	private String wjNoValidate(@RequestParam String wjno){
		boolean data = true;
		int count = wjm.getWeldedjunctionByNo(wjno);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/getWeldingJun")
	@ResponseBody
	public String getWeldingJun(HttpServletRequest request){
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String parentId = request.getParameter("parent");
		String wjno = request.getParameter("wjno");
		String welderid = request.getParameter("welderid");
		WeldDto dto = new WeldDto();
		if(!iutil.isNull(parentId)){
			//数据权限处理
			BigInteger uid = lm.getUserId(request);
			String afreshLogin = (String)request.getAttribute("afreshLogin");
			if(iutil.isNull(afreshLogin)){
				return "0";
			}
			int types = insm.getUserInsfType(uid);
			if(types==21){
				parentId = insm.getUserInsfId(uid).toString();
			}
		}
		BigInteger parent = null;
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(wjno)){
			dto.setSearch(wjno);//用来保存任务编号
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getJMByWelder(page, dto ,welderid);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("firsttime", wjm.getFirsttime(dto, w.getMachid(),welderid , w.getWeldedJunctionno()));
				json.put("lasttime", wjm.getLasttime(dto, w.getMachid(),welderid , w.getWeldedJunctionno()));
				json.put("fweldingtime", new DecimalFormat("0.0000").format((float)Integer.valueOf(w.getCounts().toString())/3600));
				json.put("id", w.getId());
				json.put("machid",w.getMachid());
				json.put("machine_num", w.getMachine_num());
				json.put("weldedJunctionno", w.getWeldedJunctionno().substring(2, 8));
				json.put("dyne", w.getDyne());
				json.put("maxElectricity", w.getMaxElectricity());
				json.put("minElectricity", w.getMinElectricity());
				json.put("maxValtage", w.getMaxValtage());
				json.put("minValtage", w.getMinValtage());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	@RequestMapping("/getEvaluate")
	@ResponseBody
	public String getEvaluate(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//客户端执行操作
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
			iutil.Authority(client);
			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"giveToServer\"}";
			String obj2 = "{\"TASKNO\":\""+request.getParameter("taskNo")+"\",\"WELDERNO\":\""+request.getParameter("welderNo")+"\",\"MACHINENO\":\""+request.getParameter("machineNo")+"\",\"STATUS\":\""+request.getParameter("operateid")+"\",\"TASKID\":\""+request.getParameter("taskid")+"\",\"WELDERID\":\""+request.getParameter("welderid")+"\",\"MACHINEID\":\""+request.getParameter("machineid")+"\",\"OPERATOR\":\""+user.getId()+"\",\"ID\":\""+request.getParameter("id")+"\",\"RESULT\":\""+request.getParameter("result")+"\",\"RESULTID\":\""+request.getParameter("resultid")+"\",\"STARTTIME\":\""+request.getParameter("starttime")+"\",\"ENDTIME\":\""+request.getParameter("endtime")+"\"}";
			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else if(!objects[0].toString().equals("false")){
				obj.put("success", true);
				obj.put("msg", objects[0].toString());
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}
	
	@RequestMapping("/taskImport")
	@ResponseBody
	public String taskImport(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			WeldedJunction wj = new WeldedJunction();
			String taskstr = request.getParameter("taskstr");
			ary = JSONArray.fromObject(taskstr);
			for(int i=0;i<ary.size();i++){
				obj = ary.getJSONObject(i); 
				wj.setWeldedJunctionno(String.valueOf(obj.get("taskNo")));
				if(obj.get("start")==null||"".equals(obj.get("start"))){
					wj.setStartTime(null);
				}else{
					wj.setStartTime(String.valueOf(obj.get("start")));
				}
				if(String.valueOf(obj.get("fengineering_symbol"))==null||"null".equals(String.valueOf(obj.get("fengineering_symbol")))||"".equals(String.valueOf(obj.get("fengineering_symbol")))){
					wj.setFengineering_symbol("-");//工程符号
				}else{
					wj.setFengineering_symbol(String.valueOf(obj.get("fengineering_symbol")));//工程符号
				}
				String fmed = String.valueOf(obj.get("fweld_method"));
				if(fmed==null||"".equals(fmed)||"null".equals(fmed)){
					wj.setFweld_method("-");//焊接方法
				}else{
					wj.setFweld_method(String.valueOf(obj.get("fweld_method")));//焊接方法
				}
				String fpos = String.valueOf(obj.get("fweld_position"));
				if(fpos==null||"".equals(fpos)||"null".equals(fpos)){
					wj.setFweld_position("-");//焊接位置
				}else{
					wj.setFweld_position(String.valueOf(obj.get("fweld_position")));//焊接位置
				}
				String fbmt = String.valueOf(obj.get("fbase_material_type"));
				if(fbmt==null||"".equals(fbmt)||"null".equals(fbmt)){
					wj.setFbase_material_type("-");//母材型号
				}else{
					wj.setFbase_material_type(String.valueOf(obj.get("fbase_material_type")));//母材型号
				}
				String fmm = String.valueOf(obj.get("fweld_material_model"));
				if(fmm==null||"".equals(fmm)||"null".equals(fmm)){
					wj.setFweld_material_model("-");//焊材型号
				}else{
					wj.setFweld_material_model(String.valueOf(obj.get("fweld_material_model")));//焊材型号
				}
				String person = String.valueOf(obj.get("personid"));
				if(person==null||"".equals(person)||"null".equals(person)){
					wj.setFwelder_id(null);//操作人id
				}else{
					wj.setFwelder_id(new BigInteger(String.valueOf(obj.get("personid"))));//操作人id
				}
				wj.setIid(new BigInteger(String.valueOf(obj.get("itemid"))));
				String ftd = String.valueOf(obj.get("ftechnological_design"));
				if(ftd==null||"".equals(ftd)||"null".equals(ftd)){
					wj.setFtechnological_design("-");//工艺设计
				}else{
					wj.setFtechnological_design(ftd);//工艺设计
				}
				String wur = String.valueOf(obj.get("fwarm_up_requirement"));
				if(wur==null||"".equals(wur)||"null".equals(wur)){
					wj.setFwarm_up_requirement("-");//预热要求
				}else{
					wur = wur.replace("&lt;", "<");
					wur = wur.replace("&gt;", ">");
					wj.setFwarm_up_requirement(wur);//预热要求
				}
				String fct = String.valueOf(obj.get("finter_channel_temperature"));
				if(fct==null||"".equals(fct)||"null".equals(fct)){
					wj.setFinter_channel_temperature("-");//道间温度
				}else{
					fct = fct.replace("&lt;", "<");
					fct = fct.replace("&gt;", ">");
					wj.setFinter_channel_temperature(fct);//道间温度
				}
				String fcr = String.valueOf(obj.get("fcarbon_requirement"));
				if(fcr==null||"".equals(fcr)||"null".equals(fcr)){
					wj.setFcarbon_requirement("-");//碳刨要求
				}else{
					wj.setFcarbon_requirement(String.valueOf(obj.get("fcarbon_requirement")));//碳刨要求
				}
				String fhr = String.valueOf(obj.get("fpost_heat_requirement"));
				if(fhr==null||"".equals(fhr)||"null".equals(fhr)){
					wj.setFpost_heat_requirement("-");//后热要求
				}else{
					wj.setFpost_heat_requirement(String.valueOf(obj.get("fpost_heat_requirement")));//后热要求
				}
				String faw = String.valueOf(obj.get("fannealed_weld"));
				if(faw==null||"".equals(faw)||"null".equals(faw)){
					wj.setFannealed_weld("-");//退火焊道
				}else{
					wj.setFannealed_weld(String.valueOf(obj.get("fannealed_weld")));//退火焊道
				}
				String fac = String.valueOf(obj.get("fassembly_clearance"));
				if(fac==null||"".equals(fac)||"null".equals(fac)){
					wj.setFassembly_clearance("-");//装配间隙
				}else{
					wj.setFassembly_clearance(String.valueOf(obj.get("fassembly_clearance")));//装配间隙
				}
				String fcd = String.valueOf(obj.get("fcarbon_depth"));
				if(fcd==null||"".equals(fcd)||"null".equals(fcd)){
					wj.setFcarbon_depth("-");//碳刨深度
				}else{
					wj.setFcarbon_depth(String.valueOf(obj.get("fcarbon_depth")));//碳刨深度
				}
				String fcw = String.valueOf(obj.get("fcarbon_width"));
				if(fcw==null||"".equals(fcw)||"null".equals(fcw)){
					wj.setFcarbon_width("-");//碳刨宽度
				}else{
					wj.setFcarbon_width(String.valueOf(obj.get("fcarbon_width")));//碳刨宽度
				}
				String fpht = String.valueOf(obj.get("fpost_heat_temperature"));
				if(fpht==null||"".equals(fpht)||"null".equals(fpht)){
					wj.setFpost_heat_temperature("-");//后热温度
				}else{
					wj.setFpost_heat_temperature(String.valueOf(obj.get("fpost_heat_temperature")));//后热温度
				}
				String faht = String.valueOf(obj.get("fafter_hot_time"));
				if(faht==null||"".equals(faht)||"null".equals(faht)){
					wj.setFafter_hot_time("-");//后热时间
				}else{
					wj.setFafter_hot_time(String.valueOf(obj.get("fafter_hot_time")));//后热时间
				}
				wj.setFwpslib_id(new BigInteger(String.valueOf(obj.get("wpsid"))));//工艺库id
				if(obj.get("flag").equals("0")){
					wjm.addTask(wj);
				}else{
					wjm.updateTask(wj);
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}

	//后台解析json
	@RequestMapping("/taskImportion")
	@ResponseBody
	public String taskImportion(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//客户端执行操作
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
		iutil.Authority(client);
		try{
			WeldedJunction wj = new WeldedJunction();
			String taskstr = request.getParameter("taskstr");
			ary = JSONArray.fromObject(taskstr);
			Object[] objects = null;
			for(int i=0;i<ary.size();i++){
				obj = ary.getJSONObject(i); 
				wj.setWeldedJunctionno(String.valueOf(obj.get("taskNo")));
				wj.setSerialNo(String.valueOf(obj.get("desc")));
				if(obj.get("welderNo")==null||obj.get("welderNo")==""){
					wj.setUnit(null);
				}else{
					wj.setUnit(String.valueOf(obj.get("welderNo")));
				}			
				if(obj.get("machineNo")==null||obj.get("machineNo")==""){
					wj.setExternalDiameter(null);
				}else{
					wj.setExternalDiameter(String.valueOf(obj.get("machineNo")));
				}
/*				Insframework itemid = new Insframework();
				itemid.setId(new BigInteger(String.valueOf(obj.get("id"))));*/
//				wj.setIid(new BigInteger(String.valueOf(obj.get("insId"))));
				wj.setStartTime(String.valueOf(obj.get("starttime")));
				wj.setEndTime(sdf.format(new Date()));
				System.out.println(sdf.format(new Date()));
/*				wjm.addTask(wj);*/
				String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"giveToServer\"}";
				String obj2 = "{\"TASKNO\":\""+obj.get("taskNo")+"\",\"WELDERNO\":\""+obj.get("welderNo")+"\",\"MACHINENO\":\""+obj.get("machineNo")+"\",\"STATUS\":\""+1+"\",\"TASKID\":\""+obj.get("taskid")+"\",\"WELDERID\":\""+obj.get("welderid")+"\",\"MACHINEID\":\""+obj.get("machineid")+"\",\"OPERATOR\":\""+user.getId()+"\",\"ID\":\""+obj.get("id")+"\",\"RESULT\":\"\",\"RESULTID\":\"\",\"STARTTIME\":\""+obj.get("starttime")+"\",\"ENDTIME\":\""+sdf.format(new Date())+"\"}";
				objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
				
			}
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/getFreeWelder")
	@ResponseBody
	public String getFreeWelder(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<Person> list = ps.getFreeWelder(page,str);
		long total = 0;
		if(list != null){
			PageInfo<Person> pageinfo = new PageInfo<Person>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				json.put("id", list.get(i).getId());
				json.put("name", list.get(i).getName());
				json.put("welderno", list.get(i).getWelderno());
				json.put("insname", list.get(i).getLevename());
				json.put("qualiname", list.get(i).getQualiname());
				json.put("back", list.get(i).getBack());
				json.put("owner", list.get(i).getInsid());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getFreeJunction")
	@ResponseBody
	public String getFreeJunction(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getFreeJunction(page, str);
		long total = 0;
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				json.put("id", list.get(i).getId());
				json.put("junctionno", list.get(i).getWeldedJunctionno());
				json.put("desc", list.get(i).getSerialNo());
				json.put("itemid", list.get(i).getInsfid());
				json.put("itemname", list.get(i).getUnit());
//				json.put("welderno", list.get(i).getPipelineNo());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getInsframework")
	@ResponseBody
	public String getInsframework(HttpServletRequest request,BigInteger id){
		JSONObject obj = new JSONObject();
		String serach="";
		try{
			int instype = insm.getTypeById(id);
			int bz=0;
			if(instype==20){
				
			}else if(instype==23){
				serach = "j.fitemId="+id;
			}else{
				List<Insframework> ls = insm.getInsIdByParent(id,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+id+")";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("success", serach);
		return obj.toString();
	}
	
	@RequestMapping("/getTaskview")
	@ResponseBody
	public String getTaskview(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String search = request.getParameter("search");
		List<Wps> wpsList = wps.gettaskview(page,search);
		long total = 0;
		if(wpsList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(wpsList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:wpsList){
				json.put("fid", wps.getFid());
				json.put("fproduct_drawing_no", wps.getFproduct_drawing_no());
				json.put("fproduct_number", wps.getFproduct_number());
				json.put("fproduct_name", wps.getFproduct_name());
				json.put("fproduct_version", wps.getFproduct_version());
				json.put("fprocessname", wps.getFprocessname());
				json.put("fwps_lib_version", wps.getFwps_lib_version());
				json.put("fstarttime", wps.getFstarttime());
				json.put("endtime", wps.getEndtime());
				json.put("fwpsnum", wps.getFwpsnum());
				json.put("dianame", wps.getDianame());
				json.put("fjunction", wps.getFjunction());
				json.put("fwelding_area", wps.getFwelding_area());
				json.put("fstep_number", wps.getFstep_number());
				json.put("weldername", wps.getWeldername());
				json.put("conname", wps.getConname());
				json.put("fspec_number", wps.getFemployee_id());
				json.put("fspec_name", wps.getFemployee_name());
				json.put("fspec_version", wps.getFemployee_version());
				json.put("fstep_name", wps.getFstep_name());
				json.put("fstep_version", wps.getFstep_version());
				json.put("fitem", wps.getFitem());
				int touch = wps.getFtorch();
				json.put("touch", touch);
				if(touch == 1) {
					json.put("touch_name", "完成");
				}else {
					json.put("touch_name", "未完成");
				}
				int flag = wps.getFlag();
				json.put("flag", flag);
				if(flag == 0) {
					json.put("flag_name", "自建");
				}else {
					json.put("flag_name", "MES");
				}
				int fstatus = wps.getFstatus();
				json.put("fstatus", fstatus);
				json.put("fback", wps.getFback());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	
	@RequestMapping("/getunstard")
	@ResponseBody
	public String getunstard(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String search = request.getParameter("search");
		List<Wps> wpsList = wps.getunstard(page,search);
		long total = 0;
		if(wpsList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(wpsList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:wpsList){
				json.put("fid", wps.getFid());
				json.put("fwelded_junction_no", wps.getFwelded_junction_no());//电子跟踪卡号
				json.put("fproduct_drawing_no", wps.getFproduct_drawing_no());//产品图号
				json.put("fproduct_name", wps.getFproduct_name());//产品名称
				if("".equals(wps.getFsuffix_number())||wps.getFsuffix_number()==null) {
					json.put("fproduct_version", wps.getFprefix_number()+wps.getFproduct_number());
				}else {
					json.put("fproduct_version", wps.getFprefix_number()+wps.getFproduct_number()+wps.getFsuffix_number());//产品序号
				}
				json.put("fprocessname", wps.getFprocessname());//规程编号
				json.put("fwps_lib_version", wps.getFwps_lib_version());//规程版本号
				json.put("fwpsnum", wps.getFwpsnum());//任务编号
				json.put("dianame", wps.getDianame());//产品图号
				json.put("femployee_num", wps.getFemployee_id());//工序号
				json.put("femployee_name", wps.getFemployee_name());//工序名称
				json.put("fjunction", wps.getFjunction());//焊缝编号
				json.put("fweldingarea", wps.getFwelding_area());//焊接部位
				json.put("fstep_number", wps.getFstep_number());//工步号
				json.put("fstep_name", wps.getFstep_name());//工步名称
				json.put("weldername", wps.getWeldername());//焊工号
				json.put("conname", wps.getConname());//焊机编号
				json.put("fitem", wps.getFitem());//组织机构
				json.put("ftime", getTimeStrBySecond(wps.getUnstandardtime()));
				int flag = wps.getFlag();
				json.put("flag", flag);//来源
				if(flag == 0) {
					json.put("flag_name", "自建");
				}else {
					json.put("flag_name", "MES");
				}
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getOperateArea")
	@ResponseBody
	public String getOperateArea(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONObject jsonb = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONArray aryb = new JSONArray();
		int instype = 0;
		try{
			List<Insframework> ls;
			String serach="";
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
			BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
			int bz=0;
			if(instype==20){
				ls = insm.getOperateArea(serach, 22);
				for(Insframework insf : ls ){
					json.put("id", insf.getId());
					json.put("name", insf.getName());
					ary.add(json);
				}
			}else if(instype==23){
				serach = "and i.fid="+userinsid;
				ls = insm.getOperateArea(serach, 23);
				json.put("id", insm.getParent(userinsid).getId());
				json.put("name", insm.getParent(userinsid).getName());
				ary.add(json);
				for(Insframework insf : ls ){
					jsonb.put("id", insf.getId());
					jsonb.put("name", insf.getName());
					aryb.add(jsonb);
				}
			}else if(instype==21){
				List<Insframework> lns = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : lns ){
					if(bz==0){
						serach=serach+"and (i.fid="+inns.getId();
					}else{
						serach=serach+" or i.fid="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or i.fid="+userinsid+")";
				ls = insm.getOperateArea(serach, 22);
				for(Insframework insf : ls ){
					json.put("id", insf.getId());
					json.put("name", insf.getName());
					ary.add(json);
				}
			}else{
				List<Insframework> lns = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : lns ){
					if(bz==0){
						serach=serach+"and (i.fid="+inns.getId();
					}else{
						serach=serach+" or i.fid="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or i.fid="+userinsid+")";
				ls = insm.getOperateArea(serach, 23);
				json.put("id", userinsid);
				json.put("name", insm.getInsframeworkById(userinsid));
				ary.add(json);
				for(Insframework insf : ls ){
					jsonb.put("id", insf.getId());
					jsonb.put("name", insf.getName());
					aryb.add(jsonb);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		obj.put("banzu", aryb);
		obj.put("type", instype);
		return obj.toString();
	}
	
	@RequestMapping("/getTeam")
	@ResponseBody
	public String getTeam(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			String serach="";
			serach = request.getParameter("searchStr");
			List<Insframework> ls = insm.getOperateArea(serach, 23);
			for(Insframework insf : ls ){
				json.put("id", insf.getId());
				json.put("name", insf.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getWelderAll")
	@ResponseBody
	public String getWelderAll(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			BigInteger uid = insm.getUserInsframework();
			List<Person> ls = ps.findAll(uid);
			for(Person ps : ls ){
				json.put("id", ps.getId());
				json.put("name", ps.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getWpslibAll")
	@ResponseBody
	public String getWpslibAll(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			List<Wps> ls = wps.getAllWpslib();
			for(Wps wps : ls ){
				json.put("id", wps.getInsid());
				json.put("name", wps.getInsname());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 工艺通过
	 * @Description
	 * @author Bruce
	 * @date 2020年2月26日下午2:59:33
	 * @param request
	 * @param wps
	 * @return
	 */
	@RequestMapping("/passReview")
	@ResponseBody
	public String passReview(HttpServletRequest request,@ModelAttribute WeldedJunction weldtask){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			String value = request.getParameter("value");
			wjm.passReview(fid,value);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 工艺驳回
	 * @Description
	 * @author Chen
	 * @date 2020年3月2日下午2:59:25
	 * @param request
	 * @param wps
	 * @return
	 */
	@RequestMapping("/turnDown")
	@ResponseBody
	public String turnDown(HttpServletRequest request,@ModelAttribute WeldedJunction weldtask){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			String downReason = request.getParameter("downReason");
			weldtask.setInsfid(new BigInteger(fid));
			weldtask.setFback(downReason);
			wjm.turnDown(weldtask);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 获取工艺规程下拉框
	 * @return
	 */
	@RequestMapping("/getWpsSelect")
	@ResponseBody
	public String getWpsSelect(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
//			List<Insframework> list = im.getOperateArea(null,23);
//			for(Insframework i:list){
//				json.put("id", i.getId());
//				json.put("name", i.getName());
//				ary.add(json);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getCardList")
	@ResponseBody
	public String getCardList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String search = request.getParameter("search");
		List<WeldedJunction> cardList = wjm.getCardList(page,search);
		long total = 0;
		if(cardList != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(cardList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction wjm:cardList){
				json.put("fid", wjm.getId());
				json.put("fwelded_junction_no", wjm.getWeldedJunctionno());
				json.put("ftask_no", wjm.getFtask_no());
				json.put("fitemId", wjm.getIid());
				json.put("fitemName", wjm.getIname());
				json.put("fwps_lib_name", wjm.getFwps_lib_name());
				json.put("fwps_lib_version", wjm.getFwps_lib_version());
				json.put("fproduct_drawing_no", wjm.getFproduct_drawing_no());
				json.put("fproduct_name", wjm.getFproduct_name());
				int flag = wjm.getFlag();
				json.put("flag", flag);
				if(flag == 0) {
					json.put("flag_name", "自建");
				}else {
					json.put("flag_name", "MES");
				}
				json.put("fstatus", wjm.getFstatus());
				json.put("fback", wjm.getFback());
				json.put("dyne", wjm.getDyne());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addCard")
	@ResponseBody
	public String addCard(HttpServletRequest request,@ModelAttribute WeldedJunction wj){
		JSONObject obj = new JSONObject();
		try{
			String fwelded_junction_no = request.getParameter("fwelded_junction_no");
			String ftask_no = request.getParameter("ftask_no");
			String fitemId = request.getParameter("fitemId");
			String fwps_lib_id = request.getParameter("fwps_lib_id");
			String cardFlag = request.getParameter("cardFlag");
			int fproduct_number = Integer.parseInt(request.getParameter("fproduct_number"));
			String prefix_number = request.getParameter("fprefix_number");
			String fsuffix_number = request.getParameter("fsuffix_number");
			int init_number = Integer.parseInt(request.getParameter("init_number"));
			wj.setWeldedJunctionno(fwelded_junction_no);
			wj.setFtask_no(ftask_no);
			wj.setIid(new BigInteger(fitemId));
			wj.setFwpslib_id(new BigInteger(fwps_lib_id));
			wj.setFlag(Integer.parseInt(cardFlag));
			wjm.addCard(wj);
//			BigInteger id = wj.getId();
			for(int i=0;i<fproduct_number;i++) {
				wj.setFprefix_number(prefix_number);
				wj.setFsuffix_number(fsuffix_number);
				wj.setFproduct_number(String.valueOf(init_number+i));
				wjm.addProductNum(wj);
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateCard")
	@ResponseBody
	public String updateCard(HttpServletRequest request,@ModelAttribute WeldedJunction wj){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			String fwelded_junction_no = request.getParameter("fwelded_junction_no");
			String ftask_no = request.getParameter("ftask_no");
			String fitemId = request.getParameter("fitemId");
			String fwps_lib_id = request.getParameter("fwps_lib_id");
			String cardFlag = request.getParameter("cardFlag");
			int fproduct_number = Integer.parseInt(request.getParameter("fproduct_number"));
			String prefix_number = request.getParameter("fprefix_number");
			String fsuffix_number = request.getParameter("fsuffix_number");
			int init_number = Integer.parseInt(request.getParameter("init_number"));
			wj.setId(new BigInteger(fid));
			wj.setWeldedJunctionno(fwelded_junction_no);
			wj.setFtask_no(ftask_no);
			wj.setIid(new BigInteger(fitemId));
			wj.setFwpslib_id(new BigInteger(fwps_lib_id));
			wj.setFlag(Integer.parseInt(cardFlag));
			wjm.updateCard(wj);
			wjm.deleteProduct(fid);
//			BigInteger id = wj.getId();
			for(int i=0;i<fproduct_number;i++) {
				wj.setFprefix_number(prefix_number);
				wj.setFsuffix_number(fsuffix_number);
				wj.setFproduct_number(String.valueOf(init_number+i));
				wjm.addProductNum(wj);
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/deleteCard")
	@ResponseBody
	public String deleteCard(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			wjm.deleteCard(fid);
			wjm.deleteProduct(fid);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/getProductList")
	@ResponseBody
	public String getProductList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String search = request.getParameter("search");
		List<WeldedJunction> productList = wjm.getProductList(page,search);
		long total = 0;
		if(productList != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(productList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction wjm:productList){
				json.put("fid", wjm.getId());
				if("".equals(wjm.getFsuffix_number())||wjm.getFsuffix_number()==null) {
					json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number());
				}else {
					json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number()+wjm.getFsuffix_number());
				}
				json.put("fwps_lib_name", wjm.getFwps_lib_name());
				json.put("fwps_lib_version", wjm.getFwps_lib_version());
				json.put("fstatus", wjm.getFstatus());
				json.put("machineid",wjm.getMachid());
				json.put("fwps_lib_id", wjm.getFwpslib_id());
				json.put("fproduct_drawing_no", wjm.getFproduct_drawing_no());
				json.put("fproduct_name", wjm.getFproduct_name());
				json.put("fproduct_version", wjm.getFproduct_version());
				json.put("flag", wjm.getFlag());
				if(wjm.getFstatus()==null){
					json.put("fstatus", "2");
				}
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getProductByCardid")
	@ResponseBody
	public String getProductByCardid(HttpServletRequest request){
		String fid = request.getParameter("fid");
		WeldedJunction product = wjm.getProductByCardid(fid);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			json.put("fprefix_number", product.getFprefix_number());
			if(!"".equals(product.getFsuffix_number())&&product.getFsuffix_number()!=null) {
				json.put("fsuffix_number", product.getFsuffix_number());
			}
			json.put("fproduct_number", product.getId());
			json.put("finit_number", product.getFproduct_number());
			json.put("fwps_lib_id", product.getFwpslib_id());
			ary.add(json);
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("row", ary);
		return obj.toString();
	}
	
	@RequestMapping("/cardChangeWps")
	@ResponseBody
	public String cardChangeWps(HttpServletRequest request,@ModelAttribute WeldedJunction wj){
		JSONObject obj = new JSONObject();
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String userId = String.valueOf(user.getId());
			String fid = request.getParameter("fid");
			String wpsId = request.getParameter("wpsId");
			wj.setId(new BigInteger(fid));
			wj.setFwpslib_id(new BigInteger(wpsId));
			String oldWpsid = wjm.getWpsIdByCardId(fid);
			wjm.updateProductNum(wj);
			wjm.addProductWpsHistory(fid,oldWpsid,userId,time);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
		
	@RequestMapping("/getProductWpsHistory")
	@ResponseBody
	public String getProductWpsHistory(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String search = request.getParameter("search");
		List<WeldedJunction> list = wjm.getProductWpsHistory(page,search);
		long total = 0;
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction wjm:list){
				json.put("fwelded_junction_no", wjm.getWeldedJunctionno());
				json.put("fwps_lib_name", wjm.getFwps_lib_name());
				json.put("fwps_lib_version", wjm.getFwps_lib_version());
				if("".equals(wjm.getFsuffix_number())||wjm.getFsuffix_number()==null) {
					json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number());
				}else{
					json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number()+wjm.getFsuffix_number());
				}
				json.put("fproduct_drawing_no", wjm.getFproduct_drawing_no());
				json.put("fproduct_name", wjm.getFproduct_name());
				json.put("fproduct_version", wjm.getFproduct_version());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/cardvalidate")
	@ResponseBody
	private String enovalidate(@RequestParam String cardName){
		boolean data = true;
		int count = wjm.getCardCount(cardName);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/taskvalidate")
	@ResponseBody
	private String taskvalidate(@RequestParam String taskName){
		boolean data = true;
		int count = wjm.getTaskCount(taskName);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	public String getTimeStrBySecond(BigInteger timeParam ) {
		if(timeParam == null){
			return "00:00:00";
		}
		BigInteger[] str = timeParam.divideAndRemainder(new BigInteger("60"));//divideAndRemainder返回数组。第一个是商第二个时取模
		BigInteger second = str[1];
		BigInteger minuteTemp = timeParam.divide(new BigInteger("60"));//subtract：BigInteger相减，multiply：BigInteger相乘，divide : BigInteger相除
        if (minuteTemp.compareTo(new BigInteger("0"))>0) {//compareTo：比较BigInteger类型的大小，大则返回1，小则返回-1 ，等于则返回0
        	BigInteger[] minstr = minuteTemp.divideAndRemainder(new BigInteger("60"));
    		BigInteger minute = minstr[1];
    		BigInteger hour = minuteTemp.divide(new BigInteger("60"));
            if (hour.compareTo(new BigInteger("0"))>0) {
                return (hour.compareTo(new BigInteger("9"))>0 ? (hour + "") : ("0" + hour)) + ":" + (minute.compareTo(new BigInteger("9"))>0 ? (minute + "") : ("0" + minute))
                        + ":" + (second .compareTo(new BigInteger("9"))>0 ? (second + "") : ("0" + second));
            } else {
                return "00:" + (minute.compareTo(new BigInteger("9"))>0 ? (minute + "") : ("0" + minute)) + ":"
                        + (second .compareTo(new BigInteger("9"))>0 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:00:" + (second .compareTo(new BigInteger("9"))>0 ? (second + "") : ("0" + second));
        }
	}
}
