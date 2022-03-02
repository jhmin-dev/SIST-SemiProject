<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>번개 맞은 당근 나라</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<div class="page-main">
<!-- 동네 선택, 카테고리 선택, 검색 시작 -->
	<form id="search" action="main.do" method="get">
		<ul class="flex-row space-between">
			<li class="flex-row">
				<i class="bi bi-map-fill"></i>
				<div class="modal">
					<div class="modal-content flex-row">
						<select name="sido">
							<option value="" selected>시/도 선택</option>
						</select>
						<select name="sigungu" style="display: none;">
							<option value="" selected>시/군/구 선택</option>
						</select>
						<select name="bname" style="display: none;">
							<option value="" selected>읍/면/동 선택</option>
						</select>
						<i class="bi bi-search"></i>
					</div>
				</div>
				<select name="category">
					<option disabled <c:if test="${empty param.category}">selected</c:if>>카테고리 선택</option>
					<c:forEach var="item" items="${listCategory}">
					<option value="${item.category}" <c:if test="${param.category==item.category}">selected</c:if>>${item.name}</option>
					</c:forEach>
				</select>
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
				<i class="bi bi-search"></i>
			</li>
			<li class="flex-row">
				<input type="button" value="목록" onclick="location.href = 'main.do';">
				<input type="button" class="point" value="물품 등록" onclick="location.href = '${pageContext.request.contextPath}/product/addForm.do';" <c:if test="${empty user}">disabled title="로그인 후 물품을 등록할 수 있습니다"</c:if>>
			</li>
		</ul>
	</form>
<!-- 동네 선택, 카테고리 선택, 검색 끝 -->
<!-- 목록 출력 시작 -->
	<c:if test="${count==0}">
	<div class="search-none flex-column">
		<div class="search-notice flex-row justify-center">
			검색된 물품이 없습니다.
		</div>
	</div>
	</c:if>
	<c:if test="${count>0}">
	<ul class="list-main flex-row">
		<c:forEach var="item" items="${list}">
		<li class="flex-column">
			<a class="flex-column" href="${pageContext.request.contextPath}/product/detail.do?product=${item.product}">
			<img src="${pageContext.request.contextPath}/upload/${item.photo1}">
			<div class="list-product-title ellipsis">${item.title}</div>
			<div class="price">
				<c:if test="${item.price==0}">
				나눔
				</c:if>
				<c:if test="${item.price>0}">
				<fmt:formatNumber value="${item.price}"/>원
				</c:if>
			</div>
			</a>
			<div class="info"><a href="main.do?sido=${item.memberVO.sido}&sigungu=${item.memberVO.sigungu}&bname=${item.memberVO.bname}">${item.memberVO.bname}</a> · <span class="time" data-registered="${item.registered}" data-modified="${item.modified}"></span></div>
			<div class="info gray">
				관심 ${item.likes} · 댓글 ${item.replies} · 채팅 ${item.chats}
			</div>
		</li>
		</c:forEach>
	</ul>
	<div class="paging flex-row justify-center">
		${pagingHtml}
	</div>
	</c:if>
<!-- 목록 출력 끝 -->	
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/StringUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/UIUtil.js"></script>
<script type="text/javascript">
	let search = document.getElementById('search');
	let search_btn = document.getElementsByClassName('bi-search');
	// 돋보기 클릭시 submit 이벤트 발생
	for(let i=0;i<search_btn.length;i++) {
		search_btn[i].addEventListener('click', function() {
			search.submit();
		}, false);
	}
	
	let address_btn = document.getElementsByClassName('bi-map-fill')[0];
	let modal = document.getElementsByClassName('modal')[0];
	// 지도 클릭시 동네 선택 창 보이기
	address_btn.addEventListener('click', function() {
		modal.classList.toggle('show');
	}, false);
	// 외부영역 클릭 시 동네 선택 창 닫기
	document.addEventListener('click', function(event) {
		event.target === modal ? modal.classList.remove('show') : false
		event.target === modal ? search.submit() : false
	}, false);
	
	// 동네 대분류, 중분류, 소분류 만들기
	const arrayAddress = [];
	<c:forEach var="item" items="${listAddress}" varStatus="status">
	arrayAddress.push({
		address:"${item.address}",
		sido:"${item.sido}",
		sigungu:"${item.sigungu}",
		bname:"${item.bname}",
		index:${status.index}
	})
	</c:forEach>
	let distinctSido = [...new Map(arrayAddress.map(item => [item.sido, item])).values()];
	let distinctSigungu = [...new Map(arrayAddress.map(item => [item.sigungu, item])).values()];
	let distinctBname = [...new Map(arrayAddress.map(item => [item.bname, item])).values()];
	
	let sido = document.getElementsByName('sido')[0];
	let sigungu = document.getElementsByName('sigungu')[0];
	let bname = document.getElementsByName('bname')[0];
	// 동네 대분류 목록 만들기
	for(let i=0; i<distinctSido.length; i++) {
		let option = new Option(distinctSido[i].sido);
		sido.options[i+1] = option;
	}
	// 동네 중분류 목록 만들기
	sido.addEventListener('change', function() {
		sigungu.style.display = '';
		removeOptions(sigungu);
		removeOptions(bname);
		for(let i=0, j=0;i<distinctSigungu.length;i++) {
			if(sido.options[sido.selectedIndex].text==distinctSigungu[i].sido) {
				let option = new Option(distinctSigungu[i].sigungu);
				sigungu.options[j+1] = option;
				j++;
			}
		}
	}, false);
	// 동네 소분류 목록 만들기
	sigungu.addEventListener('change', function() {
		bname.style.display = '';
		removeOptions(bname);
		for(let i=0, j=0;i<distinctBname.length;i++) {
			if(sigungu.options[sigungu.selectedIndex].text==distinctBname[i].sigungu) {
				let option = new Option(distinctBname[i].bname);
				bname.options[j+1] = option;
				j++;
			}
		}
	}, false);
	// 소분류 선택시 submit 이벤트 발생
	bname.addEventListener('change', function() {
		search.submit();
	}, false);
	// 중분류, 소분류 초기화
	function removeOptions(selectElement) {
		var i, L = selectElement.options.length - 1;
		for(i = L; i >= 1; i--) {
			selectElement.remove(i);
		}
	}
	
	// 카테고리 선택시 submit 이벤트 발생
	document.getElementsByName('category')[0].addEventListener('change', function() {
		search.submit();
	}, false)
	
	// 수정 또는 등록 시간 처리
	const times = document.querySelectorAll('.list-main span.time');
	if(times) getTimeFormatted();
</script>
</body>
</html>