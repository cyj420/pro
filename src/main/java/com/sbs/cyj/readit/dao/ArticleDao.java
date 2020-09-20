package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Article;

@Mapper
public interface ArticleDao {

	void write(Map<String, Object> param);

	Article getArticleById(int id);
	
	List<Article> getArticlesByMemberIdAndBoardIdAndSearchKeyword(int memberId, int boardId, String searchKeyword);

	List<Article> getArticlesByBoardIdAndSearchKeyword(int boardId, String searchKeyword);

	void delete(int id);

	void modify(Map<String, Object> param);

	List<Article> getArticlesByMemberId(int memberId);

	void updateHitByArticleId(int id);

	List<Article> getArticlesByMemberIdAndBoardIdAndSearchKeywordForPrint(int memberId, int boardId, String searchKeyword, int start, int itemsInOnePage);

	List<Article> getArticlesByBoardIdAndSearchKeywordForPrint(int boardId, String searchKeyword, int start, int itemsInOnePage);

}
