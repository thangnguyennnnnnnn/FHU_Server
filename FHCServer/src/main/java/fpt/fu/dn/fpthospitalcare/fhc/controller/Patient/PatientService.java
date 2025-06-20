package fpt.fu.dn.fpthospitalcare.fhc.controller.Patient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.AppointmentModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.AppointmentModelDB;
import fpt.fu.dn.fpthospitalcare.fhc.model.EditAppoinmentModel;

@Transactional
@Component
public class PatientService extends FHCCommonService {
	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
			case 1: 
				rs = addAppointment();
				break;
			case 2: 
				rs = getAppoinments();
				break;
			case 3:
				rs = editAppointment();
				break;
			case 4:
				rs = getAppointmentDetail();
				break;
			case 5:
				rs = deleteAppointment();
				break;	
			default:
				rs = 0;
				break;
		}
		return rs;
		
	}
	
	public int addAppointment() throws SQLException {
		AppointmentModel appointmentModel = new AppointmentModel();
		ObjectMapper mapper = new ObjectMapper();
		appointmentModel = mapper.convertValue(getReciveObject(), appointmentModel.getClass());
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_INSERT_APPOINTMENT = "INSERT INTO FHC_APPOINMENT "
				+ "	(APPOINMENT_ID,"
				+ "USER_ID,"
				+ "APPOINMENT_DATE,"
				+ "APPOINMENT_TIME,"
				+ "EMAIL,"
				+ "PHONE,"
				+ "SYMPTOM,"
				+ "CREATE_USER_ID,"
				+ "CREATE_DATETIME,"
				+ "UPDATE_USER_ID,"
				+ "UPDATE_DATETIME,"
				+ "ACTIVE,"
				+ "CONFIRMED ) "
				+ "	VALUES (?,?,?,?,?,?,?,?,?,?,?,?,'NOTYET') ";
		
		try {
			
			String currentDate = getCurrentDate("ddMMyyyyhhmmss");
			String appointmentId = "APID"+ currentDate;
			
			ps = getSC().prepareStatement(SQL_INSERT_APPOINTMENT);
			ps.setString(1, appointmentId);
			ps.setString(2, appointmentModel.getUserId());
			ps.setString(3, appointmentModel.getAppoinmentDate());
			ps.setString(4, appointmentModel.getAppoinmentTime());
			ps.setString(5, appointmentModel.getEmail());
			ps.setString(6, appointmentModel.getPhone());
			ps.setString(7, appointmentModel.getSymptom());
			ps.setString(8, appointmentModel.getUserId());
			ps.setString(9, currentDate);
			ps.setString(10, appointmentModel.getUserId());
			ps.setString(11, currentDate);
			ps.setString(12, appointmentModel.getActive());
			
			ps.executeUpdate();
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int getAppoinments() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		
		String userID = params.get("userID");
		
		String SQL_SELECT_APPOINTMENTS = "SELECT "
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
				+ "	FF.NAME  "
				+ "FROM  "
				+ "	FHC_APPOINMENT FA  "
				+ "	LEFT JOIN FHC_USER_INFORMATION FUI ON FA.DOCTOR_ID = FUI.USER_ID "
				+ "	LEFT JOIN FHC_FACULTY FF  ON FA.EXAMINATION_TYPE = FF.ID  "
				+ "WHERE FA.CREATE_USER = ? "
				+ "ORDER BY FA.APPOINMENT_ID  DESC;";
	
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_APPOINTMENTS);
			ps.setString(1, userID);
			rs = ps.executeQuery();
			
			List<AppointmentModelDB> appoinmentList = new ArrayList<>();
			int count = 0;
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
				appoinmentList.add(ap);
			}
			
			if (count > 0) {
				setReturnObject(appoinmentList);
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
	
	public int getAppointmentDetail() throws SQLException {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		String appointmentId = (String) getReciveObject();
		
		String SQL_SELECT_APPOINTMENTS;
		SQL_SELECT_APPOINTMENTS = "";
		SQL_SELECT_APPOINTMENTS += " SELECT ";
		SQL_SELECT_APPOINTMENTS += " APPOINMENT_ID, ";
		SQL_SELECT_APPOINTMENTS += " FIRST_NAME + ' '+MIDDLE_NAME+' '+LAST_NAME as FULL_NAME, ";
		SQL_SELECT_APPOINTMENTS += " APPOINMENT_DATE, ";
		SQL_SELECT_APPOINTMENTS += " APPOINMENT_TIME, ";
		SQL_SELECT_APPOINTMENTS += " FA.EMAIL, ";
		SQL_SELECT_APPOINTMENTS += " PHONE, ";
		SQL_SELECT_APPOINTMENTS += " SYMPTOM, ";
		SQL_SELECT_APPOINTMENTS += " ACTIVE ";
		SQL_SELECT_APPOINTMENTS += " FROM FHC_APPOINMENT FA INNER JOIN USER_INFORMATION UI ";
		SQL_SELECT_APPOINTMENTS += " ON FA.USER_ID = UI.USER_ID ";
		SQL_SELECT_APPOINTMENTS += " WHERE APPOINMENT_ID=? ";
	
		try {
			ps = getSC().prepareStatement(SQL_SELECT_APPOINTMENTS);
			ps.setString(1, appointmentId);
			rs = ps.executeQuery();
			
			AppointmentModel aim = new AppointmentModel();
			
			String fullName="";
			
			int dataCount = 0;
			while(rs.next()) {
				fullName = rs.getString("FULL_NAME");
				aim.setAppoinmentDate(ds.formatDateYYMMDD(rs.getString("APPOINMENT_DATE")));
				aim.setAppoinmentTime(ds.formatDateHHMMSS(rs.getString("APPOINMENT_TIME")));
				aim.setEmail(rs.getString("EMAIL"));
				aim.setPhone(rs.getString("PHONE"));
				aim.setSymptom(rs.getString("SYMPTOM"));
				aim.setActive(rs.getString("ACTIVE"));
				
				dataCount++;
			}
			
			if (dataCount > 0) {
				HashMap<String, Object> returnObject = new HashMap<String, Object>();
				
				returnObject.put("fullName", fullName);
				returnObject.put("appoinmentId", appointmentId);
				returnObject.put("appointmentDetail", aim);
				
				setReturnObject(returnObject);
			} else {
				setResultNo(ConstantVariable.DB_NOTFOUND);
				setReturnObject(null);
				
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
			
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
	
	public int editAppointment() throws SQLException {
		EditAppoinmentModel appointmentModel = new EditAppoinmentModel();
		ObjectMapper mapper = new ObjectMapper();
		appointmentModel = mapper.convertValue(getReciveObject(), appointmentModel.getClass());
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_UPDATE_APPOINMENT;
		SQL_UPDATE_APPOINMENT = "";
		SQL_UPDATE_APPOINMENT += " UPDATE FHC_APPOINMENT SET "
				+ " APPOINMENT_DATE=?, "
				+ " APPOINMENT_TIME=?, "
				+ " EMAIL=?, "
				+ " PHONE=?, "
				+ " SYMPTOM=?, "
				+ " UPDATE_USER_ID=?, "
				+ " UPDATE_DATETIME=?, "
				+ " ACTIVE=? "
				+ " WHERE APPOINMENT_ID=? ";
		
		try {
			
			String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			
			ps = getSC().prepareStatement(SQL_UPDATE_APPOINMENT);

			ps.setString(1, appointmentModel.getAppoinmentDate());
			ps.setString(2, appointmentModel.getAppoinmentTime());
			ps.setString(3, appointmentModel.getEmail());
			ps.setString(4, appointmentModel.getPhone());
			ps.setString(5, appointmentModel.getSymptom());
			ps.setString(6, appointmentModel.getUserId());
			ps.setString(7, currentDate);
			ps.setString(8, appointmentModel.getActive());
			ps.setString(9, appointmentModel.getAppoinmentId());
			
			ps.executeUpdate();
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int deleteAppointment() throws SQLException {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		String appointmentId = (String) getReciveObject();
		
		String SQL_DELETE_APPOINTMENT;
		SQL_DELETE_APPOINTMENT = "DELETE FROM FHC_APPOINMENT WHERE APPOINMENT_ID = ? ";
	
		try {
			ps = getSC().prepareStatement(SQL_DELETE_APPOINTMENT);
			ps.setString(1, appointmentId);
			ps.executeUpdate();
			 
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
}
