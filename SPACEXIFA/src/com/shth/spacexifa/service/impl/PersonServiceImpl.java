package com.shth.spacexifa.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.shth.spacexifa.dao.PersonMapper;
import com.shth.spacexifa.model.Person;
import com.shth.spacexifa.page.Page;
import com.shth.spacexifa.service.PersonService;

@Service
@Transactional
public class PersonServiceImpl implements PersonService{

	@Resource
	private PersonMapper mapper;
	
	@Override
	public List<Person> findAll(Page page, BigInteger parent, String str) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		List<Person> findAllList = mapper.findAll(parent,str);
		return findAllList;
	}

	@Override
	public List<Person> findLeve(int type) {
		return mapper.findLeve(type);
	}

	@Override
	public void save(Person welder) {
		mapper.save(welder);
	}

	@Override
	public Person findById(BigInteger fid) {
		return mapper.findById(fid);
	}

	@Override
	public void update(Person welder) {
		mapper.update(welder);
	}

	@Override
	public void delete(BigInteger fid) {
		mapper.delete(fid);
	}

	@Override
	public List<Person> dic() {
		return mapper.dic();
	}

	@Override
	public List<Person> ins() {
		return mapper.ins();
	}

	@Override
	public int getUsernameCount(String welderno) {
		return mapper.getUsernameCount(welderno);
	}

	@Override
	public List<Person> getWelder() {
		return mapper.findAll(null, null);
	}

	@Override
	public List<Person> getWeldername(BigInteger insid) {
		return mapper.getWeldername(insid);
	}

	@Override
	public List<Person> findAllWelders() {
		return mapper.findAllWelders();
	}

	@Override
	public Person getIdByWelderno(String welderno) {
		return mapper.getIdByWelderno(welderno);
	}

	@Override
	public List<Person> getFreeWelder(Page page,String str) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getFreeWelder(str);
	}

	@Override
	public List<Person> findAll(BigInteger parent) {
		return mapper.findAll(parent,null);
	}

	@Override
	public List<Person> findAll(BigInteger parent, String str) {
		return mapper.findAll(parent, str);
	}

	@Override
	public String getInsidByFid(BigInteger fid) {
		// TODO Auto-generated method stub
		return mapper.getInsidByFid(fid);
	}

}