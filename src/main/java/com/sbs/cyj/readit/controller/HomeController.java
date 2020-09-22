package com.sbs.cyj.readit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbs.cyj.readit.dto.Novel;
import com.sbs.cyj.readit.service.NovelService;

@Controller
public class HomeController {
	@Autowired
	NovelService novelService;
	
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		List<Novel> novels = novelService.getNovelsByHit();
		model.addAttribute("novels", novels);
		return "home/main";
	}
	
	@RequestMapping("/")
	public String showIndex() {
		return "redirect:/usr/home/main";
	}
}
