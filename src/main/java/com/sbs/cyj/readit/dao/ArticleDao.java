package com.sbs.cyj.readit.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleDao {

	void write(Map<String, Object> param);

}
