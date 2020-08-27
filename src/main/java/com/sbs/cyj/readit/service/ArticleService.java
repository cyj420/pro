package com.sbs.cyj.readit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.ArticleDao;
import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.File;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.ResultData;
import com.sbs.cyj.readit.util.Util;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private FileService fileService;

	public int write(Map<String, Object> param) {
		articleDao.write(param);
		
		int id = Util.getAsInt(param.get("id"));

//		String fileIdsStr = (String) param.get("fileIdsStr");
//		
//		if (fileIdsStr != null && fileIdsStr.length() > 0) {
//			List<Integer> fileIds = Arrays.asList(fileIdsStr.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
//					.collect(Collectors.toList());
//
//			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
//			// 그것을 뒤늦게라도 이렇게 고처야 한다.
//			for (int fileId : fileIds) {
//				fileService.changeRelId(fileId, id);
//			}
//		}
		
		return id;
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}
	
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}
	
	public Article getArticleById(Member actor, int id) {
		Article article = articleDao.getArticleById(id);
		
		updateForPrintInfo(actor, article);
		
		List<File> files = fileService.getFiles("article", article.getId(), "common", "attachment");

		Map<String, File> filesMap = new HashMap<>();

		for (File file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}

		Util.putExtraVal(article, "file__common__attachment", filesMap);

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

	public List<Article> getArticlesByMemberIdAndBoardId(int memberId, int boardId) {
		return articleDao.getArticlesByMemberIdAndBoardId(memberId, boardId);
	}

	public List<Category> getCategories(int boardId) {
		return articleDao.getCategories(boardId);
	}

	public List<Article> getArticlesByBoardId(int boardId) {
		return articleDao.getArticlesByBoardId(boardId);
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

}