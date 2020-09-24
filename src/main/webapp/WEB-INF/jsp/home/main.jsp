<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Main" />
<%@ include file="../part/head.jspf"%>
<style>
.main {
	max-width: 1100px;
	margin: 0 auto;
	text-align: center;
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
	opacity: 0.2;
	transition: 0.3s;
}

.card:hover img {
	opacity: 0.5;
}

.card>.novelName {
	font-weight: 700;
	position: absolute;
	text-align: center;
	top: 40%;
	left: 50%;
	font-size: 1.2rem;
	transform: translateX(-50%);
	transition: 0.3s;
}

.card:hover>.novelName {
	text-shadow: 2px 2px 2px gray;
}

.card>.writer {
	font-weight: 500;
	position: absolute;
	text-align: center;
	bottom: 20px;
	right: 20px;
	font-style: italic;
	color: white;
	text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;
}

.card>.hit {
	font-weight: 300;
	position: absolute;
	text-align: center;
	top: 20px;
	right: 20px;
	color: white;
}

.card>div:nth-child(2)>p {
	word-break: keep-all;
	margin: 0;
}

.card>.writer>p {
	margin: 0;
}

.card>.hit>p {
	margin: 0;
}

.card>.hit {
	display: none;
}

.card:hover>.hit {
	display: block;
	text-shadow: -1px 0 red, 0 1px red, 1px 0 red, 0 -1px red;
}

.card:hover>.writer {
	color: black;
	text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;
}
</style>
<div class="con">
	<div class="main">
		<c:forEach items="${novels }" var="novel">
			<a
				href="../novel/${novel.extra.writer }-list?mode=novel&novelId=${novel.id }"
				class="card"> 
				<img class="mobile-cannot-see" alt=""
				src="https://cdn.pixabay.com/photo/2015/06/08/15/11/typewriter-801921_1280.jpg" />
				<div class="novelName">
					<c:if test="${novel.name.length() < 30 }">
						<p>${novel.name }</p>
					</c:if>
					<c:if test="${novel.name.length() >= 30 }">
						<p style="font-size: 1rem;">${novel.name}</p>
					</c:if>
				</div>
				<div class="writer">
					<p>w. ${novel.extra.writer }</p>
				</div>
				<div class="hit">
					<p>Hit! ${novel.totalHit }</p>
				</div>
			</a>
		</c:forEach>
	</div>
</div>

<%@ include file="../part/foot.jspf"%>