<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../part/head.jspf"%>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<script>
	var MemberModifyForm__submitDone = false;
	function MemberModifyForm__submit(form) {
		if (MemberModifyForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		if(form.nickname.value.trim().length == 0){
			alert('닉네임 칸은 비울 수 없습니다.');
			form.nickname.focus();
			return;
		}

		if(form.email.value.trim().length == 0){
			alert('이메일 칸은 비울 수 없습니다.');
			form.email.focus();
			return;
		}

		if(form.loginPw.value.length != 0 && form.loginPw.value.trim().length == 0){
			alert('비밀번호 칸에 공백은 넣을 수 없습니다.');
			form.loginPw.focus();
			return;
		}

		if(form.loginPw.value.length != 0){
			form.loginPwReal.value = sha256(form.loginPw.value);
		}
		
		form.loginPw.value = '';

		form.submit();
		MemberModifyForm__submitDone = true;
	}
</script>
<h1>회원정보 수정</h1>
<div class="con">
	<form action="doModify" method="post" onsubmit="MemberModifyForm__submit(this); return false;">
		<input name="id" value="${loginedMember.id}" hidden="hidden">
		<input name="loginPwReal" hidden="hidden">
		<div>
			<label>ID : <input name="loginId" value="${loginedMember.loginId}" disabled="disabled">
			</label>
		</div>
		<div>
			<label>이름 : <input name="name" value="${loginedMember.name}" disabled="disabled">
			</label>
		</div>
		<div>
			<label>닉네임 : <input name="nickname" value="${loginedMember.nickname}">
			</label>
		</div>
		<div>
			<label>email : <input name="email" type="email" value="${loginedMember.email}">
			</label>
		</div>
		<div>
			<label>비밀번호 : <input name="loginPw" type="password">
			</label>
		</div>
		<input type="submit" value="정보 수정">
	</form>
</div>
<%@ include file="../part/foot.jspf"%>