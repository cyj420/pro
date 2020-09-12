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
}