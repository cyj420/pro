package com.sbs.cyj.readit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.SeriesDao;
import com.sbs.cyj.readit.dto.Series;

@Service
public class SeriesService {
	@Autowired
	private SeriesDao seriesDao;

	public List<Series> getSeriesByMemberId(int memberId) {
		return seriesDao.getSeriesByMemberId(memberId);
	}

	public Series getSeriesByArticleId(int articleId) {
		return seriesDao.getSeriesByArticleId(articleId);
	}
}