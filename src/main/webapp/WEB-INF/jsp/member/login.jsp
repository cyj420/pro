<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>로그인</h1>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<script>
	var MemberLoginForm__submitDone = false;
	function MemberLoginForm__submit(form) {
		if (MemberLoginForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		form.loginId.value = form.loginId.value.trim();

		if (form.loginId.value.length == 0) {
			form.loginId.focus();
			alert('로그인 아이디를 입력해주세요.');

			return;
		}

		form.loginPw.value = form.loginPw.value.trim();

		if (form.loginPw.value.length == 0) {
			form.loginPw.focus();
			alert('로그인 비밀번호를 입력해주세요.');

			return;
		}

		form.loginPwReal.value = sha256(form.loginPw.value);
		form.loginPw.value = '';

		form.submit();
		MemberLoginForm__submitDone = true;
	}
</script>
<div class="con">
	<form action="doLogin" method="post" onsubmit="MemberLoginForm__submit(this); return false;">
		<input type="hidden" name="redirectUri" value="${param.redirectUri}">
		<input name="loginPwReal" hidden="hidden">
		<div>
			<label>ID : <input name="loginId" type="text">
			</label>
		</div>
		<div>
			<label>PW : <input name="loginPw" type="password">
			</label>
		</div>
		<input type="submit" value="로그인">
		<button class="btn btn-info" onclick="history.back();" type="button">취소</button>
	</form>
	<a href="./findLoginId">ID 찾기</a>
	<a href="./findLoginPw">PW 찾기</a>
</div>
<%@ include file="../part/foot.jspf"%>