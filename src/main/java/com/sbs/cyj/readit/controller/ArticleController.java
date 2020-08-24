package com.sbs.cyj.readit.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.service.ArticleService;
import com.sbs.cyj.readit.service.MemberService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private MemberService memberService;
	
	// 게시글 작성
	@RequestMapping("usr/article/{loginId}-write")
	public String showWrite(@PathVariable("loginId") String loginId, Model model, String listUrl, HttpSession session) {
		if ( listUrl == null ) {
			listUrl = "./" + loginId + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		
		Member loginedMember = (Member) session.getAttribute("loginedMember");
		model.addAttribute("loginedMember", loginedMember);
		
		List<Category> categories = articleService.getCategories(loginedMember.getId());
		model.addAttribute("categories", categories);

		return "article/write";
	}
	
	@RequestMapping("usr/article/{loginId}-doWrite")
	@ResponseBody
	public String doWrite(@RequestParam Map<String, Object> param, HttpSession session, @PathVariable("loginId") String loginId, Model model) {

		Member loginedMember = (Member) session.getAttribute("loginedMember");
				
//		Map<String, Object> newParam = Util.getNewMapOf(param, "title", "body", "fileIdsStr");
		
		param.put("memberId", loginedMember.getId());
		
		int id = articleService.write(param);
		return "<script> alert('" + id + "번째 글을 작성하였습니다.'); location.replace('../home/main'); </script>";
	}
	
	// 게시글 리스트 (개인)
	@RequestMapping("usr/article/{loginId}-list")
	public String showList(Model model, @PathVariable("loginId") String loginId, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + loginId + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		
		Member member = memberService.getMemberByLoginId(loginId);
		model.addAttribute("member", member);
		
		List<Article> articles = articleService.getArticlesByMemberId(member.getId());
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
	@RequestMapping("usr/article/{loginId}-detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model, @PathVariable("loginId") String loginId, String listUrl) {
		if ( listUrl == null ) {
			listUrl = "./" + loginId + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		Member member = memberService.getMemberByLoginId(loginId);
		model.addAttribute("member", member);
		
		int id = Integer.parseInt((String) param.get("id")); 
		Article article = articleService.getArticleById(id);
		model.addAttribute("article", article);
		
		
		
		return "article/detail";
	}
}