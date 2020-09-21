<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${chapters != null }">
	<c:set var="pageTitle" value="[${nickname}] ${novel.name } 셋업" />
</c:if>
<c:if test="${chapters == null }">
	<c:set var="pageTitle" value="${nickname} 설정" />
</c:if>
<%@ include file="../part/head.jspf"%>
<style>
form{
	display: inline;
}

.selected-page{
color:red;
font-weight:700;
}
</style>
<div class="con">
	<table class="table-list">
		<colgroup>
			<col width="100" />
			<col width="200" />
			<col width="200" />
			<col width="150" />
			<col width="200" />
			<col width="300" />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>날짜</th>
				<th>카테고리</th>
				<th>연작 유무</th>
				<th>작성자</th>
				<th>시리즈명</th>
				<th>비고</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${novels}" var="novel">
				<tr>
					<td>${novel.id}</td>
					<td>${novel.regDate}</td>
					<td>
						${novel.extra.cateName}
					</td>
					<td>
						<c:if test="${novel.seriesStatus == true}">
							O
						</c:if>
						<c:if test="${novel.seriesStatus == false}">
							X
						</c:if>
					</td>
					<td><a href="/usr/novel/${novel.extra.writer}-list">${novel.extra.writer}</a></td>
					<td>
						<a href="/usr/novel/${novel.extra.writer}-list?novelId=${novel.id}">${novel.name}</a>
					</td>
					<td>
						<a href="/usr/novel/${novel.extra.writer}-modify?id=${novel.id}"><button>수정</button></a>
						<form method="post" action="${novel.extra.writer}-doDelete?id=${novel.id}" >
							<input type="hidden" name="redirectUri" value="/usr/novel/${novel.extra.writer}-setUp">
							<button type="submit" onclick="if ( confirm('정말 삭제하시겠습니까?') == false ) { return false; }">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<!-- 페이징 시작 -->
	<div class="paging">
		<c:if test="${page > 1 }">
			<a href="?page=${page-1}">
			◀
			</a>
		</c:if>
		<c:forEach var="i" begin="1" end="${fullPage}" step="1">
			<c:if test="${page == i }">
				<span>
					<a class="selected-page" 
					href="?page=${i}">[${i }]
					</a>
			</c:if>
			<c:if test="${page != i }">
				<span>
					<a class="not-selected-page" 
					href="?page=${i}">[${i }]
					</a>
				</span>
			</c:if>
		</c:forEach>
		<c:if test="${page < fullPage }">
			<a href="?page=${page + 1}">▶</a>
		</c:if>
	</div>
	<!-- 페이징 끝 -->
	
	<button>
		<a href="./genNovel">새로운 소설 생성</a>
	</button>
</div>
<%@ include file="../part/foot.jspf"%>