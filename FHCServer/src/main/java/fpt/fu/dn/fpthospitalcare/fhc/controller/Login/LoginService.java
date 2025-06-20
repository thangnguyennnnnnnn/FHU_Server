package fpt.fu.dn.fpthospitalcare.fhc.controller.Login;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.UserLoginModel;

@Component
public class LoginService extends FHCCommonService {
	
	@Override
	public int run(int workProgram) throws Exception {
		int rs = ConstantVariable.SUCCESS_NUMBER;
		switch (workProgram) {
		case 1:
			rs = login();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	public int login() throws Exception {

		UserLoginModel userLoginModel = new UserLoginModel();
		ObjectMapper mapper = new ObjectMapper();
		userLoginModel = mapper.convertValue(getReciveObject(), userLoginModel.getClass());

		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		int roleNumber;
		String user = "";
		String userId = "";
		String checkUpdatePassword = null;
		String lastname = null;
		String avt = null;
		
		String SQL_SELECT_USER = "SELECT  "
				+ "	U.ID, "
				+ "	U.USERNAME ,  "
				+ "	FUR.ROLE_ID AS ROLE, "
				+ "	U.UPDATE_AT, "
				+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME, "
				+ "	FUI.URL_AVATA "
				+ "FROM "
				+ "	FHC_USER U "
				+ "INNER JOIN FHC_USER_ROLE FUR ON "
				+ "	(U.ID = FUR.USER_ID) "
				+ "LEFT JOIN FHC_USER_INFORMATION FUI ON "
				+ "	U.ID = FUI.USER_ID "
				+ "WHERE "
				+ "	U.USERNAME = ? "
				+ "	AND U.PASSWORD = ? AND U.ACTIVED = '1'; ";
		
		String SQL_UPDATE_USER = "UPDATE "
		+ "	FHC_USER "
		+ "SET "
		+ "	IS_ACTIVE = 'Y', "
		+ "	LAST_LOGIN = ? "
		+ "WHERE "
		+ "	USERNAME = ?; ";
		
		String SQL_SELECT_ROOM = "SELECT "
				+ "	ROOM_ID "
				+ "FROM "
				+ "	ROOM_SOCKET "
				+ "WHERE USER_ID = ?;";
		
		String SQL_SELECT_STAFF_IS_ACTIVE = "SELECT "
				+ "	IS_ACTIVE "
				+ "FROM "
				+ "	FHC_STAFF "
				+ "WHERE "
				+ "	STAFF_ID = ?; ";

		try {
			ps = getSC().prepareStatement(SQL_SELECT_USER);
			ps.setString(1, userLoginModel.getPhone());
			ps.setString(2, cs.encrypt(userLoginModel.getPassword()));
			rs = ps.executeQuery();
			HashMap<String, Object> userAuthen = new HashMap<>();
			if (rs.next()) {
				userId = rs.getString(1);
				user = rs.getString(2);
				roleNumber = rs.getInt(3);
				checkUpdatePassword = rs.getString(4);
				lastname = rs.getString(5);
				avt = ConstantVariable.STORAGE_CLOUD + rs.getString(6);
				if (rs != null) {
					rs.close();
				}
				
				if (ps != null) {
					ps.close();
				}
				
				String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
				
				ps = getSC().prepareStatement(SQL_UPDATE_USER);
				ps.setString(1, currentDate);
				ps.setString(2, userLoginModel.getPhone());
				ps.executeUpdate();
				ps.close();
				
				ps = getSC().prepareStatement(SQL_SELECT_ROOM);
				ps.setString(1, userId);
				rs = ps.executeQuery();
				List<String> rooms = new ArrayList<>();
				while (rs.next()) {
					rooms.add(rs.getString(1));
				}
				ps.close();
				rs.close();
				
				ps = getSC().prepareStatement(SQL_SELECT_STAFF_IS_ACTIVE);
				ps.setString(1, userId);
				rs = ps.executeQuery();
				String activeStaff = "";
				if (rs.next()) {
					activeStaff = rs.getString(1);
				}
				

				userAuthen.put("userId", userId);
				userAuthen.put("user", cs.encrypt(user));
				userAuthen.put("userDisp", user);
				userAuthen.put("role", roleNumber);
				userAuthen.put("rooms", rooms);
				userAuthen.put("isStaffActive", activeStaff);
				userAuthen.put("isPasswordChanged", checkUpdatePassword);
				userAuthen.put("lastname", lastname);
				userAuthen.put("avt", avt);
				
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
				return executeNo;
			}

			// Object trả về client
			setReturnObject(userAuthen);
		} catch (SQLException e) {
			
			throw e;
			// }
		} catch (Exception e) {
			throw e;
		}
		return executeNo;
	}
}
