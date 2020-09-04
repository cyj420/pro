<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>나의 시리즈</h1>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<div class="con">
	<table>
		<colgroup>
			<col width="100" />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>작성 날짜</th>
				<th>카테고리</th>
				<th>제목</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${series}" var="s">
				<tr>
					<td>${s.id}</td>
					<td>${s.regDate}</td>
					<td><a href="/usr/article/${board.code}-list?cateId=${s.cateId}">${s.extra.cateName}</a></td>
					<td>
						<a href="/usr/article/${board.code}-list?seriesId=${s.id}">${s.name}</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<button><a href="/usr/article/${board.code}-addSeries">시리즈 추가</a></button>
</div>
<%@ include file="../part/foot.jspf"%>