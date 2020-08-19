<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>로그인</h1>
<div class="con">
	<form action="doLogin" method="post">
		<div>
			<label>ID : <input name="loginId">
			</label>
		</div>
		<div>
			<label>PW : <input name="loginPw">
			</label>
		</div>
		<input type="submit" value="로그인">
	</form>
	<a href="./findLoginId">ID 찾기</a>
</div>
<%@ include file="../part/foot.jspf"%>