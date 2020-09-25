<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="[${nickname}] 챕터 작성" />
<%@ include file="../part/head.jspf"%>
<script>
	function NovelWriteForm__submit(form) {
		form.title.value = form.title.value.trim();

		if (form.title.value.length == 0) {
			form.title.focus();
			alert('제목을 입력해주세요.');

			return;
		}

		form.body.value = form.body.value.trim();

		if (form.body.length == 0) {
			form.body.focus();
			alert('내용을 입력해주세요.');

			return;
		}

		form.submit();
	}

	function NovelWriteForm__getAutoNovelTitle(input) {
		var form = input.form;

		$.get('getAutoNovelTitle', {
			novelId : form.novelId.value
		}, function(data) {
			var $title = form.title;

			if (data.resultCode.substr(0, 2) == 'S-') {
			} else {
			}
		}, 'json');
	}
	NovelWriteForm__getAutoNovelTitle = _.debounce(
			NovelWriteForm__getAutoNovelTitle, 100);
</script>
<style>
h1{
	text-align: center;
}
.writeChapter {
	max-width: 900px;
	margin: 0 auto;
}

.writeChapter>form tbody td {
	width: 80%;
}

.writeChapter>form tbody td input {
	height: 25px;
	width: 90%;
}

.writeChapter>form tbody td select {
	height: 25px;
}

.writeChapter table {
	margin: 0;
	width: 100%;
}

.writeChapter>form tbody {
	width: 100%;
}
</style>
<div class="con">
	<div class="writeChapter">
		<form method="post" action="${member.nickname}-doWrite" method="post"
			onsubmit="NovelWriteForm__submit(this); return false;">
			<input type="hidden" name="redirectUri"
				value="/usr/novel/${member.nickname}-detail?id=#id">

			<table>
				<colgroup>
					<col width="50" />
					<col width="600" />
				</colgroup>
				<tbody>
					<tr>
						<th>제목</th>
						<td>
							<div>
								<input type="text" placeholder="제목을 입력해주세요." name="title" />
							</div>
						</td>
					</tr>
					<tr>
						<th>소설 선택</th>
						<td>
							<div>
								<select name="novelId"
									onchange="NovelWriteForm__getAutoNovelTitle(this);">
									<c:if test="${novel == null }">
										<c:forEach items="${novels}" var="n">
											<option value="${n.id}">${n.name}</option>
										</c:forEach>
										<option disabled>===단편===</option>
										<c:forEach items="${novelsNotSeries}" var="n">
											<option value="${n.id}">${n.name}</option>
										</c:forEach>
									</c:if>
									<c:if test="${novel != null }">
										<c:forEach items="${novels}" var="n">
											<c:if test="${novel.id == n.id }">
												<option value="${novel.id}" selected="selected">${novel.name}</option>
											</c:if>
											<c:if test="${novel.id != n.id }">
												<option value="${n.id}">${n.name}</option>
											</c:if>
										</c:forEach>
										<option disabled>===단편===</option>
										<c:forEach items="${novelsNotSeries}" var="n">
											<option value="${n.id}">${n.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<div>
								<textarea name="body" rows="30" cols="50"
									placeholder="내용을 입력해주세요."></textarea>
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
</div>
<%@ include file="../part/foot.jspf"%>