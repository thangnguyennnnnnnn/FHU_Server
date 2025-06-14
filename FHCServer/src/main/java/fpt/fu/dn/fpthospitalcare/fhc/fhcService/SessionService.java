package fpt.fu.dn.fpthospitalcare.fhc.fhcService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SessionService {
	
	private HashMap<String, String> listSession;
	
	public HashMap<String, String> getListSession() {
		return listSession;
	}

	public void setListSession(HashMap<String, String> listSession) {
		this.listSession = listSession;
	}

	
	public int checkSession(String sessionAuthor, Connection c) throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		try {
			String SELECT_SESSION = "SELECT * FROM MSession WHERE SessionAuthor = ?";
			PreparedStatement ps;
			ResultSet rs;
			ps = c.prepareStatement(SELECT_SESSION);
			ps.setString(1, sessionAuthor);
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				do {
					listSession.put(rs.getString(1), rs.getString(2));
				} while (rs.next());
			} else {
				executeNo = ConstantVariable.OUT_SESSION;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
		return executeNo;
	}
	
	//public int checkSessionLogin() {
	//	if (listSession.containsKey("USER_LOGIN")) {
			//String 
	//	}
	//}
}
