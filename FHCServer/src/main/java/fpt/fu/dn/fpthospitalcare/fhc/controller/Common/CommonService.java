package fpt.fu.dn.fpthospitalcare.fhc.controller.Common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.model.SpecializationModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.NotificationModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.PositionModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.BuildingModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.FloorModel;
import fpt.fu.dn.fpthospitalcare.fhc.model.RoomModel;

public class CommonService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
			case 1: 
				rs = getSpecializations();
				break;
			case 2: 
				rs = getPositionAll();
				break;
			case 3: 
				rs = getBuilding();
				break;
			case 4: 
				rs = getWorkFloor();
				break;
			case 5: 
				rs = getWorkRoom();
				break;
			case 6: 
				rs = createNotification();
				break;
			case 7: 
				rs = getNotifications();
				break;
			case 8: 
				rs = readNotification();
				break;
			default:
				rs = 0;
				break;
		}
		return rs;
	}
	
	public int getSpecializations() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_FACULTY = "SELECT F.ID , F.NAME FROM FHC_FACULTY F ORDER BY F.NAME;";
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_FACULTY);
			rs = ps.executeQuery();
			
			List<SpecializationModel> specializationList = new ArrayList<>();
			while(rs.next()) {
				SpecializationModel sm = new SpecializationModel();
				sm.setId(rs.getString(1));
				sm.setName(rs.getString(2));
				specializationList.add(sm);
			}
			
			setReturnObject(specializationList);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int getBuilding() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_BUILDING = "SELECT ID, BUILDING_NAME, BUILDING_ASDRESS FROM FHC_BUILDING;";
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_BUILDING);
			rs = ps.executeQuery();
			
			List<BuildingModel> buildingList = new ArrayList<>();
			while(rs.next()) {
				BuildingModel bm = new BuildingModel();
				bm.setBuildingId(rs.getString(1));
				bm.setBuildingName(rs.getString(2));;
				bm.setBuildingAddress(rs.getString(3));
				buildingList.add(bm);
			}
			
			setReturnObject(buildingList);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int getPositionAll() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_POSITION = "SELECT "
				+ "	ID, "
				+ "	ROLE_ID, "
				+ "	POSITION_NAME, "
				+ "	ACTIVE "
				+ "FROM "
				+ "	FHC_POSITION_ROLE "
				+ ";";
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_POSITION);
			rs = ps.executeQuery();
			
			List<PositionModel> positionList = new ArrayList<>();
			while(rs.next()) {
				PositionModel pm = new PositionModel();
				pm.setPosId(rs.getString(1));
				pm.setRoleId(rs.getString(2));;
				pm.setPosName(rs.getString(3));
				pm.setActive(rs.getString(4));
				positionList.add(pm);
			}
			
			setReturnObject(positionList);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int getWorkFloor() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_WORK_FLOOR = "SELECT ID, BUILDING_ID, FLOOR FROM FHC_WORK_FLOOR;";
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_WORK_FLOOR);
			rs = ps.executeQuery();
			
			List<FloorModel> floorList = new ArrayList<>();
			while(rs.next()) {
				FloorModel fm = new FloorModel();
				fm.setId(rs.getString(1));
				fm.setBuildingId(rs.getString(2));;
				fm.setFloor(rs.getString(3));
				floorList.add(fm);
			}
			
			setReturnObject(floorList);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int getWorkRoom() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		String SQL_SELECT_WORK_ROOM = "SELECT ID, FLOOR_ID, ROOM FROM FHC_WORK_ROOM;";
		
		try {
			
			ps = getSC().prepareStatement(SQL_SELECT_WORK_ROOM);
			rs = ps.executeQuery();
			
			List<RoomModel> roomList = new ArrayList<>();
			while(rs.next()) {
				RoomModel room = new RoomModel();
				room.setId(rs.getString(1));
				room.setFloorId(rs.getString(2));;
				room.setRoom(rs.getString(3));
				roomList.add(room);
			}
			
			setReturnObject(roomList);
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int createNotification() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		NotificationModel noti = new NotificationModel();
		ObjectMapper mapper = new ObjectMapper();
		noti = mapper.convertValue(getReciveObject(), noti.getClass());
		
		String SQL_INSET_NOTI = "INSERT INTO FHC_NOTIFICATION "
				+ "	(RECIVE_ROOM, "
				+ "	CONTENT, "
				+ "	[TIME], "
				+ "	LINK, "
				+ "	READED) "
				+ "VALUES(?,?,?,?,?); ";
		
		try {
			
			ps = getSC().prepareStatement(SQL_INSET_NOTI);
			ps.setString(1, noti.getReciveRoom());
			ps.setString(2, noti.getContent());
			ps.setString(3, noti.getTime());
			ps.setString(4, noti.getLink());
			ps.setInt(5, 2);
			int count = ps.executeUpdate();
			
			if (count > 0) {
				setReturnObject(noti);
			} else {
				executeNo = ConstantVariable.ERROR_NUMBER;
			}
		}
		catch (Exception e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
    }
	
	public int getNotifications() throws Exception {

		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		String userId = params.get("userId") == null ? "" : params.get("userId");
		
		String SQL_SEARCH_NOTI = "SELECT "
				+ "	ID, "
				+ "	RECIVE_ROOM, "
				+ "	CONTENT, "
				+ "	[TIME], "
				+ "	LINK, "
				+ "	READED "
				+ "FROM "
				+ "	FHC.dbo.FHC_NOTIFICATION "
				+ "WHERE "
				+ "	RECIVE_ROOM = ? "
				+ "	OR RECIVE_ROOM IN ( "
				+ "	SELECT "
				+ "		RS.ROOM_ID "
				+ "	FROM "
				+ "		ROOM_SOCKET RS "
				+ "	WHERE "
				+ "		RS.USER_ID = ?) "
				+ "ORDER BY "
				+ "	ID DESC;";
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(SQL_SEARCH_NOTI);
			ps.setString(1, userId);
			ps.setString(2, userId);
			rs = ps.executeQuery();
			
			int count = 0;
			List<NotificationModel> notiList = new ArrayList<>();
			while(rs.next()) {
				count++;
				NotificationModel noti = new NotificationModel();
				noti.setNotiId(rs.getString(1));
				noti.setReciveRoom(rs.getString(2));
				noti.setContent(rs.getString(3));
				noti.setTime(rs.getString(4));
				noti.setLink(rs.getString(5));
				noti.setReaded(rs.getString(6));
				notiList.add(noti);
			}
			
			if (count > 0) {
				setReturnObject(notiList);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
			
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
	
	public int readNotification() throws Exception {

		@SuppressWarnings("unchecked")
		HashMap<String, String> params = (HashMap<String, String>) getReciveObject();
		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		String id = params.get("id") == null ? "" : params.get("id");
		
		String SQL_READ_NOTI = "UPDATE "
				+ "	FHC_NOTIFICATION "
				+ "SET "
				+ "	READED = 1 "
				+ "WHERE "
				+ "	ID = ?; ";
		
		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(SQL_READ_NOTI);
			ps.setString(1, id);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		
		return executeNo;
	}
}
