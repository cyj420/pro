package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Novel;

@Mapper
public interface NovelDao {

	List<Category> getCategories();
	
	List<Novel> getNovelsByMemberId(int memberId);

	List<Novel> getNovels();

	Novel getNovelById(int id);

	List<Novel> checkNovelExistByMemberId(int memberId);

	void genDefaultNovel(Map<String, Object> param);

	void genNovel(Map<String, Object> param);

	void delete(int id);

	Novel isExistsNameDup(String name);

	void modifyNovel(Map<String, Object> param);

	List<Novel> getNovelsByCateId(int cateId);

	void updateTotalHitByNovelId(int novelId, int totalHit);

	List<Novel> getNovelsBySearchKeyword(String searchKeyword);

	List<Novel> getNovelsByMemberIdAndSearchKeyword(int memberId, String searchKeyword);

	void upTotalChByNovelId(int novelId);

	void downTotalChByNovelId(int novelId);

	void updateTotalChByNovelId(int novelId, int totalCh);

	List<Novel> getNovelsByMemberIdForSetup(int memberId);

	List<Novel> getNovelsForPrint(int start, int itemsInOnePage);

	List<Novel> getNovelsBySearchKeywordForPrint(String searchKeyword, int start, int itemsInOnePage);

	List<Novel> getNovelsByMemberIdAndSearchKeywordForPrint(int memberId, String searchKeyword, int start, int itemsInOnePage);

	List<Novel> getNovelsByMemberIdForPrint(int memberId, int start, int itemsInOnePage);

}
