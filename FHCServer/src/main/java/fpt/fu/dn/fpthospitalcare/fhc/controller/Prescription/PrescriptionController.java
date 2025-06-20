package fpt.fu.dn.fpthospitalcare.fhc.controller.Prescription;

import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
	
	// Khai báo service: service là nơi implement all logic
	@Autowired
	private PrescriptionService prescriptionService;

	@RequestMapping(value = "/create-prescription", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel createPrescription(@RequestParam("prescriptionList") String prescriptionList,
			@RequestParam("note") String note,
			@RequestParam("numberDay") String numberDay,
			@RequestParam("userId") String userId,
			@RequestParam("appoinmentId") String appoinmentId) {
		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, Object> param = new HashMap<>();
		param.put("prescriptionList", prescriptionList);
		param.put("note", note);
		param.put("numberDay", numberDay);
		param.put("userId", userId);
		param.put("appoinmentId", appoinmentId);
		
		// Setting model mà service xử dụng
		prescriptionService.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = prescriptionService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Tạo đơn thuốc thành công!");
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
		} catch (SQLException e) {
			rm.setErrorCode(ConstantVariable.DUPLICATE_KEY);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
	
	@RequestMapping(value = "/get-prescription", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getPrescription(@RequestParam("userId") String userId,
			@RequestParam("appointmentId") String appointmentId) {
		// Khai báo định nghĩa function:
		int workProgram = 2;

		HashMap<String, Object> param = new HashMap<>();
		param.put("appointmentId", appointmentId);
		param.put("userId", userId);
		
		// Setting model mà service xử dụng
		prescriptionService.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = prescriptionService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(prescriptionService.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy dữ liệu!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
		} catch (SQLException e) {
			rm.setErrorCode(ConstantVariable.DUPLICATE_KEY);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
	
	@RequestMapping(value = "/update-prescription-card", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel updatePrescription(@RequestParam("medicineModelList") String medicineModelList,
			@RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 3;

		HashMap<String, Object> param = new HashMap<>();
		param.put("medicineModelList", medicineModelList);
		param.put("userId", userId);
		
		// Setting model mà service xử dụng
		prescriptionService.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = prescriptionService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(prescriptionService.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.NO_CHANGE_ERROR) {
				rm.setErrorCode(ConstantVariable.NO_CHANGE_ERROR);
				rm.setErrorMessage("Chưa có thay đổi trong số lượng thuốc!");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Bạn không có quyền thực thi chức năng này!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
		} catch (SQLException e) {
			rm.setErrorCode(ConstantVariable.DUPLICATE_KEY);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
}
