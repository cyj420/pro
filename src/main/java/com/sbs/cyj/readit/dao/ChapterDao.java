package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Chapter;

@Mapper
public interface ChapterDao {

	void write(Map<String, Object> param);

	Chapter getChapterById(int id);

	List<Chapter> getChaptersByNovelId(int novelId);

	void deleteChaptersByNovelId(int novelId);

	void deleteChapterById(int id);

	void modifyChapter(Map<String, Object> param);

	List<Chapter> getChapters();

	void updateHitByChapterId(int id);

	List<Chapter> getChaptersBySearchKeywordAndSearchKeywordType(String searchKeyword, String searchKeywordType);

	List<Chapter> getChaptersByWriterId(int memberId);

	List<Chapter> getChaptersByWriterIdAndSearchKeywordAndSearchKeywordType(int memberId, String searchKeyword, String searchKeywordType);

	List<Chapter> getChaptersForPrint(int start, int itemsInOnePage);

	List<Chapter> getChaptersBySearchKeywordAndSearchKeywordTypeForPrint(String searchKeyword, String searchKeywordType,
			int start, int itemsInOnePage);

}
