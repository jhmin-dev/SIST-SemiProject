<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- header 시작 -->
<ul class="header">
	<li class="header-title">
		<a href="${pageContext.request.contextPath}/main/main.do">번개 맞은 당근 나라</a>
	</li>
	<li class="header-menu">
		<ul>
			<li>
				<c:if test="${!empty user}">
				<a href="${pageContext.request.contextPath}/member/logout.do">
					<div>로그아웃</div>
				</a>
				</c:if>
				<c:if test="${empty user}">
				<a href="${pageContext.request.contextPath}/member/loginForm.do">
					<div>로그인/회원 가입</div>
				</a>
				</c:if>
			</li>
			<li>
			<a class="header-who" href="${pageContext.request.contextPath}/member/myPage.do">
				<div>MY 페이지</div>
				<c:if test="${!empty user && !empty profile}">
				<img class="header-profile" src="${pageContext.request.contextPath}/upload/${profile}">
				</c:if>
				<c:if test="${!empty user && empty profile}">
				<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
  <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
  <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
</svg>
				</c:if>
			</a>
			</li>
			<li>
				<a class="header-chat" href="${pageContext.request.contextPath}/chat/chat.do">
					<c:if test="${empty user}">
					<i class="bi bi-chat-fill disabled"></i>
					</c:if>
					<c:if test="${!empty user}">
					<i class="bi bi-chat-fill"></i>
					</c:if>
				</a>
			</li>
		</ul>
	</li>
</ul>
<!-- header 끝 -->







