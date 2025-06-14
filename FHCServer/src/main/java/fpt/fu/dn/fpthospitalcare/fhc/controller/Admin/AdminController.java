package fpt.fu.dn.fpthospitalcare.fhc.controller.Admin;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping(value = "/create-report", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel createReport(@RequestParam("userId") String userId,
			@RequestParam("month") String month,
			@RequestParam("year") String year,
			@RequestParam("type") String type) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("month", month);
		params.put("year", year);
		params.put("type", type);
		
		// Khai báo service: service là nơi implement all logic
		AdminService adminService = new AdminService();
		
		adminService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = adminService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(adminService.getReturnObject());
			} else if (exeNo == ConstantVariable.FILE_ERROR) {
				rm.setErrorMessage("Không tìm thấy file template hoặc có lỗi trong quá trình xử lý file!");
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value = "/get-feedback", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getFeedback(@RequestParam("id") String id,
			@RequestParam("date") String date,
			@RequestParam("userId") String userId) {

		// Khai báo định nghĩa function:
		int workProgram = 2;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("date", date);
		params.put("id", id);
		
		// Khai báo service: service là nơi implement all logic
		AdminService adminService = new AdminService();
		
		adminService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = adminService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(adminService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorMessage("Không tìm thấy feedback trong thời gian :" + date);
				rm.setErrorCode(exeNo);
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
