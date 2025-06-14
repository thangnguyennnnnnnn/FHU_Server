package fpt.fu.dn.fpthospitalcare.fhc.controller.Signup;

import java.sql.SQLException;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.MailService;
import fpt.fu.dn.fpthospitalcare.fhc.model.UserInformationModel;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

@Transactional
public class SignupService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
			case 1: 
				rs = registerUser();
				break;
			case 2: 
				rs = confirmAccount();
				break;
			default:
				rs = 0;
				break;
		}
		return rs;
	}
	
	/**
	 * Đăng ký người dùng
	 * 
	 * @return executeNo 
	 * @throws Exception 
	 */
	public int registerUser() throws Exception {
		UserInformationModel userInformation = new UserInformationModel();
		ObjectMapper mapper = new ObjectMapper();
		userInformation = mapper.convertValue(getReciveObject(), userInformation.getClass());
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_INSERT_USER = "INSERT INTO FHC.dbo.FHC_USER "
				+ "(ID, USERNAME, PASSWORD, CREATED_AT, IS_ACTIVE, LAST_LOGIN, ACTIVED) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?); ";
		
		String SQL_INSERT_USER_INFO = "INSERT INTO FHC.dbo.FHC_USER_INFORMATION "
				+ "(USER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDATE, GENDER, NATIONALITY, PHONE_NUMBER, EMAIL, ADDRESS, URL_AVATA) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
		
		String SQL_INSERT_USER_ROLE = "INSERT INTO FHC.dbo.FHC_USER_ROLE "
				+ "(USER_ID, ROLE_ID, CREATED_BY, CREATED_AT, IS_ACTIVE) "
				+ "VALUES(?, ?, ?, ?, ?); ";
		
		try {
			
			String currentDate = getCurrentDate("ddMMyyyyhhmmss");
			String id = "U"+ currentDate;
			userInformation.setUserId(id);
			
			ps = getSC().prepareStatement(SQL_INSERT_USER);
			
			ps.setString(1, userInformation.getUserId());
			ps.setString(2, userInformation.getUsername());
			ps.setString(3, cs.encrypt(userInformation.getPassword()));
			ps.setString(4, currentDate);
			ps.setString(5, "N");
			ps.setString(6, null);
			ps.setString(7, userInformation.getUserId());
			
			int count = ps.executeUpdate();
			
			if (count != 0) {
				if (ps != null) {
					ps.close();
				}
				
				ps = getSC().prepareStatement(SQL_INSERT_USER_INFO);
				ps.setString(1, userInformation.getUserId());
				ps.setString(2, userInformation.getFirstName());
				ps.setString(3, userInformation.getMiddleName());
				ps.setString(4, userInformation.getLastName());
				ps.setString(5, userInformation.getBirthday());
				ps.setString(6, userInformation.getGender());
				ps.setString(7, userInformation.getNationality());
				ps.setString(8, userInformation.getPhoneNumber());
				ps.setString(9, userInformation.getEmail());
				ps.setString(10, userInformation.getAddress());
				ps.setString(11, userInformation.getUrlAvt());
				
				count = ps.executeUpdate();
				userInformation.setUsernameEncryto(cs.encrypt(userInformation.getUsername()));
				if (count > 0) {
					
					ps = getSC().prepareStatement(SQL_INSERT_USER_ROLE);
					ps.setString(1, userInformation.getUserId());
					ps.setInt(2, 4);
					ps.setString(3, userInformation.getUserId());
					ps.setString(4, currentDate);
					ps.setString(5, "Y");
					
					count = ps.executeUpdate();
					
					if (count > 0) {
						MailService mail = new MailService();
						MimeMultipart multipart = new MimeMultipart();
						MimeBodyPart textPart = new MimeBodyPart();
						String subject = "FHC Chào Mừng " + userInformation.getLastName();
						String message = "Chào bạn!\n\n"
			                    + "Chúng tôi rất vui khi được bạn tin tưởng.\n\n"
								+ "Thông tin tài khoản của bạn là: Tài khoản: " + userInformation.getUsername() + ", Mật khẩu: " + userInformation.getPassword() + "\n\n"
								+ "Đây là link xác nhận tài khoản của bạn:\n\n"
			                    + "Vui lòng truy cập để xác nhận tài khoản: ";
						textPart.setText(message);
						multipart.addBodyPart(textPart);
						
						MimeBodyPart linkPart = new MimeBodyPart();
						linkPart.setText("https://fhc-client.web.app/xac-thuc-tai-khoan?token=" + userInformation.getUserId());
						//linkPart.setText("http://localhost:4200/xac-thuc-tai-khoan?token=" + userInformation.getUserId());
						multipart.addBodyPart(linkPart);
						
						mail.sendMail(userInformation.getEmail(), subject, message, multipart);
						
						setReturnObject(userInformation);
					} else {
						executeNo = ConstantVariable.ERROR_NUMBER;
					}
				} else {
					executeNo = ConstantVariable.ERROR_NUMBER;
				}
			} else {
				executeNo = ConstantVariable.ERROR_NUMBER;
			}
		} catch (SQLException sqle) {
			if (sqle.getErrorCode() == 2627) {
				executeNo = ConstantVariable.DUPLICATE_KEY;
			} else {
				throw sqle;
			}
		} catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		} 

		return executeNo;
    }
	
	public int confirmAccount() throws Exception {
		
		String token = (String) getReciveObject();
		
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_CONFIRM_USER = "UPDATE "
				+ "	FHC_USER "
				+ "SET "
				+ "	ACTIVED = 1 "
				+ "WHERE "
				+ "	ACTIVED = ? ; ";
		
		try {
			
			ps = getSC().prepareStatement(SQL_CONFIRM_USER);
			ps.setString(1, token);
			
			int count = 0;
			count = ps.executeUpdate();
			
			if (count == 0) {
				executeNo = ConstantVariable.ERROR_NUMBER;
			}
		} catch (SQLException sqle) {
			if (sqle.getErrorCode() == 2627) {
				executeNo = ConstantVariable.DUPLICATE_KEY;
			}
		} catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		} 

		return executeNo;
    }
}
