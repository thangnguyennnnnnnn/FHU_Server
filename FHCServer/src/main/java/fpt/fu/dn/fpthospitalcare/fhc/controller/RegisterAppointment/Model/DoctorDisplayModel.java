package fpt.fu.dn.fpthospitalcare.fhc.controller.RegisterAppointment.Model;

import java.util.List;

import fpt.fu.dn.fpthospitalcare.fhc.model.Faculty;

public class DoctorDisplayModel {
	private String userId;
	private String fullname;
	private String birthdate;
	private String phone;
	private String email;
	private String roleId;
	private List<Faculty> facultys;
	private String appointmentId;
	private String urlAvata;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birhdate) {
		this.birthdate = birhdate;
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public List<Faculty> getFacultys() {
		return facultys;
	}
	public void setFacultys(List<Faculty> facultys) {
		this.facultys = facultys;
	}
	public String getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getUrlAvata() {
		return urlAvata;
	}
	public void setUrlAvata(String urlAvata) {
		this.urlAvata = urlAvata;
	}
}
