package com.sbs.cyj.readit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Series;

@Mapper
public interface SeriesDao {

	List<Series> getSeriesByMemberId(int memberId);

	Series getSeriesByArticleId(int articleId);

}
