package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.NovelDao;
import com.sbs.cyj.readit.dto.Category;
import com.sbs.cyj.readit.dto.Novel;
import com.sbs.cyj.readit.util.Util;

@Service
public class NovelService {
	@Autowired
	private NovelDao novelDao;

	public List<Category> getCategories() {
		return novelDao.getCategories();
	}

	public List<Novel> getNovelsByMemberId(int memberId) {
		return novelDao.getNovelsByMemberId(memberId);
	}

	public List<Novel> getNovels() {
		return novelDao.getNovels();
	}
	

	public Novel getNovelById(int id) {
		return novelDao.getNovelById(id);
	}

	public boolean checkNovelExistByMemberId(int memberId) {
		Novel novel = null;
		List<Novel> novels = novelDao.checkNovelExistByMemberId(memberId);
		if(novels.size() > 0) {
			novel = novels.get(0);
		}
		if(novel == null) {
			return false;
		}
		return true;
	}

	public void genDefaultNovel(Map<String, Object> param) {
		novelDao.genDefaultNovel(param);
	}

	public int genNovel(Map<String, Object> param) {
		novelDao.genNovel(param);
		
		int id = Util.getAsInt(param.get("id"));
		
		return id;
	}

	public void delete(int id) {
		novelDao.delete(id);
	}

	public boolean isExistsNameDup(String name) {
		Novel novel = novelDao.isExistsNameDup(name);
		
		if(novel != null) {
			return true;
		}
		return false;
	}

	public int modifyNovel(Map<String, Object> param) {
		novelDao.modifyNovel(param);
		
		int id = Util.getAsInt(param.get("id"));
		
		return id;
	}

	public List<Novel> getNovelsByCateId(int cateId) {
		return novelDao.getNovelsByCateId(cateId);
	}

	public void updateTotalHitByNovelId(int novelId, int totalHit) {
		novelDao.updateTotalHitByNovelId(novelId, totalHit);
	}

	public List<Novel> getNovelsBySearchKeyword(String searchKeyword) {
		return novelDao.getNovelsBySearchKeyword(searchKeyword);
	}

	public List<Novel> getNovelsByMemberIdAndSearchKeyword(int memberId, String searchKeyword) {
		return novelDao.getNovelsByMemberIdAndSearchKeyword(memberId, searchKeyword);
	}

	public void upTotalChByNovelId(int novelId) {
		novelDao.upTotalChByNovelId(novelId);
	}

	public void downTotalChByNovelId(int novelId) {
		novelDao.downTotalChByNovelId(novelId);
	}

	public void updateTotalChByNovelId(int novelId, int totalCh) {
		novelDao.updateTotalChByNovelId(novelId, totalCh);
	}

	public List<Novel> getNovelsByMemberIdForSetup(int memberId) {
		return novelDao.getNovelsByMemberIdForSetup(memberId);
	}

	public List<Novel> getNovelsForPrint(int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return novelDao.getNovelsForPrint(start, itemsInOnePage);
	}

	public List<Novel> getNovelsBySearchKeywordForPrint(String searchKeyword, int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return novelDao.getNovelsBySearchKeywordForPrint(searchKeyword, start, itemsInOnePage);
	}

	public List<Novel> getNovelsByMemberIdAndSearchKeywordForPrint(int memberId, String searchKeyword,
			int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return novelDao.getNovelsByMemberIdAndSearchKeywordForPrint(memberId, searchKeyword, start, itemsInOnePage);
	}

	public List<Novel> getNovelsByMemberIdForPrint(int memberId, int itemsInOnePage, int page) {
		int start = itemsInOnePage * ( page - 1 );
		return novelDao.getNovelsByMemberIdForPrint(memberId, start, itemsInOnePage);
	}

//	public List<Novel> getNovelsByIdAndSearchKeyword(int novelId, String searchKeyword) {
//		return novelDao.getNovelsByIdAndSearchKeyword(novelId, searchKeyword);
//	}
//
//	public List<Novel> getNovelsByIdAndSearchKeywordForPrint(int novelId, String searchKeyword, int itemsInOnePage,
//			int page) {
//		int start = itemsInOnePage * ( page - 1 );
//		return novelDao.getNovelsByIdAndSearchKeywordForPrint(novelId, searchKeyword, start, itemsInOnePage);
//	}
	
	
}