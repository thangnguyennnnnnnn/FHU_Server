package fpt.fu.dn.fpthospitalcare.fhc.controller.Home;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.model.HomeModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@RestController
@RequestMapping("/home")
public class HomeController {
	
	private HomeService home;
	
	private ReturnModel rm;

	@RequestMapping(value="/index", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    public ReturnModel home() {
		int workProgram = 1;
		home = new HomeService();
		rm = new ReturnModel();
		try {
			int exeNo = home.execute(workProgram);
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(home.returnObject);
			}
		} catch (Exception e) {
			rm.setErrorCode(2);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
	
	@RequestMapping(value="/index2", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    public ReturnModel home2(@RequestBody HomeModel homeModel) {
		int workProgram = 2;
		home = new HomeService();
		home.setReciveObject(homeModel);
		rm = new ReturnModel();
		try {
			int exeNo = home.execute(workProgram);
			if (exeNo == ConstantVariable.SUCCESS_NUMBER) {
				rm.setReturnObject(home.returnObject);
			}
		} catch (Exception e) {
			rm.setErrorCode(2);
			rm.setErrorMessage(e.getMessage());
			rm.setReturnObject(null);
		}
		return rm;
    }
}

