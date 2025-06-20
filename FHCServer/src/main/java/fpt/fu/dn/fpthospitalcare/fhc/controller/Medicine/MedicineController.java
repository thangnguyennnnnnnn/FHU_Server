package fpt.fu.dn.fpthospitalcare.fhc.controller.Medicine;

import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
	
	// Khai báo service: service là nơi implement all logic
	@Autowired
	private MedicineService medicineServive;

	@RequestMapping(value = "/upload-medicine", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel uploadMedicineFile(@RequestParam("file") MultipartFile file,
			@RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 1;

		HashMap<String, Object> param = new HashMap<>();
		param.put("medicinFile", file);
		param.put("userId", userId);

		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Đã thêm các loại thuốc thành công!");
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy thông tin người dùng. Hãy liên hệ admin để được giải quyết!");
			} else if (exeNo == ConstantVariable.FILE_EMPTY) {
				rm.setErrorCode(ConstantVariable.FILE_EMPTY);
				rm.setErrorMessage("File rỗng! Vui lòng thử lại.");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else if (exeNo == ConstantVariable.FILE_FORMAT_ERROR) {
				rm.setErrorCode(ConstantVariable.FILE_FORMAT_ERROR);
				rm.setErrorMessage("Nội dung file không chính xác!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
		} catch(SQLException e) {
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

	@RequestMapping(value = "/search-medicine", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel searchMedicine(@RequestParam("name") String name, @RequestParam("userId") String userId,
			@RequestParam("ingredients") String ingredients, @RequestParam("uses") String uses ,@RequestParam("quantity") String quantity) {
		// Khai báo định nghĩa function:
		int workProgram = 2;

		HashMap<String, Object> param = new HashMap<>();
		param.put("name", name);
		param.put("userId", userId);
		param.put("ingredients", ingredients);
		param.put("uses", uses);
		param.put("quantity", quantity);

		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Tìm kiếm thành công!");
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy thông tin thuốc!");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
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
	
	@RequestMapping(value = "/export-report", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel createReport(@RequestParam("year") String year, @RequestParam("month") String month,
			@RequestParam("reportType") String reportType, @RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 3;

		HashMap<String, Object> param = new HashMap<>();
		param.put("year", year);
		param.put("month", month);
		param.put("reportType", reportType);
		param.put("userId", userId);

		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Tạo báo cáo thành công!");
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy dữ liệu!");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
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

	@RequestMapping(value = "/update-medicine", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel updateMedicine(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("model") String model) {
		// Khai báo định nghĩa function:
		int workProgram = 4;

		HashMap<String, Object> param = new HashMap<>();
		param.put("medicinFile", file);
		param.put("model", model);
		
		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Chỉnh sửa thành công!");
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else if (exeNo == ConstantVariable.FILE_EMPTY) {
				rm.setErrorCode(ConstantVariable.FILE_EMPTY);
				rm.setErrorMessage("File rỗng! Vui lòng thử lại.");
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorCode(ConstantVariable.DUPLICATE_KEY);
				rm.setErrorMessage("Loại thuốc này đã tồn tại!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
		} catch(SQLException e) {
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
	
	@RequestMapping(value = "/search-report", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel searchReport(@RequestParam("year") String year,
			@RequestParam("month") String month, @RequestParam("userId") String userId, @RequestParam("type") String type, @RequestParam("typeValue") String typeValue) {
		// Khai báo định nghĩa function:
		int workProgram = 5;

		HashMap<String, Object> param = new HashMap<>();
		param.put("year", year);
		param.put("month", month);
		param.put("type", type);
		param.put("userId", userId);
		param.put("typeValue", typeValue);
		
		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				if (type.equals("1")) {
					rm.setErrorMessage("Không tìm thấy dữ liệu tháng " + month + "/" + year + "!");
				}
				if (type.equals("2")) {
					rm.setErrorMessage("Không tìm thấy dữ liệu trong tháng " + month + "/" + year);
				}
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
	
	@RequestMapping(value = "/download-report", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel downloadReport(@RequestParam("filename") String filename, @RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 6;

		HashMap<String, Object> param = new HashMap<>();
		param.put("filename", filename);
		param.put("userId", userId);
		
		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else if (exeNo == ConstantVariable.FILE_EMPTY) {
				rm.setErrorCode(ConstantVariable.FILE_EMPTY);
				rm.setErrorMessage("Có lỗi trong việc đọc file! Hãy thử lại.");
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
	
	@RequestMapping(value = "/create-import-request", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel createImportRequest(@RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 7;
		
		HashMap<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		
		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);
		
		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Số lượng các loại thuốc đều đầy đủ, chưa cần nhập thêm.");
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
	
	@RequestMapping(value = "/approve-request", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel approveRequest(@RequestParam("filename") String filename, @RequestParam("userId") String userId, @RequestParam("type") String type) {
		// Khai báo định nghĩa function:
		int workProgram = 8;

		HashMap<String, Object> param = new HashMap<>();
		param.put("filename", filename);
		param.put("userId", userId);
		param.put("type", type);
		
		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(medicineServive.getReturnObject());
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
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
	
	@RequestMapping(value = "/import-quantity-medicine", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel importQuantityFromFile(@RequestParam("file") MultipartFile file,
			@RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 9;

		HashMap<String, Object> param = new HashMap<>();
		param.put("medicinFile", file);
		param.put("userId", userId);

		// Setting model mà service xử dụng
		medicineServive.setReciveObject(param);

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = medicineServive.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Đã thêm số lượng các loại thuốc thành công!");
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
			} else if (exeNo == ConstantVariable.FILE_EMPTY) {
				rm.setErrorCode(ConstantVariable.FILE_EMPTY);
				rm.setErrorMessage("File rỗng! Vui lòng thử lại.");
			} else if (exeNo == ConstantVariable.AUTHOR_ERROR_NUMBER) {
				rm.setErrorCode(ConstantVariable.AUTHOR_ERROR_NUMBER);
				rm.setErrorMessage("Chức năng này không dành cho bạn!");
			} else if (exeNo == ConstantVariable.FILE_FORMAT_ERROR) {
				rm.setErrorCode(ConstantVariable.FILE_FORMAT_ERROR);
				rm.setErrorMessage("Nội dung file không chính xác!");
			} else if (exeNo == ConstantVariable.NUMBER_REQUEST_NOT_FOUND) {
				rm.setErrorCode(ConstantVariable.NUMBER_REQUEST_NOT_FOUND);
				rm.setErrorMessage("Không tìm thấy số yêu cầu!");
			} else if (exeNo == ConstantVariable.NOT_APPROVED) {
				rm.setErrorCode(ConstantVariable.NOT_APPROVED);
				rm.setErrorMessage("Yêu cầu này chưa được chấp thuận, hãy liên hệ admin!");
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy dữ liệu!");
			} else if (exeNo == ConstantVariable.FILE_EMPTY) {
				rm.setErrorCode(ConstantVariable.FILE_EMPTY);
				rm.setErrorMessage("Không tìm thấy dữ liệu!");
			} else if (exeNo == ConstantVariable.FILE_ERROR) {
				rm.setErrorCode(ConstantVariable.FILE_ERROR);
				rm.setErrorMessage("Đọc file bị lỗi! Vui lòng kiểm tra lại dữ liệu trong file.");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
		} catch(SQLException e) {
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
