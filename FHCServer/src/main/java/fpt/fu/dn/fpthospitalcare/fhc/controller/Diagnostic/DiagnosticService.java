package fpt.fu.dn.fpthospitalcare.fhc.controller.Diagnostic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Diagnostic.Model.DiagnosticDisplayModel;

public class DiagnosticService extends FHCCommonService {
	
	private final int DOCTOR_ROLE_NUMBER = 2;
	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
		case 1:
			rs = searchDiagnostics();
			break;
		case 2:
			rs = updateApointment();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	/**
	 * Lấy danh sách lich khám tương ứng
	 * 
	 * @return
	 * @throws Exception 
	 */
	public int searchDiagnostics() throws Exception {

		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		String userId = params.get("userId") == null ? "" : params.get("userId");
		String appointmentId = params.get("appointmentId") == null ? "" : params.get("appointmentId");
		String name = params.get("name") == null ? "" : params.get("name");
		
		String SQL_SEARCH_DIAGNOSTIC = "SELECT "
				+ "	FDR.APPOINTMENT_ID, "
				+ "	FA.FULLNAME , "
				+ "	FA.PHONE, "
				+ "	FDR.DIAGNOSTIC_DOCTOR_ID, "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME AS DOCTOR_FULL_NAME, "
				+ "	FUI.PHONE_NUMBER DOCTOR_PHONE_NUMBER, "
				+ "	FDR.DIAGNOSTIC_RESULT, "
				+ "	FDR.DIAGNOSTIC_STATUS, "
				+ "	FDR.DAY_OF_REVIEW, "
				+ "	FDR.DIAGNOSIS_START_TIME , "
				+ "	FDR.DIAGNOSIS_END_TIME "
				+ "FROM "
				+ "	FHC_DIAGNOSTIC_RESULT FDR "
				+ "LEFT JOIN FHC_APPOINMENT FA ON "
				+ "	FDR.APPOINTMENT_ID = FA.APPOINMENT_ID "
				+ "LEFT JOIN FHC_USER_INFORMATION FUI ON "
				+ "	FDR.DIAGNOSTIC_DOCTOR_ID = FUI.USER_ID ";

		/**
		 * Validate các item 
		 */
		if (appointmentId.equals("") && userId.equals("")
				&& name.equals("") ) {
			executeNo = ConstantVariable.INPUT_EMPTY_VALUE;
			return executeNo;
		} else {
			SQL_SEARCH_DIAGNOSTIC += "WHERE ";
		}
		
		boolean isAppend = false;
		
		if (!appointmentId.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_DIAGNOSTIC += "FDR.APPOINTMENT_ID = '" + appointmentId + "' ";
			} else {
				SQL_SEARCH_DIAGNOSTIC += "AND FDR.APPOINTMENT_ID = '" + appointmentId + "' ";
			}
			isAppend = true;
		}
		
		if (!userId.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_DIAGNOSTIC += "FA.CREATE_USER = '" + userId + "' ";
			} else {
				SQL_SEARCH_DIAGNOSTIC += "AND FA.CREATE_USER = '" + userId + "' ";
			}
			isAppend = true;
		}
		
		if (!name.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_DIAGNOSTIC += "FA.FULLNAME LIKE N'%" + name + "%' ";
			} else {
				SQL_SEARCH_DIAGNOSTIC += "AND FA.FULLNAME LIKE N'%" + name + "%' ";
			}
			isAppend = true;
		}
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(SQL_SEARCH_DIAGNOSTIC);
			ps.executeQuery();
			rs = ps.executeQuery();
			
			int count = 0;
			List<DiagnosticDisplayModel> diagnosticList = new ArrayList<>();
			while(rs.next()) {
				count++;
				DiagnosticDisplayModel diagnostic = new DiagnosticDisplayModel();
				diagnostic.setAppointmentId(rs.getString(1));
				diagnostic.setFullname(rs.getString(2));
				diagnostic.setPhone(rs.getString(3));
				diagnostic.setDiagnosticDoctorId(rs.getString(4) == null ? "Đang cập nhập..." : rs.getString(4));
				diagnostic.setDoctorFullName(rs.getString(5) == null ? "Đang cập nhập..." : rs.getString(5));
				diagnostic.setDoctorPhoneNumber(rs.getString(6) == null ? "Đang cập nhập..." : rs.getString(6));
				diagnostic.setDiagnosticResult(rs.getString(7) == null ? "Đang cập nhập..." : rs.getString(7));
				diagnostic.setDiagnosticStatus(rs.getString(8) == null ? "Đang cập nhập..." : rs.getString(8));
				diagnostic.setDayOfReview(rs.getString(9) == null ? "Đang cập nhập..." : rs.getString(9));
				diagnostic.setDiagnosisStartTime(rs.getString(10) == null ? "Đang cập nhập..." : rs.getString(10));
				diagnostic.setDiagnosisEndTime(rs.getString(11) == null ? "Đang cập nhập..." : rs.getString(11));
				
				diagnosticList.add(diagnostic);
			}
			
			if (count > 0) {
				setReturnObject(diagnosticList);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
			
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
	
	public int updateApointment() throws Exception {

		DiagnosticDisplayModel diagnostic = new DiagnosticDisplayModel();
		ObjectMapper mapper = new ObjectMapper();
		diagnostic = mapper.convertValue(getReciveObject(), diagnostic.getClass());

		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		try {
			int roleUser = authentication(diagnostic.getDiagnosticDoctorId());
			
			if (DOCTOR_ROLE_NUMBER != roleUser) {
				executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		String SQL_UPDATE_SATUS_CARD = "UPDATE "
				+ "	FHC_APPOINTMENT_CARD "
				+ "SET "
				+ "	STATUS_ID = :STATUS "
				+ "WHERE "
				+ "	APPOINTMENT_ID = ?; ";
		
		String SQL_UPDATE_DIAGNOSTIC_CARD = "UPDATE "
				+ "	FHC_DIAGNOSTIC_RESULT "
				+ "SET "
				+ "	DIAGNOSIS_START_TIME = ?, "
				+ "	DIAGNOSTIC_DOCTOR_ID = ?, "
				+ "	DIAGNOSTIC_RESULT = ?, "
				+ "	DIAGNOSTIC_STATUS = ?, "
				+ "	DAY_OF_REVIEW = ?, "
				+ "	DIAGNOSIS_END_TIME = ?, "
				+ "	UPDATE_TIME = ?, "
				+ "	UPDATE_USER = ? "
				+ "WHERE "
				+ "	APPOINTMENT_ID = ?;";
		try {
			String currentDate = getCurrentDate("ddMMyyyyhhmmss");
			String diagnosisTime = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			
			diagnostic.setDiagnosisStartTime(diagnosisTime);
			if (diagnostic.getDiagnosticStatus().equals("0")) {
				diagnostic.setDiagnosisEndTime(null);
				SQL_UPDATE_SATUS_CARD = SQL_UPDATE_SATUS_CARD.replace(":STATUS", "2");
			} else {
				diagnostic.setDiagnosisEndTime(diagnosisTime);
				SQL_UPDATE_SATUS_CARD = SQL_UPDATE_SATUS_CARD.replace(":STATUS", "3");
			}
			
			ps = getSC().prepareStatement(SQL_UPDATE_DIAGNOSTIC_CARD);
			ps.setString(1, diagnostic.getDiagnosisStartTime());
			ps.setString(2, diagnostic.getDiagnosticDoctorId());
			ps.setString(3, diagnostic.getDiagnosticResult());
			ps.setString(4, diagnostic.getDiagnosticStatus());
			ps.setString(5, diagnostic.getDayOfReview().equals("") ? null : diagnostic.getDayOfReview());
			ps.setString(6, diagnostic.getDiagnosisEndTime());
			ps.setString(7, currentDate);
			ps.setString(8, diagnostic.getDiagnosticDoctorId());
			ps.setString(9, diagnostic.getAppointmentId());
			int count = ps.executeUpdate();

			ps.close();
			if (count > 0) {
				ps = getSC().prepareStatement(SQL_UPDATE_SATUS_CARD);
				ps.setString(1, diagnostic.getAppointmentId());
				count = ps.executeUpdate();
				if (count > 0) {
					setReturnObject(diagnostic);
				}
				
			}
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
}
