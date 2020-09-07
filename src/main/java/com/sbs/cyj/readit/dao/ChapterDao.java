package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Chapter;

@Mapper
public interface ChapterDao {

	void write(Map<String, Object> param);

	Chapter getChapterById(int id);

	List<Chapter> getChaptersByNovelId(int novelId);

}
