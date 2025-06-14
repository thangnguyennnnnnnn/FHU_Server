package fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.MedicineModel;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription.Model.PrescriptionCardModel;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription.Model.MedicineModel2;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription.Model.PrescriptionReturnModel;

public class PrescriptionService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = ConstantVariable.SUCCESS_NUMBER;
		switch (workProgram) {
		case 1:
			rs = createPrescription();
			break;
		case 2:
			rs = getPrescription();
			break;
		case 3:
			rs = updatePrescription();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public int createPrescription() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		String prescriptionList = (String) param.get("prescriptionList");
		String note = (String)param.get("note");
		String numberDay = (String)param.get("numberDay");
		String userId = (String)param.get("userId");
		String appoinmentId = (String)param.get("appoinmentId");
		
		List<MedicineModel> medicineImport = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		medicineImport = mapper.readValue(prescriptionList, List.class);
		
		if (authentication(userId) != 2) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		String SQL_INSERT_PRESCRIPTION = "INSERT INTO FHC_PRESCRIPTION "
				+ "	(CREATED_BY, "
				+ "	CREATED_AT, "
				+ "	NOTE, "
				+ "	IS_ACTIVE, "
				+ "	APPOINMENT_ID,  "
				+ "	NUM_OF_DAY, "
				+ "	STATUS) "
				+ "VALUES (?,?,?,?,?,?,?)";
		
		String SQL_INSERT_PRESCRIPTION_DETAIL = "INSERT INTO FHC_PRESCRIPTION_DETAIL "
				+ "	(MEDICINE_ID, "
				+ "	UNIT, "
				+ "	QUANTITY, "
				+ "	[DATE], "
				+ " PRESCRIPTION_ID, NOTE ) "
				+ "VALUES ";

		try {
	        int countRecord = 0;
	        
	        String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
	        
	        ps = getSC().prepareStatement(SQL_INSERT_PRESCRIPTION);
	        ps.setString(1, userId);
	        ps.setString(2, currentDate);
	        ps.setString(3, note);
	        ps.setString(4, "0");
	        ps.setString(5, appoinmentId);
	        ps.setString(6, numberDay);
	        ps.setString(7, "Tạo thành công");
	        countRecord = ps.executeUpdate();
	        
	        if (countRecord > 0) {
	        	ps.close();
	        	StringBuilder sqlDetail = new StringBuilder();
	        	sqlDetail.append(SQL_INSERT_PRESCRIPTION_DETAIL);
	        	
	        	int cout = 0;
	        	for (int i = 0; i < medicineImport.size(); i++) {
		        	MedicineModel medicine = mapper.convertValue(medicineImport.get(i), MedicineModel.class);
		        	sqlDetail.append("(");
		        	sqlDetail.append("'" + medicine.getId() + "',");
		        	sqlDetail.append("N'" + medicine.getUnit() + "',");
		        	sqlDetail.append("'0',");
		        	sqlDetail.append("'" + getCurrentDate("yyyy/MM/dd") + "',");
		        	sqlDetail.append("'" + appoinmentId + "',");
		        	sqlDetail.append("N'" + medicine.getNote() + "'");
		        	sqlDetail.append("),");
		        	cout++;
		        }
	        	
	        	if (cout > 0) {
	        		countRecord = 0;
	        		ps = getSC().prepareStatement(sqlDetail.toString().trim().replaceFirst(",$", ""));
	        		countRecord = ps.executeUpdate();
	        		
	        		if (countRecord == 0) {
	        			executeNo = ConstantVariable.ERROR_NUMBER;
	        		}
	        	} else {
	        		executeNo = ConstantVariable.ERROR_NUMBER;
	        	}
	        	
	        } else {
	        	executeNo = ConstantVariable.ERROR_NUMBER;
	        }
            
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	public int getPrescription() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		//String userId = (String)param.get("userId");
		String appointmentId = (String)param.get("appointmentId");
		
		String SQL_SELECT_PRESCRIPTION_DETAIL = "SELECT  "
				+ "	FP.APPOINMENT_ID, "
				+ " FP.ID, "
				+ "	FA.FULLNAME, "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' '+ FUI.LAST_NAME, "
				+ "	FP.NOTE, "
				+ "	FP.NUM_OF_DAY, "
				+ "	FP.CREATED_AT, "
				+ "	FP.STATUS "
				+ "FROM FHC_PRESCRIPTION FP "
				+ "LEFT JOIN FHC_APPOINMENT FA ON FP.APPOINMENT_ID = FA.APPOINMENT_ID "
				+ "LEFT JOIN FHC_USER_INFORMATION FUI ON FP.CREATED_BY = FUI.USER_ID "
				+ "WHERE FP.APPOINMENT_ID = ?";
		
		String SQL_SELECT_DETAIL = "SELECT  "
				+ "	FPD.MEDICINE_ID, "
				+ "	FMG.NAME, "
				+ "	FPD.QUANTITY, "
				+ "	FPD.UNIT, "
				+ "	FPD.[DATE], "
				+ "	FPD.PRESCRIPTION_ID, "
				+ "	FMG.QUANTITY, "
				+ " FPD.NOTE "
				+ "FROM FHC_PRESCRIPTION_DETAIL FPD "
				+ "LEFT JOIN FHC_MEDICAL_GOODS FMG ON FPD.MEDICINE_ID = FMG.ID "
				+ "WHERE FPD.PRESCRIPTION_ID = ?";

		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_PRESCRIPTION_DETAIL);
			ps.setString(1, appointmentId);
	        
			rs = ps.executeQuery();
			PrescriptionCardModel prescriptionCard = new PrescriptionCardModel();
			int countRecord = 0;
			if (rs.next()) {
				prescriptionCard.setAppoinmentId(rs.getString(1));
				prescriptionCard.setId(rs.getString(2));
				prescriptionCard.setPatientName(rs.getString(3));
				prescriptionCard.setDoctorName(rs.getString(4));
				prescriptionCard.setNote(rs.getString(5));
				prescriptionCard.setNumOfDay(rs.getString(6));
				prescriptionCard.setCreateAt(rs.getString(7));
				prescriptionCard.setStatus(rs.getString(8));
				countRecord++;
				
			}
			ps.close();
			rs.close();
			
			ps = getSC().prepareStatement(SQL_SELECT_DETAIL);
			ps.setString(1, prescriptionCard.getAppoinmentId());
			rs = ps.executeQuery();
			List<MedicineModel2> MedicineDetailList = new ArrayList<>();
	        while (rs.next()) {
	        	MedicineModel2 m = new MedicineModel2();
	        	m.setMedicineId(rs.getString(1));
	        	m.setName(rs.getString(2));
	        	m.setQuantity(rs.getInt(3));
	        	m.setUnit(rs.getString(4));
	        	m.setDate(rs.getString(5));
	        	m.setPrescriptionId(rs.getString(6));
	        	m.setTotalQuantity(rs.getInt(7));
	        	m.setTotalQuantityDisplay(rs.getInt(7));
	        	m.setNote(rs.getString(8));
	        	
	        	MedicineDetailList.add(m);
	        	countRecord++;
	        }
            
	        if (countRecord == 0) {
	        	executeNo = ConstantVariable.DB_NOTFOUND;
	        	return executeNo;
	        }
	        PrescriptionReturnModel returnModel = new PrescriptionReturnModel();
	        returnModel.setMedicineModelList(MedicineDetailList);
	        returnModel.setPrescriptionCardModel(prescriptionCard);
	        
	        setReturnObject(returnModel);
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	@SuppressWarnings("unchecked")
	public int updatePrescription() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		String userId = (String)param.get("userId");
		String medicineModelList = (String)param.get("medicineModelList");
		String appoinmentID = "";
		if (authentication(userId) != 5) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		List<MedicineModel> medicineList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		medicineList = mapper.readValue(medicineModelList, List.class);
		
		String SQL_CHECK_CHANGE = "SELECT "
				+ "	QUANTITY "
				+ "FROM "
				+ "	FHC_PRESCRIPTION_DETAIL "
				+ "WHERE "
				+ "	MEDICINE_ID = ? "
				+ "	AND QUANTITY = ? AND PRESCRIPTION_ID = ?; ";
		
		String SQL_QUANTITY_TOTAL = "SELECT "
				+ "	QUANTITY "
				+ "FROM "
				+ "	FHC_MEDICAL_GOODS "
				+ "WHERE "
				+ "	ID = ? ";
		
		String SQL_UPDATE_DETAIL = "UPDATE "
				+ "	FHC_PRESCRIPTION_DETAIL "
				+ "SET "
				+ "	QUANTITY = ? "
				+ "WHERE "
				+ "	MEDICINE_ID = ?; ";
		
		String SQL_UPDATE_QUANTITY = "UPDATE "
				+ "	FHC_MEDICAL_GOODS "
				+ "SET "
				+ "	QUANTITY = ? "
				+ "WHERE "
				+ "	ID  = ?; ";
		
		String SQL_UPDATE_PRESCRIPTION = "UPDATE "
				+ "	FHC_PRESCRIPTION "
				+ "SET "
				+ "	UPDATE_AT = ?, "
				+ "	UPDATE_BY = ?, "
				+ " STATUS = ? "
				+ "WHERE "
				+ "	APPOINMENT_ID = ?; ";

		try {
			int countDetail = 0;
			int count = 0;
			for (int i = 0; i < medicineList.size(); i++) {
				MedicineModel2 medicine = mapper.convertValue(medicineList.get(i), MedicineModel2.class);
				appoinmentID = medicine.getPrescriptionId();
				ps = getSC().prepareStatement(SQL_CHECK_CHANGE);
				ps.setString(1, medicine.getMedicineId());
				ps.setInt(2, medicine.getQuantity());
				ps.setString(3, medicine.getPrescriptionId());
				rs = ps.executeQuery();
				if (rs.next()) {
					executeNo = ConstantVariable.NO_CHANGE_ERROR;
					return executeNo;
				}
				ps.close();
				rs.close();
				
				int quantity = 0;
				ps = getSC().prepareStatement(SQL_QUANTITY_TOTAL);
				ps.setString(1, medicine.getMedicineId());
				rs = ps.executeQuery();
				if (rs.next()) {
					quantity = rs.getInt(1);
				}
				
				ps.close();
				rs.close();
				
				ps = getSC().prepareStatement(SQL_UPDATE_DETAIL);
				ps.setInt(1, medicine.getQuantity());
				ps.setString(2, medicine.getMedicineId());
				
				ps.executeUpdate();
				countDetail++;
				
				ps.close();
				
				int newTotalQuantity = quantity - medicine.getQuantity();
				
				ps = getSC().prepareStatement(SQL_UPDATE_QUANTITY);
				ps.setInt(1, newTotalQuantity);
				ps.setString(2, medicine.getMedicineId());
				
				ps.executeUpdate();
				count++;
			}
			
			if (count == medicineList.size() && countDetail == medicineList.size()) {
				ps.close();
				
				ps = getSC().prepareStatement(SQL_UPDATE_PRESCRIPTION);
				ps.setString(1, getCurrentDate("dd/MM/yyyy HH:mm:ss"));
				ps.setString(2, userId);
				ps.setString(3, "Đã hoàn thành");
				ps.setString(4, appoinmentID);
				
				ps.executeUpdate();
			} else {
				executeNo = ConstantVariable.ERROR_UPDATE;
			}
			
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
}
