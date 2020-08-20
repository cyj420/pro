<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../part/head.jspf"%>
<h1>Main Page</h1>
<a href="https://naver.com" target="_blank">네이버</a>
<c:if test="${loginedMember != null }">
	<a href="/usr/article/write">글 작성하기</a>
</c:if>
<a href="/usr/article/list">리스트 보기</a>

<%@ include file="../part/foot.jspf"%>