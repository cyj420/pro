<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../part/head.jspf"%>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<h1>회원정보</h1>
<div class="con">
	<form action="doMyPage" method="post" onsubmit="MemberMyPageForm__submit(this); return false;">
		<input name="id" value="${loginedMember.id}" hidden="hidden">
		<input type="hidden" name="redirectUri" value="${param.redirectUri}">
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
			<a href="./sendAuthMail" >인증 메일 보내기</a>
		</c:if>
		
		<input type="submit" value="정보 수정하기">
	</form>
</div>
<%@ include file="../part/foot.jspf"%>