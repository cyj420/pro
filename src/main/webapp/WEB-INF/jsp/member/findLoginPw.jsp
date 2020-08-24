<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>PW 찾기</h1>
<div class="con">
	<form action="doFindLoginPw" method="post">
		<div>
			<label>ID : <input name="loginId">
			</label>
		</div>
		<div>
			<label>이름 : <input name="name">
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