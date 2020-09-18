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

		if (req.getParameter("novelId") != null) {
			String strNovelId = req.getParameter("novelId");
			int novelId = Integer.parseInt(strNovelId);

			Novel novel = novelService.getNovelById(novelId);
			model.addAttribute("novel", novel);
		}

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
		novelService.upTotalChByNovelId(novelId);

		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id + "번째 글을 작성하였습니다."));
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}

	// 챕터 삭제
	@RequestMapping("usr/novel/{nickname}-doDeleteChapter")
	public String doDeleteChapter(HttpServletRequest req, @PathVariable("nickname") String nickname, String listUrl,
			Model model) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		int id = Integer.parseInt(req.getParameter("id"));
		Chapter chapter = chapterService.getChapterById(id);

		List<Chapter> chapters = chapterService.getChaptersByNovelId(chapter.getNovelId());

		String redirectUri = listUrl;

		if (chapters.size() > 1) {
			redirectUri = listUrl + "?novelId=" + chapter.getNovelId();
		}

		if (loginedMemberId == chapter.getMemberId()) {
			int novelId = chapterService.getChapterById(id).getNovelId();
			chapterService.deleteChapterById(id);
			novelService.downTotalChByNovelId(novelId);

			model.addAttribute("msg", "삭제되었습니다.");
		} else {
			model.addAttribute("msg", "잘못된 접근입니다.");
		}

		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}

	// 챕터 수정
	@RequestMapping("usr/novel/{nickname}-modifyChapter")
	public String showModifyChapter(@PathVariable("nickname") String nickname, Model model, String listUrl,
			HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		int id = Integer.parseInt(req.getParameter("id"));
		Chapter chapter = chapterService.getChapterById(id);

		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		model.addAttribute("member", member);

		model.addAttribute("chapter", chapter);

		List<Novel> novels = novelService.getNovelsByMemberId(memberId);
		model.addAttribute("novels", novels);

		return "novel/modifyChapter";
	}

	@RequestMapping("usr/novel/{nickname}-doModifyChapter")
	public String doModifyChapter(@RequestParam Map<String, Object> param, HttpServletRequest req,
			@PathVariable("nickname") String nickname, Model model) {
		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);

		int novelId = Integer.parseInt((String) param.get("novelId"));
		int cateId = novelService.getNovelById(novelId).getCateId();

		param.put("memberId", member.getId());
		param.put("cateId", cateId);

		int id = chapterService.modifyChapter(param);

		String redirectUri = (String) param.get("redirectUri");
		redirectUri = redirectUri.replace("#id", id + "");

		model.addAttribute("msg", String.format(id + "번째 글을 수정하였습니다."));
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}

	// 게시글 리스트 (전체)
	@RequestMapping("usr/novel/list")
	public String showTotalList(Model model, String listUrl, HttpServletRequest req) {
		Member member = (Member) req.getAttribute("loginedMember");
		if (listUrl == null) {
			listUrl = "./" + member.getNickname() + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		if (req.getParameter("mode") != null) {
			String str = req.getParameter("mode");

			int page = 1;
			int fullPage = 0;
			int itemsInOnePage = 5;
			String searchKeyword = req.getParameter("searchKeyword");

			if (str.equals("totalNovel")) {
				List<Novel> novels = null;

				if (searchKeyword == null || searchKeyword.trim().length() == 0) {
					novels = novelService.getNovels();

					for (int i = 0; i < novels.size(); i++) {
						int novelId = novels.get(i).getId();
						List<Chapter> chapters = chapterService.getChaptersByNovelId(novelId);
						novelService.updateTotalChByNovelId(novelId, chapters.size());
					}
				} else {
					novels = novelService.getNovelsBySearchKeyword(searchKeyword);
				}

				// 노벨 조회수
				for (int i = 0; i < novels.size(); i++) {
					int totalHit = 0;

					List<Chapter> chapters = chapterService.getChaptersByNovelId(novels.get(i).getId());

					for (int j = 0; j < chapters.size(); j++) {
						totalHit += chapters.get(j).getHit();
					}

					novelService.updateTotalHitByNovelId(novels.get(i).getId(), totalHit);
				}

				// novels를 다시 불러오는 이유: hit의 최신화
				// 페이징
				fullPage = (novels.size() - 1) / itemsInOnePage + 1;
				page = Integer.parseInt(req.getParameter("page"));

				if (searchKeyword == null || searchKeyword.trim().length() == 0) {
					novels = novelService.getNovelsForPrint(itemsInOnePage, page);

				} else {
					novels = novelService.getNovelsBySearchKeywordForPrint(searchKeyword, itemsInOnePage, page);
				}
				model.addAttribute("novels", novels);
			} else if (str.equals("totalChapter")) {
				List<Chapter> chapters = chapterService.getChapters();
				
				if (searchKeyword == null || searchKeyword.trim().length() == 0) {
					fullPage = (chapters.size() - 1) / itemsInOnePage + 1;
					page = Integer.parseInt(req.getParameter("page"));
					chapters = chapterService.getChaptersForPrint(itemsInOnePage, page);
				} else {
					String searchKeywordType = req.getParameter("searchKeywordType");
					chapters = chapterService.getChaptersBySearchKeywordAndSearchKeywordType(searchKeyword, searchKeywordType);
					
					fullPage = (chapters.size() - 1) / itemsInOnePage + 1;
					page = Integer.parseInt(req.getParameter("page"));
					
					chapters = chapterService.getChaptersBySearchKeywordAndSearchKeywordTypeForPrint(searchKeyword, searchKeywordType, itemsInOnePage, page);
				}
				model.addAttribute("chapters", chapters);
			}
			model.addAttribute("page", page);
			model.addAttribute("fullPage", fullPage);
		}

		return "novel/totalList";
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

		int page = 1;
		int fullPage = 0;
		int itemsInOnePage = 5;
		String searchKeyword = req.getParameter("searchKeyword");
		
		List<Chapter> chapters = null;
		
		// 소설당 챕터 개수 파악하기 위함
		for (int i = 0; i < novels.size(); i++) {
			int novelId = novels.get(i).getId();
			chapters = chapterService.getChaptersByNovelId(novelId);
			novelService.updateTotalChByNovelId(novelId, chapters.size());
		}

		if (req.getParameter("mode") != null) {
			String mode = req.getParameter("mode");

			if (mode.equals("chapter")) {
				// 챕터 별 보기일 때
				if (searchKeyword != null) {
					String searchKeywordType = req.getParameter("searchKeywordType");
					chapters = chapterService.getChaptersByWriterIdAndSearchKeywordAndSearchKeywordType(memberId,
							searchKeyword, searchKeywordType);
				} else {
					chapters = chapterService.getChaptersByWriterId(memberId);
				}
				model.addAttribute("chapters", chapters);
			}
		}

		if (req.getParameter("novelId") != null) {
			String strNovelId = req.getParameter("novelId");
			int novelId = Integer.parseInt(strNovelId);

			// 페이징
			fullPage = (chapters.size() - 1) / itemsInOnePage + 1;
			if(req.getParameter("page") != null) {
				page = Integer.parseInt(req.getParameter("page"));
			}
			
			chapters = chapterService.getChaptersByNovelIdForPrint(novelId, itemsInOnePage, page);
			
			model.addAttribute("chapters", chapters);

			int totalHit = 0;

			for (int i = 0; i < chapters.size(); i++) {
				totalHit += chapters.get(i).getHit();
			}

			// 노벨 조회수
			novelService.updateTotalHitByNovelId(novelId, totalHit);

			Novel novel = novelService.getNovelById(novelId);
			model.addAttribute("novel", novel);
			model.addAttribute("novelId", novelId);
		}

		// 노벨 조회수
		for (int i = 0; i < novels.size(); i++) {
			int totalHit = 0;

			chapters = chapterService.getChaptersByNovelId(novels.get(i).getId());

			for (int j = 0; j < chapters.size(); j++) {
				totalHit += chapters.get(j).getHit();
			}

			novelService.updateTotalHitByNovelId(novels.get(i).getId(), totalHit);
		}

		// 페이징
		fullPage = (novels.size() - 1) / itemsInOnePage + 1;
		if(req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}

		if (req.getParameter("mode") != null) {
			if (req.getParameter("mode").equals("novel") && searchKeyword != null) {
				novels = novelService.getNovelsByMemberIdAndSearchKeyword(memberId, searchKeyword);
				fullPage = (novels.size() - 1) / itemsInOnePage + 1;
				novels = novelService.getNovelsByMemberIdAndSearchKeywordForPrint(memberId, searchKeyword, itemsInOnePage, page);
			}
		}
		else {
			novels = novelService.getNovelsByMemberIdForPrint(memberId, itemsInOnePage, page);
		}
		
		model.addAttribute("page", page);
		model.addAttribute("fullPage", fullPage);
		model.addAttribute("novels", novels);
		model.addAttribute("nickname", nickname);
		model.addAttribute("loginedMemberId", (int) req.getAttribute("loginedMemberId"));

		return "novel/list";
	}

	// 챕터 상세보기
	@RequestMapping("usr/novel/{nickname}-detail")
	public String showDetail(@RequestParam Map<String, Object> param, Model model,
			@PathVariable("nickname") String nickname, String listUrl, HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		Member loginedMember = memberService.getMemberById(loginedMemberId);
		model.addAttribute("loginedMember", loginedMember);

		int id = Integer.parseInt((String) param.get("id"));

		// 조회수
		chapterService.updateHitByChapterId(id);

		Chapter chapter = chapterService.getChapterById(id);

		Novel novel = novelService.getNovelById(chapter.getNovelId());

		// 같은 소설의 이전글/다음글
		if (novel.isSeriesStatus()) {
			List<Chapter> chapters = chapterService.getChaptersByNovelId(novel.getId());
			int novelSize = chapters.size();

			if (novelSize > 1) {
				model.addAttribute("novel", novel);

				int novelCh = 0;
				int novelPreCh = 0;
				int novelNextCh = 0;

				for (int i = 0; i < novelSize; i++) {
					if (chapters.get(novelSize - i - 1).getId() == chapter.getId()) {
						novelCh = i + 1;
						if (i != 0) {
							novelPreCh = chapters.get(novelSize - i).getId();
							String novelPreChName = chapters.get(novelSize - i).getTitle();
							model.addAttribute("novelPreCh", novelPreCh);
							model.addAttribute("novelPreChName", novelPreChName);
						}
						if (i != novelSize - 1) {
							novelNextCh = chapters.get(novelSize - i - 2).getId();
							String novelNextChName = chapters.get(novelSize - i - 2).getTitle();
							model.addAttribute("novelNextCh", novelNextCh);
							model.addAttribute("novelNextChName", novelNextChName);
						}
					}
				}
				model.addAttribute("novelSize", novelSize);
				model.addAttribute("novelCh", novelCh);
			}
		}

		// 소설과 무관한 이전글/다음글
		{
			List<Chapter> chapters = null;

			if (req.getParameter("searchKeyword") != null) {
				String mode = req.getParameter("mode");
				String searchKeywordType = req.getParameter("searchKeywordType");
				String searchKeyword = req.getParameter("searchKeyword");

				if (mode.substring(0, 5).equals("total")) {
					// total로 판단
					chapters = chapterService.getChaptersBySearchKeywordAndSearchKeywordType(searchKeyword,
							searchKeywordType);
				} else {
					// 작가별로 판단
					int memberId = memberService.getMemberByNickname(nickname).getId();
					chapters = chapterService.getChaptersByWriterIdAndSearchKeywordAndSearchKeywordType(memberId,
							searchKeyword, searchKeywordType);
				}

				model.addAttribute("mode", mode);
				model.addAttribute("searchKeywordType", searchKeywordType);
				model.addAttribute("searchKeyword", searchKeyword);
			} else {
				Member writer = memberService.getMemberByNickname(nickname);
				chapters = chapterService.getChaptersByWriterId(writer.getId());
			}

			int size = chapters.size();

			if (size > 1) {
				int ch = 0;
				int preCh = 0;
				int nextCh = 0;

				for (int i = 0; i < size; i++) {
					if (chapters.get(size - i - 1).getId() == chapter.getId()) {
						ch = i + 1;
						if (i != 0) {
							preCh = chapters.get(size - i).getId();
							String preChName = chapters.get(size - i).getTitle();
							model.addAttribute("preCh", preCh);
							model.addAttribute("preChName", preChName);
						}
						if (i != size - 1) {
							nextCh = chapters.get(size - i - 2).getId();
							String nextChName = chapters.get(size - i - 2).getTitle();
							model.addAttribute("nextCh", nextCh);
							model.addAttribute("nextChName", nextChName);
						}
					}
				}
				model.addAttribute("ch", ch);
			}
		}
		if (req.getParameter("novelId") != null) {
			model.addAttribute("novelId", req.getParameter("novelId"));
		}
		model.addAttribute("chapter", chapter);

		return "novel/detail";
	}

	// 소설 삭제
	@RequestMapping("usr/novel/{nickname}-doDelete")
	public String doDelete(@RequestParam Map<String, Object> param, HttpServletRequest req,
			@PathVariable("nickname") String nickname, String listUrl, Model model) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		int id = Integer.parseInt(req.getParameter("id"));
		Novel novel = novelService.getNovelById(id);

		if (loginedMemberId == novel.getMemberId()) {
			novelService.delete(id);
			chapterService.deleteChaptersByNovelId(id);
		}

		String redirectUri = (String) param.get("redirectUri");

		model.addAttribute("msg", "삭제되었습니다.");
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}

	// 소설 수정
	@RequestMapping("usr/novel/{nickname}-modify")
	public String showModifyNovel(@PathVariable("nickname") String nickname, Model model, String listUrl,
			HttpServletRequest req) {
		if (listUrl == null) {
			listUrl = "./" + nickname + "-list";
		}
		model.addAttribute("listUrl", listUrl);

		int id = Integer.parseInt(req.getParameter("id"));
		Novel novel = novelService.getNovelById(id);

		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		model.addAttribute("member", member);

		List<Category> categories = novelService.getCategories();
		model.addAttribute("categories", categories);

		model.addAttribute("novel", novel);

		return "novel/modifyNovel";
	}

	@RequestMapping("usr/novel/{nickname}-doModify")
	public String doModifyNovel(@RequestParam Map<String, Object> param, HttpServletRequest req,
			@PathVariable("nickname") String nickname, Model model) {
		int memberId = (int) req.getAttribute("loginedMemberId");

		int writerId = memberService.getMemberByNickname(nickname).getId();

		if (memberId == writerId) {
			int id = novelService.modifyNovel(param);

			String redirectUri = (String) param.get("redirectUri");
			redirectUri = redirectUri.replace("#id", id + "");

			model.addAttribute("msg", String.format(id + "번째 노벨을 수정하였습니다."));
			model.addAttribute("redirectUri", redirectUri);
		} else {
			model.addAttribute("msg", "잘못된 접근입니다.");
			model.addAttribute("redirectUri", "/home/main");
		}

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

		if (loginedMember.getNickname().equals(nickname)) {
			List<Novel> novels = novelService.getNovelsByMemberIdForSetup(memberId);
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

	// ajax - 소설 제목 중복 방지
	@RequestMapping("/usr/novel/getNameDup")
	@ResponseBody
	public ResultData doGetNameDup(@RequestParam String name, Model model) {
		if (novelService.isExistsNameDup(name)) {
			return new ResultData("F-1", String.format("이미 존재하는 소설 제목입니다."), name);
		} else {
			return new ResultData("S-1", String.format("사용할 수 있는 소설 제목입니다."), name);
		}
	}

	// ajax - 자동 제목 생성
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