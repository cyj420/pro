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
	@ResponseBody
	public String doWrite(@RequestParam Map<String, Object> param, HttpSession session, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);

		Member loginedMember = (Member) session.getAttribute("loginedMember");
				
//		Map<String, Object> newParam = Util.getNewMapOf(param, "title", "body", "fileIdsStr");
		
		param.put("boardId", board.getId());
		param.put("memberId", loginedMember.getId());
		
		int id = articleService.write(param);
		return "<script> alert('" + id + "번째 글을 작성하였습니다.'); location.replace('../home/main'); </script>";
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
		
		String str = "";
		if(req.getParameter("memberId")!=null) {
			str = req.getParameter("memberId");
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
		Article article = articleService.getArticleById(loginedMember, id);
		model.addAttribute("article", article);
		
		return "article/detail";
	}
	
	@RequestMapping("usr/article/{boardCode}-doDelete")
	@ResponseBody
	public String doDelete(@RequestParam Map<String, Object> param, HttpSession session, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);

		Member loginedMember = (Member) session.getAttribute("loginedMember");
		
		int id = Integer.parseInt(req.getParameter("id"));
		
		String str = "";
		
		if(loginedMember.getId() == Integer.parseInt(req.getParameter("memberId"))) {
			articleService.delete(id);
			str = "alert('" + id + "번째 글을 삭제하였습니다.'); location.replace('../article/" + board.getCode() + "-list');";
		}
		else {
			str = "alert('게시글 삭제 실패'); history.back();";
		}

		return "<script>" + str + "</script>";
	}
}