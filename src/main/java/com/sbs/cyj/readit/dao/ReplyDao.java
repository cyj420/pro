package com.sbs.cyj.readit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Reply;

@Mapper
public interface ReplyDao {

	void write(Map<String, Object> param);

	List<Reply> getForPrintReplies(Map<String, Object> param);

	Reply getReplyById(int id);

	void deleteReplyById(int id);
	
}
