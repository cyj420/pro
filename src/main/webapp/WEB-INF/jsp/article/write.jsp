<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물 작성" />
<%@ include file="../part/head.jspf"%>
<%@ include file="../part/toastuiEditor.jspf"%>
<script>
	function ArticleWriteForm__submit(form) {
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
	<form method="post" action="${board.code}-doWrite" method="post" onsubmit="ArticleWriteForm__submit(this); return false;" >
		<input type="hidden" name="body" />
		<input type="hidden" name="redirectUri" value="/usr/article/${board.code}-detail?id=#id">
		
		<table>
			<colgroup>
				<col width="100">
			</colgroup>
			<tbody>
				<tr>
					<th>제목</th>
					<td class="title">
						<div>
							<input type="text" placeholder="제목을 입력해주세요." name="title" autocomplete="off"/>
						</div>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<div class="form-control-box">
							<c:if test="${board.code != 'notice' }">
								<script type="text/x-template">
# 이미지 예시
![img](https://cdn.pixabay.com/photo/2019/11/08/11/56/cat-4611189_960_720.jpg)

# 유투브 동영상 첨부 예시
```youtube
https://www.youtube.com/watch?v=mYm7vOHGT-Q
```
                        		</script>
							</c:if>
							<c:if test="${board.code == 'notice' }">
								<script type="text/x-template">
                        		</script>
							</c:if>
							<div data-relTypeCode="article" data-relId="0" class="toast-editor input-body"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th>작성</th>
					<td>
						<button type="submit">작성</button>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>