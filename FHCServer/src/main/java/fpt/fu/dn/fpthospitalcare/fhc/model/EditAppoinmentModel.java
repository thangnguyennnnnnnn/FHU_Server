package fpt.fu.dn.fpthospitalcare.fhc.model;

public class EditAppoinmentModel {
	
	private String appoinmentId;
	
	private String userId;
    
    private String appoinmentDate;
    
    private String appoinmentTime;
    
    private String email;
    
    private String phone;
    
    private String symptom;
    
    private String active;

	public String getAppoinmentId() {
		return appoinmentId;
	}

	public void setAppoinmentId(String appoinmentId) {
		this.appoinmentId = appoinmentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAppoinmentDate() {
		return appoinmentDate;
	}

	public void setAppoinmentDate(String appoinmentDate) {
		this.appoinmentDate = appoinmentDate;
	}

	public String getAppoinmentTime() {
		return appoinmentTime;
	}

	public void setAppoinmentTime(String appoinmentTime) {
		this.appoinmentTime = appoinmentTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSymptom() {
		return symptom;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
    
    
}
