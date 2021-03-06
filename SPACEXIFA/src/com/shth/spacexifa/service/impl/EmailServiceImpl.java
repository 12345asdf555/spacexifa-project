package com.shth.spacexifa.service.impl;

import com.github.pagehelper.PageHelper;
import com.shth.spacexifa.dao.EmailMapper;
import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.Email;
import com.shth.spacexifa.page.Page;
import com.shth.spacexifa.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
	@Autowired
	private EmailMapper em;
	
	@Override
	public List<Email> getEmailAll(Page page,String str) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return em.getEmailAll(str);
	}

	@Override
	public int getEmailAddressCount(String femailaddress) {
		return em.getEmailAddressCount(femailaddress);
	}

	@Override
	public boolean addEmail(Email email) {
		return em.addEmail(email);
	}

	@Override
	public boolean editEmail(Email email) {
		return em.editEmail(email);
	}

	@Override
	public boolean deleteEmail(String femailaddress) {
		return em.deleteEmail(femailaddress);
	}

	@Override
	public List<Email> getEmailHistory(Page page, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return em.getEmailHistory(dto);
	}
	
}
