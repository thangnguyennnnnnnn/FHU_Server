package fpt.fu.dn.fpthospitalcare.fhc.controller.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReciveModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/login")
public class LoginController {

	// Khai báo service: service là nơi implement all logic
	@Autowired
	private LoginService loginService;
	
	// "/signup": URL mapping localhot:user/signup
	@RequestMapping(value = "/login-system", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel signup(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 1;


		// Setting model mà service xử dụng
		loginService.setReciveObject(reciveModel.getModel());

		// userService.setSessionAuthor(reciveModel.getSessionAuthor());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = loginService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {

				// Nếu OK thì setting object return về client
				if (loginService.getReturnObject() != null) {
					rm.setReturnObject(loginService.getReturnObject());
				} else {
					rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
					rm.setErrorMessage("Có lỗi xảy ra 1");
				}
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorCode(ConstantVariable.DUPLICATE_KEY);
				rm.setErrorMessage("Lỗi Trùng Session");
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy user");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra 2");
			}

		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
}
