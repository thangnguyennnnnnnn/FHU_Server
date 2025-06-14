package fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription.Model;

import java.util.List;

public class PrescriptionReturnModel {
	PrescriptionCardModel prescriptionCardModel;
	List<MedicineModel2> MedicineModelList;
	public PrescriptionCardModel getPrescriptionCardModel() {
		return prescriptionCardModel;
	}
	public void setPrescriptionCardModel(PrescriptionCardModel prescriptionCardModel) {
		this.prescriptionCardModel = prescriptionCardModel;
	}
	public List<MedicineModel2> getMedicineModelList() {
		return MedicineModelList;
	}
	public void setMedicineModelList(List<MedicineModel2> medicineModelList) {
		MedicineModelList = medicineModelList;
	}
	
}
