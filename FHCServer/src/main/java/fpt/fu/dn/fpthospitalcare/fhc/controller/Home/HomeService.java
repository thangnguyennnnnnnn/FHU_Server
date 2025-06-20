package fpt.fu.dn.fpthospitalcare.fhc.controller.Home;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.HomeModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.ReturnModel;

@Transactional
@Component
public class HomeService extends FHCCommonService{

	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
			case 1: 
				rs = home();
				break;
			case 2:
				rs = home2();
				break;
			default:
				rs = 0;
				break;
		}
		return rs;
		
	}
	
	public int home() throws SQLException {
		List<HomeModel> h = new ArrayList<>();
		int count = 0;
		try {
			String query = "SELECT * FROM TestDB;";
			ps = getSC().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				HomeModel hm = new HomeModel();
				hm.setA(rs.getString(2));
				hm.setB(rs.getString(3));
				h.add(hm);
			}
			setReturnObject(h);
		} catch (SQLException e) {
			throw e;
		}
		return count;
    }
	
	public int home2() throws SQLException {
		int count = 0;
		try {
			String query = "INSERT INTO TestDB VALUES(?, ?);";
			ps = getSC().prepareStatement(query);
			ps.setNString(1, ((HomeModel) getReciveObject()).getA());
			ps.setNString(2, ((HomeModel) getReciveObject()).getB());
			count = ps.executeUpdate();
			setReturnObject(new ReturnModel());
		} catch (SQLException e) {
			throw e;
		}
		return count;
    }
	
}
