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
.reply-list>table{
	width: 100%;
	background: rgb(221, 221, 221);
}
.reply-write table{
	width: 100%;
	border: 1px solid #424242;
}

.reply-total{
	border: 3px solid black;
	margin: 60px 0;
	padding: 20px 0;
}
</style>
<div class="con">
	<table class="table-detail">
		<colgroup>
			<col width="100" />
			<col width="600" />
		</colgroup>
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
				<th>조회수</th>
				<td>${article.hit}</td>
			</tr>
			<tr>
				<th>닉네임</th>
				<td>
				<a href="/usr/article/${board.code}-list?memberId=${article.memberId}">${article.extra.writer}</a>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>${article.title}</td>
			</tr>
			<c:if test="${article.memberId == loginedMember.id || loginedMember.id == 1}">
				<tr>
					<th>비고</th>
					<td>
					<c:if test="${article.memberId == loginedMember.id }">
						<a href="/usr/article/${board.code}-modify?id=${article.id}">수정</a>
						/
					</c:if>
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
	
	<!-- 이전글/다음글 START -->
	<c:if test="${preCh != null }">
		<div>
			이전글 : <a href="/usr/article/${board.code}-detail?id=${preCh}&memberId=${memberId}&searchKeyword=${param.searchKeyword}">${preChName }</a>
		</div>
	</c:if>
	<c:if test="${nextCh != null }">
		<div>
			다음글 : <a href="/usr/article/${board.code}-detail?id=${nextCh}&memberId=${memberId}&searchKeyword=${param.searchKeyword}">${nextChName }</a>
		</div>
	</c:if>
	<!-- 이전글/다음글 END -->
	
	<div class="reply-total">
		<!-- 댓글 작성 START -->
		<c:if test="${loginedMember != null}">
			<div class="reply-write con">
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
							relType : 'article',
							relId : param.id,
							secret : false,
							body : form.body.value
						}, function(data) {
							alert(data.msg);
						}, 'json');
						form.body.value = '';
					}
				</script>
		
			
				<form action="" onsubmit="ReplyWriteForm__submit(this); return false;">
					<table>
						<colgroup>
							<col width="100" />
							<col width="600" />
						</colgroup>
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
			</div>
		</c:if>
		
		<!-- 댓글 작성 END -->
		
		<!-- 댓글 리스트 START -->
		
		<div class="reply-list con">
		<h2>댓글 리스트</h2>
			<table>
				<colgroup>
					<col width="50" class="mobile-cannot-see">
					<col width="100" class="mobile-cannot-see">
					<col width="80">
					<col width="400">
					<col width="80">
				</colgroup>
				<thead>
					<tr>
						<th class="mobile-cannot-see">번호</th>
						<th class="mobile-cannot-see">날짜</th>
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
					relType : 'article',
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
				html += '<td class="mobile-cannot-see">' + reply.id + '</td>';
				html += '<td class="mobile-cannot-see">' + reply.regDate + '</td>';
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
	
</div>
<%@ include file="../part/foot.jspf"%>