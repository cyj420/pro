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
	@ResponseBody
	public String doLogin(@RequestParam Map<String, Object> param, Model model, HttpSession session) {
		Member member = memberService.login(param);
		
		if(member==null) {
			return "<script> alert('로그인 실패'); history.back(); </script>";
		}
		
		session.setAttribute("loginedMember", member);
		
		return "<script> alert('안녕하세요, "+ member.getNickname() +"님'); location.replace('../home/main'); </script>";
	}
	
	// 로그아웃
	@RequestMapping("usr/member/doLogout")
	@ResponseBody
	public String doLogout(Model model, HttpSession session) {
		session.removeAttribute("loginedMember");
		
		return "<script> alert('로그아웃 되었습니다.'); location.replace('../home/main'); </script>";
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
	
	// ID찾기
	@RequestMapping("usr/member/findLoginId")
	public String findLoginId() {
		return "member/findLoginId";
	}
	
	@RequestMapping("usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(@RequestParam Map<String, Object> param, Model model, HttpSession session) {
		String loginId = memberService.findLoginIdByNameAndEmail(param);
		
		StringBuilder sb = new StringBuilder();

		sb.append("alert('ID : " + loginId + "');");
		sb.append("location.replace('./login');");

		sb.insert(0, "<script>");
		sb.append("</script>");

		
		if(loginId==null) {
			return "<script> alert('일치하는 ID가 없습니다.'); location.replace('../home/main'); </script>";
		}
		
		return sb.toString();
	}
}
