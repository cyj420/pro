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
	max-width: 500px;
	border: 1px solid black;
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
}
.findIdOrPw>a:hover{
	text-decoration: underline;
}

@media ( max-width :800px ) {
	.findArea {
		padding: 30px 20px;
		text-align: center;
		padding-right: 90px;
	}
	.findAreaInside{
		margin-left: 0;
	}
	.findAreaInside input {
		width: 100px;
	}
	.findIdOrPw{
		margin-right: -20%;
	}
}
@media ( min-width :801px ) {
	.findArea {
		padding: 100px;
	}
	.findIdOrPw{
		text-align: center;
	}
}
</style>
<div class="con">
	<div class="findArea">
		<form action="doFindLoginId" method="post">
			<div class="findAreaInside">
				<div>
					<label>이름 : <input name="name" type="text" autocomplete="off"/>
					</label>
				</div>
				<div>
					<label>email : <input name="email" type="email" autocomplete="off"/>
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