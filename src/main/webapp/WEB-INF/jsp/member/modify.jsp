<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>회원정보 수정</h1>
<div class="con">
	<form action="doModify" method="post">
		<input name="id" value="${loginedMember.id}" type="hidden">
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
		<input type="submit" value="정보 수정">
	</form>
</div>
<%@ include file="../part/foot.jspf"%>