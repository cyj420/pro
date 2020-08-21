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

		return "article/write";
	}
	
	@RequestMapping("usr/article/{boardCode}-doWrite")
	@ResponseBody
	public String doWrite(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
		System.out.println("boardCode : "+boardCode);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);

		Member loginedMember = (Member) req.getAttribute("loginedMember");
				
//		Map<String, Object> newParam = Util.getNewMapOf(param, "title", "body", "fileIdsStr");
		
		System.out.println("board.getId() : "+ board.getId());
		
		// 여기서 문제 발생... 왜 null???
		System.out.println("loginedMember.toString() : " + loginedMember.toString());
		System.out.println("loginedMember.getId() : "+ loginedMember.getId());
		
		param.put("boardId", board.getId());
		param.put("memberId", loginedMember.getId());
		
		int id = articleService.write(param);
		return "<script> alert('" + id + "번째 글을 작성하였습니다.'); location.replace('../home/main'); </script>";
	}
	
	// 게시글 리스트 (개인)
	@RequestMapping("usr/article/{boardCode}-list")
	public String showList(Model model, @PathVariable("boardCode") String boardCode, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		List<Article> articles = articleService.getArticlesByMemberId(board.getMemberId());
		model.addAttribute("articles", articles);
		
		return "article/list";
	}
	
	// 게시글 리스트 (전체)
	@RequestMapping("usr/article/totalList")
	public String showTotalList(Model model) {
		List<Article> articles = articleService.getArticles();
		model.addAttribute("articles", articles);
		
		return "article/totalList";
	}
	
	// 게시글 상세보기
	@RequestMapping("usr/article/{boardCode}-detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model, @PathVariable("boardCode") String boardCode, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + boardCode + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		Board board = boardService.getBoardByCode(boardCode);
		model.addAttribute("board", board);
		
		int id = Integer.parseInt((String) param.get("id")); 
		Article article = articleService.getArticleById(id);
		model.addAttribute("article", article);
		
		return "article/detail";
	}
}