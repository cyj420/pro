package com.sbs.cyj.readit.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sbs.cyj.readit.dto.Board;
import com.sbs.cyj.readit.service.BoardService;

@Component("beforeActionInterceptor") // 컴포넌트 이름 설정
public class BeforeActionInterceptor implements HandlerInterceptor {
	@Autowired
	@Value("${custom.logoText}")
	private String siteName;
	@Autowired
	BoardService boardService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		request.setAttribute("logoText", this.siteName);
		
		List<Board> boards = boardService.getBoards();
		request.setAttribute("boards", boards);

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}