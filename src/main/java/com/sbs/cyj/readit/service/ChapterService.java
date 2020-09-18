package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.ChapterDao;
import com.sbs.cyj.readit.dto.Chapter;
import com.sbs.cyj.readit.util.Util;

@Service
public class ChapterService {
	@Autowired
	private ChapterDao chapterDao;

	public int write(Map<String, Object> param) {
		chapterDao.write(param);
		
		int id = Util.getAsInt(param.get("id"));
		
		return id;
	}

	public Chapter getChapterById(int id) {
		return chapterDao.getChapterById(id);
	}

	public List<Chapter> getChaptersByNovelId(int novelId) {
		return chapterDao.getChaptersByNovelId(novelId);
	}

	public void deleteChaptersByNovelId(int novelId) {
		chapterDao.deleteChaptersByNovelId(novelId);
	}

	public void deleteChapterById(int id) {
		chapterDao.deleteChapterById(id);
	}

	public int modifyChapter(Map<String, Object> param) {
		chapterDao.modifyChapter(param);
		
		int id = Util.getAsInt(param.get("id"));
		
		return id;
	}

	public List<Chapter> getChapters() {
		return chapterDao.getChapters();
	}

	public void updateHitByChapterId(int id) {
		chapterDao.updateHitByChapterId(id);
	}

	public List<Chapter> getChaptersBySearchKeywordAndSearchKeywordType(String searchKeyword, String searchKeywordType) {
		return chapterDao.getChaptersBySearchKeywordAndSearchKeywordType(searchKeyword, searchKeywordType);
	}

	public List<Chapter> getChaptersByWriterId(int memberId) {
		return chapterDao.getChaptersByWriterId(memberId);
	}

	public List<Chapter> getChaptersByWriterIdAndSearchKeywordAndSearchKeywordType(int memberId, String searchKeyword, String searchKeywordType) {
		return chapterDao.getChaptersByWriterIdAndSearchKeywordAndSearchKeywordType(memberId, searchKeyword, searchKeywordType);
	}

	public List<Chapter> getChaptersForPrint(int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return chapterDao.getChaptersForPrint(start, itemsInOnePage);
	}

	public List<Chapter> getChaptersBySearchKeywordAndSearchKeywordTypeForPrint(String searchKeyword,
			String searchKeywordType, int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return chapterDao.getChaptersBySearchKeywordAndSearchKeywordTypeForPrint(searchKeyword,	searchKeywordType, start, itemsInOnePage);
	}
}