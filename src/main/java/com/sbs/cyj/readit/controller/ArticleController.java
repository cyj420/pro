package com.sbs.cyj.readit.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Board;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.ResultData;
import com.sbs.cyj.readit.service.ArticleService;
import com.sbs.cyj.readit.service.BoardService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private BoardService boardService;
	
	// 게시글 작성
	@RequestMapping("usr/article/{boardCode}-write")
	public String showWrite(@PathVariable("boardCode") String boardCode, HttpServletRequest req, Model model, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		if(boardCode.equals("notice")) {
			int loginedMemberId = (int)req.getAttribute("loginedMemberId");
			if(loginedMemberId != 1) {
				model.addAttribute("msg", "공지사항 작성은 관리자만 가능합니다.");
				model.addAttribute("redirectUri", "../home/main");
				
				return "common/redirect";
			}
		}
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
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
		
		if(req.getParameter("memberId")!=null) {
			String str = req.getParameter("memberId");
			int memberId = Integer.parseInt(str);
			articles = articleService.getArticlesByMemberIdAndBoardId(memberId, boardId);
		}
		else {
			articles = articleService.getArticlesByBoardId(boardId);
		}
		
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

		// 조회수
		articleService.updateHitByArticleId(id);
		
		Article article = articleService.getArticleById(loginedMember, id);
		
		model.addAttribute("article", article);
		
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
	
}