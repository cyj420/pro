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
	max-width: 900px;
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
	<!-- 여기까지 본문 -->
	
	
	<!-- 여기부터 댓글 -->
	<c:if test="${loginedMember != null}">
		<h2>댓글 작성</h2>
		<div>
			<script>
			    function Reply__submitWriteForm(form){
				    form.body.value = form.body.value.trim();
				    if(form.body.value.length == 0){
					    alert('내용을 입력하세요.');
					    form.body.focus();
					    return;
					}

				    $.post('./doWriteReplyAjax', {
						articleId : ${param.id},
						memberId : ${loginedMember.id}
						body : form.body.value
					}, function(data){
						if(data.msg){
							alert(data.msg);
						}
						if(data.resultCode.substr(0,2) == 'S-'){
							location.reload(); // 임시
						}
					}, 'json');

					form.body.value = '';
				}
		    </script>

			<form onsubmit="Reply__submitWriteForm(this); return false;">
				<input type="hidden" name="memberId" value="${loginedMember.id}" /> 
				<input type="hidden" name="articleId" value="${param.id}" />
			
				<table>
					<tbody>
						<tr>
							<th>내용</th>
							<td>
								<div class="form-control-box">
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
</div>
<%@ include file="../part/foot.jspf"%>