package com.sbs.cyj.readit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cyj.readit.dto.Chapter;
import com.sbs.cyj.readit.dto.Member;
import com.sbs.cyj.readit.dto.Reply;
import com.sbs.cyj.readit.dto.ResultData;
import com.sbs.cyj.readit.service.ChapterService;
import com.sbs.cyj.readit.service.ReplyService;

@Controller
public class ReplyController {
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ChapterService chapterService;
	
	// 댓글 작성
	@RequestMapping("/usr/reply/doWriteReplyAjax")
	@ResponseBody
	public String doWriteReplyAjax(@RequestParam Map<String, Object> param, @RequestParam String relType, @RequestParam int relId, String body, Model model, HttpServletRequest req) {
		int memberId = (int) req.getAttribute("loginedMemberId");
		param.put("memberId", memberId);
		
		if(param.get("secret").equals("true")) {
			param.put("secretStatus", 1);
		}
		else {
			param.put("secretStatus", 0);
		}
		
		replyService.write(param);
		String msg = "댓글이 생성되었습니다.";

		String redirectUri = (String) param.get("redirectUri");
		System.out.println("redirectUri : " + redirectUri);
		
		model.addAttribute("msg", msg);
		model.addAttribute("redirectUri", redirectUri);
		
		return "article/modify";
	}
	
	// 댓글 리스트
	@RequestMapping("/usr/reply/getForPrintReplies")
	@ResponseBody
	public ResultData getForPrintArticleReplies(@RequestParam Map<String, Object> param, HttpServletRequest req, Model model) {
		Map<String, Object> rsDataBody = new HashMap<>();

		Member actor = (Member) req.getAttribute("loginedMember");
		param.put("actor", actor);
		
		List<Reply> replies = replyService.getForPrintReplies(param);
		rsDataBody.put("replies", replies);
		
		if(replies.size()!=0) {
			String relType = replies.get(0).getRelType();
			int relId = replies.get(0).getRelId();
			
			if(relType.equals("novel")) {
				Chapter chapter = chapterService.getChapterById(relId);
				int chapterWriterId = chapter.getMemberId();
				if( actor.getId() == chapterWriterId || actor.getId() == 1 ) {
					rsDataBody.put("canViewSecretReply", true);
				}
			}
		}
		
		return new ResultData("S-1", String.format("%d개의 댓글을 불러왔습니다.", replies.size()), rsDataBody);
	}
	
	// 댓글 삭제
	@RequestMapping("/usr/reply/doDeleteReplyAjax")
	@ResponseBody
	public ResultData doDeleteReplyAjax(int id, HttpServletRequest req) {
		Member loginedMember = (Member) req.getAttribute("loginedMember");
		Reply ar = replyService.getReplyById(id);
		
		if(replyService.actorCanDelete(loginedMember, ar) == false) {
			return new ResultData("F-1", String.format("%d번 댓글을 삭제할 권한이 없습니다.", id));
		}
		replyService.deleteReplyById(id);
		
		return new ResultData("S-1", String.format("%d번 댓글을 삭제하였습니다.", id));
	}
	
	// 댓글 수정
	@RequestMapping("/usr/reply/doModifyReplyAjax")
	@ResponseBody
	public ResultData doReplyModify(@RequestParam int id, String body) {
//		Reply reply = replyService.getReplyById(id);
//		if(reply.isSecretStatus()) {
//			body = body.substring(4);
//		}
		replyService.modifyReplyById(""+id, body);
		return new ResultData("S-1", String.format("%d번 댓글을 수정하였습니다.", id));
	}
}