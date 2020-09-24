<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원정보" />
<%@ include file="../part/head.jspf"%>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<style>
h1{
	text-align: center;
}
.myPage {
	max-width: 300px;
	margin: 0 auto;
	position: relative;
}

.myPage>form>div {
	height: 30px;
	margin-bottom: 10px;
}

.myPage>form>div input {
	position: absolute;
	right: 0;
	height: 25px;
}

.mailAuthStatus {
	position: absolute;
	left: 310px;
	top: 123px;
	width: 110px;
}

.mailAuthStatus>div {
	margin: 0;
	padding: 0;
	color: red;
	font-size: .9rem;
}

.button {
	position: absolute;
	right: 40px;
	bottom: -70px;
}

@media ( max-width :800px ) {
	.mailAuthStatus {
		left: 130px !important;
		top: 150px;
	}
}
</style>
<div class="con">
	<div class="myPage">
		<form action="doMyPage" method="post"
			onsubmit="MemberMyPageForm__submit(this); return false;">
			<input name="id" value="${loginedMember.id}" hidden="hidden" /> <input
				type="hidden" name="redirectUri" value="${param.redirectUri}" />
			<div>
				<label>ID : <input name="loginId"
					value="${loginedMember.loginId}" disabled="disabled" />
				</label>
			</div>
			<div>
				<label>이름 : <input name="name"
					value="${loginedMember.name}" disabled="disabled" />
				</label>
			</div>
			<div>
				<label>닉네임 : <input name="nickname"
					value="${loginedMember.nickname}" disabled="disabled" />
				</label>
			</div>
			<div>
				<label>email : <input name="email"
					type="email" value="${loginedMember.email}" disabled="disabled" />
				</label>
			</div>
			<div class="button">
				<input type="submit" value="정보 수정하기" />
			</div>
		</form>
		<div class="mailAuthStatus">
			<c:if test="${loginedMember.authStatus == false}">
				<button>
					<a href="./sendAuthMail">인증 메일 보내기</a>
				</button>
			</c:if>
			<c:if test="${loginedMember.authStatus == true}">
				<div>* 메일 인증 완료</div>
			</c:if>
		</div>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>