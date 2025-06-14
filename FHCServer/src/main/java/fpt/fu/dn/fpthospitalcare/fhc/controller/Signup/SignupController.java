package fpt.fu.dn.fpthospitalcare.fhc.controller.Signup;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReciveModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/signup")
public class SignupController {

	@RequestMapping(value = "/register-user", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel registerUser(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		// Khai báo service: service là nơi implement all logic
		SignupService signupService = new SignupService();

		// Setting model mà service xử dụng
		signupService.setReciveObject(reciveModel.getModel());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = signupService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(signupService.getReturnObject());
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("User đã đăng ký");
			} else {

				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");

			}
			// rm.setSessionAuthor(patientService.sessionAuthor);

		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
	
	@RequestMapping(value = "/accept-token", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel confirmAccount(@RequestParam String token) {

		// Khai báo định nghĩa function:
		int workProgram = 2;

		// Khai báo service: service là nơi implement all logic
		SignupService signupService = new SignupService();

		// Setting model mà service xử dụng
		signupService.setReciveObject(token);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = signupService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(signupService.getReturnObject());
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("User đã đăng ký");
			} else {

				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");

			}
			// rm.setSessionAuthor(patientService.sessionAuthor);

		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
}
