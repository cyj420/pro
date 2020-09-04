package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Series;

@Mapper
public interface ArticleDao {

	void write(Map<String, Object> param);

	List<Article> getArticles();

	Article getArticleById(int id);
	
	List<Article> getArticlesByMemberIdAndBoardId(int memberId, int boardId);

	List<Category> getCategories(int boardId);

	List<Article> getArticlesByBoardId(int boardId);

	void delete(int id);

	void modify(Map<String, Object> param);

	List<Series> getSeriesByMemberId(int memberId);

	List<Article> getArticlesBySeriesId(int seriesId);

	List<Article> getArticlesByCateId(int cateId);
}
