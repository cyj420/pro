<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="새로운 소설" />
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

	function NovelGenForm__checkNameDup(input){
		var form = input.form;

		form.name.value = form.name.value.trim();

		if(form.name.value.length == 0){
			$(form.name).next().empty();
			return;
		}
		
		$.get(
			'getNameDup',
			{
				name: form.name.value
			},
			function(data){
				var $message = $(form.name).next();

				if(data.resultCode.substr(0, 2) == 'S-'){
					$message.empty().append('<div style="color: green;">' + data.msg + '</div>');
					NovelGenForm__validName = data.name;
				}
				else {
					$message.empty().append('<div style="color: red;">' + data.msg + '</div>');
					NovelGenForm__validName = '';
				}
			}, 
			'json'
		);
	}
</script>
<div class="con">
	<form method="post" action="doGenNovel" method="post" onsubmit="NovelGenForm__submit(this); return false;" >
		<input type="hidden" name="redirectUri" value="/usr/novel/${member.nickname}-list">
		<table>
			<colgroup>
				<col width="100">
			</colgroup>
			<tbody>
				<tr>
					<th>소설 제목</th>
					<td>
						<div>
							<input type="text" placeholder="소설 제목을 입력해주세요." name="name"
							onkeyup="NovelGenForm__checkNameDup(this);" maxlength="30" autocomplete="off"/>
							<div class="message-msg"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th>연작 유무</th>
					<td>
						<div>
							<select name="seriesStatus">
								<option value="true">장편</option>
								<option value="false">단편</option>
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
<%@ include file="../part/foot.jspf"%>