<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>물품 등록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jhmin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<div class="page-main">
	<ul class="add flex-column">
		<li class="title flex-row justify-center">
			물품 등록
		</li>
		<li>
			<form action="add.do" method="post" enctype="multipart/form-data">
				<ul class="flex-column">
					<li>
						<div>
							<label for="photo" class="upload flex-column justify-center">
								<i class="bi bi-camera-fill"></i>
								<div><span class="upload-count">0</span>/5</div>
							</label>
							<input type="file" name="photo" id="photo" accept="image/*" multiple>
							<ul class="list-thumbnail">
								<li class="flex-row">
									<img class="thumbnail hidden">
									<img class="thumbnail hidden">
									<img class="thumbnail hidden">
									<img class="thumbnail hidden">
									<img class="thumbnail hidden">
								</li>
							</ul>
						</div>
					</li>
					<li>
						<div>
							<label for="title">제목</label>
							<input type="text" name="title" id="title">
						</div>
					</li>
					<li>
						<div>
							<label for="price">가격</label>
							<input type="text" name="price" id="price">
						</div>
					</li>
					<li>
						<div>
							<label for="category">카테고리</label>
							<select name="category">
								<option disabled selected>카테고리 선택</option>
								<c:forEach var="item" items="${listCategory}">
								<option value="${item.category}">${item.name}</option>
								</c:forEach>
							</select>
						</div>
					</li>
					<li>
						<div>
							<label for="content">설명</label>
							<textarea name="content" id="content"></textarea>
						</div>
					</li>
				</ul>
				<input type="submit" hidden> <%-- <input> 태그에서 엔터 입력시 submit 이벤트 발생 --%>
			</form>
			<div class="caution hide">
				<i class="bi bi-exclamation-triangle"></i>
				<span>
					
				</span>
			</div>
			<div class="modal">
				<div class="modal-content flex-column">
					<i class="bi bi-exclamation-triangle-fill"></i>
					<span>
					</span>
					<input type="button" class="point" value="확인">
				</div>
			</div>
		</li>
		<li class="flex-row justify-center">
			<input type="button" class="big point" value="등록하기" id="add">
			<input type="button" class="big" value="홈으로" onclick="location.href = '${pageContext.request.contextPath}/main/main.do';">
		</li>
	</ul>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validateInput.js"></script>
<script type="text/javascript">
	//유효성 검증 및 등록 처리를 위한 변수 선언
	let add = document.querySelector('form');
	let add_btn = document.getElementById('add');

	// 태그에 클래스 부여
	let labels = document.querySelectorAll('label:not(.upload)');
	for(let i=0;i<labels.length;i++) {
		labels[i].classList.add('subtitle');
	}
	let firstdivs = document.querySelectorAll('form > ul > li > div:first-child');
	for(let i=0;i<firstdivs.length;i++) {
		firstdivs[i].classList.add('flex-row', 'justify-start');
	}
	
	// <input> 태그에서 엔터 입력시 등록 처리
	add.addEventListener('submit', function(event) {
		addProduct(event);
	}, false);
	// 가입하기 버튼 클릭시 등록 처리
	add_btn.addEventListener('click', function() {
		addProduct();
	})
	
	// 등록 처리하는 함수 정의
	function addProduct(event) {
		let isValid = true; // 유효성 검증 결과를 보관할 변수 선언
		
		
		
		if(isValid) add.submit(); // 유효성 검증을 통과하면 submit 이벤트 발생
		else if(event) event.preventDefault(); // 유효성 검증을 통과하지 못하고 함수가 event 객체를 전달받은 경우 기본 이벤트 제거
		
		return isValid;
	} // end of addProduct
	
	// 파일 업로드하기
	let upload_count = document.querySelector('.upload-count');
	let photo = document.getElementById('photo'); // <input type="file">
	let dt = new DataTransfer(); // 누적 파일 목록
	let thumbnails = document.querySelectorAll('img.thumbnail'); // 썸네일 목록
	photo.addEventListener('change', function(event) {
		let selected = event.target.files; // 현재 선택한 파일 목록
		if(selected.length==0) return; // 아무것도 선택하지 않으면 함수 실행 중단
		if(dt.files.length + selected.length>5) { // 누적 파일 목록과 현재 선택한 파일 목록의 파일 수 합이 5를 초과하면 누적 파일 목록을 갱신하지 않음
			alert('사진은 최대 5장까지 등록 가능합니다!');
			return;
		}
		else { // 누적 파일 목록과 현재 선택한 파일 목록의 파일 수 합이 5 이하이면 누적 파일 목록을 갱신
			for(let i=0;i<selected.length;i++) {
				dt.items.add(selected[i]);
			}
		}

		// <input type="file">의 FileList를 누적 파일 목록으로 바꿔치기
		photo.files = dt.files;
		
		// 총 업로드한 파일 수 나타내기
		upload_count.textContent = dt.files.length;
		if(dt.files.length>0) upload_count.classList.add('enabled');
		else upload_count.classList.remove('enabled');
		
		// 현재까지 업로드한 이미지의 썸네일 불러오기
		let reader = new FileReader();
		updateThumbnail(reader, dt, 0);
	}, false);
	
	// 순차적으로 썸네일 불러오는 함수 정의
	function updateThumbnail(reader, dt, i) {
		if(i>=dt.files.length) return;
		
		reader.readAsDataURL(dt.files[i]);
		
		reader.onload = function() {
			thumbnails[i].src = reader.result;
			thumbnails[i].classList.remove('hidden');
			updateThumbnail(reader, dt, i+1);
		}
	}
</script>
</body>
</html>