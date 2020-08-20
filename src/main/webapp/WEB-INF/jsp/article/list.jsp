<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>게시글 리스트</h1>
<div class="con">
	<table>
		<colgroup>
			<col width="100" />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>날짜</th>
				<th>제목</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${articles}" var="article">
				<tr>
					<td>${article.id}</td>
					<td>${article.regDate}</td>
					<td>
						<a href="/usr/article/detail?id=${article.id}">${article.title}</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@ include file="../part/foot.jspf"%>