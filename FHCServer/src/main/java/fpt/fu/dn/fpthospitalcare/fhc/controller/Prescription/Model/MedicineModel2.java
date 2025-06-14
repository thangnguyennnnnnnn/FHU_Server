package fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription.Model;

public class MedicineModel2 {
	private String medicineId;
	private String name;
	private int quantity;
	private String unit;
	private String date;
	private String prescriptionId;
	private int totalQuantity;
	private int totalQuantityDisplay;
	private String note;
	public String getMedicineId() {
		return medicineId;
	}
	public void setMedicineId(String medicineId) {
		this.medicineId = medicineId;
	}
	public int getQuantity() {
		return quantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPrescriptionId() {
		return prescriptionId;
	}
	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public int getTotalQuantityDisplay() {
		return totalQuantityDisplay;
	}
	public void setTotalQuantityDisplay(int totalQuantityDisplay) {
		this.totalQuantityDisplay = totalQuantityDisplay;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
