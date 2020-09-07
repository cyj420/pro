package com.sbs.cyj.readit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Novel;

@Mapper
public interface NovelDao {

	List<Category> getCategories();
	
	List<Novel> getNovelsByMemberId(int memberId);

	List<Novel> getNovels();

	void genNovelDefault(String id, String nickname);

	Novel getNovelById(int id);


}
