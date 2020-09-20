<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물 리스트" />
<%@ include file="../part/head.jspf"%>
<style>
.selected-page{
color:red;
font-weight:700;
}
</style>

<div class="con">
	<form method="post" action="${board.code}-doAddArticleIntoSeries" method="post" onsubmit="SeriesAddForm__submit(this); return false;">
		<table class="table-list">
			<colgroup>
				<col width="100" />
				<col width="200" />
				<col width="200" />
				<col width="500" />
				<col width="100" />
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>작성자</th>
					<th>제목</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${articles}" var="article">
					<tr>
						<td>${article.id}</td>
						<td>${article.regDate}</td>
						<td><a href="/usr/article/${board.code}-list?memberId=${article.memberId}">${article.extra.writer}</a></td>
						<td>
							<a href="/usr/article/${board.code}-detail?id=${article.id}&memberId=${param.memberId}&searchKeyword=${param.searchKeyword}">${article.title}</a>
						</td>
						<td>${article.hit}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	
	<!-- 페이징 시작 -->
	<div class="paging">
		<c:if test="${param.memberId == null}">
			<c:if test="${param.searchKeyword == null}">
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
			</c:if>
			<c:if test="${param.searchKeyword != null}">
				<c:if test="${page > 1 }">
					<a href="?page=${page-1}&searchKeyword=${searchKeyword}">
					◀
					</a>
				</c:if>
				<c:forEach var="i" begin="1" end="${fullPage}" step="1">
					<c:if test="${page == i }">
						<span>
							<a class="selected-page" 
							href="?page=${i}&searchKeyword=${searchKeyword}">[${i }]
							</a>
					</c:if>
					<c:if test="${page != i }">
						<span>
							<a class="not-selected-page" 
							href="?page=${i}&searchKeyword=${searchKeyword}">[${i }]
							</a>
						</span>
					</c:if>
				</c:forEach>
				<c:if test="${page < fullPage }">
					<a href="?page=${page + 1}&searchKeyword=${searchKeyword}">▶</a>
				</c:if>
			</c:if>
		</c:if>
		
		<c:if test="${param.memberId != null}">
			<c:if test="${searchKeyword == null}">
				<c:if test="${page > 1 }">
					<a href="?page=${page-1}&memberId=${param.memberId}&searchKeyword=${searchKeyword}">
					◀
					</a>
				</c:if>
				<c:forEach var="i" begin="1" end="${fullPage}" step="1">
					<c:if test="${page == i }">
						<span>
							<a class="selected-page" 
							href="?page=${i}&memberId=${param.memberId}">[${i }]
							</a>
						</span>
					</c:if>
					<c:if test="${page != i }">
						<span>
							<a class="not-selected-page" 
							href="?page=${i}&memberId=${param.memberId}">[${i }]
							</a>
						</span>
					</c:if>
				</c:forEach>
				<c:if test="${page < fullPage }">
					<a href="?page=${page + 1}&memberId=${param.memberId}">▶</a>
				</c:if>
			</c:if>
			
			<c:if test="${param.searchKeyword != null}">
				<c:if test="${page > 1 }">
					<a href="?page=${page-1}&memberId=${param.memberId}&searchKeyword=${searchKeyword}">
					◀
					</a>
				</c:if>
				<c:forEach var="i" begin="1" end="${fullPage}" step="1">
					<c:if test="${page == i }">
						<span>
							<a class="selected-page" 
							href="?page=${i}&memberId=${param.memberId}&searchKeyword=${searchKeyword}">[${i }]
							</a>
						</span>
					</c:if>
					<c:if test="${page != i }">
						<span>
							<a class="not-selected-page" 
							href="?page=${i}&memberId=${param.memberId}&searchKeyword=${searchKeyword}">[${i }]
							</a>
						</span>
					</c:if>
				</c:forEach>
				<c:if test="${page < fullPage }">
					<a href="?page=${page + 1}&memberId=${param.memberId}&searchKeyword=${searchKeyword}">▶</a>
				</c:if>
			</c:if>
		</c:if>
	</div>
	<!-- 페이징 끝 -->
	
	<!-- 검색 시작 -->
	<div class="con search-box flex flex-jc-c">
		<c:if test="${param.memberId == null}">
			<form action="/usr/article/${board.code}-list?page=1">
		</c:if>
		<c:if test="${param.memberId != null}">
			<form action="/usr/article/${board.code}-list?page=1&memberId=${param.memberId}">
		</c:if>
				<input type="hidden" name="page" value="1" />
				<input type="hidden" name="memberId" value="${param.memberId}" />
				<input type="text" name="searchKeyword" value="${param.searchKeyword}" />
				<button type="submit">검색</button>
			</form>
	</div>
	<!-- 검색 끝 -->
	
	<c:if test="${loginedMember != null }">
		<li>
			<c:forEach items="${boards}" var="b">
				<c:if test="${boardCode == b.code }">
					<c:if test="${b.code == 'notice' }">
						<c:if test="${loginedMemberId == 1 }">
							<a href="/usr/article/${b.code}-write"><button>WRITE</button></a>
						</c:if>
					</c:if>
					<c:if test="${b.code != 'notice' }">
						<a href="/usr/article/${b.code}-write"><button>WRITE</button></a>
					</c:if>
				</c:if>
			</c:forEach>
		</li>
	</c:if>
</div>
<%@ include file="../part/foot.jspf"%>