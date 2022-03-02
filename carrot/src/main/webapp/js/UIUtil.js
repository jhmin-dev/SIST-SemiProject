// 매너 평점 처리
function fillMannerRate() {
	if(!rate) { // 매너 평가 점수 평균 값이 없으면(=인자로 빈 문자열이 전달되면)
		for(let i=0;i<stars.length;i++) {
			if(i<2) stars[i].classList.replace('bi-star', 'bi-star-fill'); // 채워진 별 2개
			if(i==2) stars[i].classList.replace('bi-star', 'bi-star-half'); // 반만 채워진 별 1개
			stars[i].classList.add('disabled'); // 색 변경
		}
		
		info.textContent = '표시할 매너 평점이 없어요';
	}
	else { // 매너 평가 점수 평균 값이 있으면
		const floor = Math.floor(rate); // 버림 값
		const round = Math.round(rate); // 반올림 값
		for(let i=0;i<stars.length;i++) {
			if(i<floor) stars[i].classList.replace('bi-star', 'bi-star-fill');
			if(i==floor && floor!=round) stars[i].classList.replace('bi-star', 'bi-star-half');
		}
		
		info.textContent = '매너 평점 ';
		const span = document.createElement('span');
		span.classList.add('bold');
		span.textContent = rate;
		info.appendChild(span);
	}
}

// 물품 목록에서 수정 또는 등록 시간 처리
function getTimeFormatted() {
	for(let i=0;i<times.length;i++) {
		if(!times[i].dataset.modified) {
			times[i].textContent = getTimeSince(times[i].dataset.registered);
			times[i].title = times[i].dataset.registered;
		}
		else {
			times[i].textContent = '수정 ' + getTimeSince(times[i].dataset.modified);
			times[i].title = times[i].dataset.modified;
		}
	}	
}