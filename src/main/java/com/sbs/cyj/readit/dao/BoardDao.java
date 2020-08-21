package com.sbs.cyj.readit.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Board;

@Mapper
public interface BoardDao {

	void generateBoard(Map<String, Object> param);

	Board getBoardByCode(String boardCode);

}
