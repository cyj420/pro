package com.sbs.cyj.readit.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.ArticleDao;
import com.sbs.cyj.readit.util.Util;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;

	public int write(Map<String, Object> param) {
		articleDao.write(param);
		int id = Util.getAsInt(param.get("id"));
		return id;
	}
}
