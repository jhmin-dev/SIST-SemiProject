<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 : ${productVO.title}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<div class="page-main">
	<div class="chat flex-row justify-center">
<!-- 물품별 채팅방 목록 시작 -->
		<ul class="chat-other flex-column justify-start">
			<li class="flex-row justify-center">
				<div class="chat-title">메시지함</div>
			</li>
			<li class="flex-row justify-center">
				<form class="search-area" method="get">
					<select name="filter">
						<option value="1" <c:if test="${param.filter==1}">selected</c:if>>전체</option>
						<option value="2" <c:if test="${param.filter==2}">selected</c:if>>거래 중</option>
						<option value="3" <c:if test="${param.filter==3}">selected</c:if>>거래 완료</option>
					</select>
				</form>
			</li>
			<c:if test="${empty chatrooms && empty param.chatroom}">
<!-- 채팅방 목록이 없는 경우 시작 -->
			<li class="list-area flex-column">
				<div class="chat-notice">
					채팅 중인 물품이 없습니다.
				</div>
			</li>
<!-- 채팅방 목록이 없는 경우 끝 -->
			</c:if>
			<c:if test="${!empty chatrooms || !empty param.chatroom}">
<!-- 채팅방 목록이 있는 경우 시작 -->
			<li class="list-area">
				<ul class="flex-column">

				</ul>
			</li>
<!-- 채팅방 목록이 있는 경우 끝 -->
			</c:if>
		</ul>
		<hr class="vertical">
<!-- 물품별 채팅방 목록 끝 -->
<!-- 현재 채팅 시작 -->
		<ul class="chat-main flex-column">
<!-- 채팅방 목록이 없는 경우 시작 -->
		<c:if test="${empty chatrooms && empty param.chatroom}">
			<li class="chat-header who-area">
				<div class="chat-title no-reply">환영합니다</div>
			</li>
			<li><hr><li>
			<li class="read-area">
				<ul class="flex-column">
					<li class="flex-column">
						<div class="chat-you">
							<i class="chat-profile-icon bi bi-person-hearts"></i>
							<div class="flex-row align-end">
								<div class="chat-content">${nickname}님, 반갑습니다!</div>
							</div>
						</div>
						<div class="chat-you">
							<div class="flex-row align-end">
								<div class="chat-content">번개 맞은 당근 나라에서 동네 주민들과의 가깝고 따뜻한 거래를 지금 경험해보세요.</div>
							</div>
						</div>
						<div class="chat-you">
							<div class="flex-row align-end">
								<div class="chat-content">궁금한 게 있을 땐 FAQ를 읽어보세요!</div>
							</div>
						</div>
					</li>
				</ul>
			</li>
			<li><hr></li>
			<li class="buttons no-reply flex-row justify-center">
				<input type="button" class="reverse-point" value="우리 동네 물품 보러가기" onclick="location.href = '${pageContext.request.contextPath}/main/main.do';">
				<input type="button" class="point" value="FAQ 바로가기" onclick="">
			</li>
<!-- 채팅방 목록이 없는 경우 끝 -->
		</c:if>
		<c:if test="${!empty chatrooms || !empty param.chatroom}">
<!-- 채팅방 목록이 있는 경우 시작 -->
<!-- 현재 채팅 헤더 시작 -->
			<li class="chat-header who-area flex-row space-between align-start">
				<div class="chat-title">${opponentVO.nickname}</div>
				<c:if test="${chatroomVO.seller!=0}">
				<div class="manner flex-column align-end">
					<div class="manner-stars">
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
						<i class="bi bi-star"></i>
					</div>
					<div class="manner-info gray underline"></div>
				</div>
				</c:if>
			</li>
			<li><hr></li>
			<c:if test="${chatroomVO.seller!=0}">
			<li class="chat-header product-area flex-row space-between">
				<div class="flex-row align-start">
					<img src="${pageContext.request.contextPath}/upload/${productVO.photo1}">
					<div class="flex-column">
						<div class="chat-title">${productVO.title}</div>
						<div class="chat-subtitle">
							<c:if test="${productVO.price>0}">
							<fmt:formatNumber value="${productVO.price}"/>원
							</c:if>
							<c:if test="${productVO.price==0}">
							나눔
							</c:if>
						</div>
					</div>
				</div>
				<c:choose>
				<c:when test="${productVO.deleted==1}">
				<button type="button" class="point square" disabled>삭제된 물품</button>
				</c:when>
				<c:when test="${productVO.complete==1}">
					<c:if test="${productVO.buyer==user}"> <%-- chatroom의 buyer은 구매(희망)자고 product의 buyer이 실제 구매자 --%>
					<button type="button" class="point square" onclick="" >거래 후기 남기기</button>
					</c:if>
					<c:if test="${productVO.buyer!=user}">
					<button type="button" class="point square" disabled>거래 완료된 물품</button>
					</c:if>
				</c:when>				
				<c:when test="${chatroomVO.seller==user}">
				<button type="button" class="point square" id="complete">거래 완료하기</button>
				</c:when>
				<c:otherwise>
				<button type="button" class="reverse-point square" onclick="location.href = '${pageContext.request.contextPath}/product/detail.do?product=${chatroomVO.product}'">물품 정보 보러가기</button>
				</c:otherwise>
				</c:choose>
			</li>
			<li><hr></li>
			</c:if>
<!-- 현재 채팅 헤더 끝 -->
<!-- 주고 받은 메시지 불러오기 시작 -->	
			<li class="read-area">
				<ul class="flex-column">
				
				</ul>
			</li>
			<li><hr></li>
<!-- 주고 받은 메시지 불러오기 끝 -->
<!-- 메시지 보내기 시작 -->
			<c:if test="${chatroomVO.seller!=0}">
			<li>
				<form class="send-area flex-row justify-center">
					<input type="text" name="content" id="content">
					<i class="bi bi-send-fill" id="send"></i>
				</form>
			</li>
			</c:if>
<!-- 메시지 보내기 끝 -->
<!-- 채팅방 목록이 있는 경우 끝 -->
		</c:if>
		</ul>
<!-- 현재 채팅 끝 -->
	</div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/StringUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/UIUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validateInput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/chat.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
	const cp = '${pageContext.request.contextPath}';

	// 매너 평점 처리
	const rate = '${opponentVO.rate}';
	const stars = document.querySelectorAll('.manner-stars i.bi');
	const info = document.querySelector('.manner-info');
	if(stars && info) fillMannerRate();

	// 필터 선택시 submit 이벤트 발생
	document.querySelector('select[name=filter]').addEventListener('change', function() {
		document.querySelector('.search-area').submit();
	}, false)
	
	// ajax 통신용 파라미터 변수 선언
	const filter = ${empty param.filter} ? undefined : '${param.filter}';
	const chatroom = '${chatroomVO.chatroom}';
	const product = '${chatroomVO.product}';
	const user = '${user}';
	const opponent = ${user}=='${chatroomVO.seller}' ? '${chatroomVO.buyer}' : '${chatroomVO.seller}';
	const content = document.getElementById('content');

	// 메시지 보내기 처리
	if(${(!empty chatrooms || !empty param.chatroom) && chatroomVO.seller!=0}) {
		// <input> 태그에서 엔터 입력시 메시지 발송
		document.querySelector('.send-area').addEventListener('submit', function(event) {
			event.preventDefault(); // 기본 이벤트 제거
			sendChat();
		}, false);
		// 아이콘 버튼 클릭시 메시지 발송
		document.getElementById('send').addEventListener('click', function() {
			sendChat();
		}, false);
		
		// 900자 제한
		validateBytesLength({content:900})
	}

	// 채팅 상대방 프로필 사진 가져오기
	const chat_profile = ${!empty opponentVO.profile} ? '<img class="chat-profile" src="' + cp + '/upload/' + profile + '">' : '<i class="chat-profile-icon bi bi-person-circle"></i>';
	
	// 채팅 내역 새로고침
	if(${!empty chatrooms || !empty param.chatroom}) {
		// 초기 새로고침
		getListChat(1);
		
		// 1초에 한 번 새로고침
		setInterval(function() {
			if(document.visibilityState=='visible') { // 현재 창/탭이 활성화되어 있으면
				refreshListChat();
			}
		}, 1000); // end of setInterval
	}
	
	// 채팅방 목록 새로고침
	if(${!empty chatrooms || !empty param.chatroom}) {
		// 초기 새로고침
		getListChatRoom();
		
		// 1초에 한 번 새로고침
		setInterval(function() {
			if(document.visibilityState=='visible') { // 현재 창/탭이 활성화되어 있으면
				refreshListChatRoom();
			}
		}, 1000); // end of setInterval
	}

	// 거래 완료하기
	let complete_btn = document.getElementById('complete');
	if(complete_btn!=null) {
		complete_btn.addEventListener('click', function() {
			if(${user!=chatroomVO.seller}) return;
			
			// 확인 모달 UI 열기
			
			$.ajax({
				url: cp + '/product/complete.do',
				type:'post',
				data:{
					product:product,
					buyer:opponent
				},
				dataType:'json',
				timeout:10000,
				success:function(param) {
					if(param.result=='logout') {
						alert('로그인 후 거래를 완료할 수 있습니다!');	
					}
					else if(param.result=='success') {
						location.reload(); // 현재 페이지를 새로고침하여 버튼 상태 변경
					}
					else {
						alert('거래 완료 처리에 실패했습니다!');	
					}
				},
				error:function() {
					alert('네트워크 오류가 발생했습니다!');
				}
			});
		}, false);
	}
</script>
</body>
</html>