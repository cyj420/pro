<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../part/head.jspf"%>
<h1>ID 찾기</h1>
<div class="con">
	<form action="doFindLoginId" method="post">
		<div>
			<label>이름 : <input name="name">
			</label>
		</div>
		<div>
			<label>email : <input name="email">
			</label>
		</div>
		<input type="submit" value="ID 찾기">
	</form>
</div>
<%@ include file="../part/foot.jspf"%>