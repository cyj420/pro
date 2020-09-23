package com.sbs.cyj.readit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbs.cyj.readit.dto.Chapter;
import com.sbs.cyj.readit.dto.Novel;
import com.sbs.cyj.readit.service.ChapterService;
import com.sbs.cyj.readit.service.NovelService;

@Controller
public class HomeController {
	@Autowired
	NovelService novelService;
	@Autowired
	ChapterService chapterService;
	
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		// 노벨 조회수
		List<Novel> novels = novelService.getNovels();
		for (int i = 0; i < novels.size(); i++) {
			int totalHit = 0;

			List<Chapter> chapters = chapterService.getChaptersByNovelId(novels.get(i).getId());

			for (int j = 0; j < chapters.size(); j++) {
				totalHit += chapters.get(j).getHit();
			}

			novelService.updateTotalHitByNovelId(novels.get(i).getId(), totalHit);
		}
		
		novels = novelService.getNovelsByHit();
		model.addAttribute("novels", novels);
		return "home/main";
	}
	
	@RequestMapping("/")
	public String showIndex() {
		return "redirect:/usr/home/main";
	}
}
