package com.shth.spacexifa.service;

import java.math.BigInteger;
import java.util.List;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.Report;
import com.shth.spacexifa.page.Page;

public interface ReportService {
	BigInteger getWpsid(BigInteger machid);
	Report getWps(BigInteger wpsid);
	Report getSyspara();
	List<Report> findAllWelder(Page page,WeldDto dto);
	List<Report> findMachine(String weldid);
	long getWeldingTime(WeldDto dto,BigInteger machid,String weldid);
	long getOnTime(WeldDto dto,BigInteger machid);
	long getRealEle(WeldDto dto,BigInteger machid);
	long getRealVol(WeldDto dto,BigInteger machid);
	long getHjTime(BigInteger machid,String time);
	long getZxTime(BigInteger machid,String time);
	String getFirstTime(BigInteger machid,String time);
	List<Report> getAllPara(BigInteger parent,String str,String time);
	List<Report> historyData(Page page,WeldDto dto,String fid,BigInteger mach, String welderid,String fsolder_layer,String fweld_bead);
	List<Report> historyData(WeldDto dto,String fid,BigInteger mach, String welderid,String fsolder_layer,String fweld_bead);
	
	/**
	 * 
	 * @param taskNum 任务编号
	 * @param fsolder_layer 焊层号
	 * @param fweld_bead 焊道号
	 * @return
	 */
	Report getEleVolRange(String taskNum,String fsolder_layer,String fweld_bead);
}
