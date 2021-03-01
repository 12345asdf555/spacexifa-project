package com.shth.spacexifa.dao;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.Wps;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface WpsMapper {
	List<Wps> findAll(@Param("parent")BigInteger parent,@Param("str")String str);
	List<Wps> findAllSpe(@Param("machine")BigInteger machine,@Param("chanel")BigInteger chanel);
	List<Wps> findSpe(@Param("machine")BigInteger machine,@Param("chanel")String chanel);
	List<Wps> AllSpe(@Param("machine")BigInteger machine,@Param("chanel")String chanel);
	List<Wps> findHistory(@Param("parent")BigInteger parent);
	void save(Wps wps);
	void give(Wps wps);
	BigInteger findByUid(long uid);
	void update(Wps wps);
	int getUsernameCount(@Param("fwpsnum")String fwpsnum);
	Wps findById(BigInteger fid);
	Wps findSpeById(BigInteger fid);
	void delete(BigInteger fid);
	String findIpById(BigInteger fid);
	List<Wps> findStepFile(@Param("condition")String condition);
	void deleteHistory(BigInteger fid);
	int findCount(@Param("machine")BigInteger machine, @Param("chanel")String chanel);
	void saveSpe(Wps wps);
	void updateSpe(Wps wps);
	List<Wps> getWpslibList(@Param("search")String search);
	public List<Wps> findSpecificationByFid(@Param("wpslibid") BigInteger wpslibid);
	List<Wps> getLibraryJunction(@Param("library_id")String library_id);
	List<Wps> getMainwpsList(@Param("parent")BigInteger parent);
	int getWpslibNameCount(@Param("wpsName")String wpsName);
	void saveWpslib(Wps wps);
	void updateWpslib(Wps wps);
	List<Wps> getWpslibStatus();
	void deleteWpslib(@Param("fid")BigInteger fid);
	void deleteWpsBelongLib(@Param("fid")BigInteger fid);
	void deleteMainWps(@Param("fid")BigInteger fid);
	int getCountByWpslibidChanel(@Param("wpslibid")BigInteger wpslibid,@Param("chanel")int chanel);
	List<Wps> getTaskstatus(@Param("search1")String search1);
	List<Wps> getWeldTime(@Param("search")String search);
	List<Wps> getSxWpsList(@Param("parent")BigInteger parent);
	boolean saveSxWps(Wps wps);
	boolean saveSxWpsHistory(Wps wps);
	boolean saveOtcWpsHistory(Wps wps);
	boolean editSxWps(Wps wps);
	List<Wps> getWpslibMachineHistoryList(@Param("machineNum")String machineNum, @Param("wpslibName")String wpslibName, @Param("dto")WeldDto dto);
	Wps getSxDetail(@Param("machineId")String machineId, @Param("chanel")String chanel, @Param("time")String time);
	Wps getOtcDetail(@Param("machineId")String machineId, @Param("chanel")String chanel, @Param("time")String time);
	String getIdByWpslibname(@Param("wpslibname")String wpslibname);
	
	List<Wps> getFnsDetail(@Param("machine")BigInteger machine, @Param("chanel")String chanel);
	List<Wps> getFnsJobList(@Param("machine")BigInteger machine);
	void addJob(Wps wps);
	void updateJob(Wps wps);
	void deleteJob(@Param("machine")String machine,@Param("chanel")String chanel);
	List<String> getTpsiMaterial();
	List<String> getTpsiGas();
	List<String> getTpsiWire();
	
	int getCountByWpsidAndLayerroad(@Param("wpsid")String wpsid,@Param("layer")String layer,@Param("road")String road);
	void addWpsDetail(Wps wps);
	void updateWpsDetail(Wps wps);
	void updateWpsDetailById(Wps wps);
	
	List<Wps> getAllWpslib();
	List<Wps> getWpsList(@Param("search")String search);
	List<Wps> gettaskview(@Param("search")String search);
	List<Wps> getEmployee(@Param("fid")String fid);
	List<Wps> getStep(@Param("employeeId")String employeeId);
	List<Wps> getEmployeStep(@Param("stepId")String stepId,@Param("productId")String productId,@Param("junctionId")String junctionId);
	List<Wps> getJunctionStep(@Param("search1")String search1,@Param("productId")String productId);
	List<Wps> getJunction(@Param("stepId")String stepId);
	List<Wps> getJunctionWeld(@Param("junctionId")String junctionId);
	List<Wps> getJunctionByStepid(@Param("stepId")String stepId,@Param("productId")String productId);
	List<Wps> getEmployee1(@Param("employ_id")String employ_id,@Param("productId")String productId);
	List<Wps> getJunctionByStepid1(@Param("stepId")String stepId);
	List<Wps> getDetail(@Param("stepId")String stepId);


	int addWpsLibrary(Wps wps);
	int updateWpsLibrary(Wps wps);
	int deleteWpsByIds(List<Integer> ids);

	void addWps(Wps wps);
	void updateWps(Wps wps);
	void deleteWps(String fid);
	void deleteWpsEmp(String fid);
	void deleteWpsEmpStep(String fid);
	void deleteWpsEmpStepJun(String fid);
	void deleteWpsEmpStepSpe(String fid);
	void deleteWpsEmpSJ(String fid);
	void addEmployee(Wps wps);
	void updateEmployee(Wps wps);
	void deleteEmployee(String fid);
	void deleteEmployeeStep(String fid);
	void deleteEmployeeStepJun(String fid);
	void deleteEmployeeSJ(String fid);
	void deleteEmployeeStepSpe(String fid);
	void addStep(Wps wps);
	void updateStep(Wps wps);
	void deleteStep(String fid);
	void deleteStepJun(String fid);
	void deleteStepSpe(String fid);
	void addJunction(Wps wps);
	void updateJunction(Wps wps);
	void deleteJunction(String fid);
	void addDetail(Wps wps);
	void updateDetail(Wps wps);
	void deleteDetail(String fid);
	void passReview(@Param("fid")String fid,@Param("value")String value);
	void turnDown(Wps wps);
	List<Wps> getWpsCombobox();
	List<Wps> gettrackcard();
	int getProcudtCount(@Param("pdn")String pdn, @Param("procudt")String procudt);
	int getWpsversionCount(@Param("wln")String wln, @Param("wpsversion")String wpsversion, @Param("pdn")String pdn, @Param("pv")String pv);
	void addEmployee1(Wps wps);
	void addStep1(Wps wps);
	void addWps1(Wps wps);
	List<Wps> getWeldwps(@Param("wps_lib_id")String wps_lib_id);
	List<Wps> getstepall();
	List<Wps> getEmployee1(@Param("employ_id")String employ_id);
	List<Wps> getunstard(@Param("search")String search);
	void finishWork(@Param("fid")String fid);
	List<Wps> getHistoryDatagridList(@Param("search")String search);
	List<Wps> getEmployeeByJun(@Param("search")String search);
	List<Wps> getJunctionByWpsid(@Param("search")String search);
	List<Wps> getTaskParameter(@Param("search")String search,@Param("search2")String search2);
	void addTaskresultRow(Wps wps);
	void updateTaskresult(Wps wps);
	void overTaskresult(Wps wps);
	int overCard(Wps wps);
	void addWeldpramatia(Wps wps);
	void addStepJunction(@Param("stepId")String stepId,@Param("junctionId")String junctionId );
	void deleteStepJunction(@Param("stepId")String stepId,@Param("search")String search);

	List<Wps> findJobSetNumber(@Param("dto") WeldDto dto);
	Wps findWeldedJunctionByNum(@Param("workticket_num") String workticket_num);
	Wps findCraftByJunctionId(@Param("junction_id") BigInteger junction_id);

	Wps findTaskResultById(@Param("machineId") BigInteger machineId,@Param("cardId") BigInteger cardId);
	Wps findTaskResultByWelderId(@Param("wederId") BigInteger wederId);

	int updateTaskResultById(Wps wps);
	int updateStatusByFids(@Param("list")List<String> list,@Param("type") int type);
	List<Wps> findAllWorkNumer();
}