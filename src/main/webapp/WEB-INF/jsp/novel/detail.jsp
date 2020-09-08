<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${chapter.title} 소설  상세보기" />
<%@ include file="../part/head.jspf"%>
<%@ include file="../part/toastuiEditor.jspf"%>
<style>
video{
	max-width: 500px;
}
img{
	max-width: 500px;
}
.toast-editor-viewer{
	max-width: 1300px;
	min-width: 500px;
}

.reply-list>table>tbody>tr[data-modify-mode="N"] .modify-mode-visible {
	display: none;
}

.reply-list>table>tbody>tr[data-modify-mode="Y"] .modify-mode-invisible{
	display: none;
}

.reply-list>table>tbody>tr>.reply-body-td>.modify-mode-visible>form {
	width: 100%;
	display: block;
}

.reply-list>table>tbody>tr>.reply-body-td>.modify-mode-visible>form>textarea
	{
	width: 100%;
	height: 100px;
	box-sizing: border-box;
	display: block;
}
</style>
<div class="con">
	<table>
		<tbody>
			<tr>
				<th>번호</th>
				<td>${chapter.id}</td>
			</tr>
			<tr>
				<th>날짜</th>
				<td>${chapter.regDate}</td>
			</tr>
			<tr>
				<th>닉네임</th>
				<td>
				<a href="/usr/novel/${chapter.extra.writer}-list">${chapter.extra.writer}</a>
				</td>
			</tr>
			<c:if test="${chapter.extra.series == 1}">
				<tr>
					<th>시리즈명</th>
					<td>
					<a href="/usr/novel/${chapter.extra.writer}-list?novelId=${chapter.novelId}">${chapter.extra.novelName}</a>
					</td>
				</tr>
			</c:if>
			<tr>
				<th>제목</th>
				<td>${chapter.title}</td>
			</tr>
			<c:if test="${chapter.memberId == loginedMember.id }">
				<tr>
					<th>비고</th>
					<td>
					<a href="/usr/novel/${chapter.extra.writer}-modify?id=${chapter.id}">수정</a>
					/
					<a href="/usr/novel/${chapter.extra.writer}-doDelete?id=${chapter.id}">삭제</a>
					</td>
				</tr>
			</c:if>
			<tr>
				<th>내용</th>
				<td>${chapter.body}</td>
			</tr>
		</tbody>
	</table>
	<!-- 본문 END -->
	
	<!-- 댓글 작성 START -->
	<c:if test="${loginedMember != null}">
		<h2>댓글 작성</h2>
		<script>
			function ReplyWriteForm__submit(form) {
				form.body.value = form.body.value.trim();
				if (form.body.value.length == 0) {
					alert('댓글을 입력해주세요.');
					form.body.focus();
					return;
				}
				$.post('./../reply/doWriteReplyAjax', {
					relType : 'novel',
					relId : param.id,
					body : form.body.value
				}, function(data) {
					alert(data.msg);
				}, 'json');
				form.body.value = '';
			}
		</script>
	
		<form action="" onsubmit="ReplyWriteForm__submit(this); return false;">
	
			<table>
				<tbody>
					<tr>
						<th>내용</th>
						<td>
							<div>
								<textarea maxlength="300" name="body" placeholder="내용을 입력해주세요."
									class="height-300"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<th>작성</th>
						<td><input type="submit" value="작성"></td>
					</tr>
				</tbody>
			</table>
		</form>
	</c:if>
	
	<!-- 댓글 작성 END -->
	
	<!-- 댓글 리스트 START -->
	
	<h2>댓글 리스트</h2>
	<div class="reply-list table-box con">
		<table>
			<colgroup>
				<col width="80">
				<col width="180">
				<col width="180">
				<col>
				<col width="200">
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>작성자</th>
					<th>내용</th>
					<th>비고</th>
				</tr>
			</thead>
			<tbody>
	
			</tbody>
		</table>
	</div>
	
	<script>
		function Reply__turnOnModifyMode(el) {
			var $tr = $(el).closest("tr");
			
			var body = $tr
			  .find(">.reply-body-td>.modify-mode-invisible")
			  .html()
			  .trim();
			
			$tr.find(">.reply-body-td>.modify-mode-visible>form>textarea").val(body);
			
			$tr.attr("data-modify-mode", "Y");
			$tr.siblings('[data-modify-mode="Y"]').attr("data-modify-mode", "N");
		}

		function Reply__turnOffModifyMode(el) {
			var $tr = $(el).closest("tr");
			$tr.attr("data-modify-mode", "N");
		}
		
		var ReplyList__$box = $('.reply-list');
		var ReplyList__$tbody = ReplyList__$box.find('tbody');
		var ReplyList__lastLodedId = 0;

		function ReplyList__loadMore() {
			$.get('./../reply/getForPrintReplies', {
				relType : 'novel',
				relId : param.id,
				from : ReplyList__lastLodedId + 1
			}, function(data) {
				if ( data.body.replies && data.body.replies.length > 0 ) {
					ReplyList__lastLodedId = data.body.replies[data.body.replies.length - 1].id;
					ReplyList__drawReplies(data.body.replies);
				}
				setTimeout(ReplyList__loadMore, 2000);
			}, 'json');
		}
		
		function ReplyList__drawReplies(replies) {
			for ( var i = 0; i < replies.length; i++ ) {
				var reply = replies[i];
				ReplyList__drawReply(reply);
			}
		}

		function ReplyList__submitModifyForm(form) {
			  
			form.body.value = form.body.value.trim();
			if (form.body.value.length == 0) {
				alert("내용을 입력해주세요.");
				form.body.focus();
				return;
			}
			var id = form.id.value;
			var newBody = form.body.value;
	
			$.post('./../reply/doModifyReplyAjax',{
				id : id,
				body : newBody
			}, function(data){
				if(data.resultCode && data.resultCode.substr(0,2)=='S-'){
					var $tr = $('.reply-list tbody > tr[data-id="' + id + '"] .reply-body');
					$tr.empty().append(newBody);
					Reply__turnOffModifyMode(form);
				}
			}, 'json');
			var $tr = $(form).closest("tr");
			$tr
				.find(">.reply-body-td>.modify-mode-invisible")
				.empty()
				.append(newBody);
		}
		
		function ReplyList__delete(el) {
			if (confirm("해당 댓글을 삭제하시겠습니까?") == false) {
		    	return;
			}
			var $tr = $(el).closest('tr');
			var id = $tr.attr('data-id');
			
			$.post('./../reply/doDeleteReplyAjax',{
				id : id
			}, function(data){
				$tr.remove();
			}, 'json');
		}
		
		function ReplyList__drawReply(reply) {
			var html = '';

			html = '<tr data-id="'+reply.id+'" data-modify-mode="N">';
			html += '<td>' + reply.id + '</td>';
			html += '<td>' + reply.regDate + '</td>';
			html += '<td>' + reply.extra.writer + '</td>';
			html += '<td class="reply-body-td">';
			html += '<div class="modify-mode-invisible reply-body">' + reply.body + '</div>';
			html += '<div class="modify-mode-visible">';

			html += '<form action="" onsubmit="ReplyList__submitModifyForm(this); return false;">';
			html += '<input type="hidden" name="id" value="' + reply.id + '" />';
			html += '<textarea maxlength="300" name="body"></textarea>';
			html += '<button type="submit" onclick="Reply__turnOffModifyMode(this);">수정완료</button>';
			html += '</form>';
			html += '</div>';
			html += '</td>';
			html += '<td>';
			if (reply.extra.actorCanDelete) {
			html += '<button onclick="ReplyList__delete(this,'+ reply.id+');">삭제</button>';
				html += '<button onclick="Reply__turnOnModifyMode(this);" class="modify-mode-invisible" href="javascript:;">수정</button>';
				html += '<button onclick="Reply__turnOffModifyMode(this);" class="modify-mode-visible" href="javascript:;">취소</button>';
			}
			html += '</td>';
			html += '</tr>';

			var $tr = $(html);
			$tr.data('data-originBody', reply.body);
			ReplyList__$tbody.prepend($tr);
		}
		
		ReplyList__loadMore();
	</script>
	
	<!-- 댓글 리스트 END -->
	
</div>
<%@ include file="../part/foot.jspf"%>