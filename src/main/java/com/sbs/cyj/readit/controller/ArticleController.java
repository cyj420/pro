package com.sbs.cyj.readit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.service.ArticleService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	
	// 글 작성
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
}
