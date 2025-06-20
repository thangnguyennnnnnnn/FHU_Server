package fpt.fu.dn.fpthospitalcare.fhc.controller.Diagnostic;

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
@RequestMapping("/diagnostic")
public class DiagnosticController {
	
	// Khai báo service: service là nơi implement all logic
	@Autowired
	private DiagnosticService diagnosticService;
	
	@RequestMapping(value = "/get-diagnostic-cards", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getDiagnostics(@RequestParam("userId") String userId,
			@RequestParam("appointmentId") String appointmentId,
			@RequestParam("name") String name) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("appointmentId", appointmentId);
		params.put("name", name);
		
		diagnosticService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = diagnosticService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(diagnosticService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Không tìm được thông tin!");
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
	
	@RequestMapping(value = "/update-diagnostic-card", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel updateDiagnostic(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 2;
		
		diagnosticService.setReciveObject(reciveModel.getModel());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = diagnosticService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(diagnosticService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Không tìm được thông tin!");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Bạn không có quyền thực hiện chức năng này!");
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
