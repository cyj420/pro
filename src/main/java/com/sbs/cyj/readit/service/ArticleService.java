package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.ArticleDao;
import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Category;
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

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}

	public List<Article> getArticlesByMemberIdAndBoardId(int memberId, int boardId) {
		return articleDao.getArticlesByMemberIdAndBoardId(memberId, boardId);
	}

	public List<Category> getCategories(int boardId) {
		return articleDao.getCategories(boardId);
	}

	public List<Article> getArticlesByBoardId(int boardId) {
		return articleDao.getArticlesByBoardId(boardId);
	}
}