package com.sbs.cyj.readit.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sbs.cyj.readit.dto.Member;

@Mapper
public interface MemberDao {

	void join(Map<String, Object> param);

	Member login(Map<String, Object> param);

	void modify(Map<String, Object> param);

	Member getMemberById(int id);

	String findLoginIdByNameAndEmail(Map<String, Object> param);

	Member getMemberByLoginId(String loginId);

	Member getMemberByEmail(String email);

	void resetLoginPw(Map<String, Object> param);

	void withdrawal(int id);

	Member isJoinableLoginId(String loginId);

	void doAuthMail(int id);
}
