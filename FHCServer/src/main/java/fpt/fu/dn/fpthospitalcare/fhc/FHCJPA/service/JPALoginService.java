package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.dto.UserDto;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository.FhcStaffRepository;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository.FhcUserRepository;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository.SocketRepository;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.CryptoService;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@Service
public class JPALoginService {
	
	private final FhcUserRepository fhcUserRepository;
	
	private final SocketRepository socketRepository;
	
	private final FhcStaffRepository fhcStaffRepository;

    public JPALoginService(FhcUserRepository fhcUserRepository, SocketRepository socketRepository, FhcStaffRepository fhcStaffRepository) {
        this.fhcUserRepository = fhcUserRepository;
        this.socketRepository = socketRepository;
        this.fhcStaffRepository = fhcStaffRepository;
    }

    public ReturnModel login(String username, String password) throws Exception {
    	String passwordEncrypt = (new CryptoService()).encrypt(password);
        UserDto userDto = fhcUserRepository.findUserInfo(username, passwordEncrypt);

        if (userDto == null) {
        	ReturnModel returnModel = new ReturnModel();
        	returnModel.setErrorCode(ConstantVariable.DB_NOTFOUND);
        	returnModel.setErrorMessage("Không tìm thấy user");
			return returnModel;
        }
        
        String userId = userDto.getId();
        //String username = userDto.getUsername();
        String role = userDto.getRole();
        String checkUpdatePassword = userDto.getUpdatedAt();
        String fullname = userDto.getFullName();
        String avtUrl = ConstantVariable.STORAGE_CLOUD + userDto.getUrlAvata();
        
        LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentDateValue = currentDate.format(formatter);
        
        fhcUserRepository.activateUser(currentDateValue, username);
        
        String roomId = socketRepository.getRoomId(userId);
        List<String> rooms = new ArrayList<>();
        rooms.add(roomId);
        
        String actived = fhcStaffRepository.getStaffActived(userId);

        HashMap<String, Object> userAuthen = new HashMap<>();
        userAuthen.put("userId", userId);
		userAuthen.put("user", (new CryptoService()).encrypt(username));
		userAuthen.put("userDisp", username);
		userAuthen.put("role", role);
		userAuthen.put("rooms", rooms);
		userAuthen.put("isStaffActive", actived);
		userAuthen.put("isPasswordChanged", checkUpdatePassword);
		userAuthen.put("lastname", fullname);
		userAuthen.put("avt", avtUrl);
		
		ReturnModel returnModel = new ReturnModel(userAuthen);
        return returnModel;
    }
}
