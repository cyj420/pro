package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.ArticleDao;
import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.ResultData;
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
	
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}
	
	public Article getArticleById(Member actor, int id) {
		Article article = articleDao.getArticleById(id);
		
		updateForPrintInfo(actor, article);
		
		return article;
	}
	
	// 액터가 해당 댓글을 수정할 수 있는지 알려준다.
	public boolean actorCanModify(Member actor, Article article) {
		return actor != null && actor.getId() == article.getMemberId() ? true : false;
	}

	// 액터가 해당 댓글을 삭제할 수 있는지 알려준다.
	public boolean actorCanDelete(Member actor, Article article) {
		return actorCanModify(actor, article);
	}

	private void updateForPrintInfo(Member actor, Article article) {
		Util.putExtraVal(article, "actorCanDelete", actorCanDelete(actor, article));
		Util.putExtraVal(article, "actorCanModify", actorCanModify(actor, article));
	}

	public List<Article> getArticlesByMemberIdAndBoardIdAndSearchKeyword(int memberId, int boardId, String searchKeyword) {
		return articleDao.getArticlesByMemberIdAndBoardIdAndSearchKeyword(memberId, boardId, searchKeyword);
	}

	public List<Article> getArticlesByBoardIdAndSearchKeyword(int boardId, String searchKeyword) {
		return articleDao.getArticlesByBoardIdAndSearchKeyword(boardId, searchKeyword);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	public void modify(Map<String, Object> param) {
		articleDao.modify(param);
	}

	public ResultData checkActorCanModify(Member actor, int id) {
		boolean actorCanModify = actorCanModify(actor, id);

		if (actorCanModify) {
			return new ResultData("S-1", "가능합니다.", "id", id);
		}

		return new ResultData("F-1", "권한이 없습니다.", "id", id);
	}

	private boolean actorCanModify(Member actor, int id) {
		Article article = articleDao.getArticleById(id);

		return actorCanModify(actor, article);
	}

	public List<Article> getArticlesByMemberId(int memberId) {
		return articleDao.getArticlesByMemberId(memberId);
	}

	public void updateHitByArticleId(int id) {
		articleDao.updateHitByArticleId(id);
	}

	public List<Article> getArticlesByMemberIdAndBoardIdAndSearchKeywordForPrint(int memberId, int boardId, 
			String searchKeyword, int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return articleDao.getArticlesByMemberIdAndBoardIdAndSearchKeywordForPrint(memberId, boardId, searchKeyword, start, itemsInOnePage);
	}

	public List<Article> getArticlesByBoardIdAndSearchKeywordForPrint(int boardId, String searchKeyword, int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return articleDao.getArticlesByBoardIdAndSearchKeywordForPrint(boardId, searchKeyword, start, itemsInOnePage);
	}

}