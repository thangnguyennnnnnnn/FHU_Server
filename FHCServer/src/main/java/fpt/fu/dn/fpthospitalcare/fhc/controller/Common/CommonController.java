package fpt.fu.dn.fpthospitalcare.fhc.controller.Common;

import java.util.HashMap;

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
@RequestMapping("/fhc-common")
public class CommonController {
	
	@RequestMapping(value="/get-specialization", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel getSpecializations() {

		// Khai báo định nghĩa function: 
		int workProgram = 1;
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			//rm.setSessionAuthor(patientService.sessionAuthor);
			
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value="/get-position", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel getPositionAll() {

		// Khai báo định nghĩa function: 
		int workProgram = 2;
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			//rm.setSessionAuthor(patientService.sessionAuthor);
			
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value="/get-building", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel getBuilding() {

		// Khai báo định nghĩa function: 
		int workProgram = 3;
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			//rm.setSessionAuthor(patientService.sessionAuthor);
			
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value="/get-work-floor", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel getWorkFloor() {

		// Khai báo định nghĩa function: 
		int workProgram = 4;
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			//rm.setSessionAuthor(patientService.sessionAuthor);
			
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value="/get-work-room", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel getWorkRoom() {

		// Khai báo định nghĩa function: 
		int workProgram = 5;
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			//rm.setSessionAuthor(patientService.sessionAuthor);
			
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value="/create-noti", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    public ReturnModel createNotification(@RequestBody ReciveModel recive) {

		// Khai báo định nghĩa function: 
		int workProgram = 6;
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		// Đặt object nhận từ client
		commonService.setReciveObject(recive.getModel());
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			//rm.setSessionAuthor(patientService.sessionAuthor);
			
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value = "/get-notifications", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getNotifications(@RequestParam("userId") String userId) {

		// Khai báo định nghĩa function:
		int workProgram = 7;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		commonService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(commonService.getReturnObject());
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
	
	@RequestMapping(value = "/read-notification", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel readNotification(@RequestParam("id") String id) {

		// Khai báo định nghĩa function:
		int workProgram = 8;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		
		// Khai báo service: service là nơi implement all logic
		CommonService commonService = new CommonService();
		
		commonService.setReciveObject(params);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = commonService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
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
}
