<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ID 찾기" />
<%@ include file="../part/head.jspf"%>
<style>
h1 {
	text-align: center;
}

.findArea {
	width: 500px;
	border: 1px solid black;
	padding: 100px;
	margin: 0 auto;
}

.findAreaInside {
	text-align: right;
	display: inline-block;
	margin-left: 80px;
}

.findAreaInside input {
	margin-bottom: 5px;
	height: 20px;
	padding-left: 5px;
}

.findArea .button {
	height: 56px;
	position: absolute;
	margin-left: 10px;
}
.findIdOrPw{
	margin-top: 20px;
	text-align: center;
}
.findIdOrPw>a:hover{
	text-decoration: underline;
}
</style>
<div class="con">
	<div class="findArea">
		<form action="doFindLoginId" method="post">
			<div class="findAreaInside">
				<div>
					<label>이름 : <input name="name" type="text" />
					</label>
				</div>
				<div>
					<label>email : <input name="email" type="email" />
					</label>
				</div>
			</div>
			<input type="submit" class="button" value="ID 찾기" />
		</form>
		<div class="findIdOrPw">
			<a href="./findLoginPw">PW 찾기</a>
		</div>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>