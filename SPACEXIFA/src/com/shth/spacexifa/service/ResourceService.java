package com.shth.spacexifa.service;

import java.util.List;

import com.shth.spacexifa.model.Resources;
import com.shth.spacexifa.page.Page;


public interface ResourceService {
	void save(Resources resource);
	boolean update(Resources resource);
	boolean delete(int id);
	Resources findById(Integer id);
	List<Resources> findAll(Page page, String search);
	List<String> getResourcesByAuthName(String authName);
	int getResourcenameCount(String resourceName);
	List<String> getAuthByRes(String url);
	List<Resources> getResourceByUserid(int id);
	List<String> getAuthName(int id);
	List<Resources> getResourceByAdmin();
}