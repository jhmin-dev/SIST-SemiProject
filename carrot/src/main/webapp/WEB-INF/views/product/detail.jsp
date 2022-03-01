<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>물품 상세 정보 : ${productVO.title}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.carousel.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reply.css">
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<div class="page-main">
	<ul class="detail flex-column">
<!-- 물품 사진 캐러셀 시작 -->
		<c:if test="${productVO.deleted==0}">
		<li>
			<div id="carousel" class="carousel slide" data-bs-ride="carousel">
				<div class="carousel-indicators">
					<button type="button" data-bs-target="#carousel" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
					<c:if test="${!empty productVO.photo2}">
					<button type="button" data-bs-target="#carousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
					</c:if>
					<c:if test="${!empty productVO.photo3}">
					<button type="button" data-bs-target="#carousel" data-bs-slide-to="2" aria-label="Slide 2"></button>
					</c:if>
					<c:if test="${!empty productVO.photo4}">
					<button type="button" data-bs-target="#carousel" data-bs-slide-to="3" aria-label="Slide 2"></button>
					</c:if>
					<c:if test="${!empty productVO.photo5}">
					<button type="button" data-bs-target="#carousel" data-bs-slide-to="4" aria-label="Slide 2"></button>
					</c:if>
				</div>	
				<div class="carousel-inner">
					<div class="carousel-item active">
						<img class="d-block w-100"  src="${pageContext.request.contextPath}/upload/${productVO.photo1}">
					</div>
					<c:if test="${!empty productVO.photo2}">
					<div class="carousel-item">
						<img class="d-block w-100" src="${pageContext.request.contextPath}/upload/${productVO.photo2}">
					</div>
					</c:if>
					<c:if test="${!empty productVO.photo3}">
					<div class="carousel-item">
						<img class="d-block w-100" src="${pageContext.request.contextPath}/upload/${productVO.photo3}">
					</div>
					</c:if>
					<c:if test="${!empty productVO.photo4}">
					<div class="carousel-item">
						<img class="d-block w-100" src="${pageContext.request.contextPath}/upload/${productVO.photo4}">
					</div>
					</c:if>
					<c:if test="${!empty productVO.photo5}">
					<div class="carousel-item">
						<img class="d-block w-100" src="${pageContext.request.contextPath}/upload/${productVO.photo5}">
					</div>
					</c:if>
				</div>
				<c:if test="${!empty productVO.photo2}">
				<button class="carousel-control-prev" type="button" data-bs-target="#carousel" data-bs-slide="prev">
					<span class="carousel-control-prev-icon" aria-hidden="true"></span>
					<span class="visually-hidden">Previous</span>
				</button>
				<button class="carousel-control-next" type="button" data-bs-target="#carousel" data-bs-slide="next">
					<span class="carousel-control-next-icon" aria-hidden="true"></span>
					<span class="visually-hidden">Next</span>
				</button>
				</c:if>
			</div>
		</li>
		</c:if>
<!-- 물품 사진 캐러셀 끝 -->
<!-- 판매자 프로필, 매너 평가 시작 -->
		<li class="seller">
			<a class="flex-row space-between" href="#">
				<div class="who flex-row">
					<c:if test="${empty sellerVO.profile}">
					<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
						<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
						<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
					</svg>
					</c:if>
					<c:if test="${!empty sellerVO.profile}">
					<img class="profile" src="${pageContext.request.contextPath}/upload/${sellerVO.photo}">
					</c:if>	
					<div class="flex-column justify-end">
						<div>${sellerVO.nickname}</div>
						<div>${sellerVO.home}</div>
					</div>
				</div>
				<div class="manner flex-column justify-end align-end">
					<div class="manner-stars">
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
					</div>
					<div class="manner-info gray underline"></div>
				</div>
			</a>
		</li>
		<li><hr></li>
<!-- 판매자 프로필, 매너 평가 끝 -->
<!-- 물품 판매글 시작 -->
		<li class="product flex-column">
			<div class="title">${productVO.title}</div>
			<div class="gray">
				<a class="underline" href="${pageContext.request.contextPath}/main/main.do?category=${categoryVO.category}">${categoryVO.name}</a> · 
				<c:if test="${empty productVO.modified}">${productVO.registered}</c:if>
				<c:if test="${!empty productVO.modified}">${productVO.modified}</c:if>
				<c:if test="${productVO.deleted==0 && !empty productVO.modified}"> · 수정됨</c:if>
				<c:if test="${productVO.deleted==1}"> · 삭제됨</c:if>
			</div>
			<c:if test="${productVO.deleted==0}">
			<div class="subtitle">
				<c:if test="${productVO.price>0}">
				<fmt:formatNumber value="${productVO.price}"/>원
				</c:if>
				<c:if test="${productVO.price==0}">
				나눔
				</c:if>
			</div>
			<div class="content">${productVO.content}</div>
			<div class="gray"><a id="toggle_comments">댓글 <span id="current_replies">${productVO.replies}</span></a> · 채팅 ${productVO.chats} · 관심 <span id="current_likes">${productVO.likes}</span></div>
			</c:if>
			<c:if test="${productVO.deleted==1}">
			<div class="content deleted">삭제된 물품입니다.</div>
			</c:if>
		</li>
		<li><hr></li>
<!-- 물품 판매글 끝 -->
<!-- 버튼들 시작 -->
		<li class="flex-row space-between">
			<c:if test="${exist}">
			<i class="bi bi-heart-fill" id="like"></i>
			</c:if>
			<c:if test="${!exist}">
			<i class="bi bi-heart" id="like"></i>
			</c:if>
			<div class="other">
				<input type="button" class="big" value="이전으로" onclick="history.go(-1);">
				<c:if test="${productVO.deleted==0}">
				<c:choose>
					<c:when test="${user==productVO.member}">
					<c:if test="${productVO.complete==0}">
					<input type="button" class="big point" value="거래 완료하기" id="complete" onclick="location.href = '${pageContext.request.contextPath}/chat/chat.do?filter=2';">
					</c:if>
					<input type="button" class="big point" value="물품 수정하기" onclick="location.href = 'modifyForm.do?product=${productVO.product}';" <c:if test="${productVO.complete==1}">disabled</c:if>>
					</c:when>
					<c:when test="${user==productVO.buyer}">
					<input type="button" class="big point" value="거래 후기 남기기" onclick="">
					</c:when>
					<c:otherwise>
					<input type="button" class="big point" value="채팅으로 거래하기" id="link_chatroom" <c:if test="${empty user || productVO.complete==1}">disabled</c:if> <c:if test="${empty user}">title="로그인 후 채팅으로 거래할 수 있습니다"</c:if>>
					</c:otherwise>
				</c:choose>
				</c:if>
			</div>
		</li>
		<li><hr></li>
<!-- 버튼들 끝 -->
<!-- 댓글 시작 -->
		<li class="comment-list hide">
			<button type="button" class="comment-more reverse-silver hide">댓글 더보기</button>
			<div class="modal" id="modify_area">
				<div class="modal-content flex-column">
					<textarea name="content" <c:if test="${empty user}">disabled title="로그인 후 댓글을 수정할 수 있습니다"</c:if>></textarea>
					<input type="button" class="point" value="댓글 수정" id="modify_comment" <c:if test="${empty user}">disabled title="로그인 후 댓글을 수정할 수 있습니다"</c:if>>
				</div>
			</div>
			<ul>
			
			</ul>
		</li>
		<li class="comment-list hide"><hr></li>
		<li class="comment-write hide flex-row justify-center align-start">
			<textarea name="content" <c:if test="${empty user}">disabled title="로그인 후 댓글을 작성할 수 있습니다"</c:if>></textarea>
			<input type="button" class="point" value="댓글 작성" id="write_comment" <c:if test="${empty user}">disabled title="로그인 후 댓글을 작성할 수 있습니다"</c:if>>
		</li>
		<li class="comment-write hide"><hr></li>
<!-- 댓글 끝 -->
<!-- 실시간 중고 더보기 시작 -->
		<li>
			<div class="title">실시간 중고 더보기</div>
			<ul class="list-other flex-row space-between">
				<c:forEach var="other" items="${list}">
				<li class="flex-column">
					<a class="flex-column" href="${pageContext.request.contextPath}/product/detail.do?product=${other.product}">
					<img src="${pageContext.request.contextPath}/upload/${other.photo1}">
					<div class="list-product-title ellipsis">${other.title}</div>
					<div class="price">
						<c:if test="${other.price==0}">
						나눔
						</c:if>
						<c:if test="${other.price>0}">
						<fmt:formatNumber value="${other.price}"/>원
						</c:if>
					</div>
					</a>
					<div class="info"><a href="${pageContext.request.contextPath}/main/main.do?sido=${other.memberVO.sido}&sigungu=${other.memberVO.sigungu}&bname=${other.memberVO.bname}">${other.memberVO.bname}</a> · <span class="time" data-registered="${other.registered}" data-modified="${other.modified}"></span></div>
					<div class="info gray">
						관심 ${other.likes} · 댓글 ${other.replies} · 채팅 ${other.chats}
					</div>
				</li>
				</c:forEach>
			</ul>
		</li>
<!-- 실시간 중고 더보기 끝 -->
	</ul>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/StringUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/UIUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validateInput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	let cp = '${pageContext.request.contextPath}';
	
	// 매너 평점 처리
	fillMannerRate('${sellerVO.rate}', document.querySelectorAll('.manner-stars i.bi'), document.querySelector('.manner-info'));
	
	// 실시간 중고 더보기에서 수정 또는 등록 시간 처리
	getTimeFormatted(document.querySelectorAll('.list-other span.time'));

/*	
	// 댓글 토글
	let comment_lists = document.getElementsByClassName('comment-list');
	let comment_writes = document.getElementsByClassName('comment-write');
	let toggle_comments = document.getElementById('toggle_comments');
	if(toggle_comments) { // 물품 미표시되는 경우, 해당 문서 객체가 존재하지 않을 수 있으므로 null이 아닌 경우에만 이벤트 리스너 추가
		toggle_comments.addEventListener('click', function() {
			for(let i=0;i<comment_writes.length;i++) comment_writes[i].classList.toggle('hide'); // 댓글 작성 영역 토글
			for(let i=0;i<comment_lists.length;i++) comment_lists[i].classList.toggle('hide'); // 댓글 목록 영역 토글
			if(!comment_lists[0].classList.contains('hide')) getListComment(1); // 댓글 목록 새로고침
		}, false); // end of addEventListener
	}
	
	// 댓글 길이 제한
	validateBytesLengthByName({content:900});
	
	// 댓글 작성
	let content = document.querySelector('.comment-write textarea[name=content]');
	document.getElementById('write_comment').addEventListener('click', function() {
		if(!content.value.trim()) return;
		writeComment(content, undefined); // null을 인자로 전달시 모델 클래스에서는 빈 문자열로 간주됨
	}, false); // end of addEventListener
	
	// 대댓글 작성 UI 토글
	let lastTarget;
	document.addEventListener('click', function(event) { // 동적 이벤트 바인딩
		let toggle_reply = event.target.closest('.comment-toggle-reply'); // 이벤트가 발생한 태그의 가장 가까운 조상 .comment-toggle-reply를 찾고
		if(toggle_reply && toggle_reply.contains(event.target)) { // 이벤트가 발생한 태그가 앞서 찾은 가장 가까운 조상 태그 자기 자신이거나 그 자식 태그이면
			// 로그인하지 않은 경우 UI 토글 이벤트 중단
			if(${empty user}) return;
			
			// 대댓글 작성 UI를 삽입할 <ul> 태그 얻기
			let reply = document.querySelector('#reply_area');
			if(reply!=null) clearReplyArea(reply); // 해당 아이디의 요소가 이미 있으면 내부 초기화
			else { // 없으면 <ul> 요소를 생성하고 아이디 부여
				reply = document.createElement('ul');
				reply.id = 'reply_area';	
			}
			
			// 부모 댓글 번호를 <ul> 태그에 저장
			reply.dataset.parent = toggle_reply.dataset.parent;
			
			// 댓글 작성 UI를 복제하고 클래스 및 아이디만 대댓글로 변경
			reply.appendChild(comment_writes[0].cloneNode(true));
			reply.querySelector('.comment-write').classList.replace('comment-write', 'reply-write');
			reply.querySelector('#write_comment').id = 'write_reply';
			
			// 대댓글 목록 바로 전(=토글 버튼 바로 다음)에 <ul> 요소 삽입
			toggle_reply.parentNode.parentNode.insertBefore(reply, toggle_reply.parentNode.parentNode.querySelector('ul.reply-list'));
			
			// UI 토글 처리
			if(lastTarget!=null && lastTarget==toggle_reply) {
				reply.classList.toggle('hide');
				toggle_reply.querySelector('i.bi').classList.toggle('bi-reply-fill');
				toggle_reply.querySelector('i.bi').classList.toggle('bi-reply');
			}
			else {
				reply.classList.remove('hide');
				if(lastTarget!=null) {
					lastTarget.querySelector('i.bi').classList.remove('bi-reply-fill');
					lastTarget.querySelector('i.bi').classList.add('bi-reply');
				}
				toggle_reply.querySelector('i.bi').classList.add('bi-reply-fill');
				toggle_reply.querySelector('i.bi').classList.remove('bi-reply');
			}
			lastTarget = toggle_reply;
		}
	}, false); // end of addEventListener
	
	// 대댓글 작성 UI 초기화
	function clearReplyArea(reply) {
		while(reply.firstChild) { // 내부를 초기화
			reply.removeChild(reply.lastChild);
		}
	}
	
	// 대댓글 작성
	document.addEventListener('click', function(event) { // 동적 이벤트 바인딩
		if(event.target && event.target.id=='write_reply') {
			let content = document.querySelector('.reply-write textarea[name=content]');
			let reply = event.target.parentNode.parentNode;
			writeComment(content, reply.dataset.parent);
			reply.classList.add('hide'); // 대댓글 작성 UI 숨기기
			clearReplyArea(reply); // 대댓글 작성 UI 내부 초기화
		}
	}, false); // end of addEventListener
	
	// 댓글 수정 UI 모달 토글
	let modify_area = document.getElementById('modify_area');
	document.addEventListener('click', function(event) { // 동적 이벤트 바인딩
		// 수정 UI 모달 닫기
		if(event.target===modify_area) { // 모달 배경 영역을 클릭하면
			clearModal(modify_area);
		}
		
		let comment_modify = event.target.closest('.comment-modify'); // 이벤트가 발생한 태그의 가장 가까운 조상 .comment-modify를 찾고
		if(comment_modify && comment_modify.contains(event.target)) { // 이벤트가 발생한 태그가 앞서 찾은 가장 가까운 조상 태그 자기 자신이거나 그 자식 태그이면
			// 로그인하지 않은 경우 수정 UI 모달 여는 이벤트 중단
			if(${empty user}) return;
			
			// 수정 UI 모달 열기
			let modify = comment_modify.parentNode.parentNode.parentNode;
			modify_area.querySelector('textarea').value = modify.querySelector('.comment-content').innerHTML.replace(/<br>/gi, '\n'); // 수정할 댓글 내용 가져오기; <textarea>에 줄바꿈되어 나타나게 처리
			modify_area.dataset.comment = modify.dataset.comment; // 수정할 댓글의 data-comment를 수정 UI에 저장 
			modify_area.style.height = document.documentElement.scrollHeight + 'px'; // 모달 배경 영역의 높이를 현재 문서 전체 높이로 변경
			modify_area.querySelector('.modal-content').style.top = (window.pageYOffset || document.documentElement.scrollTop) + window.innerHeight*2/5 + 'px'; // 모달 내용 영역의 위치를 현재 스크롤 위치의 40% 높이로 변경
			modify_area.classList.toggle('show');
			modify_area.querySelector('textarea').focus();
		}
	}, false); // end of addEventListener
	
	// 수정 UI 모달 닫는 함수 정의
	function clearModal(modal) {
		modal.classList.remove('show');
		modal.querySelector('textarea').value = '';
		modal.dataset.comment = '';
	}
	
	// 댓글 수정
	let modify_comment = document.getElementById('modify_comment');
	modify_comment.addEventListener('click', function(event) {
		let acomment = modify_area.dataset.comment;
		let content = modify_area.querySelector('textarea');

		if(!content.value.trim()) return;
		
		$.ajax({
			url:'modifyComment.do',
			type:'post',
			data:{
				acomment:acomment,
				content:content.value
			},
			dataType:'json',
			timeout:10000,
			success:function(param) {
				if(param.result=='logout') {
					alert('로그인 후 댓글을 삭제할 수 있습니다!');	
				}
				else if(param.result=='success') {
					let comment = document.querySelector('li[data-comment="' + acomment +'"]'); 
					comment.querySelector('.comment-time').textContent = getTimeSince(new Date(param.modify_date)) + ' · 수정됨'; // 댓글 시간 변경
					comment.querySelector('.comment-time').title = param.modify_date;
					comment.querySelector('.comment-content').innerHTML = content.value.replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\n/g, '<br>'); // 수정 후 댓글 내용 변경; 태그 비허용하고 줄바꿈 인정 처리
					clearModal(modify_area); // 수정 UI 모달 닫기
				}
				else if(param.result='wrongAccess') {
					alert('잘못된 접근입니다!');
				}
				else {
					alert('댓글을 수정하는 데 실패했습니다!');
				}
			},
			error:function() {
				alert('네트워크 오류가 발생했습니다!');
			}
		}); // end of ajax
	}, false); // end of addEventListener
	
	// 댓글 삭제
	document.addEventListener('click', function(event) { // 동적 이벤트 바인딩
		let comment_delete = event.target.closest('.comment-delete'); // 이벤트가 발생한 태그의 가장 가까운 조상 .comment-delete를 찾고
		if(comment_delete && comment_delete.contains(event.target)) { // 이벤트가 발생한 태그가 앞서 찾은 가장 가까운 조상 태그 자기 자신이거나 그 자식 태그이면
			// 로그인하지 않은 경우 UI 토글 이벤트 중단
			if(${empty user}) return;
		
			// 한 번 더 확인
			if(!confirm('정말로 삭제하시겠습니까?')) return;
			$.ajax({
				url:'deleteComment.do',
				type:'post',
				data:{acomment:comment_delete.parentNode.parentNode.parentNode.dataset.comment},
				dataType:'json',
				timeout:10000,
				success:function(param) {
					if(param.result=='logout') {
						alert('로그인 후 댓글을 삭제할 수 있습니다!');	
					}
					else if(param.result=='success') {
						current_replies.textContent = Number(current_replies.textContent) - 1; // 댓글 수 감소
						if(current_replies.textContent==0) { // 댓글이 삭제되어 0개가 된 경우 노출되어 있는 댓글 목록을 숨김
							for(let i=0;i<comment_lists.length;i++) comment_lists[i].classList.add('hide');
						}
						else getListComment(1); // 댓글 목록 새로고침
					}
					else if(param.result='wrongAccess') {
						alert('잘못된 접근입니다!');
					}
					else {
						alert('댓글을 삭제하는 데 실패했습니다!');
					}
				},
				error:function() {
					alert('네트워크 오류가 발생했습니다!');	
				}
			}); // end of ajax
		}
	}, false);
	
	// 댓글/대댓글 작성하는 함수 정의
	function writeComment(content, acomment_parent) {
		$.ajax({
			url:'writeComment.do',
			type:'post',
			data:{
				product:${param.product},
				content:content.value,
				acomment_parent:acomment_parent
				},
			dataType:'json',
			timeout:10000,
			success:function(param) {
				if(param.result=='logout') {
					alert('로그인 후 댓글을 작성할 수 있습니다!');
				}
				else if(param.result=='success') {
					content.value = ''; // 입력 칸 초기화
					if(acomment_parent==null) getListComment(1) // 댓글 목록 새로고침
					else getListReply(acomment_parent); // 대댓글 목록 새로고침
					if(current_replies.textContent==0) { // 첫 댓글인 경우 숨겨져 있는 댓글 목록을 노출시킴
						for(let i=0;i<comment_lists.length;i++) comment_lists[i].classList.remove('hide');
					}
					current_replies.textContent = Number(current_replies.textContent) + 1; // 댓글 수 증가
				}
				else {
					alert('댓글 작성에 실패했습니다!');
				}
			},
			error:function() {
				alert('네트워크 오류가 발생했습니다!');
			}
		}); // end of ajax
	} // end of writeComment
	
	// 페이지 처리 변수 선언
	let currentPage;
	let count;
	let rowCount;
	
	// 댓글 더보기
	document.querySelector('.comment-more').addEventListener('click', function() {
		getListComment(currentPage+1);
	}, false);
	
	// 댓글 목록 불러오는 함수 정의
	function getListComment(pageNum) {
		currentPage = pageNum;
		
		$.ajax({
			url:'listComment.do',
			type:'post',
			data:{
				pageNum:pageNum,
				product:${param.product}
			},
			dataType:'json',
			timeout:10000,
			success:function(param) {
				if(param.result=='success') {
					if(param.comments.length==0) { // 댓글이 없으면 <ul> 태그를 숨김
						for(let i=0;i<comment_lists.length;i++) comment_lists[i].classList.add('hide');
						return;
					}
					
					count = param.count;
					rowCount = param.rowCount;
	
					if(pageNum==1) { // 처음 호출시 <ul> 태그 안의 내용 초기화
						$('.comment-list ul').empty();
					}

					// 댓글 목록 불러오기
					$(param.comments).each(function(index, item) {
						// 프로필 사진 처리
						let comment_profile = item.memberVO.photo==null ? '/images/face.png' : '/upload/' + item.memberVO.photo;
						
						// 판매자 확인
						let seller_tag = ${productVO.member}==item.member ? '<span class="seller-tag">판매자</span>' : '';
						
						// 수정 여부 확인
						let modified = item.modify_date==null ? '' : ' · 수정됨'; 
						let modify_date = item.modify_date==null ? item.reg_date : item.modify_date; 

						// 댓글이 담긴 태그 만들기
						let comment = '<li class="flex-row align-start" data-comment="' + item.acomment + '">';
						// 프로필에 매너 평가 확인 페이지로 이동하는 링크 추가
						comment += '	<img class="profile" src="' + cp + comment_profile + '">';
						comment += '	<div class="comment-text flex-column">';
						comment += '		<div class="comment-subtitle">' + item.memberVO.nickname + seller_tag + '</div>';
						comment += '		<div class="comment-info"><span title="' + item.memberVO.address + '">' + getLastToken(item.memberVO.address, ' ') + '</span> · <span class="comment-time" title="' + modify_date + '">' + getTimeSince(modify_date) + modified + '</span></div>';
						if(item.deleted==1) comment += '<div class="deleted"><i class="bi bi-exclamation-triangle"></i>댓글 작성자가 삭제한 댓글입니다</div>';
						else {
							comment += '		<div class="comment-content">' + item.content + '</div>';
							comment += '		<div class="comment-menu flex-row">';
							comment += '			<a class="comment-toggle-reply" data-parent="' + item.acomment + '" <c:if test="${empty user}">title="로그인 후 댓글을 작성할 수 있습니다"</c:if>><i class="bi bi-reply"></i>답글 쓰기</a>';
							if('${user}'==item.member) {
								comment += '		<a class="comment-modify"><i class="bi bi-pencil-square"></i>수정하기</a>';
								comment += '		<a class="comment-delete"><i class="bi bi-trash3-fill"></i>삭제하기</a>';
							}
							comment += '		</div>';
						}
						comment += '		<ul class="reply-list flex-column hide">';
						comment += '		</ul>';
						comment += '	</div>';
						comment += '</li>';
						$('.comment-list > ul').prepend(comment); // <ul> 태그 안에 최신 댓글이 아래로 오도록 대댓글 추가
						
						// 현재 댓글의 대댓글 목록 새로고침
						getListReply(item.acomment);

					}); // end of each
					
					if(pageNum==1) $(window).scrollTop($('.comment-write textarea').offset().top);

					// 더보기 버튼
					if(currentPage<Math.ceil(count/rowCount)) { // 다음 페이지가 있으면
						$('.comment-more').removeClass('hide');
					}
					else {
						$('.comment-more').addClass('hide');
					}
				}
				else {
					alert('댓글을 불러오는 데 실패했습니다!');
				}				
			}, // end of success
			error:function() {
				alert('네트워크 오류가 발생했습니다!');
			}
		}); // end of ajax
	} // end of getListComment
	
	// 대댓글 목록 불러오는 함수 정의
	function getListReply(acomment_parent) {		
		$.ajax({
			url:'listReply.do',
			type:'post',
			data:{
				acomment_parent:acomment_parent,
				product:${param.product}
			},
			dataType:'json',
			timeout:10000,
			success:function(param) {
				if(param.result=='success') {
					let parent = 'li[data-comment=' + acomment_parent + '] ul.reply-list';
					// 대댓글이 없으면 <ul> 태그를 숨김
					if(param.replies.length==0) {
						$(parent).addClass('hide');
						return;
					}
					
					$(parent).empty(); // 호출시 <ul> 태그 내부를 초기화
					
					// 댓글 목록 불러오기
					$(param.replies).each(function(index, item) {
						// 프로필 사진 처리
						let comment_profile = item.memberVO.photo==null ? '/images/face.png' : '/upload/' + item.memberVO.photo;
						
						// 판매자 확인
						let seller_tag = ${productVO.member}==item.member ? '<span class="seller-tag">판매자</span>' : '';
						
						// 수정 여부 확인
						let modified = item.modify_date==null ? '' : ' · 수정됨'; 
						let modify_date = item.modify_date==null ? item.reg_date : item.modify_date;
						
						// 대댓글이 담긴 태그 만들기
						let reply = '<li class="flex-row align-start" data-comment="' + item.acomment + '">';
						// 프로필에 매너 평가 확인 페이지로 이동하는 링크 추가
						reply += '	<img class="profile" src="' + cp + comment_profile + '">';
						reply += '	<div class="comment-text flex-column">';
						reply += '		<div class="comment-subtitle">' + item.memberVO.nickname + seller_tag + '</div>';
						reply += '		<div class="comment-info"><span title="' + item.memberVO.address + '">' + getLastToken(item.memberVO.address, ' ') + '</span> · <span class="comment-time" title="' + modify_date + '">' + getTimeSince(modify_date) + modified + '</span></div>';
						reply += '		<div class="comment-content">' + item.content + '</div>';
						reply += '			<div class="comment-menu flex-row">';
						reply += '			<a class="comment-toggle-reply" data-parent="' + acomment_parent + '" <c:if test="${empty user}">title="로그인 후 댓글을 작성할 수 있습니다"</c:if>><i class="bi bi-reply"></i>답글 쓰기</a>'; // 부모 댓글 번호를 유지
						if('${user}'==item.member) {
							reply += '		<a class="comment-modify"><i class="bi bi-pencil-square"></i>수정하기</a>';
							reply += '		<a class="comment-delete"><i class="bi bi-trash3-fill"></i>삭제하기</a>';
						}
						reply += '		</div>';
						reply += '	</div>';
						reply += '</li>';
						$(parent).prepend(reply); // <ul> 태그 안에 최신 대댓글이 아래로 오도록 대댓글 추가
					}); // end of each
					
					$(parent).removeClass('hide'); // <ul> 태그를 노출시킴
				}
				else {
					alert('댓글을 불러오는 데 실패했습니다!');
				} // end of success			
			},
			error:function() {
				alert('네트워크 오류가 발생했습니다!');
			}
		}); // end of ajax
	} // end of getListComment
*/
	// 관심 물품 토글
	let like_btn = document.getElementById('like');
	let current_likes = document.getElementById('current_likes');
	if(${empty user || productVO.deleted==1}) like_btn.classList.add('disabled');
	like_btn.addEventListener('click', function() {
		if(like_btn.classList.contains('disabled')) return; // 버튼 비활성화된 경우 함수 실행 종료
		
		$.ajax({
			url:'toggleMyProduct.do',
			type:'post',
			data:{product:${productVO.product}},
			dataType:'json',
			timeout:10000,
			success:function(param) {
				if(param.result=='logout') {
					alert('로그인 후 관심 물품에 담을 수 있습니다!');
				}
				else if(param.result=='insert') { // 아이콘을 채워진 하트로 교체하고 관심 수 증가
					like_btn.classList.remove('bi-heart');
					like_btn.classList.add('bi-heart-fill');
					current_likes.textContent = Number(current_likes.textContent) + 1;
				}
				else if(param.result=='delete') { // 아이콘을 빈 하트로 교체하고 관심 수 차감
					like_btn.classList.remove('bi-heart-fill');
					like_btn.classList.add('bi-heart');
					current_likes.textContent = Number(current_likes.textContent) -1;
				}
				else {
					alert('관심 물품 추가/삭제에 실패했습니다!')
				}
			},
			error:function() {
				alert('네트워크 오류가 발생했습니다!');
			}
		}); // end of ajax
	}, false); // end of addEventListener
	/*
	// 채팅방 연결
	let link_chatroom = document.getElementById('link_chatroom');
	if(link_chatroom!=null) {
		link_chatroom.addEventListener('click', function() {
			$.ajax({
				url:cp + '/chat/linkChatRoom.do',
				type:'post',
				data:{
					product:${productVO.product},
					seller:${productVO.member}
				},
				dataType:'json',
				timeout:10000,
				success:function(param) {
					if(param.result=='logout') {
						alert('로그인 후 채팅할 수 있습니다!');
					}
					else if(param.result=='success') {
						location.href = cp + '/chat/chat.do?chatroom=' + param.chatroom;
					}
					else {
						alert('채팅방을 불러오는 데 실패했습니다!');
					}
				},
				error:function() {
					alert('네트워크 오류가 발생했습니다!');
				}
			})
		}, false); // end of addEventListener
	} // end of if
	*/
</script>
</body>
</html>