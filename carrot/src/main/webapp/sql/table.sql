-- MEMBER
-- Oracle에서 user는 예약어라 테이블명이나 컬럼명으로 사용 불가
CREATE TABLE member(
	member NUMBER NOT NULL,
	id VARCHAR2(30) UNIQUE NOT NULL, /* 영문 대문자만 허용 */
	auth NUMBER(1) DEFAULT 2 NOT NULL, /* 0=탈퇴, 1=정지, 2=일반, 3=관리자 */
	CONSTRAINT member_pk PRIMARY KEY (member)
);

-- MEMBER_SEQ
CREATE SEQUENCE member_seq START WITH 10; /* 0~9는 관리자 예약 */

-- MEMBER_DETAIL
CREATE TABLE member_detail(
	member NUMBER NOT NULL,
	nickname VARCHAR2(30) NOT NULL,
	password VARCHAR2(30) NOT NULL, /* 최소 길이 6자, 특수문자 1자 이상, 영문 대소문자 구별 */
	home VARCHAR2(90) NOT NULL, /* 회원이 거주하는 동네의 주소(읍면동까지) */
	sido VARCHAR2(30) NOT NULL, /* 회원이 거주하는 동네의 광역자치단체명 */
	sigungu VARCHAR2(30) NOT NULL, /* 회원이 거주하는 동네의 시/군/구명 */
	bname VARCHAR2(30) NOT NULL, /* 회원이 거주하는 동네의 법정동/리명 */
	main VARCHAR2(90), /* 메인 페이지에서 기본적으로 보여줄 동네 */
	phone VARCHAR2(13), /* 하이픈 포함 13자 */
	email VARCHAR2(50),
	profile VARCHAR2(150),
	rate NUMBER,
	registered DATE DEFAULT CURRENT_DATE NOT NULL,
	CONSTRAINT member_detail_pk PRIMARY KEY (member),
	CONSTRAINT member_detail_fk FOREIGN KEY (member) REFERENCES member (member)
);


-- CATEGORY
CREATE TABLE category(
	category NUMBER NOT NULL, /* 0=기타, 1=삽니다 */
	name VARCHAR2(90) NOT NULL,
	hidden NUMBER(1) DEFAULT 0 NOT NULL, /* 0=숨겨지지 않음, 1=숨겨짐 */
	CONSTRAINT category_pk PRIMARY KEY (category)
);

-- CATEGORY_SEQ
CREATE SEQUENCE category_seq START WITH 10;

-- CATEGORY 초기 데이터 세팅
-- Oracle에서 SEQUENCE는 UNION ON과 함께 사용 불가; UNION ON을 서브쿼리로 처리하고 그 바깥에서 SEQUENCE를 사용해야 함
INSERT INTO category (category, name)
	SELECT NVL(category, category_seq.NEXTVAL) AS category, name
	FROM (SELECT 0 AS category, '기타 중고물품' AS name FROM DUAL UNION ALL
		SELECT NULL, '디지털기기' FROM DUAL UNION ALL
		SELECT NULL, '생활가전' FROM DUAL UNION ALL
		SELECT NULL, '가구/인테리어' FROM DUAL UNION ALL
		SELECT NULL, '유아동' FROM DUAL UNION ALL
		SELECT NULL, '생활/가공식품' FROM DUAL UNION ALL
		SELECT NULL, '유아도서' FROM DUAL UNION ALL
		SELECT NULL, '스포츠/레저' FROM DUAL UNION ALL
		SELECT NULL, '여성잡화' FROM DUAL UNION ALL
		SELECT NULL, '여성의류' FROM DUAL UNION ALL
		SELECT NULL, '남성패션/잡화' FROM DUAL UNION ALL
		SELECT NULL, '게임/취미' FROM DUAL UNION ALL
		SELECT NULL, '뷰티/미용' FROM DUAL UNION ALL
		SELECT NULL, '반려동물용품' FROM DUAL UNION ALL
		SELECT NULL, '도서/티켓/음반' FROM DUAL UNION ALL
		SELECT NULL, '식물' FROM DUAL UNION ALL
		SELECT NULL, '중고차' FROM DUAL UNION ALL
		SELECT 1, '삽니다' FROM DUAL);

-- PRODUCT
CREATE TABLE product(
	product NUMBER NOT NULL,
	member NUMBER NOT NULL,
	photo1 VARCHAR2(150) NOT NULL,
	photo2 VARCHAR2(150),
	photo3 VARCHAR2(150),
	photo4 VARCHAR2(150),
	photo5 VARCHAR2(150),
	title VARCHAR2(150) NOT NULL,
	price NUMBER(9) NOT NULL, /* 0=나눔 */
	content CLOB NOT NULL,
	category NUMBER NOT NULL,
	registered DATE DEFAULT CURRENT_DATE NOT NULL,
	modified DATE,
	complete NUMBER(1) DEFAULT 0 NOT NULL, /* 0=판매 중, 1=거래 완료 */
	buyer NUMBER,
	deleted NUMBER(1) DEFAULT 0 NOT NULL, /* 0=삭제되지 않음, 1=삭제됨 */
	CONSTRAINT product_pk PRIMARY KEY (product),
	CONSTRAINT product_fk FOREIGN KEY (member) REFERENCES member (member),
	CONSTRAINT product_fk2 FOREIGN KEY (category) REFERENCES category (category),
	CONSTRAINT product_fk3 FOREIGN KEY (buyer) REFERENCES member (member)
);

-- PRODUCT_SEQ
CREATE SEQUENCE product_seq START WITH 10;

-- MYPRODUCT
CREATE TABLE myproduct(
	myproduct NUMBER NOT NULL,
	product NUMBER NOT NULL,
	member NUMBER NOT NULL,
	CONSTRAINT myproduct_pk PRIMARY KEY (myproduct),
	CONSTRAINT myproduct_fk FOREIGN KEY (product) REFERENCES product (product),
	CONSTRAINT myproduct_fk2 FOREIGN KEY (member) REFERENCES member (member),
	CONSTRAINT myproduct_uk UNIQUE (product, member)
);

-- MYPRODUCT_SEQ
CREATE SEQUENCE myproduct_seq START WITH 10;


-- MANNER
CREATE TABLE manner(
	manner NUMBER NOT NULL,
	product NUMBER NOT NULL,
	seller NUMBER NOT NULL,
	buyer NUMBER NOT NULL,
	rate NUMBER(1) NOT NULL, /* 1~5점 */
	review CLOB NOT NULL,
	CONSTRAINT manner_pk PRIMARY KEY (manner),
	CONSTRAINT manner_fk FOREIGN KEY (product) REFERENCES product (product),
	CONSTRAINT manner_fk2 FOREIGN KEY (seller) REFERENCES member (member),
	CONSTRAINT manner_fk3 FOREIGN KEY (buyer) REFERENCES member (member),
	CONSTRAINT manner_uk UNIQUE (product, seller, buyer)
);

-- MANNER_SEQ
CREATE SEQUENCE manner_seq START WITH 10;


-- CHATROOM
CREATE TABLE chatroom(
	chatroom NUMBER NOT NULL,
	product NUMBER NOT NULL,
	seller NUMBER NOT NULL,
	buyer NUMBER NOT NULL,
	CONSTRAINT chatroom_pk PRIMARY KEY (chatroom),
	CONSTRAINT chatroom_fk FOREIGN KEY (product) REFERENCES product (product),
	CONSTRAINT chatroom_fk2 FOREIGN KEY (seller) REFERENCES member (member),
	CONSTRAINT chatroom_fk3 FOREIGN KEY (buyer) REFERENCES member (member),
	CONSTRAINT chatroom_uk UNIQUE (product, seller, buyer)
);

-- CHATROOM_SEQ
CREATE SEQUENCE chatroom_seq START WITH 10;

-- CHAT
CREATE TABLE chat(
	chat NUMBER NOT NULL,
	chatroom NUMBER NOT NULL,
	member NUMBER NOT NULL,
	opponent NUMBER NOT NULL,
	content VARCHAR2(900) NOT NULL,
	sent DATE DEFAULT CURRENT_DATE NOT NULL,
	received DATE,
	read NUMBER(1) DEFAULT 0 NOT NULL, /* 0=읽지 않음, 1=읽음 */
	CONSTRAINT chat_pk PRIMARY KEY (chat),
	CONSTRAINT chat_fk FOREIGN KEY (chatroom) REFERENCES chatroom (chatroom),
	CONSTRAINT chat_fk2 FOREIGN KEY (member) REFERENCES member (member),
	CONSTRAINT chat_fk3 FOREIGN KEY (opponent) REFERENCES member (member)
);

-- CHAT_SEQ
CREATE SEQUENCE chat_seq START WITH 10;


-- REPLY
CREATE TABLE reply(
	reply NUMBER NOT NULL,
	product NUMBER NOT NULL,
	member NUMBER NOT NULL,
	content VARCHAR2(900) NOT NULL,
	parent NUMBER,
	registered DATE DEFAULT CURRENT_DATE NOT NULL,
	modified DATE,
	deleted NUMBER(1) DEFAULT 0 NOT NULL, /* 0=삭제되지 않음, 1=삭제됨 */
	CONSTRAINT reply_pk PRIMARY KEY (reply),
	CONSTRAINT reply_fk FOREIGN KEY (product) REFERENCES product (product),
	CONSTRAINT reply_fk2 FOREIGN KEY (member) REFERENCES member (member)
);
ALTER TABLE reply ADD CONSTRAINT reply_fk3 FOREIGN KEY (parent) REFERENCES reply (reply);

-- REPLY_SEQ
CREATE SEQUENCE reply_seq START WITH 10;