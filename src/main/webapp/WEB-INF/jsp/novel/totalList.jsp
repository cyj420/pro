<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${chapters != null }">
	<c:set var="pageTitle" value="전체 챕터 리스트" />
</c:if>
<c:if test="${chapters == null }">
	<c:set var="pageTitle" value="전체 소설 리스트" />
</c:if>
<%@ include file="../part/head.jspf"%>
<div class="con">
	<table class="table-list">
		<colgroup>
			<col width="100" />
			<col width="200" />
			<col width="150" />
			<col width="300" />
			<col width="300" />
			<col width="100" />
		</colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>날짜</th>
				<th>작성자</th>
				<c:if test="${chapters == null }">
					<th>카테고리명</th>
				</c:if>
				<th>시리즈명</th>
				<c:if test="${chapters != null }">
					<th>제목</th>
				</c:if>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${chapters == null }">
				<c:forEach items="${novels}" var="novel">
					<tr>
						<td>${novel.id}</td>
						<td>${novel.regDate}</td>
						<td><a href="/usr/novel/${novel.extra.writer}-list">${novel.extra.writer}</a></td>
						<td>${novel.extra.cateName}</td>
						<td><a href="/usr/novel/${novel.extra.writer}-list?novelId=${novel.id}">${novel.name}</a></td>
						<td>${novel.totalHit }</td>
					</tr>
				</c:forEach>
			</c:if>
			
			<c:if test="${chapters != null }">
				<c:forEach items="${chapters}" var="chapter">
					<tr>
						<td>${chapter.id}</td>
						<td>${chapter.regDate}</td>
						<td><a href="/usr/novel/${chapter.extra.writer}-list">${chapter.extra.writer}</a></td>
						<td><a href="/usr/novel/${chapter.extra.writer}-list?novelId=${chapter.novelId}">${chapter.extra.novelName}</a></td>
						<td>
							<a href="/usr/novel/${chapter.extra.writer}-detail?id=${chapter.id}">${chapter.title}</a>
						</td>
						<td>${chapter.hit}</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>
<%@ include file="../part/foot.jspf"%>