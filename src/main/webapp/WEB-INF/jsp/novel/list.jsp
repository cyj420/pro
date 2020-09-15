<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${chapters != null }">
	<c:set var="pageTitle" value="[${nickname}] 소설명 : ${novel.name }" />
</c:if>
<c:if test="${chapters == null }">
	<c:if test="${nickname != null }">
		<c:set var="pageTitle" value="${nickname}의 소설 리스트" />
	</c:if>
</c:if>
<%@ include file="../part/head.jspf"%>
<div class="con">
	<table class="table-list">
		<colgroup>
			<col width="100" />
			<col width="200" />
			<col width="150" />
			<col width="300" />
			<c:if test="${chapters == null }">
				<col width="300" />
			</c:if>
		</colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>날짜</th>
				<th>작성자</th>
				<c:if test="${chapters == null }">
					<th>카테고리명</th>
					<th>시리즈명</th>
				</c:if>
				<c:if test="${chapters != null }">
					<th>제목</th>
				</c:if>
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
					</tr>
				</c:forEach>
			</c:if>
			
			<c:if test="${chapters != null }">
				<c:forEach items="${chapters}" var="chapter">
					<tr>
						<td>${chapter.id}</td>
						<td>${chapter.regDate}</td>
						<td><a href="/usr/novel/${novel.extra.writer}-list">${chapter.extra.writer}</a></td>
						<td>
							<a href="/usr/novel/${novel.extra.writer}-detail?id=${chapter.id}">${chapter.title}</a>
						</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	<c:if test="${novel.memberId == loginedMemberId }">
		<button>
			<a href="/usr/novel/${nickname}-write?novelId=${novel.id}">챕터 작성</a>
		</button>
	</c:if>
</div>
<%@ include file="../part/foot.jspf"%>