<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${chapters != null }">
	<c:if test="${param.mode == 'chapter' }">
		<c:set var="pageTitle" value="${nickname}의 챕터 리스트" />
	</c:if>
	<c:if test="${param.mode == 'novel' }">
		<c:set var="pageTitle" value="${nickname}의 소설 리스트" />
	</c:if>
	<c:if test="${param.novelId > 0 }">
		<c:set var="pageTitle"
			value="[${nickname}] 소설명 : ${novel.name } [${novel.totalCh }]" />
	</c:if>
</c:if>
<c:if test="${chapters == null }">
	<c:if test="${nickname != null }">
		<c:set var="pageTitle" value="${nickname}의 장편 소설 리스트" />
	</c:if>
</c:if>
<%@ include file="../part/head.jspf"%>
<style>
.selected-page{
color:red;
font-weight:700;
}
</style>
<div class="con">
	<form action="/usr/novel/${nickname}-list">
		<c:if test="${param.mode == 'chapter'}">
			<input type="hidden" name="mode" value="novel" />
			<button type="submit">소설 별 보기</button>
		</c:if>
		<c:if test="${param.mode == 'novel' && param.novelId == null}">
			<input type="hidden" name="mode" value="chapter" />
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
						<td><a
							href="/usr/novel/${novel.extra.writer}-list?mode=novel">${novel.extra.writer}</a></td>
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
						<td><a
							href="/usr/novel/${chapter.extra.writer}-list?mode=novel">${chapter.extra.writer}</a></td>
						<td>
							<a href="/usr/novel/${chapter.extra.writer}-list?mode=novel&novelId=${chapter.novelId}">${chapter.extra.novelName}</a>
						</td>
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
			<c:if test="${param.searchKeyword == null}">
				<a href="?mode=${param.mode}&novelId=${novelId}&page=${page-1}">
				◀
				</a>
			</c:if>
			<c:if test="${param.searchKeyword != null}">
				<a href="?mode=${param.mode}&novelId=${novelId}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${page-1}">
				◀
				</a>
			</c:if>
		</c:if>
		<c:forEach var="i" begin="1" end="${fullPage}" step="1">
			<c:if test="${param.searchKeyword == null}">
				<c:if test="${page == i }">
					<span>
						<a class="selected-page" 
						href="?mode=${param.mode}&novelId=${novelId}&page=${i}">[${i }]
						</a>
					</span>
				</c:if>
				<c:if test="${page != i }">
					<span>
						<a class="not-selected-page" 
						href="?mode=${param.mode}&novelId=${novelId}&page=${i}">[${i }]
						</a>
					</span>
				</c:if>
			</c:if>
			<c:if test="${param.searchKeyword != null}">
				<c:if test="${page == i }">
					<span>
						<a class="selected-page" 
						href="?mode=${param.mode}&novelId=${novelId}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${i}">[${i }]
						</a>
					</span>
				</c:if>
				<c:if test="${page != i }">
					<span>
						<a class="not-selected-page" 
						href="?mode=${param.mode}&novelId=${novelId}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${i}">[${i }]
						</a>
					</span>
				</c:if>
			</c:if>
		</c:forEach>
		<c:if test="${page < fullPage }">
			<c:if test="${param.searchKeyword == null}">
				<a href="?mode=${param.mode}&novelId=${novelId}&page=${page+1}">
				▶
				</a>
			</c:if>
			<c:if test="${param.searchKeyword != null}">
				<a href="?mode=${param.mode}&novelId=${novelId}&searchKeywordType=${param.searchKeywordType}&searchKeyword=${param.searchKeyword}&page=${page + 1}">
				▶
				</a>
			</c:if>
		</c:if>
	</div>
	<!-- 페이징 끝 -->

	<!-- 검색 시작 -->
	<div class="con search-box flex flex-jc-c">
		<c:if test="${param.novelId < 1 || param.novelId == null }">
			<form action="/usr/novel/${nickname}-list">
				<input type="hidden" name="mode" value="${param.mode }" />
				<select name="searchKeywordType" type="hidden">
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
		</c:if>
	</div>
	<!-- 검색 끝 -->
	<c:if test="${novel.memberId == loginedMemberId }">
		<button>
			<a href="/usr/novel/${nickname}-write?novelId=${novel.id}">챕터 작성</a>
		</button>
	</c:if>
</div>
<%@ include file="../part/foot.jspf"%>