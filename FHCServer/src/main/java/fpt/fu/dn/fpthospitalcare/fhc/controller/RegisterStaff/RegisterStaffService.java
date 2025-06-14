package fpt.fu.dn.fpthospitalcare.fhc.controller.RegisterStaff;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.StaffModel;


public class RegisterStaffService extends FHCCommonService {
	
	private static final int ADMIN_ROLE_NUMBER = 1;
	
	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
		case 1:
			rs = registerStaff();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	public int registerStaff() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		StaffModel staff = new StaffModel();
		ObjectMapper mapper = new ObjectMapper();
		staff = mapper.convertValue(getReciveObject(), staff.getClass());
		
		try {
			int roleUser = authentication(staff.getCreateUserId());
			
			if (ADMIN_ROLE_NUMBER != roleUser) {
				executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		String SQL_UPDATE_ROLE_USER = "UPDATE FHC_USER_ROLE "
				+ "SET ROLE_ID = ? "
				+ "WHERE USER_ID=?; ";
		
		String SQL_INSERT_STAFF_INFO = "INSERT INTO FHC_STAFF "
				+ "	(STAFF_ID, "
				+ "	FACULTY_ID, "
				+ "	IS_ACTIVE, "
				+ "	POSITION_ID, "
				+ "	BUILDING_ID, "
				+ "	FLOOR_ID, "
				+ "	ROOM_ID,"
				+ " CREATE_USER,"
				+ " CREATE_AT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?); ";
		
		try {
			String currentTime = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			
			ps = getSC().prepareStatement(SQL_UPDATE_ROLE_USER);
			ps.setString(1, staff.getRoleId());
			ps.setString(2, staff.getStaffId());
			int count = ps.executeUpdate();
			ps.close();
			if (count <= 0) {
				executeNo = ConstantVariable.DB_NOTFOUND;
				return executeNo;
			}
			
			ps = getSC().prepareStatement(SQL_INSERT_STAFF_INFO);
			ps.setString(1, staff.getStaffId());
			ps.setString(2, staff.getFacultyId());
			ps.setString(3, "N");
			ps.setString(4, staff.getPositionId());
			ps.setString(5, staff.getBuildingId());
			ps.setString(6, staff.getFloorId());
			ps.setString(7, staff.getRoomId());
			ps.setString(8, staff.getCreateUserId());
			ps.setString(9, currentTime); 
			
			count = ps.executeUpdate();
			
			if (count <= 0) {
				executeNo = ConstantVariable.ERROR_NUMBER;
				return executeNo;
			}
			
			setReturnObject(staff);
			
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
		
	}
}
