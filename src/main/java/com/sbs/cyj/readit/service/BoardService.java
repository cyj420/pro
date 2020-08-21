package com.sbs.cyj.readit.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.BoardDao;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;

	public void generateBoard(Map<String, Object> param) {
		boardDao.generateBoard(param);
	}
}
