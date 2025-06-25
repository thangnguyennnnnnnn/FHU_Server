package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.dto;

public class UserDto {

    private String id;
    private String username;
    private String fullName;       // Ghép từ first, middle, last
    private String role;
    private String urlAvata;
    private String updatedAt;
    
    public UserDto(String id, String username, String role, String fullName, String urlAvatar) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.fullName = fullName;
        this.urlAvata = urlAvatar;
    }
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUrlAvata() {
		return urlAvata;
	}
	public void setUrlAvata(String urlAvata) {
		this.urlAvata = urlAvata;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
