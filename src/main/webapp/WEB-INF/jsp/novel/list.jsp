<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${loginId} 노벨 리스트" />
<%@ include file="../part/head.jspf"%>
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
				<th>작성자</th>
				<th>제목</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${novels}" var="novel">
				<tr>
					<td>${novel.id}</td>
					<td>${novel.regDate}</td>
					<td><a href="/usr/novel/${novel.extra.writer}-list">${novel.extra.writer}</a></td>
					<td>
						<a href="/usr/novel/${novel.extra.writer}-detail?id=${novel.id}">${novel.name}</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@ include file="../part/foot.jspf"%>