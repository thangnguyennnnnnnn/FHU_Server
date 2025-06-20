package fpt.fu.dn.fpthospitalcare.fhc.controller.Patient;

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
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	private ReturnModel rm;
	
	@RequestMapping(value="/add-appointment", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    public ReturnModel addAppoinment(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function: 
		int workProgram = 1;
		
		//Setting model mà service xử dụng
		patientService.setReciveObject(reciveModel.getModel());
		
		//patientService.setSessionAuthor(reciveModel.getSessionAuthor());
		
		// Khai báo object trả về client
		rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = patientService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value="/getAppointments", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel getAppoinments(@RequestParam("userID") String userID) {

		// Khai báo định nghĩa function: 
		int workProgram = 2;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userID", userID);
		
		//Setting model mà service xử dụng
		patientService.setReciveObject(params);
		
		// Khai báo object trả về client
		rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = patientService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(patientService.getReturnObject());
				rm.setErrorCode(exeNo);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value="/get-appointment-detail", method=RequestMethod.GET, produces="application/json")
//	@ResponseBody
    public ReturnModel getDetailAppoinment(@RequestParam("appoinmentId") String appoinmentId) {

		// Khai báo định nghĩa function: 
		int workProgram = 4;
		
		//Setting model mà service xử dụng
		patientService.setReciveObject(appoinmentId);
		
		//patientService.setSessionAuthor(reciveModel.getSessionAuthor());
		
		// Khai báo object trả về client
		rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = patientService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
				rm.setReturnObject(patientService.getReturnObject());
			} if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy dữ liệu!");
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
	
	@RequestMapping(value="/edit-appointment", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    public ReturnModel editAppoinment(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function: 
		int workProgram = 3;
		
		//Setting model mà service xử dụng
		patientService.setReciveObject(reciveModel.getModel());
		
		//patientService.setSessionAuthor(reciveModel.getSessionAuthor());
		
		// Khai báo object trả về client
		rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = patientService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
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
	
	@RequestMapping(value="/deleteAppointment", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    public ReturnModel deleteAppoinment(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function: 
		int workProgram = 5;
		
		//Setting model mà service xử dụng
		patientService.setReciveObject(reciveModel.getModel());
		
		//patientService.setSessionAuthor(reciveModel.getSessionAuthor());
		
		// Khai báo object trả về client
		rm = new ReturnModel();
		
		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = patientService.execute(workProgram);
			
			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorCode(exeNo);
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
	
}
