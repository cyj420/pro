package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Series;

@Mapper
public interface SeriesDao {

	List<Series> getAllSeriesByMemberId(int memberId);

	Series getSeriesByArticleId(int articleId);

	int addSeries(Map<String, Object> param);

	Series getSeriesById(int seriesId);

}
