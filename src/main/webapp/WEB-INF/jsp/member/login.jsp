<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="LOGIN" />
<%@ include file="../part/head.jspf"%>
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

<style>
h1{
	text-align: center;
}
.loginArea {
	min-width: 300px;
	max-width: 500px;
	border: 1px solid black;
	padding: 20px;
	margin: 0 auto;
}

@media ( max-width :800px ) {
	.loginAreaInside {
		text-align: right;
		display: inline-block;
		margin-left: -100px;
	}
	.loginArea form{
		text-align: center;
		font-size: .8rem;
	}
	.loginAreaInside input {
		width: 50%;
	}
	.findIdOrPw{
		font-size: .8rem;
	}
}

@media ( min-width :801px ) {
	.loginArea {
		padding: 100px;
	}
	.loginAreaInside {
		text-align: right;
		display: inline-block;
		margin-left: 80px;
		margin-left: 100px;
	}
}

.login-button {
	height: 56px;
	position: absolute;
	margin-left: 10px;
}

.loginArea>form {
	margin-bottom: 20px;
}

.loginAreaInside input {
	margin-bottom: 5px;
	height: 20px;
	padding-left: 5px;
}

.findIdOrPw {
	text-align: center;
}

.findIdOrPw a:hover{
	text-decoration: underline;
}
</style>

<div class="con">
	<div class="loginArea">
		<form action="doLogin" method="post"
			onsubmit="MemberLoginForm__submit(this); return false;">
			<input type="hidden" name="redirectUri" value="${param.redirectUri}" />
			<input name="loginPwReal" hidden="hidden" />
			<div class="loginAreaInside">
				<div>
					<label>아이디 : <input name="loginId" type="text" />
					</label>
				</div>
				<div>
					<label>비밀번호 : <input name="loginPw"
						type="password" />
					</label>
				</div>
			</div>
			<input type="submit" class="login-button" value="로그인" />
		</form>
		<div class="findIdOrPw">
			<label>ID/PW를 잊어버리셨나요?</label> 
			<div>
			<a href="./findLoginId">ID 찾기</a>
			 | 
			<a href="./findLoginPw">PW 찾기</a>
			</div>
		</div>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>