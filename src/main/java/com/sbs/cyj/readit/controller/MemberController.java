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
	
	@RequestMapping("/member/join")
	public String showJoin() {
		return "member/join";
	}
	
	@RequestMapping("/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param, Model model) {
		String pw = (String) param.get("loginPw");
		if(pw != null) {
			if(pw.trim().length()!=0) {
				System.out.println("pw.trim().length()!=0");
				//여기서 아웃
			}
			if(param.get("loginPw").equals(param.get("loginPwConfirm")) && pw.trim().length()!=0) {
				memberService.join(param);
				return "<script> alert('회원가입 성공'); location.replace('../home/main'); </script>";
			}
		}
		return "<script> alert('회원가입 실패'); history.back(); </script>";
	}
	
	@RequestMapping("/member/login")
	public String showLogin() {
		return "member/login";
	}
	
	@RequestMapping("/member/doLogin")
	@ResponseBody
	public String doLogin(@RequestParam Map<String, Object> param, Model model, HttpSession session) {
		Member member = memberService.login(param);
		
		if(member==null) {
			return "<script> alert('로그인 실패'); location.replace('../home/main'); </script>";
		}
		
		session.setAttribute("loginedMember", member);
		
		return "<script> alert('안녕하세요, "+ member.getNickname() +"님'); location.replace('../home/main'); </script>";
	}
	
	@RequestMapping("/member/doLogout")
	public String doLogout(Model model, HttpSession session) {
		session.removeAttribute("loginedMember");
		
		return "./home/main";
	}
	
	@RequestMapping("/member/modify")
	public String showModify() {
		return "member/modify";
	}
	
	@RequestMapping("/member/doModify")
	public String doModify(@RequestParam Map<String, Object> param, Model model, HttpSession session) {
		memberService.modify(param);
		int id = Integer.parseInt((String) param.get("id"));
		Member member = memberService.getMemberById(id);
		session.setAttribute("loginedMember", member);
		
		return "./home/main";
	}
	
	@RequestMapping("/member/findLoginId")
	public String findLoginId() {
		return "member/findLoginId";
	}
	
	@RequestMapping("/member/doFindLoginId")
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
