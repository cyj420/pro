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

import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Chapter;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.Novel;
import com.sbs.cyj.readit.service.ChapterService;
import com.sbs.cyj.readit.service.MemberService;
import com.sbs.cyj.readit.service.NovelService;

@Controller
public class NovelController {
	@Autowired
	private NovelService novelService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ChapterService chapterService;
	
	// 소설 작성
	@RequestMapping("usr/novel/{nickname}-write")
	public String showWrite(@PathVariable("nickname") String nickname, Model model, String listUrl, HttpServletRequest req) {
		if ( listUrl == null ) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		model.addAttribute("member", member);
		
		List<Category> categories = novelService.getCategories();
		model.addAttribute("categories", categories);
		
		List<Novel> novels = novelService.getNovelsByMemberId(memberId);
		model.addAttribute("novels", novels);

		return "novel/write";
	}
	
	@RequestMapping("usr/novel/{nickname}-doWrite")
	public String doWrite(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("nickname") String nickname, Model model) {
		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);

		param.put("memberId", member.getId());
		
		int id = chapterService.write(param);
		
		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id+"번째 글을 작성하였습니다."));
		model.addAttribute("redirectUri", redirectUri);
		
		return "common/redirect";
	}
	
	// 게시글 리스트 (전체)
	@RequestMapping("usr/novel/total-list")
	public String showTotalList(Model model, String listUrl, HttpServletRequest req) {
		Member member = (Member) req.getAttribute("loginedMember");
		if ( listUrl == null ) {
			listUrl = "./" + member.getNickname() + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		
		List<Novel> novels = novelService.getNovels();
		
		model.addAttribute("novels", novels);
		
		return "novel/list";
	}
	
	// 게시글 리스트
	@RequestMapping("usr/novel/{nickname}-list")
	public String showList(@PathVariable("nickname") String nickname, Model model, String listUrl, HttpServletRequest req) {
		if ( listUrl == null ) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		
		int memberId = memberService.getMemberByNickname(nickname).getId();
		
		List<Novel> novels = novelService.getNovelsByMemberId(memberId);
		
		if(req.getParameter("novelId")!=null) {
			String strNovelId = req.getParameter("novelId");
			int novelId = Integer.parseInt(strNovelId);
			
			List<Chapter> chapters = chapterService.getChaptersByNovelId(novelId);
			model.addAttribute("chapters", chapters);
			
			Novel novel = novelService.getNovelById(novelId);
			model.addAttribute("novel", novel);
		}
//		List<Chapter> chapters = chapterService.getChaptersByNovelId(novelId);
		
//		if(req.getParameter("memberId")!=null && req.getParameter("cateId")!=null) {
//			String strMemberId = req.getParameter("memberId");
//			int memberId = Integer.parseInt(strMemberId);
//			String strCateId = req.getParameter("cateId");
//			int cateId = Integer.parseInt(strCateId);
//			novels = novelService.getArticlesByMemberIdAndCateId(memberId, cateId);
//			int selectMode = 1;
//			model.addAttribute("selectMode", selectMode);
//		}
//		else if(req.getParameter("memberId")!=null) {
//			str = req.getParameter("memberId");
//			int memberId = Integer.parseInt(str);
//			articles = articleService.getArticlesByMemberIdAndBoardId(memberId, boardId);
//		}
//		else if(req.getParameter("seriesId")!=null) {
//			str = req.getParameter("seriesId");
//			int seriesId = Integer.parseInt(str);
//			articles = articleService.getArticlesBySeriesId(seriesId);
//			series = seriesService.getSeriesById(seriesId);
//		}
//		else if(req.getParameter("cateId")!=null) {
//			str = req.getParameter("cateId");
//			int cateId = Integer.parseInt(str);
//			articles = articleService.getArticlesByCateId(cateId);
//		}
//		else {
//			articles = articleService.getArticlesByBoardId(boardId);
//		}
		
		model.addAttribute("novels", novels);
//		model.addAttribute("chapters", chapters);
		
		return "novel/list";
	}

	// 게시글 상세보기
	@RequestMapping("usr/novel/{nickname}-detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model, @PathVariable("nickname") String nickname, String listUrl, HttpServletRequest req) {
		if ( listUrl == null ) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		Member loginedMember = (Member)req.getAttribute("loginedMember");
		model.addAttribute("loginedMember", loginedMember);
		
		int id = Integer.parseInt((String) param.get("id")); 
		Chapter chapter = chapterService.getChapterById(id);
		
		model.addAttribute("chapter", chapter);
		
//		if(series != null) {
//			model.addAttribute("series", series);
//	
//			// 동일 시리즈 이전글 다음글을 위함
//			List<Article> articles = articleService.getArticlesBySeriesId(series.getId());
//			int size = articles.size();
//			int ch = 0;
//			int preCh = 0;
//			int nextCh = 0;
//			for(int i=0; i<size; i++) {
//				if(articles.get(size-i-1).getId() == article.getId()) {
//					ch = i+1;
//					if(i != 0) {
//						preCh = articles.get(size-i).getId();
//						String preChName = articles.get(size-i).getTitle();
//						model.addAttribute("preCh", preCh);
//						model.addAttribute("preChName", preChName);
//					}
//					if(i != size-1) {
//						nextCh = articles.get(size-i-2).getId();
//						String nextChName = articles.get(size-i-2).getTitle();
//						model.addAttribute("nextCh", nextCh);
//						model.addAttribute("nextChName", nextChName);
//					}
//				}
//			}
//			model.addAttribute("size", size);
//			model.addAttribute("ch", ch);
//		}
		
		return "novel/detail";
	}
//	
//	// 게시글 삭제
//	@RequestMapping("usr/article/{boardCode}-doDelete")
//	@ResponseBody
//	public String doDelete(@RequestParam int id, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
//		Board board = boardService.getBoardByCode(boardCode);
//		model.addAttribute("board", board);
//
//		Member loginedMember = (Member) req.getAttribute("loginedMember");
//		
//		String str = "";
//		
//		Article a = articleService.getArticleById(id);
//		
//		if(loginedMember.getId() == a.getMemberId()) {
//			articleService.delete(id);
//			str = "alert('" + id + "번째 글을 삭제하였습니다.'); location.replace('../article/" + board.getCode() + "-list');";
//		}
//		else {
//			str = "alert('게시글 삭제 실패'); history.back();";
//		}
//
//		return "<script>" + str + "</script>";
//	}
//	
//	// 게시글 수정
//	@RequestMapping("usr/article/{boardCode}-modify")
//	public String showModify(@PathVariable("boardCode") String boardCode, Model model, String listUrl, @RequestParam int id, HttpServletRequest req) {
//		if ( listUrl == null ) {
//			listUrl = "./" + boardCode + "-list";
//		}
//		model.addAttribute("listUrl", listUrl);
//		Board board = boardService.getBoardByCode(boardCode);
//		model.addAttribute("board", board);
//		
//		List<Category> categories = articleService.getCategories(board.getId());
//		model.addAttribute("categories", categories);
//		
//		Member loginedMember = (Member) req.getAttribute("loginedMember");
//		
//		Article article = articleService.getArticleById(loginedMember, id);
//		model.addAttribute("article", article);
//		
//		return "article/modify";
//	}
//	
//	@RequestMapping("usr/article/{boardCode}-doModify")
//	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
//		Board board = boardService.getBoardByCode(boardCode);
//		model.addAttribute("board", board);
//		Member loginedMember = (Member)req.getAttribute("loginedMember");
//		
//		int id = Integer.parseInt((String) param.get("id"));
//		
//		ResultData checkActorCanModifyResultData = articleService.checkActorCanModify(loginedMember, id);
//		
//		if (checkActorCanModifyResultData.isFail() ) {
//			model.addAttribute("historyBack", true);
//			model.addAttribute("msg", checkActorCanModifyResultData.getMsg());
//			
//			return "common/redirect";
//		}
//		
//		articleService.modify(param);
//		
//		String redirectUri = (String) param.get("redirectUri");
//		
//		redirectUri = redirectUri.replace("#id", id + "");
//
//		model.addAttribute("msg", String.format(id+"번째 글을 수정하였습니다."));
//		model.addAttribute("redirectUri", redirectUri);
//
//		return "common/redirect";
//	}
//	
//	// MySeries >> 현재는 단순 리스팅만 하지만 후에 이 페이지에서 시리즈 추가/삭제/수정 등 가능하도록 변경할 예정
//	@RequestMapping("/usr/article/{boardCode}-list/mySeries")
//	public String showMyAllSeries(HttpSession session, @PathVariable("boardCode") String boardCode, String listUrl, Model model) {
//		if ( listUrl == null ) {
//			listUrl = "./" + boardCode + "-list";
//		}
//		model.addAttribute("listUrl", listUrl);
//		Board board = boardService.getBoardByCode(boardCode);
//		model.addAttribute("board", board);
//		int memberId = (int) session.getAttribute("loginedMemberId");
//		List<Series> series = seriesService.getAllSeriesByMemberId(memberId);
//		model.addAttribute("series", series);
//		
//		return "article/mySeries";
//	}
//	
//	@RequestMapping("/usr/article/{boardCode}-addSeries")
//	public String addSeries(HttpSession session, @PathVariable("boardCode") String boardCode, String listUrl, Model model) {
//		if ( listUrl == null ) {
//			listUrl = "./" + boardCode + "-list";
//		}
//		model.addAttribute("listUrl", listUrl);
//		Board board = boardService.getBoardByCode(boardCode);
//		model.addAttribute("board", board);
//		List<Category> categories = articleService.getCategories(board.getId());
//		model.addAttribute("categories", categories);
//		
//		return "article/addSeries";
//	}
//	
//	@RequestMapping("/usr/article/{boardCode}-doAddSeries")
//	public String doAddSeries(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("boardCode") String boardCode, Model model) {
//		Board board = boardService.getBoardByCode(boardCode);
//		model.addAttribute("board", board);
//		
//		String redirectUri = (String) param.get("redirectUri");
//		
//		int memberId = (int) req.getAttribute("loginedMemberId");
//		param.put("memberId", memberId);
//		
//		int id = seriesService.addSeries(param);
//		
//		redirectUri = redirectUri.replace("#id", id + "");
//		model.addAttribute("msg", String.format(id+"번째 시리즈를 작성하였습니다."));
//		model.addAttribute("redirectUri", redirectUri);
//
//		return "common/redirect";
//	}
}