package com.sbs.cyj.readit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Article;
import com.sbs.cyj.readit.service.ArticleService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	
	// 게시글 작성
	@RequestMapping("usr/article/write")
	public String showWrite() {
		return "article/write";
	}
	
	@RequestMapping("usr/article/doWrite")
	@ResponseBody
	public String doWrite(@RequestParam Map<String, Object> param, Model model) {
		int id = articleService.write(param);
		return "<script> alert('" + id + "번째 글을 작성하였습니다.'); location.replace('../home/main'); </script>";
	}
	
	// 게시글 리스트
	@RequestMapping("usr/article/list")
	public String showList(Model model) {
		List<Article> articles = articleService.getArticles();
		model.addAttribute("articles", articles);
		
		return "article/list";
	}
	
	// 게시글 상세보기
	@RequestMapping("usr/article/detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model) {
		int id = Integer.parseInt((String) param.get("id")); 
		Article article = articleService.getArticleById(id);
		model.addAttribute("article", article);
		
		return "article/detail";
	}
}