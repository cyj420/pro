<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원가입" />
<%@ include file="../part/head.jspf"%>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

<script>
	var MemberJoinForm__validLoginId = '';
	var MemberJoinForm__validNickname = '';
	
	var MemberJoinForm__submitDone = false;

	function MemberJoinForm__submit(form) {
		if (MemberJoinForm__submitDone) {
			alert('처리중입니다.');
			return;
		}

		form.loginId.value = form.loginId.value.trim();

		if (form.loginId.value.length == 0) {
			alert('로그인 아이디를 입력해주세요.');
			form.loginId.focus();
			return;
		}

		if (form.loginId.value != MemberJoinForm__validLoginId) {
			alert('다른 아이디를 입력해주세요.');
			form.loginId.focus();
			return;
		}

		form.loginPw.value = form.loginPw.value.trim();

		if (form.loginPw.value.length == 0) {
			alert('로그인 비밀번호를 입력해주세요.');
			form.loginPw.focus();
			return;
		}

		if (form.loginPwConfirm.value.length == 0) {
			alert('로그인 비밀번호 확인을 입력해주세요.');
			form.loginPwConfirm.focus();
			return;
		}

		if (form.loginPw.value != form.loginPwConfirm.value) {
			alert('로그인 비밀번호 확인이 일치하지 않습니다.');
			form.loginPwConfirm.focus();
			return;
		}

		form.name.value = form.name.value.trim();

		if (form.name.value.length == 0) {
			alert('이름을 입력해주세요.');
			form.name.focus();
			return;
		}

		form.nickname.value = form.nickname.value.trim();

		if (form.nickname.value.length == 0) {
			alert('닉네임을 입력해주세요.');
			form.nickname.focus();
			return;
		}

		if (form.nickname.value != MemberJoinForm__validNickname) {
			alert('다른 닉네임을 입력해주세요.');
			form.nickname.focus();
			return;
		}

		form.email.value = form.email.value.trim();

		if (form.email.value.length == 0) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			return;
		}

		form.loginPwReal.value = sha256(form.loginPw.value);
		form.loginPw.value = '';
		form.loginPwConfirm.value = '';

		form.submit();
		MemberJoinForm__submitDone = true;
	}

	function MemberJoinForm__checkLoginIdDup(input){
		var form = input.form;

		form.loginId.value = form.loginId.value.trim();

		if(form.loginId.value.length == 0){
			$(form.loginId).next().empty();
			return;
		}
		
		$.get(
			'getLoginIdDup',
			{
				loginId: form.loginId.value
			},
			function(data){
				var $message = $(form.loginId).next();

				if(data.resultCode.substr(0, 2) == 'S-'){
					$message.empty().append('<div style="color: green;">' + data.msg + '</div>');
					MemberJoinForm__validLoginId = data.body;
				}
				else {
					$message.empty().append('<div style="color: red;">' + data.msg + '</div>');
					MemberJoinForm__validLoginId = '';
				}
			}, 
			'json'
		);
	}

	function MemberJoinForm__checkNicknameDup(input){
		var form = input.form;

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
					MemberJoinForm__validNickname = data.body;
				}
				else {
					$message.empty().append('<div style="color: red;">' + data.msg + '</div>');
					MemberJoinForm__validNickname = '';
				}
			}, 
			'json'
		);
	}

	<!-- lodash 라이브러리 (debounce) 를 이용한 딜레이(0.5초) 설정  -->
	MemberJoinForm__checkLoginIdDup = _.debounce(MemberJoinForm__checkLoginIdDup, 500);
	MemberJoinForm__checkNicknameDup = _.debounce(MemberJoinForm__checkNicknameDup, 500);
</script>
<style>
h1{
	text-align: center;
}
.joinArea {
	width: 450px;
	margin: 0 auto;
}

input {
	width: 85%;
	height: 30px;
	padding: 0 10px;
}
</style>
<div class="con">
	<div class="joinArea">
		<form method="POST" action="doJoin"
			onsubmit="MemberJoinForm__submit(this); return false;">
			<input name="loginPwReal" hidden="hidden" />
			<table>
				<colgroup>
					<col width="150" />
				</colgroup>
				<tbody>
					<tr>
						<th>아이디</th>
						<td>
							<div>
								<input type="text" placeholder="로그인 아이디 입력해주세요." name="loginId"
									onkeyup="MemberJoinForm__checkLoginIdDup(this);" maxlength="30"
									autocomplete="off" />
								<div class="message-msg"></div>
							</div>
						</td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td>
							<div>
								<input type="password" placeholder="로그인 비밀번호를 입력해주세요."
									name="loginPw" maxlength="30" />
							</div>
						</td>
					</tr>
					<tr>
						<th>비밀번호 확인</th>
						<td>
							<div>
								<input type="password" placeholder="로그인 비밀번호 확인을 입력해주세요."
									name="loginPwConfirm" maxlength="30" />
							</div>
						</td>
					</tr>
					<tr>
						<th>이름</th>
						<td>
							<div>
								<input type="text" placeholder="이름을 입력해주세요." name="name"
									maxlength="20" />
							</div>
						</td>
					</tr>
					<tr>
						<th>닉네임</th>
						<td>
							<div>
								<input type="text" placeholder="닉네임 입력해주세요." name="nickname"
									onkeyup="MemberJoinForm__checkNicknameDup(this);"
									maxlength="30" autocomplete="off" />
								<div class="message-msg"></div>
							</div>
						</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>
							<div>
								<input type="email" placeholder="이메일 입력해주세요." name="email"
									maxlength="50" />
							</div>
						</td>
					</tr>
					<tr>
						<th>가입</th>
						<td>
							<button type="submit">회원가입</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>
<%@ include file="../part/foot.jspf"%>