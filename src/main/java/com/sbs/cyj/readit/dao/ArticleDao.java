package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Article;

@Mapper
public interface ArticleDao {

	void write(Map<String, Object> param);

	List<Article> getArticles();

	Article getArticleById(int id);

	List<Article> getArticlesByMemberId(int memberId);
}
