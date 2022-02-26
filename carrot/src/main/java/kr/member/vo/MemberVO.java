package kr.member.vo;

public class MemberVO {
	private int member; // 0~9는 관리자 예약
	private String id; // 영문 대문자만 사용
	private int auth; // 0=탈퇴, 1=정지, 2=일반, 3=관리자
	private String nickname;
	private String password; // 최소 길이 6자, 특수문자 1자 이상, 영문 대소문자 구별
	private String home; // 거주 동네 주소(읍면동까지)
	private String sido; // 거주 동네의 광역지방자치단체명
	private String sigungu; // 거주 동네의 시/군/구명
	private String bname; // 거주 동네의 법정동/리명
	private String main; // 메인 페이지에서 기본으로 보여줄 동네
	private String phone; // 하이픈 포함 13자
	private String email;
	private String profile;
	private Double rate;
	private String registered;
	
	// 로그인시 입력한 아이디와 비밀번호가 유효한지 검증
	public boolean isValid(String user_password) {
		if(auth > 1 && password.equals(user_password)) { // 탈퇴 및 정지 회원은 서비스 이용 불가
			return true;
		}
		return false;
	}	
	
	public int getMember() {
		return member;
	}
	public void setMember(int member) {
		this.member = member;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getSido() {
		return sido;
	}
	public void setSido(String sido) {
		this.sido = sido;
	}
	public String getSigungu() {
		return sigungu;
	}
	public void setSigungu(String sigungu) {
		this.sigungu = sigungu;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getMain() {
		return main;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getRegistered() {
		return registered;
	}
	public void setRegistered(String registered) {
		this.registered = registered;
	}
}