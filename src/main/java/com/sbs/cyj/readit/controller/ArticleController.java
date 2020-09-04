package com.sbs.cyj.readit.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Board;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.ResultData;
import com.sbs.cyj.readit.dto.Series;
import com.sbs.cyj.readit.service.ArticleService;
import com.sbs.cyj.readit.service.BoardService;
import com.sbs.cyj.readit.service.SeriesService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private SeriesService seriesService;
	
	// 게시글 작성
	@RequestMapping("usr/article/{boardCode}-write")
	public String showWrite(@PathVariable("boardCode") String boardCode, Model model, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		List<Category> categories = articleService.getCategories(board.getId());
		model.addAttribute("categories", categories);

		return "article/write";
	}
	
	@RequestMapping("usr/article/{boardCode}-doWrite")
	public String doWrite(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);

		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		param.put("boardId", board.getId());
		param.put("memberId", loginedMemberId);
		
		int id = articleService.write(param);
		
		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id+"번째 글을 작성하였습니다."));
		model.addAttribute("redirectUri", redirectUri);
		
		return "common/redirect";
	}
	
	// 게시글 리스트
	@RequestMapping("usr/article/{boardCode}-list")
	public String showList(Model model, @PathVariable("boardCode") String boardCode, String listUrl, HttpServletRequest req) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		int boardId = board.getId();
		
		List<Article> articles;
		
		Series series = null;
		String str = "";
		
		if(req.getParameter("memberId")!=null && req.getParameter("cateId")!=null) {
			String strMemberId = req.getParameter("memberId");
			int memberId = Integer.parseInt(strMemberId);
			String strCateId = req.getParameter("cateId");
			int cateId = Integer.parseInt(strCateId);
			articles = articleService.getArticlesByMemberIdAndCateId(memberId, cateId);
			int selectMode = 1;
			model.addAttribute("selectMode", selectMode);
		}
		else if(req.getParameter("memberId")!=null) {
			str = req.getParameter("memberId");
			int memberId = Integer.parseInt(str);
			articles = articleService.getArticlesByMemberIdAndBoardId(memberId, boardId);
		}
		else if(req.getParameter("seriesId")!=null) {
			str = req.getParameter("seriesId");
			int seriesId = Integer.parseInt(str);
			articles = articleService.getArticlesBySeriesId(seriesId);
			series = seriesService.getSeriesById(seriesId);
		}
		else if(req.getParameter("cateId")!=null) {
			str = req.getParameter("cateId");
			int cateId = Integer.parseInt(str);
			articles = articleService.getArticlesByCateId(cateId);
		}
		else {
			articles = articleService.getArticlesByBoardId(boardId);
		}
		
		model.addAttribute("series", series);
		model.addAttribute("articles", articles);
		
		return "article/list";
	}
	
	// 게시글 상세보기
	@RequestMapping("usr/article/{boardCode}-detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model, @PathVariable("boardCode") String boardCode, String listUrl, HttpServletRequest req) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);

		Member loginedMember = (Member)req.getAttribute("loginedMember");
		model.addAttribute("loginedMember", loginedMember);
		
		int id = Integer.parseInt((String) param.get("id")); 
		Article article = articleService.getArticleById(loginedMember, id);
		model.addAttribute("article", article);
		
		Series series = seriesService.getSeriesByArticleId(article.getId());
		
		if(series != null) {
			model.addAttribute("series", series);
	
			// 동일 시리즈 이전글 다음글을 위함
			List<Article> articles = articleService.getArticlesBySeriesId(series.getId());
			int size = articles.size();
			int ch = 0;
			int preCh = 0;
			int nextCh = 0;
			for(int i=0; i<size; i++) {
				if(articles.get(size-i-1).getId() == article.getId()) {
					ch = i+1;
					if(i != 0) {
						preCh = articles.get(size-i).getId();
						String preChName = articles.get(size-i).getTitle();
						model.addAttribute("preCh", preCh);
						model.addAttribute("preChName", preChName);
					}
					if(i != size-1) {
						nextCh = articles.get(size-i-2).getId();
						String nextChName = articles.get(size-i-2).getTitle();
						model.addAttribute("nextCh", nextCh);
						model.addAttribute("nextChName", nextChName);
					}
				}
			}
			model.addAttribute("size", size);
			model.addAttribute("ch", ch);
		}
		
		return "article/detail";
	}
	
	// 게시글 삭제
	@RequestMapping("usr/article/{boardCode}-doDelete")
	@ResponseBody
	public String doDelete(@RequestParam int id, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);

		Member loginedMember = (Member) req.getAttribute("loginedMember");
		
		String str = "";
		
		Article a = articleService.getArticleById(id);
		
		if(loginedMember.getId() == a.getMemberId()) {
			articleService.delete(id);
			str = "alert('" + id + "번째 글을 삭제하였습니다.'); location.replace('../article/" + board.getCode() + "-list');";
		}
		else {
			str = "alert('게시글 삭제 실패'); history.back();";
		}

		return "<script>" + str + "</script>";
	}
	
	// 게시글 수정
	@RequestMapping("usr/article/{boardCode}-modify")
	public String showModify(@PathVariable("boardCode") String boardCode, Model model, String listUrl, @RequestParam int id, HttpServletRequest req) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		List<Category> categories = articleService.getCategories(board.getId());
		model.addAttribute("categories", categories);
		
		Member loginedMember = (Member) req.getAttribute("loginedMember");
		
		Article article = articleService.getArticleById(loginedMember, id);
		model.addAttribute("article", article);
		
		return "article/modify";
	}
	
	@RequestMapping("usr/article/{boardCode}-doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		
		int id = Integer.parseInt((String) param.get("id"));
		
		ResultData checkActorCanModifyResultData = articleService.checkActorCanModify(loginedMember, id);
		
		if (checkActorCanModifyResultData.isFail() ) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", checkActorCanModifyResultData.getMsg());
			
			return "common/redirect";
		}
		
		articleService.modify(param);
		
		String redirectUri = (String) param.get("redirectUri");
		
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id+"번째 글을 수정하였습니다."));
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}
	
	// MySeries >> 현재는 단순 리스팅만 하지만 후에 이 페이지에서 시리즈 추가/삭제/수정 등 가능하도록 변경할 예정
	@RequestMapping("/usr/article/{boardCode}-list/mySeries")
	public String showMyAllSeries(HttpSession session, @PathVariable("boardCode") String boardCode, String listUrl, Model model) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		int memberId = (int) session.getAttribute("loginedMemberId");
		List<Series> series = seriesService.getAllSeriesByMemberId(memberId);
		model.addAttribute("series", series);
		
		return "article/mySeries";
	}
	
	@RequestMapping("/usr/article/{boardCode}-addSeries")
	public String addSeries(HttpSession session, @PathVariable("boardCode") String boardCode, String listUrl, Model model) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		List<Category> categories = articleService.getCategories(board.getId());
		model.addAttribute("categories", categories);
		
		return "article/addSeries";
	}
	
	@RequestMapping("/usr/article/{boardCode}-doAddSeries")
	public String doAddSeries(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		String redirectUri = (String) param.get("redirectUri");
		
		int memberId = (int) req.getAttribute("loginedMemberId");
		param.put("memberId", memberId);
		
		int id = seriesService.addSeries(param);
		
		redirectUri = redirectUri.replace("#id", id + "");
		model.addAttribute("msg", String.format(id+"번째 시리즈를 작성하였습니다."));
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}
}