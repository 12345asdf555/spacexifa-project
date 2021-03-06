package com.shth.spacexifa.service.impl;

import com.github.pagehelper.PageHelper;
import com.shth.spacexifa.dao.GatherMapper;
import com.shth.spacexifa.dao.WeldingMachineMapper;
import com.shth.spacexifa.model.Gather;
import com.shth.spacexifa.page.Page;
import com.shth.spacexifa.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class GatherServiceImpl implements GatherService {
	@Autowired
	private GatherMapper gm;

	@Autowired
	private WeldingMachineMapper wmm;

	@Override
	public List<Gather> getGatherPageAll(Page page, String str,BigInteger parent) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return gm.getGatherAll(str,parent);
	}

	@Override
	public BigInteger getGatherByNo(String gatherno) {
		return gm.getGatherByNo(gatherno);
	}

	@Override
	public int getGatherNoCount(String gatherno,BigInteger item) {
		return gm.getGatherNoCount(gatherno,item);
	}

	@Override
	public Gather getGatherById(BigInteger id) {
		return gm.getGatherById(id);
	}

	@Override
	public void addGather(Gather ins){
		gm.addGather(ins);
	}

	@Override
	public void editGather(Gather ins) {
		gm.editGather(ins);
	}

	@Override
	public void deleteGather(String id) {
		//修改焊机采集序号为空
		BigInteger wid = wmm.getIdByGatherid(id);
		wmm.editGatherid(wid);
		gm.deleteGather(BigInteger.valueOf(Long.valueOf(id)));
	}

	@Override
	public List<Gather> getGatherAll(String str,BigInteger parent) {
		return gm.getGatherAll(str,parent);
	}

	@Override
	public List<Gather> getGatherByInsfid(BigInteger insfid) {
		return gm.getGatherByInsfid(insfid);
	}

	@Override
	public List<Gather> findGatherByMachineId(BigInteger machineId) {
		return gm.findGatherByMachineId(machineId);
	}

	@Override
	public int getGatherNoByItemCount(String gatherno) {
		return gm.getGatherNoByItemCount(gatherno);
	}

}
