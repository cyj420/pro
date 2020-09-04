<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>시리즈 추가</h1>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<script>
	var ArticleAddSeriesForm__submitDone = false;
	function ArticleAddSeriesForm__submit(form) {
		if (ArticleAddSeriesForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		form.name.value = form.name.value.trim();

		if (form.name.value.length == 0) {
			form.name.focus();
			alert('시리즈 이름를 입력해주세요.');

			return;
		}

		form.submit();
		ArticleAddSeriesForm__submitDone = true;
	}
</script>
<div class="con">
	<form action="${board.code}-doAddSeries" method="post" onsubmit="ArticleAddSeriesForm__submit(this); return false;">
		<input type="hidden" name="redirectUri" value="/usr/article/${board.code}-list?seriesId=#id">
		<input name="loginPwReal" hidden="hidden">
		<div>
			<label>시리즈 이름 : <input name="name" type="text">
			</label>
		</div>
		<div>
			<label>카테고리 선택 : 
				<select name="cateId">
					<c:forEach items="${categories}" var="c">
						<option value="${c.id}">${c.name}</option>
					</c:forEach>
				</select>
			</label>
		</div>
				
		<input type="submit" value="시리즈 생성">
		<button onclick="history.back();" type="button">취소</button>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>