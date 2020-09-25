package com.sbs.cyj.readit.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.cyj.readit.dao.MemberDao;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.ResultData;
import com.sbs.cyj.readit.util.Util;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MailService mailService;
	@Autowired
	private AttrService attrService;

	public int join(Map<String, Object> param) {
		memberDao.join(param);
		int id = Util.getAsInt(param.get("id"));
		
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
	
	public void sendJoinCompleteMail(Map<String, Object> param, String code) {
		String mailTitle = String.format("[%s] 가입을 축하합니다.", "Readit");
		StringBuilder mailBodySb = new StringBuilder();
		mailBodySb.append("<h1>가입이 완료되었습니다.</h1>");
		mailBodySb.append(StrAuthCode(code, (String)param.get("nickname")));
		mailBodySb.append(siteLink());

		mailService.send((String)param.get("email"), mailTitle, mailBodySb.toString());
	}
	
	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public Member getMemberByEmail(String email) {
		return memberDao.getMemberByEmail(email);
	}

	public int resetLoginPw(int actorId, Map<String, Object> param) {
		String tempPw = UUID.randomUUID().toString();
		tempPw = tempPw.substring(0, 8);
		
		attrService.setValue("member__" + actorId + "__extra__useTempPassword", "1", Util.getDateStrLater(60 * 60));
		
		sendResetMail((String)param.get("email"), (String) param.get("name"), tempPw);
		
		String newPw = "";
		try {
			newPw = transformString(tempPw);
		} catch (NoSuchAlgorithmException e) {
			return -1;
		}
		
		param.put("tempPw", newPw);
		
		memberDao.resetLoginPw(param);
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

	private void sendResetMail(String email, String name, String tempPw) {
		String mailTitle = String.format("[%s] 임시 비밀번호입니다.", "Readit");

		StringBuilder mailBodySb = new StringBuilder();
		
		mailBodySb.append(String.format("<h1>%s님의 임시 비밀번호 : [%s]</h1>", name, tempPw));
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
	
	public boolean isJoinableNickname(String nickname) {
		if(memberDao.isJoinableNickname(nickname) == null) {
			return true;
		}
		else {
			return false;
		}
	}

	public int compareDate(String lastUpdateDate) {
        String strStartDate = lastUpdateDate.substring(0, 10).replace("-", "");
        
        SimpleDateFormat nowDate = new SimpleDateFormat ( "yyyyMMdd");
        Date time = new Date();
        String strEndDate = nowDate.format(time);
        
        String strFormat = "yyyyMMdd";    //strStartDate 와 strEndDate 의 format
        
        //SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        
        int result = 0;
        
        try{
            Date startDate = sdf.parse(strStartDate);
            Date endDate = sdf.parse(strEndDate);
 
            //두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
            result = (int) ((endDate.getTime() - startDate.getTime()) / (24*60*60*1000));
        }catch(ParseException e){
        }
        return result;
    }
	
	public void sendAuthMail(String email, String code, String nickname) {
		String mailTitle = String.format("[%s] 인증을 위한 메일입니다.", "Readit");
		StringBuilder mailBodySb = new StringBuilder();
		mailBodySb.append(StrAuthCode(code, nickname));
		mailBodySb.append(siteLink());

		mailService.send(email, mailTitle, mailBodySb.toString());
	}

	public void doAuthMail(int id) {
		memberDao.doAuthMail(id);
	}
	
	private String siteLink() {
		return "<p><a href='https://readit.ilcho.site' target=\"_blank\">[Readit] 사이트로 이동</a></p>";
	}
	
	private String StrAuthCode(String code, String nickname) {
		return "<a href='https://readit.ilcho.site/usr/member/doAuthMail?code="+code+"'>["+nickname+"님 메일 인증하기]</a><br><a href='localhost:8085/usr/member/doAuthMail?code="+code+"'>[LOCAL - "+nickname+"님 메일 인증하기]</a>";
	}
	
	public String genCheckPasswordAuthCode(int actorId) {
		String authCode = UUID.randomUUID().toString();
		attrService.setValue("member__" + actorId + "__extra__modifyPrivateAuthCode", authCode, Util.getDateStrLater(60 * 60));

		return authCode;
	}

	public ResultData checkValidCheckPasswordAuthCode(int actorId, String checkPasswordAuthCode) {
		if (attrService.getValue("member__" + actorId + "__extra__modifyPrivateAuthCode").equals(checkPasswordAuthCode)) {
			return new ResultData("S-1", "유효한 키 입니다.");
		}

		return new ResultData("F-1", "유효하지 않은 키 입니다.");
	}

	public String genMailAuthCode(int actorId) {
		String authCode = UUID.randomUUID().toString();
		attrService.setValue("member__" + actorId + "__extra__mailAuthCode", authCode, Util.getDateStrLater(60 * 60));
		return authCode;
	}

	public String getNowTime() {
		SimpleDateFormat form = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date dateTime = new Date();
		String nowTime = form.format(dateTime);
		
		return nowTime;
	}

	public Member getMemberByNickname(String nickname) {
		return memberDao.getMemberByNickname(nickname);
	}

	public void removeAuthMailCode(int id) {
		attrService.remove("member__" + id + "__extra__mailAuthCode");
	}
}
