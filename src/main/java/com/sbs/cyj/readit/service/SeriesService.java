package com.sbs.cyj.readit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.SeriesDao;
import com.sbs.cyj.readit.dto.Series;
import com.sbs.cyj.readit.util.Util;

@Service
public class SeriesService {
	@Autowired
	private SeriesDao seriesDao;

	public List<Series> getAllSeriesByMemberId(int memberId) {
		return seriesDao.getAllSeriesByMemberId(memberId);
	}

	public Series getSeriesByArticleId(int articleId) {
		return seriesDao.getSeriesByArticleId(articleId);
	}

	public int addSeries(Map<String, Object> param) {
		seriesDao.addSeries(param);
		int id = Util.getAsInt(param.get("id"));
		
		return id;
	}

	public Series getSeriesById(int seriesId) {
		return seriesDao.getSeriesById(seriesId);
	}
}