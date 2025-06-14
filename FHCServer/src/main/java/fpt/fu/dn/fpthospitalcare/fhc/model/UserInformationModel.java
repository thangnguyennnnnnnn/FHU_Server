package fpt.fu.dn.fpthospitalcare.fhc.model;

import java.util.List;

public class UserInformationModel {
	
	private String userId;
	
	private String username;
	
	private String usernameEncryto;
	
	private String password;

	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String address;
	
	private String email;
	
	private String nationality;
	
	private String phoneNumber;
	
	private String birthday;
	
	private String gender;
	
	private String urlAvt;
	
	private List<EducationInfoModel> EducationInfoList;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernameEncryto() {
		return usernameEncryto;
	}

	public void setUsernameEncryto(String usernameEncryto) {
		this.usernameEncryto = usernameEncryto;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<EducationInfoModel> getEducationInfoList() {
		return EducationInfoList;
	}

	public void setEducationInfoList(List<EducationInfoModel> educationInfoList) {
		EducationInfoList = educationInfoList;
	}

	public String getUrlAvt() {
		return urlAvt;
	}

	public void setUrlAvt(String urlAvt) {
		this.urlAvt = urlAvt;
	}
	
}
