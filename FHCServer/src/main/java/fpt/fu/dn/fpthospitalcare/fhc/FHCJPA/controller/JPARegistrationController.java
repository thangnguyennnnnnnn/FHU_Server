package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.service.JPARegistrationService;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReciveModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.UserInformationModel;

@Controller
@RequestMapping("/jpa-regist-user")
public class JPARegistrationController {
	
	@Autowired
	private JPARegistrationService registrationService;

	@PostMapping(value = "/jpa-regist", produces = "application/json")
	@ResponseBody
	public ReturnModel registerUser(@RequestBody ReciveModel reciveModel) {
		
		ReturnModel returnModel = new ReturnModel();
		
		UserInformationModel userInformation = new UserInformationModel();
		ObjectMapper mapper = new ObjectMapper();
		userInformation = mapper.convertValue(reciveModel.getModel(), userInformation.getClass());
		
		try {
			
			returnModel = registrationService.registerUser(userInformation);
			
		} catch (Exception e) {
			
			if (e.getCause() instanceof ConstraintViolationException) {
				returnModel.setErrorCode(ConstantVariable.DUPLICATE_KEY);
				returnModel.setErrorMessage("Người dùng đã được đăng ký trước đó.");
				return returnModel;
		    }
			
			returnModel.setErrorCode(ConstantVariable.ERROR_NUMBER);
			returnModel.setErrorMessage("Đã xảy ra lỗi hệ thống!");
			returnModel.setReturnObject(null);
			e.printStackTrace();
		}
		
		return returnModel;
		
	}
}
