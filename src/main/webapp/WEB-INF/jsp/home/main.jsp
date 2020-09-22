<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Main" />
<%@ include file="../part/head.jspf"%>
<style>
.main {
	max-width: 1100px;
}

.card {
	width: 300px;
	height: 200px;
	margin: 20px;
	display: inline-block;
	position: relative;
}

.card img {
	width: 100%;
	opacity: 0.3;
	transition: 0.3s;
}

.card:hover img {
	opacity: 0.7;
}

.card>div:nth-child(2) {
	font-weight: 500;
	position: absolute;
	text-align: center;
	top: 40%;
	left: 50%;
	font-size: 1.2rem;
	transform: translateX(-50%);
	transition: 0.5s;
}

.card:hover>div:nth-child(2) {
	font-weight: 700;
	font-size: 1.5rem;
}

.card>div:nth-child(3) {
	font-weight: 300;
	position: absolute;
	text-align: center;
	bottom: 20px;
	right: 20px;
}

.card>div:nth-child(2)>p {
	word-break: keep-all;
	margin: 0;
}

.card>div:nth-child(3)>p {
	margin: 0;
}
</style>
<div class="main con">
	<c:forEach items="${novels }" var="novel">
		<a href="../novel/${novel.extra.writer }-list?mode=novel&novelId=${novel.id }" class="card"> <img alt=""
			src="https://cdn.pixabay.com/photo/2015/06/08/15/11/typewriter-801921_1280.jpg" />
			<div>
				<p>${novel.name }</p>
			</div>
			<div>
				<p>작가명: ${novel.extra.writer }</p>
			</div>
		</a>
	</c:forEach>
</div>

<%@ include file="../part/foot.jspf"%>