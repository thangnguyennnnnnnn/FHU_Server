package fpt.fu.dn.fpthospitalcare.fhc.controller.User;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReciveModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/user")
public class UserController {

	// "/signup": URL mapping localhot:user/signup
	@RequestMapping(value = "/signin", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel signup(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 1;

		// Khai báo service: service là nơi implement all logic
		UserService userService = new UserService();

		// Setting model mà service xử dụng
		userService.setReciveObject(reciveModel.getModel());

		// userService.setSessionAuthor(reciveModel.getSessionAuthor());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = userService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {

				// Nếu OK thì setting object return về client
				if (userService.getReturnObject() != null) {
					rm.setReturnObject(userService.getReturnObject());
				} else {
					rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
					rm.setErrorMessage("Có lỗi xảy ra");
				}
			} else if (exeNo == ConstantVariable.DUPLICATE_KEY) {
				rm.setErrorCode(ConstantVariable.DUPLICATE_KEY);
				rm.setErrorMessage("Lỗi Trùng Session");
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy user");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Có lỗi xảy ra");
			}
			// rm.setSessionAuthor(userService.sessionAuthor);

		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}

	@RequestMapping(value = "/getProfile", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ReturnModel getProfile(@RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 3;

		// Khai báo service: service là nơi implement all logic
		UserService userService = new UserService();

		// Setting model mà service xử dụng
		userService.setReciveObject(userId);

		// userService.setSessionAuthor(reciveModel.getSessionAuthor());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = userService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {

				// Nếu OK thì setting object return về client
				if (userService.getReturnObject() != null) {
					rm.setReturnObject(userService.getReturnObject());
				} else {
					rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
					rm.setErrorMessage("Có lỗi xảy ra");
				}
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy thông tin người dùng. Hãy liên hệ admin để được giải quyết!");
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

	/**
	 * Không dùng nữa
	 * 
	 * @param reciveModel
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel logout(@RequestBody ReciveModel reciveModel) {

		// Khai báo định nghĩa function:
		int workProgram = 2;

		// Khai báo service: service là nơi implement all logic
		UserService userService = new UserService();

		// Setting model mà service xử dụng
		userService.setReciveObject(reciveModel.getModel());

		// userService.setSessionAuthor(reciveModel.getSessionAuthor());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = userService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				// rm.setSessionAuthor(userService.sessionAuthor);
				rm.setReturnObject(userService.getReturnObject());
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Đã xảy ra lỗi bất thường - liên hệ admin để được xử lý!");
			}

		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
	
	/**
	 * Edit user profile
	 * @param reciveModel
	 * @return
	 */
	@RequestMapping(value = "/update-profile", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel updateProfile(@RequestBody ReciveModel reciveModel) {
		int workProgram = 4;
		UserService userService = new UserService();
		userService.setReciveObject(reciveModel.getModel());
		ReturnModel rm = new ReturnModel();
		try {
			int exeNo = userService.execute(workProgram);
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(userService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Không tìm thấy thông tin cập nhật!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Đã xảy ra lỗi bất thường - liên hệ admin để được xử lý!");
			}
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
	
	/**
	 * Đổi mật khẩu
	 * @param reciveModel
	 * @return
	 */
	@RequestMapping(value = "/change-password", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel changePassword(@RequestBody ReciveModel reciveModel) {
		int workProgram = 5;
		UserService userService = new UserService();
		userService.setReciveObject(reciveModel.getModel());
		ReturnModel rm = new ReturnModel();
		try {
			int exeNo = userService.execute(workProgram);
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(userService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(exeNo);
				rm.setErrorMessage("Không tìm thấy thông tin cập nhật! Hãy kiểm tra lại mật khẩu của bạn!");
			} else {
				rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
				rm.setErrorMessage("Đã xảy ra lỗi bất thường - liên hệ admin để được xử lý!");
			}
		} catch (Exception e) {
			rm.setErrorCode(ConstantVariable.ERROR_NUMBER);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
	}
	
	@RequestMapping(value = "/upload-avata", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ReturnModel uploadAvata(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
		// Khai báo định nghĩa function:
		int workProgram = 6;

		// Khai báo service: service là nơi implement all logic
		UserService userService = new UserService();

		HashMap<String, Object> param = new HashMap<>();
		param.put("avata", file);
		param.put("userId", userId);
		
		// Setting model mà service xử dụng
		userService.setReciveObject(param);

		// userService.setSessionAuthor(reciveModel.getSessionAuthor());

		// Khai báo object trả về client
		ReturnModel rm = new ReturnModel();

		try {
			// Main thực thi chức năng: Mỗi controller sẽ đều gọi đến fucniton execute
			int exeNo = userService.execute(workProgram);

			// kiểm tra kết quả xử lý
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setErrorMessage("Ảnh đại diện của bạn đã được update thành công!");
				rm.setErrorCode(ConstantVariable.SUCCESS_NUMBER);
				rm.setReturnObject(userService.getReturnObject());
			} else if (exeNo == ConstantVariable.DB_NOTFOUND) {
				rm.setErrorCode(ConstantVariable.DB_NOTFOUND);
				rm.setErrorMessage("Không tìm thấy thông tin người dùng. Hãy liên hệ admin để được giải quyết!");
			} else if (exeNo == ConstantVariable.FILE_EMPTY) {
				rm.setErrorCode(ConstantVariable.FILE_EMPTY);
				rm.setErrorMessage("Có lỗi khi upload file! Vui lòng thử lại.");
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
