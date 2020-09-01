<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<script>
	var MemberFindPwForm__submitDone = false;
	function MemberFindPwForm__submit(form) {
		if (MemberFindPwForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		if(form.loginId.value.trim().length == 0){
			alert('ID 칸은 비울 수 없습니다.');
			form.loginId.focus();
			return;
		}

		if(form.name.value.trim().length == 0){
			alert('이름 칸은 비울 수 없습니다.');
			form.name.focus();
			return;
		}

		if(form.email.value.trim().length == 0){
			alert('이메일 칸은 비울 수 없습니다.');
			form.email.focus();
			return;
		}

		form.submit();
		MemberFindPwForm__submitDone = true;
	}
</script>
<h1>PW 찾기</h1>
<div class="con">
	<form action="doFindLoginPw" method="post" onsubmit="MemberFindPwForm__submit(this); return false;">
		<div>
			<label>ID : <input name="loginId" type="text">
			</label>
		</div>
		<div>
			<label>이름 : <input name="name" type="text">
			</label>
		</div>
		<div>
			<label>가입 email : <input name="email" type="email">
			</label>
		</div>
		<input type="submit" value="PW 찾기">
	</form>
</div>
<%@ include file="../part/foot.jspf"%>