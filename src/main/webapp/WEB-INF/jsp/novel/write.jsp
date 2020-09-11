<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${nickname} 소설 작성" />
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
</script>
<div class="con">
	<form method="post" action="${member.nickname}-doWrite" method="post" onsubmit="NovelWriteForm__submit(this); return false;" >
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
							<input type="text" placeholder="제목을 입력해주세요." name="title"/>
						</div>
					</td>
				</tr>
				<tr>
					<th>Novel 선택</th>
					<td>
						<div>
							<select name="novelId">
								<c:forEach items="${novels}" var="n">
									<option value="${n.id}">${n.name}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<%-- 
				<tr>
					<th>카테고리 선택</th>
					<td>
						<div>
							<select name="cateId">
								<c:forEach items="${categories}" var="c">
									<option value="${c.id}">${c.name}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				 --%>
				<tr>
					<th>내용</th>
					<td>
						<div>
							<input type="text" placeholder="내용을 입력해주세요." name="body"/>
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