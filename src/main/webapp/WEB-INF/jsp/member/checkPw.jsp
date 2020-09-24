<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="비밀번호 입력" />
<%@ include file="../part/head.jspf"%>
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
<style>
h1 {
	text-align: center;
}
.checkPw {
	min-width: 250px;
	max-width: 300px;
	border: 1px solid black;
	margin: 0 auto;
	position: relative;
}
.checkPwInside {
	text-align: right;
	display: inline-block;
}
.checkPw>form {
	margin-bottom: 20px;
	text-align: center;
}
.checkPwInside input {
	margin-bottom: 5px;
	height: 20px;
	padding-left: 5px;
	max-width: 200px;
}

@media ( max-width :800px ) {
	.checkPw {
		padding: 30px;
	}
	.cancel {
		position: absolute;
		bottom: 50px;
		left: 60%;
	}
}
@media ( min-width :801px ) {
	.checkPw {
		padding: 100px;
	}
	.button {
		position: absolute;
		bottom: 80px;
		right: 240px;
	}
	.cancel {
		position: absolute;
		bottom: 80px;
		right: 180px;
	}
}
</style>
<div class="con">
	<div class="checkPw">
		<form action="doCheckPw" method="post"
			onsubmit="MemberCheckPwForm__submit(this); return false;">
			<div class="checkPwInside">
				<input type="hidden" name="redirectUri" value="${param.redirectUri}" />
				<input name="loginPwReal" hidden="hidden" />
				<div>
					<label>PW : <input name="loginPw" type="password" />
					</label>
				</div>
			</div>
			<div class="button">
				<input type="submit" value="입력" />
			</div>
		</form>
		<div class="cancel">
			<button onclick="history.back();" type="button">취소</button>
		</div>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>