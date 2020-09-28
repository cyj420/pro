<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원정보 수정" />
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

		if (form.nickname.value.trim().length == 0) {
			alert('닉네임 칸은 비울 수 없습니다.');
			form.nickname.focus();
			return;
		}

		if (form.email.value.trim().length == 0) {
			alert('이메일 칸은 비울 수 없습니다.');
			form.email.focus();
			return;
		}

		if (form.loginPw.value.trim().length == 0) {
			alert('비밀번호 칸은 비울 수 없습니다.');
			form.loginPw.focus();
			return;
		}

		form.loginPwReal.value = sha256(form.loginPw.value);

		form.newLoginPwReal.value = '';

		if (form.newLoginPw.value.length != 0) {
			if (form.newLoginPw.value == form.loginPw.value) {
				alert('새 비밀번호는 이전 비밀번호와 달라야 합니다.')
				form.newLoginPw.focus();
				return;
			}

			form.newLoginPwReal.value = sha256(form.newLoginPw.value);
		}

		form.loginPw.value = '';
		form.newLoginPw.value = '';

		if (form.nickname.value != MemberModifyForm__validNickname) {
			alert('다른 닉네임을 입력해주세요.');
			form.nickname.focus();
			return;
		}

		form.submit();
		MemberModifyForm__submitDone = true;
	}

	function MemberModifyForm__checkNicknameDup(input){
		var form = input.form;

		MemberModifyForm__validNickname = '';
		
		form.nickname.value = form.nickname.value.trim();

		if(form.nickname.value.length == 0){
			$(form.nickname).next().empty();
			return;
		}
		
		$.get(
			'getNicknameDup',
			{
				nickname: form.nickname.value
			},
			function(data){
				var $message = $(form.nickname).next();

				if(data.resultCode.substr(0, 2) == 'S-'){
					$message.empty().append('<div style="color: green;">' + data.msg + '</div>');
					MemberModifyForm__validNickname = data.body;
				}
				else {
					$message.empty().append('<div style="color: red;">' + data.msg + '</div>');
					MemberModifyForm__validNickname = '';
				}
			}, 
			'json'
		);
	}

	<!-- lodash 라이브러리 (debounce) 를 이용한 딜레이(0.5초) 설정  -->
	MemberModifyForm__checkNicknameDup = _.debounce(MemberModifyForm__checkNicknameDup, 500);
</script>
<style>
h1 {
	text-align: center;
}

.modify {
	width: 300px;
	margin: 0 auto;
	position: relative;
}

.modify>form>div {
	height: 30px;
	margin-bottom: 10px;
}

.modify>form>div input {
	position: absolute;
	right: 0;
	height: 25px;
}

.button {
	position: absolute;
	right: 40px;
	bottom: -50px;
}

.withdrawal {
	position: absolute;
	right: 40px;
	bottom: -70px;
}
.nickname{
	margin-bottom: 25px !important;
}
.message-msg{
	font-size: .8rem;
	text-align: right;
	margin-top: 10px;
}
</style>
<div class="con">
	<div class="modify">
		<form action="doModify" method="post"
			onsubmit="MemberModifyForm__submit(this); return false;">
			<input name="id" value="${loginedMember.id}" hidden="hidden" /> 
			<input type="hidden" name="redirectUri" value="${param.redirectUri}" /> 
			<input name="loginPwReal" hidden="hidden" /> 
			<input name="newLoginPwReal" hidden="hidden" />
			<div>
				<label>ID : <input name="loginId"
					value="${loginedMember.loginId}" disabled="disabled" />
				</label>
			</div>
			<div>
				<label>이름 : <input name="name" value="${loginedMember.name}"
					disabled="disabled" />
				</label>
			</div>
			<div class="nickname">
				<label>닉네임 : 
				<input name="nickname" value="${loginedMember.nickname}" onkeyup="MemberModifyForm__checkNicknameDup(this);" autocomplete="off"/>
				<div class="message-msg"></div>
				</label>
			</div>
			<div>
				<label>email : <input name="email" type="email"
					value="${loginedMember.email}" autocomplete="off"/>
				</label>
			</div>
			<div>
				<label>비밀번호 : <input name="loginPw" type="password" />
				</label>
			</div>
			<div>
				<label>새 비밀번호 : <input name="newLoginPw" type="password" />
				</label>
			</div>
			<div class="button">
				<input type="submit" value="정보 수정" />
			</div>
		</form>
		<button class="withdrawal">
			<a href="./withdrawal?id=${loginedMember.id}"
				onclick="if ( confirm('정말 탈퇴하시겠습니까?') == false ) { return false; }">
				회원 탈퇴 </a>
		</button>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>