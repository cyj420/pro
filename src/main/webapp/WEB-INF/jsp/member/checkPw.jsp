<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>패스워드 입력</h1>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<script>
	var MemberCheckPwForm__submitDone = false;
	function MemberCheckPwForm__submit(form) {
		if (MemberCheckPwForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		form.loginPw.value = form.loginPw.value.trim();

		if (form.loginPw.value.length == 0) {
			form.loginPw.focus();
			alert('비밀번호를 입력해주세요.');

			return;
		}

		form.loginPwReal.value = sha256(form.loginPw.value);
		form.loginPw.value = '';

		form.submit();
		MemberCheckPwForm__submitDone = true;
	}
</script>
<div class="con">
	<form action="doCheckPw" method="post" onsubmit="MemberCheckPwForm__submit(this); return false;">
		<input type="hidden" name="redirectUri" value="${param.redirectUri}">
		<input name="loginPwReal" hidden="hidden">
		<div>
			<label>PW : <input name="loginPw" type="password">
			</label>
		</div>
		<input type="submit" value="입력">
		<button onclick="history.back();" type="button">취소</button>
	</form>
</div>
<%@ include file="../part/foot.jspf"%>