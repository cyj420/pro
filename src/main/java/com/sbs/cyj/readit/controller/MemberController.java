package com.sbs.cyj.readit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.ResultData;
import com.sbs.cyj.readit.service.AttrService;
import com.sbs.cyj.readit.service.MemberService;
import com.sbs.cyj.readit.service.NovelService;
import com.sbs.cyj.readit.util.Util;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private AttrService attrService;
	@Autowired
	private NovelService novelService;

	// 회원가입
	@RequestMapping("/usr/member/join")
	public String showJoin() {
		return "member/join";
	}

	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param, Model model) {
		if (memberService.isJoinableLoginId((String) param.get("loginId"))) {
			int id = memberService.join(param);
			
			if (id > 0) {
				String code = memberService.genMailAuthCode(id);
				memberService.sendJoinCompleteMail(param, code);
				String now = memberService.getNowTime();
				attrService.setValue("member__" + id + "__extra__lastPasswordChangeDate", now, "2030-01-01 01:01:01");
			}
			
			return "<script> alert('" + id + "번째 회원입니다.'); location.replace('../home/main'); </script>";
		}
		return "<script> alert('회원가입 실패'); history.back(); </script>";
	}

	// 로그인
	@RequestMapping("/usr/member/login")
	public String showLogin() {
		return "member/login";
	}

	@RequestMapping("/usr/member/doLogin")
	public String doLogin(String loginId, String loginPwReal, String redirectUri, Model model, HttpSession session) {
		String loginPw = loginPwReal;
		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "존재하지 않는 회원입니다.");
			return "common/redirect";
		}

		if (member.getLoginPw().equals(loginPw) == false) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "common/redirect";
		}

		session.setAttribute("loginedMemberId", member.getId());
		session.setAttribute("loginedMember", member.getId());

		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}
		
		String str = String.format("%s님, 안녕하세요.", member.getNickname());

		if(attrService.getValue("member__" + member.getId() + "__extra__useTempPassword").equals("1")) {
			str = "현재 임시 비밀번호를 사용중입니다.";
		}
		
		if(attrService.getValue("member__" + member.getId() + "__extra__lastPasswordChangeDate").length() != 0) {
			String lastUpdateDate = attrService.getValue("member__" + member.getId() + "__extra__lastPasswordChangeDate");
			int date = memberService.compareDate(lastUpdateDate); 
			if( date > 90) {
				str = String.format("%s님 비밀번호 변경한 지 %d일이 되었습니다.\\n보완을 위해 비밀번호를 바꿔주세요.", member.getNickname(), date);
			}
		}

		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("msg", str);
		

		return "common/redirect";
	}

	// 로그아웃
	@RequestMapping("/usr/member/doLogout")
	public String doLogout(Model model, HttpSession session, String redirectUri) {
		session.removeAttribute("loginedMemberId");
		session.removeAttribute("loginedMember");

		if (redirectUri.length() == 0) {
			System.out.println("redirectUri.length() == 0");
		}
		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}

		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("msg", String.format("로그아웃 되었습니다."));

		return "common/redirect";
	}

	// ID 찾기
	@RequestMapping("/usr/member/findLoginId")
	public String findLoginId() {
		return "member/findLoginId";
	}

	// ID 찾기
	@RequestMapping("/usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(@RequestParam Map<String, Object> param, Model model) {
		String loginId = memberService.findLoginIdByNameAndEmail(param);

		StringBuilder sb = new StringBuilder();

		sb.append("alert('ID : " + loginId + "');");
		sb.append("location.replace('./login');");

		sb.insert(0, "<script>");
		sb.append("</script>");

		if (loginId == null) {
			return "<script> alert('일치하는 ID가 없습니다.'); history.back(); </script>";
		}

		return sb.toString();
	}

	// PW 찾기
	@RequestMapping("/usr/member/findLoginPw")
	public String findLoginPw() {
		return "member/findLoginPw";
	}

	@RequestMapping("/usr/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPW(@RequestParam Map<String, Object> param, Model model) {
		String loginId = (String) param.get("loginId");
		Member member = memberService.getMemberByLoginId(loginId);

		StringBuilder sb = new StringBuilder();

		if (member != null) {
			if (member.getName().equals((String) param.get("name"))
					&& member.getEmail().equals((String) param.get("email"))) {
				if (memberService.resetLoginPw(member.getId(), param) > 0) {
					sb.append("alert('임시 PW를 메일로 보냈습니다.');");
					sb.append("location.replace('./login');");
				} else {
					sb.append("alert('메일 발송에 실패했습니다.');");
					sb.append("history.back();");
				}
			} else {
				sb.append("alert('일치하는 계정이 없습니다.');");
				sb.append("history.back();");
			}
		}

		sb.insert(0, "<script>");
		sb.append("</script>");

		return sb.toString();
	}

	// 회원 탈퇴
	@RequestMapping("/usr/member/withdrawal")
	public String doWithdrawal(@RequestParam int id, Model model, HttpSession session, String redirectUri) {
		String msg = "";
		
		if((int)session.getAttribute("loginedMemberId") == id) {
			memberService.withdrawal(id);
			session.removeAttribute("loginedMemberId");
			session.removeAttribute("loginedMember");
			
			msg = "탈퇴 완료"; 
		}
		else {
			msg = "탈퇴는 본인만 가능합니다.";
		}
		

		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}

		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("msg", msg);

		return "common/redirect";
	}

	// 아이디 중복 체크
	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData doGetLoginIdDup(@RequestParam String loginId, Model model) {
		if (memberService.isJoinableLoginId(loginId)) {
			return new ResultData("S-1", String.format("사용할 수 있는 아이디입니다."), loginId);
		} else {
			return new ResultData("F-1", String.format("이미 존재하는 아이디입니다."), loginId);
		}
	}
	
	// 닉네임 중복 체크
	@RequestMapping("/usr/member/getNicknameDup")
	@ResponseBody
	public ResultData doGetNicknameDup(@RequestParam String nickname, Model model) {
		if (memberService.isJoinableNickname(nickname)) {
			return new ResultData("S-1", String.format("사용할 수 있는 닉네임입니다."), nickname);
		} else {
			return new ResultData("F-1", String.format("이미 존재하는 닉네임입니다."), nickname);
		}
	}
	
	// 메일 인증하기
	@RequestMapping("/usr/member/doAuthMail")
	public String doAuthMail(Model model, HttpSession session, @RequestParam String code) {
		String msg = "";
		if(session.getAttribute("loginedMemberId")!=null) {
			int id = (int) session.getAttribute("loginedMemberId");
			Member member = memberService.getMemberById(id);
			
			if(!memberService.getMemberById(id).isAuthStatus()) {
				if(code.equals(attrService.getValue("member__"+id+"__extra__mailAuthCode"))){
					memberService.doAuthMail(id);
					msg = "인증 성공";
					memberService.removeAuthMailCode(id);
					
					Map<String, Object> param = new HashMap<String, Object>();
					
					param.put("memberId", id);
					param.put("nickname", member.getNickname());
					
					if(!novelService.checkNovelExistByMemberId(id)) {
						novelService.genDefaultNovel(param);
					}
				}
				else {
					String str = attrService.getValue("member__"+id+"__extra__mailAuthCode");
					msg = "인증 실패\\ncode : "+code+"\\nstr : "+str;
				}
			}
			else {
				msg = "현재 로그인 중인 계정은 이미 인증된 계정입니다.";
			}
		}
		else {
			// 로그인 가드가 있으니 이건 뜰 일 없겠지만...
			msg = "인증 실패 - 비로그인 상태";
		}

		model.addAttribute("redirectUri", "/usr/home/main");
		model.addAttribute("msg", msg);

		return "common/redirect";
	}
	
	// 인증 메일 보내기
	@RequestMapping("/usr/member/sendAuthMail")
	public String doSendAuthMail(Model model, HttpSession session) {
		String msg = "";
		int memberId = (int) session.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		
		if(memberId>0) {
			String code = attrService.getValue("member__"+memberId+"__extra__mailAuthCode");
			if(code.trim().length() == 0) {
				code = memberService.genMailAuthCode(memberId);
			}
			memberService.sendAuthMail(member.getEmail(), code, member.getNickname());
			msg = "메일 발송 완료";
		}
		else {
			msg = "로그인 후 이용 가능";
		}

		model.addAttribute("redirectUri", "/usr/home/main");
		model.addAttribute("msg", msg);

		return "common/redirect";
	}
	
	// 개인정보 들어가기 전 PW 체크
	@RequestMapping("/usr/member/checkPw")
	public String showCheckPw() {
		return "member/checkPw";
	}
	
	@RequestMapping("/usr/member/doCheckPw")
	public String doCheckPw(String loginPwReal, String redirectUri, Model model, HttpServletRequest req) {
		int memberId = (int) req.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		
		String loginPw = loginPwReal;
		
		if (member.getLoginPw().equals(loginPw) == false) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "common/redirect";
		}
		
		String authCode = memberService.genCheckPasswordAuthCode(memberId);

		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/member/myPage";
		}

		redirectUri = Util.getNewUri(redirectUri, "checkPasswordAuthCode", authCode);

		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}
	
	// 회원 정보
	@RequestMapping("/usr/member/myPage")
	public String showMyPage(HttpSession session, String checkPasswordAuthCode, Model model) {
		int memberId = (int) session.getAttribute("loginedMemberId");
		ResultData checkValidCheckPasswordAuthCodeResultData = memberService.checkValidCheckPasswordAuthCode(memberId, checkPasswordAuthCode);

		if (checkPasswordAuthCode == null || checkPasswordAuthCode.length() == 0) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "비밀번호 체크 인증코드가 없습니다.");
			return "common/redirect";
		}

		if (checkValidCheckPasswordAuthCodeResultData.isFail()) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", checkValidCheckPasswordAuthCodeResultData.getMsg());
			return "common/redirect";
		}
		return "member/myPage";
	}
	
	@RequestMapping("/usr/member/doMyPage")
	public String doMyPage(String redirectUri, Model model, HttpServletRequest req) {
		int memberId = (int) req.getAttribute("loginedMemberId");
		
		String authCode = memberService.genCheckPasswordAuthCode(memberId);

		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/member/modify";
		}

		redirectUri = Util.getNewUri(redirectUri, "checkPasswordAuthCode", authCode);

		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}
	
	// 회원 정보 수정
	@RequestMapping("/usr/member/modify")
	public String showModify(HttpSession session, String checkPasswordAuthCode, Model model) {
		int memberId = (int) session.getAttribute("loginedMemberId");
		ResultData checkValidCheckPasswordAuthCodeResultData = memberService.checkValidCheckPasswordAuthCode(memberId, checkPasswordAuthCode);

		if (checkPasswordAuthCode == null || checkPasswordAuthCode.length() == 0) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "비밀번호 체크 인증코드가 없습니다.");
			return "common/redirect";
		}

		if (checkValidCheckPasswordAuthCodeResultData.isFail()) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", checkValidCheckPasswordAuthCodeResultData.getMsg());
			return "common/redirect";
		}
		return "member/modify";
	}

	@RequestMapping("/usr/member/doModify")
	public String doModify(String redirectUri, @RequestParam Map<String, Object> param, HttpSession session, Model model) {
		int memberId = (int) session.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(memberId);
		
		String loginPwReal = (String) param.get("loginPwReal"); 
		
		if(loginPwReal.equals(member.getLoginPw())) {
			String email = (String) param.get("email");
			if(member.getEmail().equals(email)) {
				param.put("email", null);
			}
			
			String newPw = (String) param.get("newLoginPwReal"); 
			
			if(newPw.length() != 0) {
				if(attrService.getValue("member__" + member.getId() + "__extra__useTempPassword").equals("1")) {
					attrService.remove("member__" + memberId + "__extra__useTempPassword");
				}
				
				String now = memberService.getNowTime();
				attrService.setValue("member__" + member.getId() + "__extra__lastPasswordChangeDate", now, "2030-01-01 01:01:01");
			}
			
			memberService.modify(param);
			int id = Integer.parseInt((String) param.get("id"));
			session.setAttribute("loginedMemberId", id);
			session.setAttribute("loginedMember", memberService.getMemberById(id));
		}
		else {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "회원 정보 수정 실패 - 비밀번호가 다릅니다.");
			return "common/redirect";
		}

		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/member/myPage";
		}

		String authCode = memberService.genCheckPasswordAuthCode(memberId);
		
		redirectUri = Util.getNewUri(redirectUri, "checkPasswordAuthCode", authCode);

		model.addAttribute("msg", "회원 정보 수정 완료");
		model.addAttribute("redirectUri", redirectUri);

		return "common/redirect";
	}
}
