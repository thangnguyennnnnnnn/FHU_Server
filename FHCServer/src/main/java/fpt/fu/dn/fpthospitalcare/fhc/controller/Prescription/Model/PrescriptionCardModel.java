package fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription.Model;

public class PrescriptionCardModel {
	private String appoinmentId;
	private String id;
	private String patientName;
	private String doctorName;
	private String note;
	private String numOfDay;
	private String createAt;
	private String status;
	public String getAppoinmentId() {
		return appoinmentId;
	}
	public void setAppoinmentId(String appoinmentId) {
		this.appoinmentId = appoinmentId;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNumOfDay() {
		return numOfDay;
	}
	public void setNumOfDay(String numOfDay) {
		this.numOfDay = numOfDay;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
