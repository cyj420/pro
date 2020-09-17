<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${chapters != null }">
	<c:set var="pageTitle" value="전체 챕터 리스트" />
</c:if>
<c:if test="${chapters == null }">
	<c:set var="pageTitle" value="전체 장편 소설 리스트" />
</c:if>
<%@ include file="../part/head.jspf"%>
<style>
.table-list a{
font-weight: bold;
}
.table-list a:hover{
color: red;
}
</style>
<div class="con">
	<form action="/usr/novel/total-list">
		<c:if test="${param.mode != 'novel'}">
			<input type="hidden" name="mode" value="novel"/>
			<button type="submit">소설 별 보기</button>
		</c:if>
		<c:if test="${param.mode == 'novel'}">
			<input type="hidden" name="mode" value="chapter"/>
			<button type="submit">챕터 별 보기</button>
		</c:if>
	</form>
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
				<th>소설 제목</th>
				<c:if test="${chapters != null }">
					<th>챕터 제목</th>
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
						<td><a href="/usr/novel/${novel.extra.writer}-list?novelId=${novel.id}">${novel.name} [${novel.totalCh }]</a></td>
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
	
	<!-- 검색 시작 -->
	<div class="con search-box flex flex-jc-c">
		<form action="/usr/novel/total-list">
			<input type="hidden" name="mode" value="${param.mode }"/>
			<!-- 
			<input type="hidden" name="page" value="1" />
			-->
			<select name="searchKeywordType" type="hidden" >
				<c:if test="${param.searchKeywordType == null }">
					<option value="name">소설 제목</option>
					<c:if test="${chapters != null }">
						<option value="title">챕터 제목</option>
					</c:if>
				</c:if>
				<c:if test="${param.searchKeywordType != null }">
					<option value="name">소설 제목</option>
					<c:if test="${param.searchKeywordType == 'title' }">
						<c:if test="${chapters != null }">
							<option value="title" selected="selected">챕터 제목</option>
						</c:if>
					</c:if>
					<c:if test="${param.searchKeywordType != 'title' }">
						<c:if test="${chapters != null }">
							<option value="title">챕터 제목</option>
						</c:if>
					</c:if>
				</c:if>
			</select> 
			<input type="text" name="searchKeyword" value="${param.searchKeyword}" />
			<button type="submit">검색</button>
		</form>
	</div>
	<!-- 검색 끝 -->
</div>
<%@ include file="../part/foot.jspf"%>