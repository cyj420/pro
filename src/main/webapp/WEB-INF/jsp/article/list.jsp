<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물 리스트" />
<%@ include file="../part/head.jspf"%>
<div class="con">
	<form method="post" action="${board.code}-doAddArticleIntoSeries" method="post" onsubmit="SeriesAddForm__submit(this); return false;">
		<table>
			<colgroup>
				<col width="100" />
				<col width="200" />
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>작성자</th>
					<th>제목</th>
					<c:if test="${selectMode == 1}">
						<th>시리즈 선택</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${articles}" var="article">
					<tr>
						<td>${article.id}</td>
						<td>${article.regDate}</td>
						<td><a href="/usr/article/${board.code}-list?memberId=${article.memberId}">${article.extra.writer}</a></td>
						<td>
							<a href="/usr/article/${board.code}-detail?id=${article.id}">${article.title}</a>
						</td>
						<td>
							<c:if test="${selectMode == 1}">
								<input type="checkbox">
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<c:if test="${series != null}">
			<button><a href="/usr/article/${board.code}-list?memberId=${series.memberId}&cateId=${series.cateId}">해당 시리즈에 게시물 추가</a></button>
		</c:if>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>