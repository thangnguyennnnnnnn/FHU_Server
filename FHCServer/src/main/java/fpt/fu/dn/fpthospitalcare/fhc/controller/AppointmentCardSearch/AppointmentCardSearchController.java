package fpt.fu.dn.fpthospitalcare.fhc.controller.AppointmentCardSearch;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/apointment-card")
public class AppointmentCardSearchController {
	
	// Khai báo service: service là nơi implement all logic
	@Autowired
	private AppointmentCardSearchService appointmentCardSearchService;
	
	@RequestMapping(value = "/search-apointments", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel searchApointments(@RequestParam("userId") String userId,
			@RequestParam("appointmentId") String appointmentId,
			@RequestParam("appointmentTime") String appointmentTime,
			@RequestParam("appointmentDate") String appointmentDate,
			@RequestParam("patientName") String patientName,
			@RequestParam("facultyId") String facultyId,
			@RequestParam("faculty") String faculty) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("appointmentId", appointmentId);
		params.put("appointmentTime", appointmentTime);
		params.put("appointmentDate", appointmentDate);
		params.put("patientName", patientName);
		params.put("facultyId", facultyId);
		params.put("faculty", faculty);
		
		
		appointmentCardSearchService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = appointmentCardSearchService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(appointmentCardSearchService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
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
	
	@RequestMapping(value = "/update-apointment", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel updateApointment(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 2;
		
		// Khai báo service: service là nơi implement all logic
		appointmentCardSearchService.setReciveObject(reciveModel.getModel());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = appointmentCardSearchService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(appointmentCardSearchService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
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
