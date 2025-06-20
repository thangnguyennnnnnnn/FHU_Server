package fpt.fu.dn.fpthospitalcare.fhc.controller.RegisterAppointment;

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
@RequestMapping("/appointment")
public class RegisterAppointmentController {
	
	// Khai báo service: service là nơi implement all logic
	@Autowired
	private RegisterAppointmentService registerAppointmentService;

//	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
//	@ResponseBody
//	public ReturnModel addAppoinment(@RequestBody ReciveModel reciveModel) {
//
//		// Khai báo định nghĩa function:
//		int workProgram = 1;
//
//		// Khai báo service: service là nơi implement all logic
//		RegisterAppointmentService registerAppointmentService = new RegisterAppointmentService();
//
//		// Setting model mà service xử dụng
//		registerAppointmentService.setReciveObject(reciveModel.getModel());
//
//		// patientService.setSessionAuthor(reciveModel.getSessionAuthor());
//
//		// Khai báo object trả về client
//		ReturnModel rm = new ReturnModel();
//
//		try {
//			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
//			int exeNo = registerAppointmentService.execute(workProgram);
//
//			// kiểm tra kết quả xử lý
//			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
//				rm.setErrorCode(exeNo);
//			} else {
//				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
//				rm.setErrorMessage("Có lỗi xảy ra");
//			}
//			// rm.setSessionAuthor(patientService.sessionAuthor);
//
//		} catch (Exception e) {
//			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
//			rm.setErrorMessage(e.getMessage());
//			rm.setReturnObject(null);
//		}
//		return rm;
//	}

	@RequestMapping(value = "/get-doctors", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getDoctors(@RequestParam("medicalExaminationTime") String medicalExaminationTime,
			@RequestParam("medicalExaminationDay") String medicalExaminationDay,
			@RequestParam("specializationId") String specializationId,
			@RequestParam("buildingId") String buildingId) {

		// Khai báo định nghĩa function:
		int workProgram = 2;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("medicalExaminationTime", medicalExaminationTime);
		params.put("medicalExaminationDay", medicalExaminationDay);
		params.put("specializationId", specializationId);
		params.put("buildingId", buildingId);
		
		registerAppointmentService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerAppointmentService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerAppointmentService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value = "/get-doctor-info", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel getDoctorInfo(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 3;
		
		registerAppointmentService.setReciveObject(reciveModel.getModel());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerAppointmentService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerAppointmentService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorMessage("Bác sĩ này đang được người dùng khác chọn! Vui lòng đợi hoặc thử chọn bác sĩ khác!");
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value = "/register-appointment", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel registerAppointment(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 1;
		
		registerAppointmentService.setReciveObject(reciveModel.getModel());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerAppointmentService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerAppointmentService.getReturnObject());
				rm.setErrorMessage("Đăng ký thành công!");
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
			} else if (exeNo == ConstantVariable.ERROR_UPDATE) {
				rm.setErrorMessage("Đăng ký không thành công! Vui lòng thử lại");
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value = "/get-appointment", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getAppointment(@RequestParam("userID") String userID,
			@RequestParam("appoinmentId") String appoinmentId) {

		// Khai báo định nghĩa function:
		int workProgram = 4;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userID", userID);
		params.put("appoinmentId", appoinmentId);
		
		registerAppointmentService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerAppointmentService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerAppointmentService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value = "/get-appointment-card", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getAppointmentCard(@RequestParam("userID") String userID,
			@RequestParam("appoinmentId") String appoinmentId) {

		// Khai báo định nghĩa function:
		int workProgram = 5;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userID", userID);
		if (!appoinmentId.equals("")) {
			params.put("appoinmentId", appoinmentId);
		}
		
		registerAppointmentService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerAppointmentService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerAppointmentService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value = "/delete-appointment", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel deleteAppointmentCard(@RequestParam("apoinmentId") String apoinmentId, @RequestParam("userId") String userId) {

		// Khai báo định nghĩa function:
		int workProgram = 6;

		HashMap<String, String> params = new HashMap<>();
		params.put("apoinmentId", apoinmentId);
		params.put("userId", userId);
		
		registerAppointmentService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = registerAppointmentService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(registerAppointmentService.getReturnObject());
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
