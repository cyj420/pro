<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${nickname} 노벨 수정" />
<%@ include file="../part/head.jspf"%>
<script>
	function NovelModifyForm__submit(form) {
		form.name.value = form.name.value.trim();

		if (form.name.value.length == 0) {
			form.name.focus();
			alert('제목을 입력해주세요.');

			return;
		}

		form.submit();
	}
</script>
<div class="con">
	<form method="post" action="${member.nickname}-doModify" method="post" onsubmit="NovelModifyForm__submit(this); return false;" >
		<input type="hidden" name="id" value="${novel.id }"/>
		<input type="hidden" name="redirectUri" value="/usr/novel/${member.nickname}-list?novelId=#id">
		
		<table>
			<colgroup>
				<col width="100">
			</colgroup>
			<tbody>
				<tr>
					<th>소설 제목</th>
					<td>
						<div>
							<input type="text" value="${novel.name }" name="name" />
						</div>
					</td>
				</tr>
				<tr>
					<th>연작 유무</th>
					<td>
						<div>
							<select name="seriesStatus">
								<c:if test="${novel.seriesStatus == true }">
									<option value="1" selected="selected">장편</option>
									<option value="0">단편</option>
								</c:if>
								<c:if test="${novel.seriesStatus == false }">
									<option value="1">장편</option>
									<option value="0" selected="selected">단편</option>
								</c:if>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th>카테고리 선택</th>
					<td>
						<div>
							<select name="cateId">
								<c:forEach items="${categories}" var="c">
									<c:if test="${c.id == novel.cateId }">
										<option value="${c.id}" selected="selected">${c.name }</option>
									</c:if>
									<c:if test="${c.id != novel.cateId }">
										<option value="${c.id}">${c.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th>전송</th>
					<td>
						<button type="submit">수정</button>
						<a href="/usr/novel/${novel.extra.writer }-setUp">취소</a>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>