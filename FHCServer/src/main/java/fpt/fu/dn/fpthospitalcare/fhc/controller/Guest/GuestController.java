package fpt.fu.dn.fpthospitalcare.fhc.controller.Guest;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/guest")
public class GuestController {
	
	@RequestMapping(value = "/send-feedback", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel createReport(@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("subject") String subject,
			@RequestParam("message") String message) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("email", email);
		params.put("subject", subject);
		params.put("message", message);
		
		// Khai báo service: service là nơi implement all logic
		GuestService guestService = new GuestService();
		
		guestService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = guestService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(guestService.getReturnObject());
			} else if (exeNo == ConstantVariable.INPUT_EMPTY_VALUE) {
					rm.setErrorMessage("Vui lòng nhập thông tin tìm kiếm!");
					rm.setErrorCode(exeNo);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Không tìm thấy tài khoản này!");
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
