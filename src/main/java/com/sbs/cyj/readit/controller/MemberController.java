package com.sbs.cyj.readit.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.service.MemberService;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	// 회원가입
	@RequestMapping("usr/member/join")
	public String showJoin() {
		return "member/join";
	}
	
	@RequestMapping("usr/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param, Model model) {
		if(memberService.getMemberByLoginId((String)param.get("loginId"))==null) {
			if(memberService.getMemberByEmail((String)param.get("email"))==null) {
				int id = memberService.join(param);
				return "<script> alert('" + id + "번째 회원입니다.'); location.replace('../home/main'); </script>";
			}
		}
		return "<script> alert('회원가입 실패'); history.back(); </script>";
	}
	
	// 로그인
	@RequestMapping("usr/member/login")
	public String showLogin() {
		return "member/login";
	}
	
	@RequestMapping("usr/member/doLogin")
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

		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}

		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("msg", String.format("%s님 반갑습니다.", member.getNickname()));

		return "common/redirect";
	}
	
	// 로그아웃
	@RequestMapping("usr/member/doLogout")
	public String doLogout(Model model, HttpSession session, String redirectUri) {
		session.removeAttribute("loginedMember");
		session.removeAttribute("loginedMemberId");
		
		if(redirectUri.length() == 0) {
			System.out.println("redirectUri.length() == 0");
		}
		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}

		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("msg", String.format("로그아웃 되었습니다."));
		
		return "common/redirect";
	}
	
	// 회원 정보 수정
	@RequestMapping("usr/member/modify")
	public String showModify() {
		return "member/myPage";
	}
	
	@RequestMapping("usr/member/doModify")
	@ResponseBody
	public String doModify(@RequestParam Map<String, Object> param, Model model, HttpSession session) {
		memberService.modify(param);
		int id = Integer.parseInt((String) param.get("id"));
		Member member = memberService.getMemberById(id);
		session.setAttribute("loginedMember", member);
		
		return "<script> alert('회원 정보 수정 완료'); location.replace('../home/main'); </script>";
	}
	
	// ID 찾기
	@RequestMapping("usr/member/findLoginId")
	public String findLoginId() {
		return "member/findLoginId";
	}
	
	// ID 찾기
	@RequestMapping("usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(@RequestParam Map<String, Object> param, Model model) {
		String loginId = memberService.findLoginIdByNameAndEmail(param);
		
		StringBuilder sb = new StringBuilder();

		sb.append("alert('ID : " + loginId + "');");
		sb.append("location.replace('./login');");

		sb.insert(0, "<script>");
		sb.append("</script>");

		
		if(loginId==null) {
			return "<script> alert('일치하는 ID가 없습니다.'); history.back(); </script>";
		}
		
		return sb.toString();
	}
	
	// PW 찾기
	@RequestMapping("usr/member/findLoginPw")
	public String findLoginPw() {
		return "member/findLoginPw";
	}
	
	@RequestMapping("usr/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPW(@RequestParam Map<String, Object> param, Model model) {
		String loginId = (String) param.get("loginId");
		Member member = memberService.getMemberByLoginId(loginId);
		
		StringBuilder sb = new StringBuilder();
		
		if(member != null) {
			if(member.getName().equals((String) param.get("name")) && member.getEmail().equals((String) param.get("email"))) {
				if(memberService.resetLoginPw(param) > 0) {
					sb.append("alert('임시 PW를 메일로 보냈습니다.');");
					sb.append("location.replace('./login');");
				}
				else {
					sb.append("alert('메일 발송에 실패했습니다.');");
					sb.append("history.back();");
				}
			}
			else {
				sb.append("alert('일치하는 계정이 없습니다.');");
				sb.append("history.back();");
			}
		}

		sb.insert(0, "<script>");
		sb.append("</script>");

		return sb.toString();
	}
	
	@RequestMapping("usr/member/withdrawal")
	@ResponseBody
	public String doWithdrawal(@RequestParam int id, Model model, HttpSession session) {
		memberService.withdrawal(id);
		session.removeAttribute("loginedMember");
		
		StringBuilder sb = new StringBuilder();

		sb.append("alert('탈퇴 완료.');");
		sb.append("location.replace('./../home/main');");

		sb.insert(0, "<script>");
		sb.append("</script>");

		return sb.toString();
	}
}
