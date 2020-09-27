<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물 수정" />
<%@ include file="../part/head.jspf"%>
<%@ include file="../part/toastuiEditor.jspf"%>
<script>
function ArticleModifyForm__submit(form) {
	form.title.value = form.title.value.trim();

	if (form.title.value.length == 0) {
		form.title.focus();
		alert('제목을 입력해주세요.');

		return;
	}

	var bodyEditor = $(form).find('.toast-editor.input-body').data('data-toast-editor');

	var body = bodyEditor.getMarkdown().trim();

	if (body.length == 0) {
		bodyEditor.focus();
		alert('내용을 입력해주세요.');

		return;
	}
	
	form.body.value = body;

	form.submit();
}
</script>
<style>
table{
	width: 100%;
}
table > tbody .title input{
	width: 30%;
	min-width: 250px;
	height: 30px;
	padding-left: 5px;
}
</style>
<div class="con">
	<form method="post" action="${board.code}-doModify" method="post" onsubmit="ArticleModifyForm__submit(this); return false;" >
		<input type="hidden" name="body" />
		<input type="hidden" name="id" value="${article.id }"/>
		<input type="hidden" name="redirectUri" value="/usr/article/${board.code}-detail?id=${article.id}">
		
		<table>
			<colgroup>
				<col width="100">
			</colgroup>
			<tbody>
				<tr>
					<th>번호</th>
					<td>${article.id }</td>
				</tr>
				<tr>
					<th>날짜</th>
					<td>${article.regDate }</td>
				</tr>
				<tr>
					<th>제목</th>
					<td class="title">
						<div>
							<input type="text" placeholder="제목을 입력해주세요." name="title" value="${article.title }" autocomplete="off"/>
						</div>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<div class="form-control-box">
							<script type="text/x-template">${article.body}</script>
							<div data-relTypeCode="article" data-relId="${article.id }" class="toast-editor input-body"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th>작성</th>
					<td>
						<button type="submit">수정</button>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>