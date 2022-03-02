package kr.chat.vo;

public class ChatVO {
	private int chat;
	private int chatroom;
	private int member;
	private int opponent;
	private String content;
	private String sent;
	private String received;
	private int read; // 0=읽지 않음, 1=읽음
	
	public int getChat() {
		return chat;
	}
	public void setChat(int chat) {
		this.chat = chat;
	}
	public int getChatroom() {
		return chatroom;
	}
	public void setChatroom(int chatroom) {
		this.chatroom = chatroom;
	}
	public int getMember() {
		return member;
	}
	public void setMember(int member) {
		this.member = member;
	}
	public int getOpponent() {
		return opponent;
	}
	public void setOpponent(int opponent) {
		this.opponent = opponent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSent() {
		return sent;
	}
	public void setSent(String sent) {
		this.sent = sent;
	}
	public String getReceived() {
		return received;
	}
	public void setReceived(String received) {
		this.received = received;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
}
