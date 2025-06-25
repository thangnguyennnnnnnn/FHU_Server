package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository.FhcUserInformationRepository;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository.FhcUserRepository;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository.FhcUserRoleRepository;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.CryptoService;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.MailService;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.UserInformationModel;

@Service
public class JPARegistrationService {
	
	private final FhcUserRepository fhcUserRepository;
	
	private final FhcUserInformationRepository fhcUserInformationRepository;
	
	private final FhcUserRoleRepository fhcUserRoleRepository;
	
	public JPARegistrationService(FhcUserRepository fhcUserRepository, FhcUserInformationRepository fhcUserInformationRepository, FhcUserRoleRepository fhcUserRoleRepository) {
		this.fhcUserRepository = fhcUserRepository;
		this.fhcUserInformationRepository = fhcUserInformationRepository;
		this.fhcUserRoleRepository = fhcUserRoleRepository;
	}
	
	public ReturnModel registerUser(UserInformationModel userInformation) throws Exception {
		
		ReturnModel returnModel = new ReturnModel();
		
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss");
        String currentDateValue = currentDate.format(formatter);
        
        String id = "U"+ currentDateValue;
        userInformation.setUserId(id);
        
        CryptoService cs = new CryptoService();
        String passwordEncryto = cs.encrypt(userInformation.getPassword());
        
        int insertUserCount = fhcUserRepository.insertUser(
        		userInformation.getUserId(),
        		userInformation.getUsername(),
        		passwordEncryto,
        		currentDateValue,
        		"N",
        		null,
        		userInformation.getUserId());
        
        if (insertUserCount != 0) {
        	int insUserInfoCount = fhcUserInformationRepository.insertFhcUserInformation(
        			userInformation.getUserId(),
        			userInformation.getFirstName(),
        			userInformation.getMiddleName(), 
        			userInformation.getLastName(), 
        			userInformation.getBirthday(), 
        			userInformation.getGender(), 
        			userInformation.getNationality(), 
        			userInformation.getPhoneNumber(), 
        			userInformation.getEmail(), 
        			userInformation.getAddress(), 
        			userInformation.getUrlAvt());
        	
        	if (insUserInfoCount > 0) {
        		int insUserRoleCount = fhcUserRoleRepository.insertFhcUserRole(
        				userInformation.getUserId(), 
        				"4", 
        				userInformation.getUserId(), 
        				currentDateValue, 
        				"Y"
        		);
        		
        		if (insUserRoleCount > 0) {
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
					linkPart.setText("http://localhost:4200/xac-thuc-tai-khoan?token=" + userInformation.getUserId());
					//linkPart.setText("http://localhost:4200/xac-thuc-tai-khoan?token=" + userInformation.getUserId());
					multipart.addBodyPart(linkPart);
					
					mail.sendMail(userInformation.getEmail(), subject, message, multipart);
					
					returnModel.setReturnObject(userInformation);
				}
        	}
        }
		
        return returnModel;
	}
}
