package com.spring.service;

import com.spring.model.WeldingMachine;
import com.spring.page.Page;

import java.math.BigInteger;
import java.util.List;

public interface WeldingMachineService {
	
	/**
	 * 查询所有焊机信息:分页
	 */
	List<WeldingMachine> getWeldingMachineAll(Page page,BigInteger parent,String str);
	List<WeldingMachine> AllMachine(Page page,BigInteger parent);
	/**
	 * 查询所有焊机信息
	 */
	List<WeldingMachine> getWeldingMachine(String str);
	
	/**
	 * 新增设备
	 */
	void addWeldingMachine(WeldingMachine wm);
	
	/**
	 * 修改设备
	 */
	void editWeldingMachine(WeldingMachine wm);
	
	/**
	 * 查询所有焊机型号信息
	 */
	List<WeldingMachine> findAllweldmachine();
	
	
	/**
	 * 查询当前组织机构下的焊机型号信息
	 */
	List<WeldingMachine> getMachines(BigInteger insid);
	
	/**
	 * 删除设备
	 * @param wid
	 */
	void deleteWeldingChine(BigInteger wid);
	/**
	 * 删除对应的工艺记录
	 */
	void deleteHistory(BigInteger wid);
	/**
	 * 根据焊机编号查找id
	 * @return
	 */
	BigInteger getWeldingMachineByEno(String eno);
	
	/**
	 * 判断焊机编号是否存在
	 * @param eno
	 * @return
	 */
	int getEquipmentnoCount(String eno);
	
	/**
	 * 判断采集序号是否存在
	 * @param gatherid
	 * @return
	 */
	int getGatheridCount(BigInteger itemid,String gather);
	
	/**
	 * 根据id查找记录
	 * @param wid
	 * @return
	 */
	WeldingMachine getWeldingMachineById(BigInteger wid);
	
	/**
	 * 根据项目名称获取项目id
	 * @param name
	 * @return
	 */
	BigInteger getInsframeworkByName(String name);
	
	/**
	 * 获取某厂商下的焊机总数
	 * @param mid 厂商id
	 * @return
	 */
	BigInteger getMachineCountByManu(BigInteger mid,BigInteger id);
	List<WeldingMachine> getAllMachine();
	/**
	 * 根据组织机构获取对应焊机
	 */
	List<WeldingMachine> getMachineByIns(BigInteger id);
	
	/**
	 * 厂商焊机型号绑定
	 */
	void addfactoryType(WeldingMachine wm);
	
	/**
	 * 删除旧厂商焊机型号绑定
	 */
	void deletefactory(BigInteger statusId);
	
	/**
	 * 获取所有焊机及其对应的采集模块
	 * @return
	 */
	List<WeldingMachine> getMachineGather();
	
	/**
	 * 获取焊机及其对应的型号
	 */
	List<WeldingMachine> getMachineModel();
	
	void resetGatherMachineid(String machineId);
	void updateGather(String machineId,String gatherId);

	/**
	 * 根据采集模块id查询焊机信息
	 * @return
	 */
	int findMachineByGatherId(String gather_id);

	/**
	 * 根据手持终端IP地址查询焊机
	 */
	List<WeldingMachine> findMachineByFip(String ip);

	/**
	 * 根据采集编号查询焊机信息
	 */
	WeldingMachine findMachineByGatherNo(String gather_no);
}