package com.shth.spacexifa.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shth.spacexifa.model.Welder;

import tk.mybatis.mapper.common.Mapper;

public interface WelderMapper extends Mapper<Welder> {
	List<Welder> getWelderAll(@Param("str")String str);
	
	void addWelder(Welder we);
	
	void editWelder(Welder we);
	
	void removeWelder(BigInteger id);
	
	int getWeldernoCount(@Param("wno")String wno);
	
	List<Welder> getFreeWelder();

	List<Welder> findAllWelderInfo();
}