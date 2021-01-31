package com.spring.service.impl;

import com.github.pagehelper.PageHelper;
import com.spring.dao.InsframeworkMapper;
import com.spring.dao.WeldingMachineMapper;
import com.spring.model.WeldingMachine;
import com.spring.page.Page;
import com.spring.service.WeldingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class WeldingMachineServiceImpl implements WeldingMachineService {

	@Autowired
	private WeldingMachineMapper wmm;

	@Autowired
	private InsframeworkMapper im;

	@Override
	public List<WeldingMachine> getWeldingMachineAll(Page page,String parent,String str) {
		PageHelper.startPage(page.getPageIndex(),page.getPageSize());
		return wmm.getWeldingMachineAll(parent,str);
	}

	@Override
	public List<WeldingMachine> findAllweldmachine() {
		return wmm.findAllweldmachine();
	}

	@Override
	public List<WeldingMachine> AllMachine(Page page,BigInteger parent) {
		PageHelper.startPage(page.getPageIndex(),page.getPageSize());
		return wmm.AllMachine(parent);
	}

	@Override
	public void addWeldingMachine(WeldingMachine wm) {
		wmm.addWeldingMachine(wm);
	}

	@Override
	public void editWeldingMachine(WeldingMachine wm) {
		wmm.editWeldingMachine(wm);
	}

	@Override
	public List<WeldingMachine> getWeldingMachine(String str) {
		return wmm.getWeldingMachineAll(null,str);
	}

	@Override
	public BigInteger getWeldingMachineByEno(String eno) {
		return wmm.getWeldingMachineByEno(eno);
	}

	@Override
	public int getEquipmentnoCount(String eno) {
		return wmm.getEquipmentnoCount(eno);
	}

	@Override
	public int getGatheridCount(BigInteger itemid,String gather) {
		return wmm.getGatheridCount(itemid,gather);
	}

	@Override
	public WeldingMachine getWeldingMachineById(BigInteger wid) {
		return wmm.getWeldingMachineById(wid);
	}

	@Override
	public void deleteWeldingChine(BigInteger wid) {
		wmm.deleteWeldingMachine(wid);
	}

	@Override
	public BigInteger getInsframeworkByName(String name) {
		return im.getInsframeworkByName(name);
	}

	@Override
	public BigInteger getMachineCountByManu(BigInteger mid,BigInteger id) {
		return wmm.getMachineCountByManu(mid,id);
	}

	@Override
	public void deleteHistory(BigInteger wid) {
		wmm.deleteHistory(wid);
	}

	@Override
	public List<WeldingMachine> getAllMachine() {
		return wmm.getAllMachine();
	}

	@Override
	public List<WeldingMachine> getMachines(BigInteger insid) {
		return wmm.getMachines(insid);
	}

	@Override
	public List<WeldingMachine> getMachineByIns(BigInteger id) {
		// TODO Auto-generated method stub
		return wmm.getMachineByIns(id);
	}

	@Override
	public List<WeldingMachine> getMachineGather() {
		// TODO Auto-generated method stub
		return wmm.getMachineGather();
	}
	@Override
	public void addfactoryType(WeldingMachine wm) {
		wmm.addfactoryType(wm);
	}

	@Override
	public void deletefactory(BigInteger statusId) {
		wmm.deletefactory(statusId);
	}

	@Override
	public List<WeldingMachine> getMachineModel() {
		// TODO Auto-generated method stub
		return wmm.getMachineModel();
	}

	@Override
	public void updateGather(String machineId, String gatherId) {
		// TODO Auto-generated method stub
		wmm.updateGather(machineId, gatherId);
	}

	@Override
	public int findMachineByGatherId(String gather_id) {
		return wmm.findMachineByGatherId(gather_id);
	}

	@Override
	public void resetGatherMachineid(String machineId) {
		// TODO Auto-generated method stub
		wmm.resetGatherMachineid(machineId);
	}

	@Override
	public List<WeldingMachine> findMachineByFip(String ip) {
		return wmm.findMachineByFip(ip);
	}

	@Override
	public WeldingMachine findMachineByGatherNo(String gather_no) {
		return wmm.findMachineByGatherNo(gather_no);
	}
}
