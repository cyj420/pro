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

		if(form.loginPw.value.length != 0){
			form.loginPwReal.value = sha256(form.loginPw.value);
		}
		
		form.loginPw.value = '';

		form.submit();
		MemberModifyForm__submitDone = true;
	}
</script>
<h1>회원정보</h1>
<div class="con">
	<form action="sendAuthMail" method="post">
		<input name="id" value="${loginedMember.id}" hidden="hidden">
		<input type="hidden" name="redirectUri" value="${param.redirectUri}">
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
			<label>닉네임 : <input name="nickname" value="${loginedMember.nickname}" disabled="disabled">
			</label>
		</div>
		<div>
			<label>email : <input name="email" type="email" value="${loginedMember.email}" disabled="disabled">
			</label>
		</div>
		<c:if test="${loginedMember.authStatus == true}">
			<div>메일 인증 완료</div>
		</c:if>
		<c:if test="${loginedMember.authStatus == false}">
			<input type="submit" value="인증 메일 발송">
		</c:if>
	</form>
	<a href="./withdrawal?id=${loginedMember.id}" onclick="if ( confirm('정말 탈퇴하시겠습니까?') == false ) { return false; }">
		<button>회원 탈퇴</button>
	</a>
</div>
<%@ include file="../part/foot.jspf"%>