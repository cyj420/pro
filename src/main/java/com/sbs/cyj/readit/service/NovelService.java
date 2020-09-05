package com.sbs.cyj.readit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.NovelDao;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Novel;

@Service
public class NovelService {
	@Autowired
	private NovelDao novelDao;

	public List<Category> getCategories() {
		return novelDao.getCategories();
	}

	public List<Novel> getNovelsByMemberId(int memberId) {
		return novelDao.getNovelsByMemberId(memberId);
	}

	public List<Novel> getNovels() {
		return novelDao.getNovels();
	}

}