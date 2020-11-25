package com.spring.controller;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.MyUser;
import com.spring.model.WeldedJunction;
import com.spring.page.Page;
import com.spring.service.InsframeworkService;
import com.spring.service.LiveDataService;
import com.spring.service.WeldedJunctionService;
import com.spring.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/weldedjunction", produces = {"text/json;charset=UTF-8"})
public class WeldedJunctionControll {
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
    IsnullUtil iutil = new IsnullUtil();

    @RequestMapping("/goWeldedJunction")
    public String goWeldedJunction() {
        return "weldingjunction/weldedjunction";
    }

    @RequestMapping("/goAddWeldedJunction")
    public String goAddWeldedJunction() {
        return "weldingjunction/addweldedjunction";
    }

    @RequestMapping("/goEditWeldedJunction")
    public String goEditWeldedJunction(HttpServletRequest request) {
        BigInteger id = new BigInteger(request.getParameter("id"));
        WeldedJunction wj = wjm.getWeldedJunctionById(id);
        wj.setWeldedJunctionno(wj.getWeldedJunctionno());
        request.setAttribute("wj", wj);
        return "weldingjunction/editweldedjunction";
    }

    @RequestMapping("/goRemoveWeldedJunction")
    public String goRemoveWeldedJunction(HttpServletRequest request) {
        BigInteger id = new BigInteger(request.getParameter("id"));
        WeldedJunction wj = wjm.getWeldedJunctionById(id);
        wj.setWeldedJunctionno(wj.getWeldedJunctionno());
        request.setAttribute("wj", wj);
        return "weldingjunction/removeweldedjunction";
    }

    /**
     * 焊工历史曲线页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/getWeldJun")
    public String getWeldJun(HttpServletRequest request) {
        if (request.getParameter("fid") != null && request.getParameter("fid") != "") {
            request.setAttribute("welderid", request.getParameter("fid"));
        }
        if (iutil.isNull(request.getParameter("wjno"))) {
            request.setAttribute("wjno", request.getParameter("wjno"));
        }
        return "td/HistoryCurve";
    }

    @RequestMapping("/goShowMoreJunction")
    public String goShowMoreJunction(HttpServletRequest request, @RequestParam String id) {
        try {
            WeldedJunction wj = wjm.getWeldedJunctionById(new BigInteger(id));
            wj.setWeldedJunctionno(wj.getWeldedJunctionno());
            request.setAttribute("wj", wj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "weldingjunction/showmore";
    }

    /**
     * 焊缝历史曲线列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/getWeldedJunctionList")
    @ResponseBody
    public String getWeldedJunctionList(HttpServletRequest request) {
        pageIndex = Integer.parseInt(request.getParameter("page"));
        pageSize = Integer.parseInt(request.getParameter("rows"));
        String serach = request.getParameter("searchStr");

        page = new Page(pageIndex, pageSize, total);

        List<WeldedJunction> list = null;
        try {
            list = wjm.getWeldedJunctionAll(page, serach);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

        long total = 0;

        if (list != null) {
            PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
            total = pageinfo.getTotal();
        }

        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            for (WeldedJunction w : list) {
                json.put("id", w.getId());
                json.put("weldedJunctionno", w.getWeldedJunctionno());
                json.put("serialNo", w.getSerialNo());
                json.put("pipelineNo", w.getPipelineNo());
                json.put("roomNo", w.getRoomNo());
                json.put("unit", w.getUnit());
                json.put("area", w.getArea());
                json.put("systems", w.getSystems());
                json.put("children", w.getChildren());
                json.put("externalDiameter", w.getExternalDiameter());
                json.put("wallThickness", w.getWallThickness());
                json.put("dyne", w.getDyne());
                json.put("specification", w.getSpecification());
                json.put("maxElectricity", w.getMaxElectricity());
                json.put("minElectricity", w.getMinElectricity());
                json.put("maxValtage", w.getMaxValtage());
                json.put("minValtage", w.getMinValtage());
                json.put("material", w.getMaterial());
                json.put("nextexternaldiameter", w.getNextexternaldiameter());
                json.put("itemname", w.getIname());
                json.put("itemid", w.getIid());
                json.put("startTime", w.getStartTime());
                json.put("endTime", w.getEndTime());
               // json.put("creatTime", w.getCreatTime());
               // json.put("updateTime", w.getUpdateTime());
                json.put("updatecount", w.getUpdatecount());
                json.put("nextwall_thickness", w.getNextwall_thickness());
                json.put("next_material", w.getNext_material());
                json.put("electricity_unit", w.getElectricity_unit());
                json.put("valtage_unit", w.getValtage_unit());
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getJunctionByWelder")
    @ResponseBody
    public String getJunctionByWelder(HttpServletRequest request) {
        pageIndex = Integer.parseInt(request.getParameter("page"));
        pageSize = Integer.parseInt(request.getParameter("rows"));
        String welder = request.getParameter("welder");
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        WeldDto dto = new WeldDto();
        if (iutil.isNull(time1)) {
            dto.setDtoTime1(time1);
        }
        if (iutil.isNull(time2)) {
            dto.setDtoTime2(time2);
        }

        page = new Page(pageIndex, pageSize, total);
        List<WeldedJunction> list = wjm.getJunctionByWelder(page, welder, dto);
        long total = 0;

        if (list != null) {
            PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
            total = pageinfo.getTotal();
        }

        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            for (WeldedJunction w : list) {
                json.put("weldedJunctionno", w.getWeldedJunctionno());
                json.put("maxElectricity", w.getMaxElectricity());
                json.put("minElectricity", w.getMinElectricity());
                json.put("maxValtage", w.getMaxValtage());
                json.put("minValtage", w.getMinValtage());
                json.put("itemname", w.getIname());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/addWeldedJunction")
    @ResponseBody
    public String addWeldedJunction(HttpServletRequest request) {
        WeldedJunction wj = new WeldedJunction();
        JSONObject obj = new JSONObject();
        try {
            MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            wj.setCreater(new BigInteger(user.getId() + ""));
            wj.setUpdater(new BigInteger(user.getId() + ""));
            wj.setWeldedJunctionno(request.getParameter("weldedJunctionno"));
            wj.setSerialNo(request.getParameter("serialNo"));
            wj.setUnit(request.getParameter("unit"));
            wj.setArea(request.getParameter("area"));
            wj.setSystems(request.getParameter("systems"));
            wj.setChildren(request.getParameter("children"));
            wj.setDyne(0);
            wj.setSpecification(request.getParameter("specification"));
            wj.setPipelineNo(request.getParameter("pipelineNo"));
            wj.setRoomNo(request.getParameter("roomNo"));
            wj.setExternalDiameter(request.getParameter("externalDiameter"));
            wj.setNextexternaldiameter(request.getParameter("nextexternaldiameter"));
            wj.setWallThickness(request.getParameter("wallThickness"));
            wj.setNextwall_thickness(request.getParameter("nextwall_thickness"));
            wj.setMaterial(request.getParameter("material"));
            wj.setNext_material(request.getParameter("next_material"));
            wj.setMaxElectricity(Double.parseDouble(request.getParameter("maxElectricity")));
            wj.setMinElectricity(Double.parseDouble(request.getParameter("minElectricity")));
            wj.setMaxValtage(Double.parseDouble(request.getParameter("maxValtage")));
            wj.setMinValtage(Double.parseDouble(request.getParameter("minValtage")));
            wj.setElectricity_unit(request.getParameter("electricity_unit"));
            wj.setValtage_unit(request.getParameter("valtage_unit"));
            String starttime = request.getParameter("startTime");
            String endtime = request.getParameter("endTime");
            if (iutil.isNull(starttime)) {
                wj.setStartTime(starttime);
            }
            if (iutil.isNull(endtime)) {
                wj.setEndTime(endtime);
            }
            String itemid = request.getParameter("itemid");
            if (iutil.isNull(itemid)) {
                wj.setInsfid(new BigInteger(itemid));
            }
            wjm.addJunction(wj);
            obj.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
        }
        return obj.toString();
    }


    @RequestMapping("/editWeldedJunction")
    @ResponseBody
    public String editWeldedJunction(HttpServletRequest request) {
        WeldedJunction wj = new WeldedJunction();
        JSONObject obj = new JSONObject();
        try {
            MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            wj.setUpdater(new BigInteger(user.getId() + ""));
            wj.setId(new BigInteger(request.getParameter("id")));
            wj.setWeldedJunctionno(request.getParameter("weldedJunctionno"));
            wj.setSerialNo(request.getParameter("serialNo"));
            wj.setUnit(request.getParameter("unit"));
            wj.setArea(request.getParameter("area"));
            wj.setSystems(request.getParameter("systems"));
            wj.setChildren(request.getParameter("children"));
            wj.setDyne(0);
            wj.setSpecification(request.getParameter("specification"));
            wj.setPipelineNo(request.getParameter("pipelineNo"));
            wj.setRoomNo(request.getParameter("roomNo"));
            wj.setExternalDiameter(request.getParameter("externalDiameter"));
            wj.setNextexternaldiameter(request.getParameter("nextexternaldiameter"));
            wj.setWallThickness(request.getParameter("wallThickness"));
            wj.setNextwall_thickness(request.getParameter("nextwall_thickness"));
            wj.setMaterial(request.getParameter("material"));
            wj.setNext_material(request.getParameter("next_material"));
            wj.setMaxElectricity(Double.parseDouble(request.getParameter("maxElectricity")));
            wj.setMinElectricity(Double.parseDouble(request.getParameter("minElectricity")));
            wj.setMaxValtage(Double.parseDouble(request.getParameter("maxValtage")));
            wj.setMinValtage(Double.parseDouble(request.getParameter("minValtage")));
            wj.setElectricity_unit(request.getParameter("electricity_unit"));
            wj.setValtage_unit(request.getParameter("valtage_unit"));
            String starttime = request.getParameter("startTime");
            String endtime = request.getParameter("endTime");
            if (iutil.isNull(starttime)) {
                wj.setStartTime(starttime);
            }
            if (iutil.isNull(endtime)) {
                wj.setEndTime(endtime);
            }
            String itemid = request.getParameter("itemid");
            if (iutil.isNull(itemid)) {
                wj.setInsfid(new BigInteger(itemid));
            }
            wjm.updateJunction(wj);
            obj.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
        }
        return obj.toString();
    }


    @RequestMapping("/removeWeldedJunction")
    @ResponseBody
    public String removeWeldedJunction(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        try {
            wjm.deleteJunction(new BigInteger(request.getParameter("id")));
            obj.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            obj.put("success", false);
            obj.put("errorMsg", e.getMessage());
        }
        return obj.toString();
    }

    @RequestMapping("/wjNoValidate")
    @ResponseBody
    private String wjNoValidate(@RequestParam String wjno) {
        boolean data = true;
        int count = wjm.getWeldedjunctionByNo(wjno);
        if (count > 0) {
            data = false;
        }
        return data + "";
    }

    @RequestMapping("/getCouneByTaskid")
    @ResponseBody
    private String getCouneByTaskid(@RequestParam BigInteger taskid, @RequestParam BigInteger type) {
        boolean data = true;
        if (String.valueOf(type) == "null" || String.valueOf(type) == null) {
            type = null;
        }
        int count = wjm.getCountByTaskid(taskid, type);
        if (count > 0) {
            data = false;
        }
        return data + "";
    }

    @RequestMapping("/getWeldingJun")
    @ResponseBody
    public String getWeldingJun(HttpServletRequest request) {
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        String parentId = request.getParameter("parent");
        String wjno = request.getParameter("wjno");
        String welderid = request.getParameter("welderid");
        WeldDto dto = new WeldDto();
        if (!iutil.isNull(parentId)) {
            //数据权限处理
            BigInteger uid = lm.getUserId(request);
            String afreshLogin = (String) request.getAttribute("afreshLogin");
            if (iutil.isNull(afreshLogin)) {
                return "0";
            }
            int types = insm.getUserInsfType(uid);
            if (types == 21) {
                parentId = insm.getUserInsfId(uid).toString();
            }
        }
        BigInteger parent = null;
        if (iutil.isNull(time1)) {
            dto.setDtoTime1(time1);
        }
        if (iutil.isNull(wjno)) {
            dto.setSearch(wjno);//用来保存任务编号
        }
        if (iutil.isNull(time2)) {
            dto.setDtoTime2(time2);
        }
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
        pageIndex = Integer.parseInt(request.getParameter("page"));
        pageSize = Integer.parseInt(request.getParameter("rows"));

        page = new Page(pageIndex, pageSize, total);
        List<WeldedJunction> list = wjm.getJMByWelder(page, dto, welderid);
        long total = 0;

        if (list != null) {
            PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
            total = pageinfo.getTotal();
        }

        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            for (WeldedJunction w : list) {
                json.put("firsttime", wjm.getFirsttime(dto, w.getMachid(), welderid, w.getWeldedJunctionno()));
                json.put("lasttime", wjm.getLasttime(dto, w.getMachid(), welderid, w.getWeldedJunctionno()));
                json.put("fweldingtime", new DecimalFormat("0.0000").format((float) Integer.valueOf(w.getCounts().toString()) / 3600));
                json.put("id", w.getId());
                json.put("machid", w.getMachid());
                json.put("machine_num", w.getMachine_num());
                json.put("weldedJunctionno", w.getWeldedJunctionno());
                json.put("dyne", w.getDyne());
                json.put("maxElectricity", w.getMaxElectricity());
                json.put("minElectricity", w.getMinElectricity());
                json.put("maxValtage", w.getMaxValtage());
                json.put("minValtage", w.getMinValtage());
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getSwDetail")
    @ResponseBody
    public String getSwDetail(HttpServletRequest request) {
        String taskno = request.getParameter("taskno");
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        WeldDto dto = new WeldDto();
        if (iutil.isNull(time1)) {
            dto.setDtoTime1(time1);
        }
        if (iutil.isNull(time2)) {
            dto.setDtoTime2(time2);
        }
        pageIndex = Integer.parseInt(request.getParameter("page"));
        pageSize = Integer.parseInt(request.getParameter("rows"));

        page = new Page(pageIndex, pageSize, total);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        time = time.substring(0, 13) + ":00:00";
        List<WeldedJunction> list = wjm.getSwDetail(page, taskno, time, dto);
        long total = 0;

        if (list != null) {
            PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
            total = pageinfo.getTotal();
        }

        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            for (WeldedJunction w : list) {
                json.put("fsolder_layer", w.getSerialNo());
                json.put("fweld_bead", w.getRoomNo());
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getWeldTask")
    @ResponseBody
    public String getWeldTask(HttpServletRequest request){
        List<WeldedJunction> list = wjm.getWeldedJunction("");
//        BigInteger userInsframework = insm.getUserInsframework();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        try{
            for(WeldedJunction w:list){
                json.put("id", w.getId());
                json.put("weldedJunctionno", w.getWeldedJunctionno());
                ary.add(json);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }
}