// 채팅방 목록 새로고침 처리 변수 선언
let latest_chat = 0;

// 채팅방 목록을 새로고침하는 함수 정의
function refreshListChatRoom(){
	$.ajax({
		url:'latestChat.do',
		type:'post',
		dataType:'json',
		timeout:10000,
		success:function(param) {
			if(param.latest_chat>latest_chat) { // 가장 최근에 받은 메시지 번호가 변경되면
				getListChatRoom(); // 채팅방 목록 새로고침
				latest_chat = param.latest_chat;
			}
		}
	}); // end of ajax
} // end of refreshListChatRoom

// 채팅방 목록 불러오는 함수 정의
function getListChatRoom() {
	$.ajax({
		url:'listChatRoom.do',
		type:'post',
		data:{filter:filter},
		dataType:'json',
		timeout:10000,
		success:function(param) {
			if(param.result=='logout') {
				alert('로그인 후 채팅방을 확인할 수 있습니다!');	
			}
			else if(param.result=='success') {
				$('.list-area ul').empty(); // 호출시 <ul> 태그 안의 내용 초기화
				
				// 필터가 선택되어 있는지 확인
				const filtered = filter!=null ? '&filter=' + filter : '';
				
				$(param.chatrooms).each(function(index, item) {
					// 채팅방의 상대방 정보 확인
					const opponentVO = user==item.seller ? item.buyerVO : item.sellerVO;
					const chatroom_profile = opponentVO.profile!=null ? '<img class="list-profile" src="' + cp + '/upload/' + opponentVO.profile + '">' : '<i class="chat-profile-icon no-reply bi bi-person-circle"></i>';
					
					// 현재 열려 있는 채팅방인지 확인
					const selected = chatroom==item.chatroom ? ' selected' : '';
					
					// 안 읽은 메시지가 있는지 확인
					const unread = item.unread>0 ? ' unread' : '';

					// 목록의 채팅방 배경색을 줄무늬 처리
					const stripe = index%2==0 ? '' : ' list-stripe';
					
					// 채팅방이 담긴 태그 만들기
					let chatroomUI =  '<li>';
					chatroomUI +=  '	<a class="flex-row space-between' + stripe + '" href="chat.do?chatroom=' + item.chatroom + filtered + '">';
					chatroomUI +=  '		<div class="flex-row">';
					chatroomUI +=  '			' + chatroom_profile;
					chatroomUI +=  '			<div class="flex-column">';
					chatroomUI +=  '				<div class="list-who flex-row align-end">';
					chatroomUI +=  '					<div class="chat-subtitle ellipsis">' + opponentVO.nickname + '</div>';
					chatroomUI +=  '					<div class="chat-info" title="' + opponentVO.home + '">' + opponentVO.bname + ' · ' + '<span title="' + item.chatVO.sent + '">' + getTimeSince(item.chatVO.sent) + '</span></div>';
					chatroomUI +=  '				</div>'; // end of list-who
					chatroomUI +=  '				<div class="latest-chat ellipsis">' + item.chatVO.content + '</div>';
					chatroomUI +=  '			</div>'; // end of flex-column
					chatroomUI +=  '		</div>'; // end of flex-row
					chatroomUI +=  '		<div class="flex-row">';
					chatroomUI +=  '			<img class="list-product" src="' + cp + '/upload/' + item.productVO.photo1 + '">';
					chatroomUI +=  '			<div class="chat-selection' + (selected || unread) + '"></div>';
					chatroomUI +=  '		</div>'; // end of flex-row
					chatroomUI +=  '	</a>'; // end of flex-row space-between
					chatroomUI +=  '</li>';
					$('.list-area ul').append(chatroomUI); // <ul> 태그 안에 채팅방 추가
				}); // end of each;
			}
			else {
				alert('채팅방을 불러오는 데 실패했습니다!');
			}
		},
		error:function() {
			alert('네트워크 오류가 발생했습니다!');
		}
	});
}

// 안 읽은 메시지가 있으면 채팅 내역을 새로고침하는 함수 정의
function refreshListChat() {
	$.ajax({
		url:'countChat.do',
		type:'post',
		data:{chatroom:chatroom},
		dataType:'json',
		timeout:10000,
		success:function(param) {
			if(param.unread>0) { // 안 읽은 메시지가 있으면
				getListChat(1); // 채팅 내역 불러오는 함수 실행
			}
		}
	}); // end of ajax
} // end of refreshListChat

// 채팅 내역 페이지 처리 변수 선언
let currentPage;
let currentHeight;
let count;
let rowCount;

// 스크롤 끝에 도달하면 추가로 채팅 내역 불러오기
document.querySelector('.read-area').addEventListener('scroll', function(event) {
	if(currentPage<Math.ceil(count/rowCount) && event.target.scrollTop==0) { // 다음 페이지가 있고 스크롤 끝에 도달하면
		getListChat(currentPage+1);
	}
}, false);

// 채팅 내역 불러오는 함수 정의
function getListChat(pageNum) {
	currentPage = pageNum;
	
	$.ajax({
		url:'listChat.do',
		type:'post',
		data:{
			pageNum:pageNum,
			chatroom:chatroom
		},
		dataType:'json',
		timeout:10000,
		success:function(param) {
			if(param.result=='logout') {
				alert('로그인 후 채팅 내역을 확인할 수 있습니다!');	
			}
			else if(param.result=='success') {
				currentHeight = $('.read-area').prop('scrollHeight');
				count = param.count;
				rowCount = param.rowCount;

				if(pageNum==1) { // 처음 호출시 <ul> 태그 안의 내용 초기화
					$('.read-area ul').empty();
				}

				// 채팅 내역 불러오기
				$(param.chats).each(function(index, item) {
					let lastIndex = $(param.chats).length-1;
					
					// 메시지가 담긴 태그 만들기
					let chatUI = '<li class="flex-column">';
					if(item.member==user) { // 현재 메시지를 보낸 회원이 로그인한 사용자인 경우
						chatUI += '	<div class="chat-me">';
					}
					else { // 현재 메시지를 보낸 회원이 상대방인 경우
						chatUI += '	<div class="chat-you">';
						if(index==lastIndex || param.chats[index+1].member==user) { // 상대방이 연속해서 메시지를 보낸 경우 프로필은 한 번만 표시
							chatUI += '		' + chat_profile;
						}
					}
					chatUI += '		<div class="flex-row align-end">'
					chatUI += '			<div class="chat-content">' + item.content + '</div>';
					chatUI += '			<div class="chat-info" title="' + item.sent + '">' + getTimeSince(item.sent) + '</div>';
					chatUI += '		</div>';
					chatUI += '	</div>';
					chatUI += '</li>';
					$('.read-area ul').prepend(chatUI); // <ul> 태그 안에 최신 메시지가 아래로 오도록 메시지 추가
				}); // end of each
				
				// 초기 새로고침 때는 스크롤을 가장 아래로 이동, 그 이후에는 채팅 내역을 추가로 불러오기 전의 스크롤 위치 유지
				if(pageNum==1) $('.read-area').scrollTop($('.read-area').prop('scrollHeight'));
				else $('.read-area').scrollTop($('.read-area').prop('scrollHeight') - currentHeight);
				
				// 채팅방 목록 새로고침
				getListChatRoom();
			}
			else {
				alert('채팅 내역을 불러오는 데 실패했습니다!');
			}
		},
		error:function() {
			alert('네트워크 오류가 발생했습니다!');
		}
	}); // end of ajax
} // end of getListChat

// 메시지 보내는 함수 정의
function sendChat() {
	if(!content.value.trim()) return; // 아무것도 입력하지 않은 경우 전송하지 않음
	
	$.ajax({
		url:'sendChat.do',
		type:'post',
		data:{
			chatroom:chatroom,
			opponent:opponent,
			content:content.value
		},
		dataType:'json',
		timeout:10000,
		success:function(param) {
			if(param.result=='logout') {
				alert('로그인 후 메시지를 보낼 수 있습니다!')
			}
			else if(param.result=='success') {
				content.value = ''; // 입력 칸 초기화
				getListChat(1); // 채팅 내역 새로고침
			}
			else {
				alert('메시지 전송에 실패하였습니다!')
			}
		},
		error:function() {
			alert('네트워크 오류가 발생했습니다!')
		}
	}); // end of ajax		
} // end of sendChat