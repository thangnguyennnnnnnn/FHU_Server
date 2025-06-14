package fpt.fu.dn.fpthospitalcare.fhc.controller.Employee;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel createReport(@RequestParam("staffId") String staffId,
			@RequestParam("staffName") String staffName,
			@RequestParam("faculty") String faculty,
			@RequestParam("userId") String userId) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("staffId", staffId);
		params.put("staffName", staffName);
		params.put("faculty", faculty);
		
		// Khai báo service: service là nơi implement all logic
		EmployeeService employeeService = new EmployeeService();
		
		employeeService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = employeeService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(employeeService.getReturnObject());
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
	
	@RequestMapping(value = "/register-working-time", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel registerWorkingTime(@RequestParam("onDate") String onDate,
			@RequestParam("reason") String reason,
			@RequestParam("userId") String userId) {

		// Khai báo định nghĩa function:
		int workProgram = 2;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("onDate", onDate);
		params.put("reason", reason);
		params.put("userId", userId);
		
		// Khai báo service: service là nơi implement all logic
		EmployeeService employeeService = new EmployeeService();
		
		employeeService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = employeeService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(employeeService.getReturnObject());
			} else if (exeNo == ConstantVariable.INPUT_EMPTY_VALUE) {
					rm.setErrorMessage("Vui lòng nhập thông tin tìm kiếm!");
					rm.setErrorCode(exeNo);
			} else if (exeNo == ConstantVariable.HAVE_APPOINTMENT) {
				rm.setErrorMessage("Bạn có lịch khám bệnh trong ngày : " + onDate + ", hãy liên hệ với bệnh nhân để xác nhận rằng bạn không có mặt trong ngày " + onDate);
				rm.setErrorCode(exeNo);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Không tìm thấy tài khoản này!");
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Bạn đã đăng ký ngày nghỉ vào: " + onDate + " trước đó.");
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
	
	@RequestMapping(value = "/get-working-time", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getWorkingTime(@RequestParam("id") String id,
			@RequestParam("userId") String userId, @RequestParam("date") String date) {

		// Khai báo định nghĩa function:
		int workProgram = 3;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("userId", userId);
		params.put("date", date);
		
		// Khai báo service: service là nơi implement all logic
		EmployeeService employeeService = new EmployeeService();
		
		employeeService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = employeeService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(employeeService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Bạn không đăng ký nghỉ vào ngày " + date + ".");
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
