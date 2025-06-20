package fpt.fu.dn.fpthospitalcare.fhc.controller.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.MenuObjectModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.UserLoginModel;
import fpt.fu.dn.fpthospitalcare.fhc.controller.User.Model.UserProfileModel;
import fpt.fu.dn.fpthospitalcare.fhc.controller.User.Model.ProfileUpdateModel;
import fpt.fu.dn.fpthospitalcare.fhc.controller.User.Model.ChangePasswordModel;

@Component
public class UserService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = ConstantVariable.SUCCESS_NUMBER;
		switch (workProgram) {
		case 1:
			rs = singin();
			break;
		case 2:
			rs = logout();
			break;
		case 3:
			rs = getProfile();
			break;
		case 4:
			rs = updateProfile();
			break;
		case 5:
			rs = changePassword();
			break;
		case 6:
			rs = uploadAvata();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}

	public int singin() throws Exception {

		UserLoginModel userLoginModel = new UserLoginModel();
		ObjectMapper mapper = new ObjectMapper();
		userLoginModel = mapper.convertValue(getReciveObject(), userLoginModel.getClass());

		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		int roleNumber;
		String user = "";

		List<Object> listReturn = new ArrayList<>();
		HashMap<String, Object> returnObjects = new HashMap<>();
		try {
			String SQL_SELECT_USER = "SELECT " + "	U.Username, " + "	FA.[ROLE] " + "FROM " + "	FHC_USER U "
					+ "LEFT JOIN FHC_AUTHENTICATION FA ON " + "	(U.Username = FA.USER_ID) " + "WHERE "
					+ "	U.Username = ? " + "	AND Password = ?;";
			ps = getSC().prepareStatement(SQL_SELECT_USER);
			ps.setString(1, userLoginModel.getPhone());
			ps.setString(2, userLoginModel.getPassword());
			rs = ps.executeQuery();
			if (rs.next()) {
				user = rs.getString(1);
				roleNumber = rs.getInt(2);
				HashMap<String, Object> userAuthen = new HashMap<>();
				userAuthen.put("user", cs.encrypt(user));
				userAuthen.put("userDisp", user);
				returnObjects.put("userAuthen", userAuthen);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
				return executeNo;
			}
			listReturn.add(returnObjects);
			ps.close();
			rs.close();

			// Get Item Menu ứng với role người dùng
			String SQL_SELECT_USER_MENU = "SELECT * FROM FHC_MENU fm WHERE [ROLE] IN (?, 0)  ORDER BY PRIORITY ASC;";
			ps = getSC().prepareStatement(SQL_SELECT_USER_MENU);
			ps.setInt(1, roleNumber);
			rs = ps.executeQuery();
			List<MenuObjectModel> listMenu = new ArrayList<>();
			while (rs.next()) {
				MenuObjectModel menuItem = new MenuObjectModel();
				menuItem.setMenuItemId(rs.getString(1));
				menuItem.setMenuItemName(rs.getString(2));
				menuItem.setMenuItemIcon(rs.getString(7));
				menuItem.setMenuRole(rs.getInt(6));
				menuItem.setMenuUrl(rs.getString(8));
				listMenu.add(menuItem);
			}
			listReturn.add(listMenu);

			// Object trả về client
			setReturnObject(listReturn);
		} catch (SQLException e) {
			// if (e.getErrorCode() == 2627) {
			// executeNo = ConstantVariable.DUPLICATE_KEY;
			// } else {
			throw e;
			// }
		} catch (Exception e) {
			throw e;
		}
		return executeNo;
	}

	public int getProfile() throws Exception {
		// String sessionAuthor = getSessionAuthor();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		String user = (String) getReciveObject();

		// List<Object> listReturn = new ArrayList<>();
		try {
			// Get user information ứng với người dùng
			String SQL_SELECT_USER_INFO = "SELECT "
					+ "	FUI.USER_ID, "
					+ "	FUI.FIRST_NAME , "
					+ "	FUI.MIDDLE_NAME , "
					+ "	FUI.LAST_NAME , "
					+ "	FUI.BIRTHDATE , "
					+ "	FUI.GENDER , "
					+ "	FUI.NATIONALITY , "
					+ "	FUI.PHONE_NUMBER , "
					+ "	FUI.EMAIL , "
					+ "	FUI.ADDRESS, "
					+ "	FUR.ROLE_ID, "
					+ "	FSR.NAME, "
					+ "	FS.FACULTY_ID, "
					+ "	FF.NAME , "
					+ "	FS.POSITION_ID, "
					+ "	FPR.POSITION_NAME, "
					+ " FUI.URL_AVATA "
					+ "FROM "
					+ "	FHC_USER_INFORMATION FUI "
					+ "	LEFT JOIN FHC_USER_ROLE FUR ON FUI.USER_ID = FUR.USER_ID "
					+ "	LEFT JOIN FHC_SYSTEMS_ROLE FSR ON FUR.ROLE_ID = FSR.ID "
					+ "	LEFT JOIN FHC_STAFF FS ON FUI.USER_ID = FS.STAFF_ID "
					+ "	LEFT JOIN FHC_FACULTY FF ON FS.FACULTY_ID = FF.ID "
					+ "	LEFT JOIN FHC_POSITION_ROLE FPR ON FS.POSITION_ID = FPR.ID  "
					+ "WHERE FUI.USER_ID = ?;";
			ps = getSC().prepareStatement(SQL_SELECT_USER_INFO);
			ps.setString(1, user);
			rs = ps.executeQuery();
			UserProfileModel userInfo = new UserProfileModel();
			if (rs.next()) {
				userInfo.setUserId(rs.getString(1));
				userInfo.setFirstName(rs.getString(2));
				userInfo.setMiddleName(rs.getString(3));
				userInfo.setLastName(rs.getString(4));
				userInfo.setBirthday(rs.getString(5));
				userInfo.setGender(rs.getString(6));
				userInfo.setNationality(rs.getString(7));
				userInfo.setPhoneNumber(rs.getString(8));
				userInfo.setEmail(rs.getString(9));
				userInfo.setAddress(rs.getString(10));
				userInfo.setRoleId(rs.getString(11));
				userInfo.setRoleName(rs.getString(12));
				userInfo.setFacultyId(rs.getString(13));
				userInfo.setFacultyName(rs.getString(14));
				userInfo.setPositionId(rs.getString(15));
				userInfo.setPositionName(rs.getString(16));
				userInfo.setUrlAvt(ConstantVariable.STORAGE_CLOUD + rs.getString(17));
			} else {
				userInfo = new UserProfileModel();
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
			rs.close();
			ps.close();

			// Object trả về client
			setReturnObject(userInfo);
		} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}

	/**
	 * Không dùng nữa
	 * 
	 * @return
	 * @throws Exception
	 */
	public int logout() throws Exception {

		// String sessionAuthor = getSessionAuthor();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
//		try {
////			String CLEAR_SESSION = "DELETE FROM MSession "
////										+ "WHERE SessionAuthor=?;";
////			ps = getSC().prepareStatement(CLEAR_SESSION);
////			ps.setString(1, sessionAuthor);
////			int deleteRow = ps.executeUpdate();
////			
////			if (deleteRow > 0) {
////				setSessionAuthor(null);
////			} else {
////				executeNo = ConstantVariable.ERROR_NUMBER;
////			}
//		} catch (SQLException e) {
//			throw e;
//		} catch (Exception e) {
//			throw e;
//		}
		return executeNo;
	}

	public int updateProfile() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		ProfileUpdateModel profileUpdate = new ProfileUpdateModel();
		ObjectMapper mapper = new ObjectMapper();
		profileUpdate = mapper.convertValue(getReciveObject(), profileUpdate.getClass());
		try {
			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String updateTime = currentDateTime.format(formatter);
			
			String SQL_UPDATE_USER_INFO = "UPDATE "
					+ "	FHC_USER_INFORMATION "
					+ "SET "
					+ "	FIRST_NAME = ?, "
					+ "	MIDDLE_NAME = ?, "
					+ "	LAST_NAME = ?, "
					+ "	ADDRESS = ?, "
					+ "	EMAIL = ?, "
					+ "	PHONE_NUMBER = ?, "
					+ "	BIRTHDATE = ?, "
					+ "	GENDER = ?, "
					+ "	NATIONALITY = ?, "
					+ "	UPDATE_AT = ? "
					+ "WHERE "
					+ "	USER_ID = ?;";
			
			String SQL_UPDATE_STAFF = "UPDATE "
					+ "	FHC_STAFF "
					+ "SET "
					+ "	IS_ACTIVE = N'Y', "
					+ "	UPDATE_AT = ? "
					+ "WHERE "
					+ "	STAFF_ID = ?; ";
			
			ps = getSC().prepareStatement(SQL_UPDATE_USER_INFO);
			ps.setString(1, profileUpdate.getFirstName());
			ps.setString(2, profileUpdate.getMiddleName());
			ps.setString(3, profileUpdate.getLastName());
			ps.setString(4, profileUpdate.getAddress());
			ps.setString(5, profileUpdate.getEmail());
			ps.setString(6, profileUpdate.getPhone());
			ps.setString(7, profileUpdate.getBirthDate());
			ps.setString(8, profileUpdate.getGender());
			ps.setString(9, profileUpdate.getNationality());
			ps.setString(10, updateTime);
			ps.setString(11, profileUpdate.getUserId());
			int count = ps.executeUpdate();
			ps.close();
			if (count > 0) {
				ps = getSC().prepareStatement(SQL_UPDATE_STAFF);
				ps.setString(1, updateTime);
				ps.setString(2, profileUpdate.getUserId());
				count = ps.executeUpdate();
				setReturnObject(profileUpdate);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		} catch (Exception e) {
			throw e;
		}
		return executeNo;
	}
	
	public int changePassword() throws Exception {
		
		ChangePasswordModel changePasswordModel = new ChangePasswordModel();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		ObjectMapper mapper = new ObjectMapper();
		changePasswordModel = mapper.convertValue(getReciveObject(), changePasswordModel.getClass());
		String SQL_UPDATE_PASSWORD = "UPDATE "
				+ "	FHC_USER "
				+ "SET "
				+ "	PASSWORD = ?, "
				+ "	UPDATE_AT = ? "
				+ "WHERE "
				+ "	ID = ? "
				+ "	AND PASSWORD = ? ";
		try {
			String updateTime = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			
			ps = getSC().prepareStatement(SQL_UPDATE_PASSWORD);
			ps.setString(1, cs.encrypt(changePasswordModel.getNewPassword()));
			ps.setString(2, updateTime);
			ps.setString(3, changePasswordModel.getUserId());
			ps.setString(4, cs.encrypt(changePasswordModel.getPassword()));

			int count = ps.executeUpdate();
			if (count > 0) {
				setReturnObject(null);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		} catch (Exception e) {
			throw e;
		}
		return executeNo;
	}
	
	public int uploadAvata() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		MultipartFile file = (MultipartFile) param.get("avata");
		String userId = (String)param.get("userId");
		
		if (file == null || file.isEmpty()) {
            return ConstantVariable.FILE_EMPTY;
        }
		
		try {
			String projectId = "fhc-server-394702";
	        String bucketName = "fhc-server-394702.appspot.com";

	        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
	        
	        byte[] imageData = file.getBytes();
            
            String path = "User/" + userId + "/avata/AVT" + getCurrentDate("yyyyMMddHHmmss");
            
            BlobId blobId = BlobId.of(bucketName, path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            
            storage.create(blobInfo, imageData);
            System.out.println("Upload thành công!");
            System.out.println("Path: " + path);
            
            String SQL_UPDATE_AVATA = "UPDATE "
            		+ "	FHC_USER_INFORMATION "
            		+ "SET "
            		+ "	UPDATE_AT = ?, "
            		+ "	URL_AVATA = ? "
            		+ "WHERE USER_ID = ?; ";
            
            String updateTime = getCurrentDate("dd/MM/yyyy HH:mm:ss");
            
            ps = getSC().prepareStatement(SQL_UPDATE_AVATA);
            ps.setString(1, updateTime);
            ps.setString(2, path);
			ps.setString(3, userId);
			
			int count = ps.executeUpdate();
			if (count > 0) {
				setReturnObject(ConstantVariable.STORAGE_CLOUD + path);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
            
		} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
}
