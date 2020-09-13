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

import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Chapter;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.Novel;
import com.sbs.cyj.readit.dto.ResultData;
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

	// 챕터 작성
	@RequestMapping("usr/novel/{nickname}-write")
	public String showWrite(@PathVariable("nickname") String nickname, Model model, String listUrl,
			HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		model.addAttribute("member", member);

		List<Novel> novels = novelService.getNovelsByMemberId(memberId);
		model.addAttribute("novels", novels);

		return "novel/write";
	}

	@RequestMapping("usr/novel/{nickname}-doWrite")
	public String doWrite(@RequestParam Map<String, Object> param, HttpServletRequest req,
			@PathVariable("nickname") String nickname, Model model) {
		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);

		int novelId = Integer.parseInt((String) param.get("novelId"));
		int cateId = novelService.getNovelById(novelId).getCateId();

		param.put("memberId", member.getId());
		param.put("cateId", cateId);

		int id = chapterService.write(param);

		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id + "번째 글을 작성하였습니다."));
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}
	
	// 챕터 삭제
	@RequestMapping("usr/novel/{nickname}-doDeleteChapter")
	public String doDeleteChapter(HttpServletRequest req, @PathVariable("nickname") String nickname, String listUrl, Model model) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		int id = Integer.parseInt(req.getParameter("id"));
		Chapter chapter = chapterService.getChapterById(id);
		
		List<Chapter> chapters = chapterService.getChaptersByNovelId(chapter.getNovelId());

		if(loginedMemberId == chapter.getMemberId()) {
			chapterService.deleteChapterById(id);

			model.addAttribute("msg", "삭제되었습니다.");
		}
		else {
			model.addAttribute("msg", "잘못된 접근입니다.");
		}
		
		String redirectUri = listUrl;
		
		if(chapters.size()>0) {
			redirectUri = listUrl+"?novelId="+chapter.getNovelId();
		}

		model.addAttribute("redirectUri", redirectUri);
		
		return "common/redirect";
	}


	// 게시글 리스트 (전체)
	@RequestMapping("usr/novel/total-list")
	public String showTotalList(Model model, String listUrl, HttpServletRequest req) {
		Member member = (Member) req.getAttribute("loginedMember");
		if (listUrl == null) {
			listUrl = "./" + member.getNickname() + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		List<Novel> novels = novelService.getNovels();

		model.addAttribute("novels", novels);

		return "novel/list";
	}

	// 소설 리스트
	// param에 novelId가 존재한다면 챕터 리스트
	@RequestMapping("usr/novel/{nickname}-list")
	public String showList(@PathVariable("nickname") String nickname, Model model, String listUrl,
			HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		int memberId = memberService.getMemberByNickname(nickname).getId();

		List<Novel> novels = novelService.getNovelsByMemberId(memberId);

		if (req.getParameter("novelId") != null) {
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

	// 소설 상세보기
	@RequestMapping("usr/novel/{nickname}-detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model,
			@PathVariable("nickname") String nickname, String listUrl, HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		Member loginedMember = (Member) req.getAttribute("loginedMember");
		model.addAttribute("loginedMember", loginedMember);

		int id = Integer.parseInt((String) param.get("id"));
		Chapter chapter = chapterService.getChapterById(id);
		
		Novel novel = novelService.getNovelById(chapter.getNovelId());

		if (novel.isSeriesStatus()) {
			List<Chapter> chapters = chapterService.getChaptersByNovelId(novel.getId());
			System.out.println("chapters.size() : " + chapters.size());
			if (chapters.size() > 1) {
				model.addAttribute("chapters", chapters);
			}
		}

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
	// 소설 삭제
	@RequestMapping("usr/novel/{nickname}-doDelete")
	public String doDelete(@RequestParam Map<String, Object> param, HttpServletRequest req, @PathVariable("nickname") String nickname, String listUrl, Model model) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		int id = Integer.parseInt(req.getParameter("id"));
		Novel novel = novelService.getNovelById(id);
		
		if(loginedMemberId == novel.getMemberId()) {
			novelService.delete(id);
			chapterService.deleteChaptersByNovelId(id);
		}

		String redirectUri = (String) param.get("redirectUri");
		
		model.addAttribute("msg", "삭제되었습니다.");
		model.addAttribute("redirectUri", redirectUri);
		
		return "common/redirect";
	}

	// 소설 설정
	@RequestMapping("usr/novel/{nickname}-setUp")
	public String showSetUp(@RequestParam Map<String, Object> param, Model model,
			@PathVariable("nickname") String nickname, String listUrl, HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		Member loginedMember = (Member) req.getAttribute("loginedMember");
		model.addAttribute("loginedMember", loginedMember);
		
		int memberId = memberService.getMemberByNickname(nickname).getId();
		
		if(loginedMember.getNickname().equals(nickname)) {
			List<Novel> novels = novelService.getNovelsByMemberId(memberId);
			model.addAttribute("novels", novels);
			
			return "novel/setUp";
		}
		
		model.addAttribute("msg", "해당 페이지 접근 권한이 없습니다.");
		model.addAttribute("redirectUri", "../home/main");
		
		return "common/redirect";
	}

	// 소설 생성
	@RequestMapping("usr/novel/genNovel")
	public String showGenNovel(Model model, String listUrl, HttpServletRequest req) {
		model.addAttribute("listUrl", listUrl);

		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		model.addAttribute("member", member);

		List<Category> categories = novelService.getCategories();
		model.addAttribute("categories", categories);

		return "novel/genNovel";
	}

	@RequestMapping("usr/novel/doGenNovel")
	public String doGenNovel(@RequestParam Map<String, Object> param, HttpServletRequest req, Model model) {
		int memberId = (int) req.getAttribute("loginedMemberId");

		param.put("memberId", memberId);

		int id = novelService.genNovel(param);

		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id + "번째 시리즈를 생성하였습니다."));
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}

	// 자동으로 제목 체크
	@RequestMapping("/usr/novel/getAutoNovelTitle")
	@ResponseBody
	public ResultData doGetAutoNovelTitle(@RequestParam int novelId, Model model) {
		if (chapterService.getChaptersByNovelId(novelId).size() > 1) {
			int size = chapterService.getChaptersByNovelId(novelId).size() + 1;
			return new ResultData("S-1", "" + size, novelId);
		} else {
			return new ResultData("F-1", String.format("없음..."), novelId);
		}
	}
}