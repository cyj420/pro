<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name} 게시물  상세보기" />
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
</style>
<div class="con">
	<table>
		<tbody>
			<tr>
				<th>번호</th>
				<td>${article.id}</td>
			</tr>
			<tr>
				<th>날짜</th>
				<td>${article.regDate}</td>
			</tr>
			<tr>
				<th>카테고리 id</th>
				<td>${article.cateId}</td>
			</tr>
			<tr>
				<th>작성자 id</th>
				<td>
				<a href="/usr/article/${board.code}-list?memberId=${article.memberId}">${article.extra.writer}</a>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>${article.title}</td>
			</tr>
			<c:if test="${article.memberId == loginedMember.id }">
				<tr>
					<th>비고</th>
					<td>
					<a href="/usr/article/${board.code}-modify?id=${article.id}">수정</a>
					/
					<a href="/usr/article/${board.code}-doDelete?id=${article.id}">삭제</a>
					</td>
				</tr>
			</c:if>
			<tr>
				<th>내용</th>
				<td>
				    <script type="text/x-template">${article.body}</script>
                    <div class="toast-editor toast-editor-viewer"></div>
				</td>
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
					articleId : param.id,
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
	<div class="article-reply-list table-box con">
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
		var ReplyList__$box = $('.article-reply-list');
		var ReplyList__$tbody = ReplyList__$box.find('tbody');
		var ReplyList__lastLodedId = 0;
		function ReplyList__loadMore() {
			$.get('./../reply/getForPrintReplies', {
				articleId : param.id,
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
		function ReplyList__delete(el) {
			if (confirm("해당 댓글을 삭제하시겠습니까?") == false) {
		    	return;
			}
			var $tr = $(el).closest('tr');
			var id = $tr.attr('data-id');	//못 찾았던 이유 > data-id="" 이런 형태여야 하는데 '='을 안 붙였음.
			
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
			html += '<td>' + reply.body + '</td>';
			html += '<td>';
			if (reply.extra.actorCanDelete) {
				html += '<button onclick="ReplyList__delete(this,' + reply.id + ');">삭제</button>';
			}
			html += '</td>';
			html += '</tr>';
			ReplyList__$tbody.prepend(html);
		}
		ReplyList__loadMore();
	</script>
	
	<!-- 댓글 리스트 END -->
	
</div>
<%@ include file="../part/foot.jspf"%>