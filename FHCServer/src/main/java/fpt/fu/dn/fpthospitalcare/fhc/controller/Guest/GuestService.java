package fpt.fu.dn.fpthospitalcare.fhc.controller.Guest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.naming.factory.SendMailFactory;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.MailService;

public class GuestService extends FHCCommonService {
	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
		case 1:
			rs = submitFeedback();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	public int submitFeedback() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		String name = (String) param.get("name");
		String email = (String) param.get("email");
		String subject = (String) param.get("subject");
		String message = (String) param.get("message");
		
		String SQL_SEND_FEEDBACK = "INSERT INTO FHC_FEEDBACK "
				+ "	(NAME, "
				+ "	EMAIL, "
				+ "	SUBJECT, "
				+ "	MESSAGE, "
				+ "	CREATE_AT, "
				+ "	IS_READED) "
				+ "VALUES(?,?,?,?,?,?); ";
		
		if (name.equals("") && email.equals("")
				&& subject.equals("") && message.equals("")) {
			executeNo = ConstantVariable.INPUT_EMPTY_VALUE;
			return executeNo;
		} 

		
		try {
			int generatedId;
			// Tạo câu sql	
			ps = getSC().prepareStatement(SQL_SEND_FEEDBACK, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, subject);
			ps.setString(4, message);
			ps.setString(5, getCurrentDate("dd/MM/yyyy HH:mm:ss"));
			ps.setString(6, "2");
			int count = ps.executeUpdate();
			if (count > 0) {
				rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                generatedId = rs.getInt(1);
	                setReturnObject(generatedId);
	            }
			}
			//MailService mail = new MailService();
			//mail.sendMail("thang24052001nguyen@gmail.com", subject, message, null);
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		return executeNo;
	}
}
