package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.NovelDao;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Novel;
import com.sbs.cyj.readit.util.Util;

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
	

	public Novel getNovelById(int id) {
		return novelDao.getNovelById(id);
	}

	public boolean checkNovelExistByMemberId(int memberId) {
		Novel novel = null;
		List<Novel> novels = novelDao.checkNovelExistByMemberId(memberId);
		if(novels.size() > 0) {
			novel = novels.get(0);
		}
		if(novel == null) {
			return false;
		}
		return true;
	}

	public void genDefaultNovel(Map<String, Object> param) {
		novelDao.genDefaultNovel(param);
	}

	public int genNovel(Map<String, Object> param) {
		novelDao.genNovel(param);
		
		int id = Util.getAsInt(param.get("id"));
		
		return id;
	}


}