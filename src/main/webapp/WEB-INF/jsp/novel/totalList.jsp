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
.selected-page{
color:red;
font-weight:700;
}
h1{
font-family: 'Noto Sans KR', sans-serif;
}
</style>
<div class="con">
	<form action="/usr/novel/list">
		<c:if test="${param.mode != 'totalNovel'}">
			<input type="hidden" name="mode" value="totalNovel"/>
			<input type="hidden" name="page" value="1"/>
			<button type="submit">소설 별 보기</button>
		</c:if>
		<c:if test="${param.mode == 'totalNovel'}">
			<input type="hidden" name="mode" value="totalChapter"/>
			<input type="hidden" name="page" value="1"/>
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
					<th>카테고리</th>
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
						<td><a href="/usr/novel/${novel.extra.writer}-list?mode=novel">${novel.extra.writer}</a></td>
						<td>${novel.extra.cateName}</td>
						<td><a href="/usr/novel/${novel.extra.writer}-list?mode=novel&novelId=${novel.id}">${novel.name} [${novel.totalCh }]</a></td>
						<td>${novel.totalHit }</td>
					</tr>
				</c:forEach>
			</c:if>
			
			<c:if test="${chapters != null }">
				<c:forEach items="${chapters}" var="chapter">
					<tr>
						<td>${chapter.id}</td>
						<td>${chapter.regDate}</td>
						<td><a href="/usr/novel/${chapter.extra.writer}-list?mode=novel">${chapter.extra.writer}</a></td>
						<td><a href="/usr/novel/${chapter.extra.writer}-list?mode=novel&novelId=${chapter.novelId}">${chapter.extra.novelName}</a></td>
						<td>
							<c:if test="${param.searchKeyword == null }">
								<a href="/usr/novel/${chapter.extra.writer}-detail?id=${chapter.id}&novelId=${chapter.novelId}">${chapter.title}</a>
							</c:if>
							<c:if test="${param.searchKeyword != null }">
								<a href="/usr/novel/${chapter.extra.writer}-detail?id=${chapter.id}&mode=${param.mode}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}">${chapter.title}</a>
							</c:if>
						</td>
						<td>${chapter.hit}</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	
	<!-- 페이징 시작 -->
	<div class="paging">
		<c:if test="${page > 1 }">
			<a href="?mode=${param.mode}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${page-1}">
			◀
			</a>
		</c:if>
		<c:forEach var="i" begin="1" end="${fullPage}" step="1">
			<c:if test="${page == i }">
				<span>
					<a class="selected-page" 
					href="?mode=${param.mode}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${i}">[${i }]
					</a>
				</span>
			</c:if>
			<c:if test="${page != i }">
				<span>
					<a class="not-selected-page" 
					href="?mode=${param.mode}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${i}">[${i }]
					</a>
				</span>
			</c:if>
		</c:forEach>
		<c:if test="${page < fullPage }">
			<a href="?mode=${param.mode}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${page + 1}">▶</a>
		</c:if>
	</div>
	<!-- 페이징 끝 -->
	
	<!-- 검색 시작 -->
	<div class="con search-box flex flex-jc-c">
		<form action="/usr/novel/list">
			<input type="hidden" name="mode" value="${param.mode }"/>
			<input type="hidden" name="page" value="1" />
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