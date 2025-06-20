package fpt.fu.dn.fpthospitalcare.fhc.controller.RegisterStaff;

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
@RequestMapping("/add-staff")
public class RegisterStaffController {
	
	// Khai báo service: service là nơi implement all logic
	@Autowired
	private RegisterStaffService registerStaffService;
	
	@RequestMapping(value = "/register-staff", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel registerStaff(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 1;
		
		registerStaffService.setReciveObject(reciveModel.getModel());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerStaffService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerStaffService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Người dùng không tồn tại!");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Bạn không có quyền sử dụng chức năng này!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}

		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
}
