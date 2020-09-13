<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${nickname} 챕터 수정" />
<%@ include file="../part/head.jspf"%>
<script>
	function ChapterModifyForm__submit(form) {
		form.title.value = form.title.value.trim();

		if (form.title.value.length == 0) {
			form.title.focus();
			alert('제목을 입력해주세요.');

			return;
		}

		form.body.value = form.body.value.trim();

		if (form.body.value.length == 0) {
			form.body.focus();
			alert('내용을 입력해주세요.');

			return;
		}
		
		form.submit();
	}
</script>
<div class="con">
	<form method="post" action="${member.nickname}-doModifyChapter" method="post" onsubmit="ChapterModifyForm__submit(this); return false;" >
		<input type="hidden" name="id" value="${chapter.id }"/>
		<input type="hidden" name="redirectUri" value="/usr/novel/${member.nickname}-detail?id=#id">
		
		<table>
			<colgroup>
				<col width="100">
			</colgroup>
			<tbody>
				<tr>
					<th>제목</th>
					<td>
						<div>
							<input type="text" value="${chapter.title }" name="title" />
						</div>
					</td>
				</tr>
				<tr>
					<th>Novel 선택</th>
					<td>
						<div>
							<select name="novelId">
								<c:forEach items="${novels}" var="n">
									<c:if test="${n.id == chapter.novelId }">
										<option value="${n.id}" selected="selected">${n.name }</option>
									</c:if>
									<c:if test="${n.id != chapter.novelId }">
										<option value="${n.id}">${n.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<div>
							<input type="text" value="${chapter.body }" name="body"/>
						</div>
					</td>
				</tr>
				<tr>
					<th>전송</th>
					<td>
						<button type="submit">수정</button>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>