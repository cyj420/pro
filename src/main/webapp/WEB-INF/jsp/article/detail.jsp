<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물  상세보기" />
<%@ include file="../part/head.jspf"%>
<%@ include file="../part/toastuiEditor.jspf"%>
<style>
video{
	max-width: 500px;
}
img{
	max-width: 500px;
}
.toast-editor-viewer{
	max-width: 900px;
	min-width: 500px;
}
</style>
<div class="con">
	<table>
		<tbody>
			<tr>
				<th>번호</th>
				<td>${article.id}</td>
			</tr>
			<tr>
				<th>날짜</th>
				<td>${article.regDate}</td>
			</tr>
			<tr>
				<th>카테고리 id</th>
				<td>${article.cateId}</td>
			</tr>
			<tr>
				<th>작성자 id</th>
				<td>
				<a href="/usr/article/${board.code}-list?memberId=${article.memberId}">${article.memberId}</a>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>${article.title}</td>
			</tr>
			<c:if test="${article.memberId == loginedMember.id }">
				<tr>
					<th>비고</th>
					<td>
					<a href="/usr/article/${board.code}-modify?id=${article.id}">수정</a>
					/
					<a href="/usr/article/${board.code}-doDelete?id=${article.id}">삭제</a>
					</td>
				</tr>
			</c:if>
			<tr>
				<th>내용</th>
				<td>
				    <script type="text/x-template">${article.body}</script>
                    <div class="toast-editor toast-editor-viewer"></div>
				</td>
			</tr>
			<%-- 
			<c:forEach var="i" begin="1" end="3" step="1">
				<c:set var="fileNo" value="${String.valueOf(i)}" />
				<c:set var="file" value="${article.extra.file__common__attachment[fileNo]}" />
				<c:if test="${file != null}">
					<tr>
						<th>첨부파일 ${fileNo}</th>
						<td>
							<c:if test="${file.fileExtTypeCode == 'video'}">
								<div class="video-box">
									<video controls src="/usr/file/streamVideo?id=${file.id}&updateDate=${file.updateDate}"></video>
								</div>
							</c:if>
							<c:if test="${file.fileExtTypeCode == 'img'}">
								<div class="img-box img-box-auto">
									<img src="/usr/file/img?id=${file.id}&updateDate=${file.updateDate}" alt="" />
								</div>
							</c:if>
						</td>
					</tr>
				</c:if>
			</c:forEach>
			 --%>
		</tbody>
	</table>
</div>
<%@ include file="../part/foot.jspf"%>