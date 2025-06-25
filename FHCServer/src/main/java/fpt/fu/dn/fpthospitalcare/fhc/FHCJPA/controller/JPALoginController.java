package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.service.JPALoginService;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReciveModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/jpa-login")
public class JPALoginController {

	@Autowired
	private JPALoginService loginService;
	
	@PostMapping(value = "/jpa-login-system", produces = "application/json")
	@ResponseBody
	public ReturnModel login(@RequestBody ReciveModel reciveModel) {
		ReturnModel returnModel;
		try {
			String username = (String) ((HashMap) reciveModel.getModel()).get("phone");
			String password = (String) ((HashMap) reciveModel.getModel()).get("password");
			returnModel = loginService.login(username, password);
			if (returnModel == null) {
				returnModel = new ReturnModel();
				returnModel.setErrorCode(ConstantVariable.ERROR_NUMBER);
				returnModel.setErrorMessage("Có lỗi xảy ra");
			}
		} catch (Exception e) {
			returnModel = new ReturnModel();
			returnModel.setErrorCode(ConstantVariable.ERROR_NUMBER);
			returnModel.setErrorMessage(e.getMessage());
			returnModel.setReturnObject(null);
			e.printStackTrace();
		}
		return returnModel;
	}
}
