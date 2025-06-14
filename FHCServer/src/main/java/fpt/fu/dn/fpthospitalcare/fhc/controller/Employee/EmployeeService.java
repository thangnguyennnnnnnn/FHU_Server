package fpt.fu.dn.fpthospitalcare.fhc.controller.Employee;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Employee.Model.WorkingTimeModel;

public class EmployeeService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
		case 1:
			rs = searchEmployee();
			break;
		case 2:
			rs = registerWorkingTime();
			break;
		case 3:
			rs = getWorkingTime();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	public int searchEmployee() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		//String userId = (String) param.get("userId");
		String staffId = (String) param.get("staffId");
		String staffName = (String) param.get("staffName");
		String faculty = (String) param.get("faculty");
		
		String SQL_SELECT_STAFF = "SELECT "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME FULLNAME, "
				+ "	FPR.POSITION_NAME + ': ' + FF.NAME POSITION_SUM, "
				+ "	FS.CREATE_AT, "
				+ "	FUI.URL_AVATA, "
				+ "	FUI.PHONE_NUMBER, "
				+ "	FUI.EMAIL, "
				+ "	N'Phòng ' + FWR.ROOM + N', Tầng ' + FWF.[FLOOR] + N', Tòa nhà ' + FB.BUILDING_NAME + ', ' + FB.BUILDING_ASDRESS  PLACE "
				+ "FROM "
				+ "	FHC_STAFF FS "
				+ "LEFT JOIN FHC_USER_INFORMATION FUI ON "
				+ "	FS.STAFF_ID = FUI.USER_ID "
				+ "LEFT JOIN FHC_POSITION_ROLE FPR ON "
				+ "	FS.POSITION_ID = FPR.ID "
				+ "LEFT JOIN FHC_FACULTY FF ON "
				+ "	FS.FACULTY_ID = FF.ID "
				+ "LEFT JOIN FHC_BUILDING FB ON "
				+ "	FS.BUILDING_ID = FB.ID  "
				+ "LEFT JOIN FHC_WORK_FLOOR FWF ON "
				+ "	FS.FLOOR_ID = FWF.ID  "
				+ "LEFT JOIN FHC_WORK_ROOM FWR ON "
				+ "	FS.ROOM_ID = FWR.ID ";
		StringBuilder sql = new StringBuilder(SQL_SELECT_STAFF);
		
		if (staffId.equals("") && staffName.equals("")
				&& faculty.equals("") ) {
			//executeNo = ConstantVariable.INPUT_EMPTY_VALUE;
			//return executeNo;
		} else {
			sql.append("WHERE ");
		}
		
		boolean isAppend = false;
		
		if (!staffId.equals("")) {
			if (!isAppend) {
				sql.append("FS.STAFF_ID = '" + staffId + "' ");
			} else {
				sql.append("AND FS.STAFF_ID = '" + staffId + "' ");
			}
			isAppend = true;
		}
		
		if (!staffName.equals("")) {
			if (!isAppend) {
				sql.append("FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME LIKE N'%" + staffName + "%' ");
			} else {
				sql.append("AND FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME LIKE N'%" + staffName + "%' ");
			}
			isAppend = true;
		}
		
		if (!faculty.equals("")) {
			if (!isAppend) {
				sql.append("FPR.POSITION_NAME + ': ' + FF.NAME LIKE N'%" + faculty + "%' ");
			} else {
				sql.append("AND FPR.POSITION_NAME + ': ' + FF.NAME LIKE N'%" + faculty + "%' ");
			}
			isAppend = true;
		}
		
		sql.append(" ;");
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			List<EmployeeSearchModel> employeeSearchList = new ArrayList<>();
			while(rs.next()) {
				EmployeeSearchModel EmployeeSearchModel = new EmployeeSearchModel();
				EmployeeSearchModel.setFullname(rs.getString(1).replaceAll("  ", " "));
				EmployeeSearchModel.setPosition(rs.getString(2));
				EmployeeSearchModel.setCreateAt(rs.getString(3));
				EmployeeSearchModel.setAvt(ConstantVariable.STORAGE_CLOUD + rs.getString(4));
				EmployeeSearchModel.setPhone(rs.getString(5));
				EmployeeSearchModel.setEmail(rs.getString(6));
				EmployeeSearchModel.setPlace(rs.getString(7));
				employeeSearchList.add(EmployeeSearchModel);
			}
			
			if (employeeSearchList.size() > 0) {
				setReturnObject(employeeSearchList);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		return executeNo;
	}
	
	public int registerWorkingTime() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		String userId = (String) param.get("userId");
		String onDate = (String) param.get("onDate");
		String reason = (String) param.get("reason");
		
		if (onDate.equals("") || reason.equals("")) {
			executeNo = ConstantVariable.INPUT_EMPTY_VALUE;
			return executeNo;
		}
		
		String SQL_SELECT_AP = "SELECT "
				+ "	APPOINMENT_ID, "
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
				+ "	CONFIRM "
				+ "FROM "
				+ "	FHC_APPOINMENT "
				+ "WHERE "
				+ "	DOCTOR_ID = ? "
				+ "	AND EXAMINATION_DATE = ? AND CONFIRM = 'Y' ; ";
		
		String SQL_INSERT_WORKING_DATE = "INSERT INTO FHC_WORKIING_TIME "
				+ "	(STAFF_ID, "
				+ "	OFF_DATE, "
				+ "	APPROVER_ID, "
				+ "	APPROVE_TIME, "
				+ "	REASON, "
				+ "	CREATE_TIME, APPROVED) "
				+ "VALUES(?,?,?,?,?,?,2); ";
		
		StringBuilder sql = new StringBuilder(SQL_SELECT_AP);
		
		sql.append(" ;");
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(sql.toString());
			ps.setString(1, userId);
			ps.setString(2, onDate);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				executeNo = ConstantVariable.HAVE_APPOINTMENT;
				return executeNo;
			}
			
			ps.close();
			rs.close();
			
			ps = getSC().prepareStatement(SQL_INSERT_WORKING_DATE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, userId);
			ps.setString(2, onDate);
			ps.setString(3, "");
			ps.setString(4, "");
			ps.setString(5, reason);
			ps.setString(6, getCurrentDate("dd/MM/yyyy HH:mm:ss"));
			int count = ps.executeUpdate();
			
			if (count > 0) {
				rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                int generatedId = rs.getInt(1);
	                setReturnObject(generatedId);
	            }
			}
			
		} catch (SQLException e) {
			if (e.getErrorCode() == ConstantVariable.DUPLICATE_KEY) {
				executeNo = ConstantVariable.DUPLICATE_KEY;
			} else {
				executeNo = ConstantVariable.ERROR_NUMBER;
				throw e;		
			}
		}
		return executeNo;
	}
	
	public int getWorkingTime() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		//String userId = (String) param.get("userId");
		String id = (String) param.get("id");
		String userId = (String) param.get("userId");
		String date = (String) param.get("date");
		
		String SQL_SELECT_WORKING = "SELECT "
				+ "	ID, "
				+ "	STAFF_ID, "
				+ "	OFF_DATE, "
				+ "	APPROVER_ID, "
				+ "	APPROVE_TIME, "
				+ "	REASON, "
				+ "	CREATE_TIME "
				+ "FROM "
				+ "	FHC_WORKIING_TIME "
				+ "WHERE ";
		StringBuilder sql = new StringBuilder(SQL_SELECT_WORKING);
		
		boolean isAppend = false;
		
		if (!id.equals("")) {
			if (!isAppend) {
				sql.append("ID = '" + id + "' ");
			} else {
				sql.append("AND ID = '" + id + "' ");
			}
			isAppend = true;
		}
		
		if (authentication(userId) != 5) {
			if (!isAppend) {
				sql.append("STAFF_ID = '" + userId + "' ");
			} else {
				sql.append("AND STAFF_ID = '" + userId + "' ");
			}
			isAppend = true;
		}
		
		if (!date.equals("")) {
			if (!isAppend) {
				sql.append("OFF_DATE = '" + date + "' ");
			} else {
				sql.append("AND OFF_DATE = '" + date + "' ");
			}
			isAppend = true;
		}
		
		sql.append(" ;");
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			List<WorkingTimeModel> workingTimeList = new ArrayList<>();
			while(rs.next()) {
				WorkingTimeModel workingTimeModel = new WorkingTimeModel();
				workingTimeModel.setId(rs.getString(1));
				workingTimeModel.setName(getFullname(rs.getString(2)));
				workingTimeModel.setStaffId(rs.getString(2));
				workingTimeModel.setOffDate(rs.getString(3));
				workingTimeModel.setAprroverId(rs.getString(4));
				workingTimeModel.setAprroveTime(rs.getString(5));
				workingTimeModel.setReason(rs.getString(6));
				workingTimeModel.setCreateTime(rs.getString(7));
				workingTimeList.add(workingTimeModel);
			}
			
			if (workingTimeList.size() > 0) {
				setReturnObject(workingTimeList);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		return executeNo;
	}
}
