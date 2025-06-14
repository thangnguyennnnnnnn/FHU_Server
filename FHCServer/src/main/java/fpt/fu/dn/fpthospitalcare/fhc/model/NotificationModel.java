package fpt.fu.dn.fpthospitalcare.fhc.model;

public class NotificationModel {
	private String notiId;
	private String reciveRoom;
	private String content;
	private String time;
	private String link;
	private String readed;
	
	public String getNotiId() {
		return notiId;
	}
	public void setNotiId(String notiId) {
		this.notiId = notiId;
	}
	public String getReciveRoom() {
		return reciveRoom;
	}
	public void setReciveRoom(String reciveRoom) {
		this.reciveRoom = reciveRoom;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getReaded() {
		return readed;
	}
	public void setReaded(String readed) {
		this.readed = readed;
	}
	
}
