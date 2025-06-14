package fpt.fu.dn.fpthospitalcare.fhc.controller.AppointmentCardSearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.AppointmentModelDB;

public class AppointmentCardSearchService extends FHCCommonService {
	
	private static final int NURSE_ROLE_NUMBER = 3;
	
	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
		case 1:
			rs = searchApointments();
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
	public int searchApointments() throws Exception {

		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		String userId = params.get("userId") == null ? "" : params.get("userId");
		String appointmentId = params.get("appointmentId") == null ? "" : params.get("appointmentId");
		String appointmentTime = params.get("appointmentTime") == null ? "" : params.get("appointmentTime");
		String appointmentDate = params.get("appointmentDate") == null ? "" : params.get("appointmentDate");
		String patientName = params.get("patientName") == null ? "" : params.get("patientName");
		String facultyId = params.get("facultyId") == null ? "" : params.get("facultyId");
		String faculty = params.get("faculty") == null ? "" : params.get("faculty");
		
		try {
			int roleUser = authentication(userId);
			
			if (NURSE_ROLE_NUMBER != roleUser && roleUser != 2) {
				executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		String SQL_SEARCH_APPOINEMENT = "SELECT "
				+ "	FA.APPOINMENT_ID, "
				+ "	FA.FULLNAME, "
				+ "	FA.PHONE, "
				+ "	FA.ADDRESS, "
				+ "	FA.FULLNAME_RELATIVE, "
				+ "	FA.PHONE_RELATIVE, "
				+ "	FA.ADDRESS_RELATIVE, "
				+ "	FA.EXAMINATION_TYPE, "
				+ "	FA.EXAMINATION_DATE, "
				+ "	FA.EXAMINATION_TIME, "
				+ "	FA.DOCTOR_ID, "
				+ "	FA.CREATE_USER, "
				+ "	FA.CREATE_TIME, "
				+ "	FA.SYMPTOM, "
				+ "	FA.CONFIRM, "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME AS FULLNAME, "
				+ "	FF.NAME, "
				+ " FAC.STATUS_ID, "
				+ "	FAC.PLACE "
				+ "FROM "
				+ "	FHC_APPOINMENT FA  "
				+ "	LEFT JOIN FHC_USER_INFORMATION FUI ON FA.DOCTOR_ID = FUI.USER_ID "
				+ "	LEFT JOIN FHC_FACULTY FF ON FA.EXAMINATION_TYPE = FF.ID "
				+ " LEFT JOIN FHC_APPOINTMENT_CARD FAC ON FA.APPOINMENT_ID = FAC.APPOINTMENT_ID ";

		/**
		 * Validate các item 
		 */
		if (appointmentId.equals("") && appointmentTime.equals("")
				&& appointmentDate.equals("") && patientName.equals("") && facultyId.equals("")
				&& faculty.equals("")) {
			executeNo = ConstantVariable.INPUT_EMPTY_VALUE;
			return executeNo;
		} else {
			SQL_SEARCH_APPOINEMENT += "WHERE ";
		}
		
		boolean isAppend = false;
		
		if (!appointmentId.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_APPOINEMENT += "APPOINMENT_ID = '" + appointmentId + "' ";
			} else {
				SQL_SEARCH_APPOINEMENT += "AND APPOINMENT_ID = '" + appointmentId + "' ";
			}
			isAppend = true;
		}
		
		if (!appointmentTime.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_APPOINEMENT += "EXAMINATION_TIME = '" + appointmentTime + "' ";
			} else {
				SQL_SEARCH_APPOINEMENT += "AND EXAMINATION_TIME = '" + appointmentTime + "' ";
			}
			isAppend = true;
		}
		
		if (!appointmentDate.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_APPOINEMENT += "EXAMINATION_DATE = '" + appointmentDate + "' ";
			} else {
				SQL_SEARCH_APPOINEMENT += "AND EXAMINATION_DATE = '" + appointmentDate + "' ";
			}
			isAppend = true;
		}
		
		if (!patientName.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_APPOINEMENT += "FULLNAME LIKE N'%"+ patientName +"%' ";
			} else {
				SQL_SEARCH_APPOINEMENT += "AND FULLNAME LIKE N'%"+ patientName +"%' ";
			}
			isAppend = true;
		}
		
		if (!facultyId.equals("")) {
			if (!isAppend) {
				SQL_SEARCH_APPOINEMENT += "EXAMINATION_TYPE = " + facultyId + " ";
			} else {
				SQL_SEARCH_APPOINEMENT += "AND EXAMINATION_TYPE = " + facultyId + " ";
			}
			isAppend = true;
		}
		
		SQL_SEARCH_APPOINEMENT += " AND FA.CONFIRM = 'Y';";
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(SQL_SEARCH_APPOINEMENT);
			rs = ps.executeQuery();
			
			int count = 0;
			List<AppointmentModelDB> apList = new ArrayList<>();
			while(rs.next()) {
				count++;
				AppointmentModelDB ap = new AppointmentModelDB();
				ap.setAppointmentBeforId(rs.getString(1));
				ap.setFullname(rs.getString(2));
				ap.setPhone(rs.getString(3));
				ap.setAddress(rs.getString(4));
				ap.setFullnameNT(rs.getString(5));
				ap.setPhoneNT(rs.getString(6));
				ap.setAddressNT(rs.getString(7));
				ap.setFacultyId(rs.getString(8));
				ap.setExaminationDate(rs.getString(9));
				ap.setExaminationTime(rs.getString(10));
				ap.setDoctorId(rs.getString(11));
				ap.setUserId(rs.getString(12));
				ap.setSymptom(rs.getString(14));
				ap.setDoctorName(rs.getString(16));
				ap.setFacultyName(rs.getString(17));
				ap.setStatus(getStatus(rs.getInt(18)));
				ap.setPlace(rs.getString(19));
				
				apList.add(ap);
			}
			
			if (count > 0) {
				setReturnObject(apList);
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

		AppointmentModelDB appointmentModel = new AppointmentModelDB();
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		ArrayList<Object> rcv = (ArrayList<Object>) getReciveObject();
		appointmentModel = mapper.convertValue(rcv.get(0), appointmentModel.getClass());
		String userId = rcv.get(1).toString();

		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		try {
			int roleUser = authentication(userId);
			
			if (NURSE_ROLE_NUMBER != roleUser) {
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
				+ "	STATUS_ID = ?, "
				+ "	PLACE = ? "
				+ "WHERE "
				+ "	APPOINTMENT_ID = ?; ";
		String SQL_INSERT_DIAGNOSTIC_INFO = "INSERT INTO FHC_DIAGNOSTIC_RESULT "
				+ "	(APPOINTMENT_ID, DIAGNOSTIC_STATUS) "
				+ "VALUES(?,?); ";
		String SQL_SELECT_PLACE_DOCTOR = "SELECT P.PLACE  "
				+ "FROM (SELECT  "
				+ "		N'Phòng ' + FWR.ROOM + N', Tầng ' + FWF.[FLOOR] + N', Tòa nhà ' + FB.BUILDING_NAME + ', ' + FB.BUILDING_ASDRESS AS PLACE, "
				+ "		FWR.ID AS ROOM_ID, "
				+ "		FWF.ID AS FLOOR_ID, "
				+ "		FWF.BUILDING_ID AS BUILDING_ID "
				+ "	FROM  FHC_BUILDING FB  "
				+ "	LEFT JOIN FHC_WORK_FLOOR FWF ON  FB.ID = FWF.BUILDING_ID "
				+ "	LEFT JOIN FHC_WORK_ROOM FWR ON  FWF.ID = FWR.FLOOR_ID ) P  "
				+ "INNER JOIN FHC_STAFF FS ON (P.ROOM_ID = FS.ROOM_ID AND P.FLOOR_ID = FS.FLOOR_ID AND P.BUILDING_ID = FS.BUILDING_ID) "
				+ "WHERE FS.STAFF_ID = ?; ";
		try {
			
				ps = getSC().prepareStatement(SQL_SELECT_PLACE_DOCTOR);
				ps.setString(1, appointmentModel.getDoctorId());
				rs = ps.executeQuery();
				if (rs.next()) {
					appointmentModel.setPlace(rs.getString(1));
				}
				ps.close();
				rs.close();
				
				ps = getSC().prepareStatement(SQL_UPDATE_SATUS_CARD);
				ps.setString(1, appointmentModel.getStatus());
				ps.setNString(2, appointmentModel.getPlace());
				ps.setString(3, appointmentModel.getAppointmentBeforId());
				int count = ps.executeUpdate();
				
				ps.close();
				if (count > 0) {
					
					ps = getSC().prepareStatement(SQL_INSERT_DIAGNOSTIC_INFO);
					ps.setString(1, appointmentModel.getAppointmentBeforId());
					ps.setString(2, "8");
					count = ps.executeUpdate();
					ps.close();
				}
				
				appointmentModel.setStatus(getStatus(Integer.parseInt(appointmentModel.getStatus())));
				setReturnObject(appointmentModel);
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
}
