package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.BoardDao;
import com.sbs.cyj.readit.dto.Board;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;

	public void generateBoard(Map<String, Object> param) {
		boardDao.generateBoard(param);
	}

	public Board getBoardByCode(String boardCode) {
		return boardDao.getBoardByCode(boardCode);
	}

	public List<Board> getBoards() {
		return boardDao.getBoards();
	}
}
