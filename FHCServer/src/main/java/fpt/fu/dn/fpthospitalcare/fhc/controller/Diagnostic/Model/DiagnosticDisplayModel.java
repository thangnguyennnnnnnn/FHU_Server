package fpt.fu.dn.fpthospitalcare.fhc.controller.Diagnostic.Model;

public class DiagnosticDisplayModel {
	private String appointmentId;
	private String fullname;
	private String phone;
	private String diagnosticDoctorId;
	private String doctorFullName;
	private String doctorPhoneNumber;
	private String diagnosticResult;
	private String diagnosticStatus;
	private String dayOfReview;
	private String diagnosisStartTime;
	private String diagnosisEndTime;
	
	public String getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDiagnosticDoctorId() {
		return diagnosticDoctorId;
	}
	public void setDiagnosticDoctorId(String diagnosticDoctorId) {
		this.diagnosticDoctorId = diagnosticDoctorId;
	}
	public String getDoctorFullName() {
		return doctorFullName;
	}
	public void setDoctorFullName(String doctorFullName) {
		this.doctorFullName = doctorFullName;
	}
	public String getDoctorPhoneNumber() {
		return doctorPhoneNumber;
	}
	public void setDoctorPhoneNumber(String doctorPhoneNumber) {
		this.doctorPhoneNumber = doctorPhoneNumber;
	}
	public String getDiagnosticResult() {
		return diagnosticResult;
	}
	public void setDiagnosticResult(String diagnosticResult) {
		this.diagnosticResult = diagnosticResult;
	}
	public String getDiagnosticStatus() {
		return diagnosticStatus;
	}
	public void setDiagnosticStatus(String diagnosticStatus) {
		this.diagnosticStatus = diagnosticStatus;
	}
	public String getDayOfReview() {
		return dayOfReview;
	}
	public void setDayOfReview(String dayOfReview) {
		this.dayOfReview = dayOfReview;
	}
	public String getDiagnosisStartTime() {
		return diagnosisStartTime;
	}
	public void setDiagnosisStartTime(String diagnosisStartTime) {
		this.diagnosisStartTime = diagnosisStartTime;
	}
	public String getDiagnosisEndTime() {
		return diagnosisEndTime;
	}
	public void setDiagnosisEndTime(String diagnosisEndTime) {
		this.diagnosisEndTime = diagnosisEndTime;
	}
	
}
