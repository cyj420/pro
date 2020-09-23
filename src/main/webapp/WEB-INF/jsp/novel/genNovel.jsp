<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="새로운 소설 작성하기" />
<%@ include file="../part/head.jspf"%>
<script>
	var NovelGenForm__submitDone = false;
	var NovelGenForm__validName = '';

	function NovelGenForm__submit(form) {
		if (NovelGenForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		form.title.value = form.title.value.trim();

		if (form.name.value.length == 0) {
			form.name.focus();
			alert('제목을 입력해주세요.');
			return;
		}

		if (form.name.value != NovelGenForm__validName) {
			alert('다른 제목을 입력해주세요.');
			form.name.focus();
			return;
		}
		form.submit();
		NovelGenForm__submitDone = true;
	}

	function NovelGenForm__checkNameDup(input) {
		var form = input.form;

		form.name.value = form.name.value.trim();

		if (form.name.value.length == 0) {
			$(form.name).next().empty();
			return;
		}

		$.get('getNameDup', {
			name : form.name.value
		}, function(data) {
			var $message = $(form.name).next();

			if (data.resultCode.substr(0, 2) == 'S-') {
				$message.empty().append(
						'<div style="color: green;">' + data.msg + '</div>');
				NovelGenForm__validName = data.name;
			} else {
				$message.empty().append(
						'<div style="color: red;">' + data.msg + '</div>');
				NovelGenForm__validName = '';
			}
		}, 'json');
	}

	NovelGenForm__checkNameDup = _.debounce(NovelGenForm__checkNameDup, 500);
</script>
<style>
h1 {
	text-align: center;
}

.genNovel {
	width: 500px;
	margin: 0 auto;
	padding: 20px;
	word-break: keep-all;
}

.novelName input {
	width: 300px;
}

.seriesStatus {
	position: relative;
}

.seriesStatus>.seriesStatusNotice {
	position: absolute;
	top: 0;
	right: -20%;
	font-size: 0.8rem;
	color: red;
}

.seriesStatus>.seriesStatusNotice>p {
	margin: 13px 0;
}

tbody input {
	height: 20px;
	padding-left: 5px;
}

tbody select {
	height: 25px;
}
</style>
<div class="con">
	<div class="genNovel">
		<form method="post" action="doGenNovel" method="post"
			onsubmit="NovelGenForm__submit(this); return false;">
			<input type="hidden" name="redirectUri"
				value="/usr/novel/${member.nickname}-list?mode=novel&page=1">
			<table>
				<colgroup>
					<col width="150">
				</colgroup>
				<tbody>
					<tr>
						<th>소설 제목</th>
						<td class="novelName">
							<div>
								<input type="text" placeholder="소설 제목을 입력해주세요." name="name"
									onkeyup="NovelGenForm__checkNameDup(this);" maxlength="30"
									autocomplete="off" />
								<div class="message-msg"></div>
							</div>
						</td>
					</tr>
					<tr>
						<th>연작 유무</th>
						<td class="seriesStatus">
							<div>
								<select name="seriesStatus">
									<option value="1">장편</option>
									<option value="0">단편</option>
								</select>
							</div>
							<div class="seriesStatusNotice">
								<p>* 단편을 선택할 경우 소설 리스트에 뜨지 않습니다.</p>
							</div>
						</td>
					</tr>
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
					<tr>
						<th>생성</th>
						<td>
							<button type="submit">생성</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>