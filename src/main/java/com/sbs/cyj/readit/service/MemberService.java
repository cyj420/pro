package com.sbs.cyj.readit.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.MemberDao;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.util.Util;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MailService mailService;

	public int join(Map<String, Object> param) {
		memberDao.join(param);
		int id = Util.getAsInt(param.get("id"));
		
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
		mailBodySb.append(siteLink());

		mailService.send(email, mailTitle, mailBodySb.toString());
	}

	private String siteLink() {
		return String.format("<p><a href=\"%s\" target=\"_blank\">%s</a>로 이동</p>", "http://localhost:8085/usr/home/main", "[사이트 이름]");
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public Member getMemberByEmail(String email) {
		return memberDao.getMemberByEmail(email);
	}

	public int resetLoginPw(Map<String, Object> param) {
		String tempPw = ""+System.currentTimeMillis();
		String newPw = "";
		try {
			newPw = transformString(tempPw);
		} catch (NoSuchAlgorithmException e) {
			return -1;
		}
		sendResetMail((String)param.get("email"), tempPw);
		
		param.put("tempPw", newPw);
		
		memberDao.resetLoginId(param);
		return 1;
	}

	private String transformString(String msg) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		return bytesToHex(md.digest());
	}
	
	private String bytesToHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for(byte b : digest) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private void sendResetMail(String email, String tempPw) {
		String mailTitle = String.format("[%s] 임시 비밀번호입니다.", "~사이트 이름~");

		StringBuilder mailBodySb = new StringBuilder();
		
		mailBodySb.append(String.format("<h1>임시 비밀번호 : [%s]</h1>", tempPw));
		mailBodySb.append(siteLink());

		mailService.send(email, mailTitle, mailBodySb.toString());
	}

	public void withdrawal(int id) {
		memberDao.withdrawal(id);
	}

	public boolean isJoinableLoginId(String loginId) {
		if(memberDao.isJoinableLoginId(loginId) == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
