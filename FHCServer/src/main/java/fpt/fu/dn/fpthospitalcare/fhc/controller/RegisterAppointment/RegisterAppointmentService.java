package fpt.fu.dn.fpthospitalcare.fhc.controller.RegisterAppointment;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.Faculty;
import fpt.fu.dn.fpthospitalcare.fhc.controller.RegisterAppointment.Model.DoctorDisplayModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.AppointmentModelDB;

public class RegisterAppointmentService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
			case 1: 
				rs = registerAppointment();
				break;
			case 2: 
				rs = getDoctors();
				break;
			case 3: 
				rs = getDoctorInfo();
				break;
			case 4: 
				rs = getAppointment();
				break;
			case 5: 
				rs = getAppointmentCard();
				break;
			case 6: 
				rs = deleteAppoinment();
				break;
			default:
				rs = 0;
				break;
		}
		return rs;
	}
	
//	/**
//	 * Đăng ký lịch khám
//	 * 
//	 * @return executeNo 
//	 * @throws SQLException
//	 */
//	public int registerAppointment() throws SQLException {
//		AppointmentModel appointmentModel = new AppointmentModel();
//		ObjectMapper mapper = new ObjectMapper();
//		appointmentModel = mapper.convertValue(getReciveObject(), appointmentModel.getClass());
//		
//		int executeNo = ConstantVariable.SUCCESS_NUMBER;
//		
//		String SQL_INSERT_APPOINTMENT = "INSERT INTO FHC_APPOINMENT "
//				+ "	(APPOINMENT_ID,"
//				+ "USER_ID,"
//				+ "APPOINMENT_DATE,"
//				+ "APPOINMENT_TIME,"
//				+ "EMAIL,"
//				+ "PHONE,"
//				+ "SYMPTOM,"
//				+ "CREATE_USER_ID,"
//				+ "CREATE_DATETIME,"
//				+ "UPDATE_USER_ID,"
//				+ "UPDATE_DATETIME,"
//				+ "ACTIVE,"
//				+ "CONFIRMED ) "
//				+ "	VALUES (?,?,?,?,?,?,?,?,?,?,?,?,'NOTYET') ";
//		
//		try {
//			
//			String currentDate = getCurrentDate();
//			String appointmentId = "APID"+ currentDate;
//			
//			ps = getSC().prepareStatement(SQL_INSERT_APPOINTMENT);
//			ps.setString(1, appointmentId);
//			ps.setString(2, appointmentModel.getUserId());
//			ps.setString(3, appointmentModel.getAppoinmentDate());
//			ps.setString(4, appointmentModel.getAppoinmentTime());
//			ps.setString(5, appointmentModel.getEmail());
//			ps.setString(6, appointmentModel.getPhone());
//			ps.setString(7, appointmentModel.getSymptom());
//			ps.setString(8, appointmentModel.getUserId());
//			ps.setString(9, currentDate);
//			ps.setString(10, appointmentModel.getUserId());
//			ps.setString(11, currentDate);
//			ps.setString(12, appointmentModel.getActive());
//			
//			ps.executeUpdate();
//		}
//		catch (Exception e) {
//			executeNo = ConstantVariable.ERROR_NUMBER;
//			throw e;
//		}
//		
//		return executeNo;
//    }
	
	/**
	 * Lấy danh sách bác sĩ tương ứng
	 * @return
	 * @throws SQLException
	 */
	public int getDoctors() throws SQLException {
		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		
		String medicalExaminationTime = params.get("medicalExaminationTime");
		String medicalExaminationDay = params.get("medicalExaminationDay");
		String specializationId = params.get("specializationId");
		String buildingId = params.get("buildingId");
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_DOCTORS = "SELECT  "
				+ "	FUI.USER_ID,  "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME AS FULLNAME,  "
				+ "	FUI.PHONE_NUMBER,  "
				+ "	FUI.EMAIL,  "
				+ "	FUR.ROLE_ID "
				+ "FROM  "
				+ "	( "
				+ "	SELECT  "
				+ "		FS.STAFF_ID, "
				+ "		FS.IS_ACTIVE "
				+ "	FROM  "
				+ "		FHC_STAFF FS "
				+ "	WHERE  "
				+ "		FS.STAFF_ID NOT IN ( "
				+ "		SELECT  "
				+ "			FA.DOCTOR_ID "
				+ "		FROM  "
				+ "			FHC_APPOINMENT FA "
				+ "		WHERE  "
				+ "			FA.EXAMINATION_DATE = ? "
				+ "			AND FA.EXAMINATION_TIME = ?) "
				+ "		AND FS.STAFF_ID NOT IN ( "
				+ "			SELECT FWT.STAFF_ID  "
				+ "			FROM FHC_WORKIING_TIME FWT "
				+ "			WHERE FWT.OFF_DATE = ? AND FWT.APPROVED = 1 "
				+ "		) "
				+ "		AND FS.FACULTY_ID = ? "
				+ "		AND FS.BUILDING_ID = ?) DOCTOR "
				+ "INNER JOIN FHC_USER_INFORMATION FUI  "
				+ "ON  "
				+ "	FUI.USER_ID = DOCTOR.STAFF_ID "
				+ "INNER JOIN FHC_USER_ROLE FUR ON  "
				+ "	FUR.USER_ID = DOCTOR.STAFF_ID "
				+ "WHERE  "
				+ "	FUR.ROLE_ID = 2 "
				+ "	AND DOCTOR.IS_ACTIVE = 'Y';";
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_DOCTORS);
			ps.setString(1, medicalExaminationDay);
			ps.setString(2, medicalExaminationTime);
			ps.setString(3, medicalExaminationDay);
			ps.setString(4, specializationId);
			ps.setString(5, buildingId);
			
			rs = ps.executeQuery();
			int count = 0;
			List<DoctorDisplayModel> listDDM = new ArrayList<>();
			while(rs.next()) {
				count++;
				DoctorDisplayModel ddm = new DoctorDisplayModel();
				ddm.setUserId(rs.getString(1));
				ddm.setFullname(rs.getString(2));
				ddm.setPhone(rs.getString(3));
				ddm.setEmail(rs.getString(4));
				ddm.setRoleId(rs.getString(5));
				
				listDDM.add(ddm);
			}
			
			if (count > 0) {
				setReturnObject(listDDM);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}

		return executeNo;
	}
	
	/**
	 * Lấy thông tin bác sĩ tương ứng
	 * @return
	 * @throws Exception 
	 */
	public int getDoctorInfo() throws Exception {
		
		AppointmentModelDB appointmentModel = new AppointmentModelDB();
		ObjectMapper mapper = new ObjectMapper();
		appointmentModel = mapper.convertValue(getReciveObject(), appointmentModel.getClass());
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		String SQL_DELETE_APPOINTMENT = "DELETE "
				+ "FROM "
				+ "	FHC_APPOINMENT "
				+ "WHERE "
				+ "	CREATE_USER = ? "
				+ "	AND APPOINMENT_ID = ?; ";
		
		String SQL_SELECT_DOCTOR = "SELECT "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME AS FULLNAME, "
				+ "	FUI.BIRTHDATE, "
				+ "	FUI.PHONE_NUMBER, "
				+ "	FUI.EMAIL, "
				+ "	FS.FACULTY_ID, "
				+ "	FF.NAME, "
				+ "	FF.DESCRIPTION, "
				+ "	FUI.USER_ID, "
				+ " FUI.URL_AVATA "
				+ "FROM "
				+ "	FHC_USER_INFORMATION FUI "
				+ "INNER JOIN FHC_STAFF FS ON "
				+ "	FUI.USER_ID = FS.STAFF_ID "
				+ "INNER JOIN FHC_FACULTY FF ON "
				+ "	FS.FACULTY_ID = FF.ID "
				+ "WHERE "
				+ "	FUI.USER_ID = ?;";
		
		try {
			
			ps = getSC().prepareStatement(SQL_DELETE_APPOINTMENT);
			ps.setString(1, appointmentModel.getUserId());
			ps.setString(2, appointmentModel.getAppointmentBeforId());
			
			ps.executeUpdate();
			ps.close();
			
			String insertAppointmentSql = createinsertFHCAppointmentQuery();
			String currentTime = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			String id = "AP" + getCurrentDate("ddMMyyyyHHmmss");
			
			ps = getSC().prepareStatement(insertAppointmentSql);
			ps.setString(1, id);
			ps.setString(2, appointmentModel.getFullname());
			ps.setString(3, appointmentModel.getPhone());
			ps.setString(4, appointmentModel.getAddress());
			ps.setString(5, appointmentModel.getFullnameNT());
			ps.setString(6, appointmentModel.getPhoneNT());
			ps.setString(7, appointmentModel.getAddressNT());
			ps.setString(8, appointmentModel.getFacultyId());
			ps.setString(9, appointmentModel.getExaminationDate());
			ps.setString(10, appointmentModel.getExaminationTime());
			ps.setString(11, appointmentModel.getDoctorId());
			ps.setString(12, appointmentModel.getUserId());
			ps.setString(13, currentTime);
			ps.setString(14, appointmentModel.getSymptom());
			ps.setString(15, "N");
			
			try {
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e){
				ps.close();
				if (e.getErrorCode() == ConstantVariable.DUPLICATE_KEY) {
					return ConstantVariable.DUPLICATE_KEY;
				} else {
					executeNo = ConstantVariable.ERROR_NUMBER;
					throw e;
				}
			}
			
			ps = getSC().prepareStatement(SQL_SELECT_DOCTOR);
			ps.setString(1, appointmentModel.getDoctorId());
			
			rs = ps.executeQuery();
			DoctorDisplayModel ddm = new DoctorDisplayModel();
			if (rs.next()) {
				ddm.setFullname(rs.getString(1));
				ddm.setBirthdate(rs.getString(2));
				ddm.setPhone(rs.getString(3));
				ddm.setEmail(rs.getString(4));
				ddm.setUserId(rs.getString(8));
				ddm.setUrlAvata(ConstantVariable.STORAGE_CLOUD + rs.getString(9));
				ddm.setAppointmentId(id);

				List<Faculty> facultys = new ArrayList<>();
				Faculty f = new Faculty();
				f.setId(rs.getString(5));
				f.setName(rs.getString(6));
				f.setDescription(rs.getString(7));
				facultys.add(f);
				
				while(rs.next()) {
					f = new Faculty();
					f.setId(rs.getString(5));
					f.setName(rs.getString(6));
					f.setDescription(rs.getString(7));
					facultys.add(f);
				}
				
				ddm.setFacultys(facultys);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
			
			setReturnObject(ddm);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}

		return executeNo;
	}
	
	private int registerAppointment() throws Exception {
		AppointmentModelDB appointmentModel = new AppointmentModelDB();
		ObjectMapper mapper = new ObjectMapper();
		appointmentModel = mapper.convertValue(getReciveObject(), appointmentModel.getClass());
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_INSERT = "UPDATE "
				+ "	FHC_APPOINMENT "
				+ "SET "
				+ "	APPOINMENT_ID = ?, "
				+ "	FULLNAME = ?, "
				+ "	PHONE = ?, "
				+ "	ADDRESS = ?, "
				+ "	FULLNAME_RELATIVE = ?, "
				+ "	PHONE_RELATIVE = ?, "
				+ "	ADDRESS_RELATIVE = ?, "
				+ "	CREATE_USER = ?, "
				+ "	CREATE_TIME = ?, "
				+ "	SYMPTOM = ?, "
				+ "	CONFIRM = ? "
				+ "WHERE "
				+ "	DOCTOR_ID = ? "
				+ "	AND EXAMINATION_TIME = ? "
				+ "	AND EXAMINATION_DATE = ? "
				+ "	AND EXAMINATION_TYPE = ?; ";
		
		String SQL_INSERT_AP_CARD = "INSERT INTO FHC_APPOINTMENT_CARD "
				+ "	(APPOINTMENT_ID, "
				+ "	NANE, "
				+ "	PHONE, "
				+ "	PHONE_2, "
				+ "	ADDRESS, "
				+ "	FACULTY_ID, "
				+ "	APPMOINTMENT_DATE, "
				+ "	APPOINTMENT_TIME, "
				+ "	DOCTOR_ID, "
				+ "	DOCTOR_NAME, "
				+ "	STATUS_ID, "
				+ "	CREATE_USER, "
				+ "	CREATE_DATE) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		String SQL_CREATE_ROOM_SOCKET = "INSERT INTO ROOM_SOCKET "
				+ "	(ROOM_ID, "
				+ "	USER_ID, "
				+ "	CREATE_AT) "
				+ "VALUES(?,?,?);";
		try {
			
			String currentTime = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			String id = "AP" + getCurrentDate("ddMMyyyyHHmmss");
			appointmentModel.setAppointmentBeforId(id);
			
			ps = getSC().prepareStatement(SQL_INSERT);
			ps.setString(1, appointmentModel.getAppointmentBeforId());
			ps.setString(2, appointmentModel.getFullname());
			ps.setString(3, appointmentModel.getPhone());
			ps.setString(4, appointmentModel.getAddress());
			ps.setString(5, appointmentModel.getFullnameNT());
			ps.setString(6, appointmentModel.getPhoneNT());
			ps.setString(7, appointmentModel.getAddressNT());
			ps.setString(8, appointmentModel.getUserId());
			ps.setString(9, currentTime);
			ps.setString(10, appointmentModel.getSymptom());
			ps.setString(11, "Y");
			ps.setString(15, appointmentModel.getFacultyId());
			ps.setString(14, appointmentModel.getExaminationDate());
			ps.setString(13, appointmentModel.getExaminationTime());
			ps.setString(12, appointmentModel.getDoctorId());
			
			int count = ps.executeUpdate();
			
			ps.close();
			
			if (count == 0) {
				executeNo = ConstantVariable.ERROR_UPDATE;
				return executeNo;
			}
			
			ps = getSC().prepareStatement(SQL_INSERT_AP_CARD);
			ps.setString(1, appointmentModel.getAppointmentBeforId());
			ps.setString(2, appointmentModel.getFullname());
			ps.setString(3, appointmentModel.getPhone());
			ps.setString(4, appointmentModel.getPhoneNT());
			ps.setString(5, appointmentModel.getAddress());
			ps.setString(6, appointmentModel.getFacultyId());
			ps.setString(7, appointmentModel.getExaminationDate());
			ps.setString(8, appointmentModel.getExaminationTime());
			ps.setString(9, appointmentModel.getDoctorId());
			ps.setString(10, appointmentModel.getDoctorName());
			ps.setInt(11, 0);
			ps.setString(12, appointmentModel.getUserId());
			ps.setString(13, currentTime);

			count = ps.executeUpdate();
			ps.close();
			if (count == 0) {
				executeNo = ConstantVariable.ERROR_UPDATE;
				return executeNo;
			}
			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String diagnosisTime = currentDateTime.format(formatter);
			
			List<String> users = new ArrayList<>();
			users.add(appointmentModel.getDoctorId());
			users.add(appointmentModel.getUserId());
			
			for (String u : users) {
				ps = getSC().prepareStatement(SQL_CREATE_ROOM_SOCKET);
				ps.setString(1,appointmentModel.getAppointmentBeforId());
				ps.setString(2,u);
				ps.setString(3,diagnosisTime);
				
				count = ps.executeUpdate();
				ps.close();
			}
			
			appointmentModel.setStatus(getStatus(0));
			setReturnObject(appointmentModel);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		return executeNo;
	}
	
	
	private String createinsertFHCAppointmentQuery() {
		String SQL_INSERT = "INSERT INTO FHC_APPOINMENT "
				+ "	(APPOINMENT_ID, "
				+ "	FULLNAME, "
				+ "	PHONE, "
				+ "	ADDRESS, "
				+ "	FULLNAME_RELATIVE, "
				+ "	PHONE_RELATIVE, "
				+ "	ADDRESS_RELATIVE, "
				+ "	EXAMINATION_TYPE, "
				+ "	EXAMINATION_DATE, "
				+ "	EXAMINATION_TIME, "
				+ "	DOCTOR_ID, "
				+ "	CREATE_USER, "
				+ "	CREATE_TIME, "
				+ "	SYMPTOM, "
				+ "	CONFIRM) "
				+ "VALUES( "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?, "
				+ "	?); ";
		return SQL_INSERT;
	}
	
	/**
	 * Lấy thông tin lịch khám
	 * @return
	 * @throws Exception 
	 */
	public int getAppointment() throws Exception {
		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		
		String userID = params.get("userID");
		String appoinmentId = params.get("appoinmentId");
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_AP = "SELECT "
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
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME AS FULLNAME,  "
				+ "	FF.NAME, "
				+ " FAC.STATUS_ID, "
				+ "	FAC.PLACE "
				+ "FROM  "
				+ "	FHC_APPOINMENT FA  "
				+ "	LEFT JOIN FHC_USER_INFORMATION FUI ON FA.DOCTOR_ID = FUI.USER_ID "
				+ "	LEFT JOIN FHC_FACULTY FF  ON FA.EXAMINATION_TYPE = FF.ID "
				+ " LEFT JOIN FHC_APPOINTMENT_CARD FAC ON FA.APPOINMENT_ID = FAC.APPOINTMENT_ID "
				+ "WHERE "
				+ "	FA.APPOINMENT_ID = ? "
				+ "	AND CONFIRM = 'Y' ";
		
			if (!userID.equals("")) {
				SQL_SELECT_AP += " AND FA.CREATE_USER = ? ;";
			}	
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_AP);
			ps.setString(1, appoinmentId);
			
			if (!userID.equals("")) {
				ps.setString(2, userID);
			}
			
			rs = ps.executeQuery();
			int count = 0;
			AppointmentModelDB ap = new AppointmentModelDB();
			while(rs.next()) {
				count++;
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
				ap.setStatus(getStatus(Integer.parseInt(rs.getString(18))));
				ap.setPlace(rs.getNString(19));
			}
			
			if (count > 0) {
				setReturnObject(ap);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}

		return executeNo;
	}
	
	/**
	 * Lấy thông tin thẻ khám
	 * @return
	 * @throws Exception 
	 */
	public int getAppointmentCard() throws Exception {
		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		
		String userID = params.get("userID");
		String appoinmentId = params.get("appoinmentId");
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_AP = "SELECT "
				+ "	APPOINTMENT_ID, "
				+ "	NANE, "
				+ "	PHONE, "
				+ "	PHONE_2, "
				+ "	ADDRESS, "
				+ "	FACULTY_ID, "
				+ "	APPMOINTMENT_DATE, "
				+ "	APPOINTMENT_TIME, "
				+ "	DOCTOR_ID, "
				+ "	DOCTOR_NAME, "
				+ "	STATUS_ID, "
				+ "	CREATE_USER, "
				+ "	CREATE_DATE, "
				+ "	PLACE "
				+ "FROM "
				+ "	FHC.dbo.FHC_APPOINTMENT_CARD "
				+ "WHERE "
				+ "	CREATE_USER = ? ";
		
		try {
			
			if (appoinmentId != null) {
				SQL_SELECT_AP = SQL_SELECT_AP + "AND APPOINTMENT_ID = ?;";
			}
			ps = getSC().prepareStatement(SQL_SELECT_AP);
			ps.setString(1, userID);
			
			if (appoinmentId != null) {
				ps.setString(2, appoinmentId);
			}
			
			rs = ps.executeQuery();
			int count = 0;
			
			List<AppointmentModelDB> apList = new ArrayList<>();
			while(rs.next()) {
				count++;
				AppointmentModelDB ap = new AppointmentModelDB();
				ap.setAppointmentBeforId(rs.getString(1));
				ap.setFullname(rs.getString(2));
				ap.setPhone(rs.getString(3));
				ap.setPhoneNT(rs.getString(4));
				ap.setAddress(rs.getString(5));
				ap.setFacultyId(rs.getString(6));
				ap.setFacultyName(getFacultyName(rs.getInt(6)).getName());
				ap.setExaminationDate(rs.getString(7));
				ap.setExaminationTime(rs.getString(8));
				ap.setDoctorId(rs.getString(9));
				ap.setDoctorName(rs.getString(10));
				ap.setStatus(getStatus(rs.getInt(11)));
				ap.setUserId(rs.getString(12));
				ap.setPlace(rs.getString(13));
				
				apList.add(ap);
			}
			
			if (count > 0) {
				setReturnObject(apList);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}

		return executeNo;
	}
	
	public int deleteAppoinment() throws Exception {
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject(); 
		String appoinmentId = params.get("apoinmentId");
		String userId = params.get("userId");
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_DELETE_AP_BY_SYSTEM = "DELETE FROM "
				+ "	FHC_APPOINMENT "
				+ "WHERE "
				+ "	APPOINMENT_ID = ? ";
		
		String SQL_DELETE_AP_WITH_USER = "DELETE FROM "
				+ "	FHC_APPOINMENT "
				+ "WHERE "
				+ "	APPOINMENT_ID = ? AND CREATE_USER = ?";
		
		String SQL_DELETE_AP_CARD_WITH_USER = "DELETE FROM "
				+ "	FHC_APPOINTMENT_CARD "
				+ "WHERE "
				+ "	APPOINTMENT_ID = ?";
		
		try {
			if (userId.equals("")) {
				ps = getSC().prepareStatement(SQL_DELETE_AP_BY_SYSTEM);
				ps.setString(1, appoinmentId);
			} else {
				ps = getSC().prepareStatement(SQL_DELETE_AP_WITH_USER);
				ps.setString(1, appoinmentId);
				ps.setString(2, userId);
			}
			
			int count =  ps.executeUpdate();
			ps.close();
			if (count > 0) {
				ps = getSC().prepareStatement(SQL_DELETE_AP_CARD_WITH_USER);
				ps.setString(1, appoinmentId);
				ps.executeUpdate();
			}
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}

		return executeNo;
	}
}
