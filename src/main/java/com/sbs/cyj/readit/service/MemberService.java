package com.sbs.cyj.readit.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.MemberDao;
import com.sbs.cyj.readit.dto.Member;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MailService mailService;

	public int join(Map<String, Object> param) {
		int id = memberDao.join(param);
		sendJoinCompleteMail((String) param.get("email"));
		return id;
	}

	public Member login(Map<String, Object> param) {
		return memberDao.login(param);
	}

	public void modify(Map<String, Object> param) {
		memberDao.modify(param);
	}

	public Member getMemberById(int id) {
		return memberDao.getMemberById(id);
	}

	public String findLoginIdByNameAndEmail(Map<String, Object> param) {
		return memberDao.findLoginIdByNameAndEmail(param);
	}
	
	private void sendJoinCompleteMail(String email) {
		String mailTitle = String.format("[%s] 가입이 완료되었습니다.", "~사이트 이름~");

		StringBuilder mailBodySb = new StringBuilder();
		mailBodySb.append("<h1>가입이 완료되었습니다.</h1>");
		mailBodySb.append(String.format("<p><a href=\"%s\" target=\"_blank\">%s</a>로 이동</p>", "http://localhost:8085/home/main", "[사이트 이름]"));

		mailService.send(email, mailTitle, mailBodySb.toString());
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public Member getMemberByEmail(String email) {
		return memberDao.getMemberByEmail(email);
	}
}
