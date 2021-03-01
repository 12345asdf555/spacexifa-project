package com.shth.spacexifa.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shth.spacexifa.model.Td;

public interface TdMapper {
	List<Td> findAll(@Param("str")String str);
	List<Td> findAllpro(long ins);
	List<Td> findAllcom();
	List<Td> findAlldiv(long ins);
	List<Td> getAllPosition(@Param("parent")BigInteger parent,@Param("str")String str);
	List<Td> getMachine(@Param("mach")BigInteger mach,@Param("parent")BigInteger parent);
	long findAllIns(long uid);
	long findInsid(String insname);
	String findweld(String weldid);
	String findInsname(long uid);
	String findPosition(String equip);
	List<Td> allWeldname();
	List<Td> findMachine(String fposition);
	List<Td> getAllPositions(@Param("parent")BigInteger parent);
	Td getLiveTime(@Param("time")String time,@Param("totime")String totime,@Param("machineid")BigInteger machineid);
	String getBootTime(@Param("time")String time,@Param("machineId")BigInteger machineId);

	Td countWelderStatusNum();
}