<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="PW 찾기" />
<%@ include file="../part/head.jspf"%>
<script>
	var MemberFindPwForm__submitDone = false;
	function MemberFindPwForm__submit(form) {
		if (MemberFindPwForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		if (form.loginId.value.trim().length == 0) {
			alert('ID 칸은 비울 수 없습니다.');
			form.loginId.focus();
			return;
		}

		if (form.name.value.trim().length == 0) {
			alert('이름 칸은 비울 수 없습니다.');
			form.name.focus();
			return;
		}

		if (form.email.value.trim().length == 0) {
			alert('이메일 칸은 비울 수 없습니다.');
			form.email.focus();
			return;
		}

		form.submit();
		MemberFindPwForm__submitDone = true;
	}
</script>
<style>
h1 {
	text-align: center;
}

.findArea {
	min-width: 250px;
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

.findAreaInside>div {
	margin-bottom: 5px;
}
.findIdOrPw{
	margin-top: 50px;
	text-align: center;
}
.findIdOrPw>a:hover{
	text-decoration: underline;
}

@media ( max-width :800px ) {
	.findArea {
		padding: 30px 20px;
		text-align: center;
		padding-right: 70px;
		position: relative;
	}
	.findAreaInside{
		margin-left: 0;
	}
	.findAreaInside input {
		width: 150px;
	}
	.findArea .button {
		position: absolute;
		bottom: 70px;
		left: 50%;
		transform: translateX(-85%);
	}
}
@media ( min-width :801px ) {
	.findArea {
		padding: 100px;
	}
	.findArea .button {
		height: 98px;
		position: absolute;
		margin-left: 10px;
	}
}
</style>
<div class="con">
	<div class="findArea">
		<form action="doFindLoginPw" method="post"
			onsubmit="MemberFindPwForm__submit(this); return false;">
			<div class="findAreaInside">
				<div>
					<label>ID : <input name="loginId" type="text" />
					</label>
				</div>
				<div>
					<label>이름 : <input name="name" type="text" />
					</label>
				</div>
				<div>
					<label>email : <input name="email" type="email" />
					</label>
				</div>
			</div>
			<input type="submit" class="button" value="PW 찾기" />
		</form>
		<div class="findIdOrPw">
			<a href="./findLoginId">ID 찾기</a>
		</div>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>