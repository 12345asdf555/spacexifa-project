package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.spring.dao.TdMapper;
import com.spring.page.Page;
import com.spring.model.Td;
import com.spring.service.TdService;

@Service
@Transactional // 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class TdServiceImpl implements TdService {

	@Resource
	private TdMapper mapper;

	public List<Td> findAll(Page page, String str) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		List<Td> findAllList = mapper.findAll(str);
		return findAllList;
	}

	public List<Td> findAllpro(long ins) {
		return mapper.findAllpro(ins);
	}

	public List<Td> findAllcom() {
		return mapper.findAllcom();
	}

	public List<Td> findAlldiv(long ins) {
		return mapper.findAlldiv(ins);
	}

	public List<Td> getAllPosition(BigInteger parent, String str) {
		return mapper.getAllPosition(parent, str);
	}

	public long findIns(long uid) {
		return mapper.findAllIns(uid);
	}

	public long findInsid(String insname) {
		return mapper.findInsid(insname);
	}

	public String findweld(String weldid) {
		return mapper.findweld(weldid);
	}

	public String findInsname(long uid) {
		return mapper.findInsname(uid);
	}

	public String findPosition(String equip) {
		return mapper.findPosition(equip);
	}

	public List<Td> allWeldname() {
		return mapper.allWeldname();
	}

	public List<Td> getAllMachine(String position) {
		return mapper.findMachine(position);
	}

	@Override
	public List<Td> getMachine(BigInteger mach, BigInteger parent) {
		return mapper.getMachine(mach, parent);
	}

	@Override
	public Td getLiveTime(String time, String totime, BigInteger machineid) {
		return mapper.getLiveTime(time, totime, machineid);
	}

	public List<Td> getAllPositions(BigInteger parent) {
		return mapper.getAllPositions(parent);
	}
	
	@Override
	public String getBootTime(String time,BigInteger machineId) {
		// TODO Auto-generated method stub
		return mapper.getBootTime(time,machineId);
	}
}