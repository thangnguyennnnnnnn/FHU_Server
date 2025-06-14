package fpt.fu.dn.fpthospitalcare.fhc.controller.Admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import fpt.fu.dn.fpthospitalcare.fhc.controller.Admin.Model.FeedbackModel;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;

public class AdminService extends FHCCommonService {

	private static final int NURSE_ADMIN_NUMBER = 1;
	
	@Override
	public int run(int workProgram) throws Exception {
		int rs = 0;
		switch (workProgram) {
		case 1:
			rs = createReport();
			break;
		case 2:
			rs = getFeedback();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	public int createReport() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		String userId = (String) param.get("userId");
		String year = (String) param.get("year");
		String month = (String) param.get("month");
		String reportType = (String) param.get("type");
		if (month.length() == 1) {
			month = "0" + month;
		}
		String reportType1;
		String reportType2;
		String reportType3;
		String reportType4;
		String reportType5;
		String[] reportTypeList = reportType.split("/");
		if (reportTypeList.length == 1) {
			reportType1 = reportTypeList[0];
			reportType2 = reportTypeList[0];
			reportType3 = reportTypeList[0];
			reportType4 = reportTypeList[0];
			reportType5 = reportTypeList[0];
		} else if (reportTypeList.length == 5) {
			reportType1 = reportTypeList[0];
			reportType2 = reportTypeList[1];
			reportType3 = reportTypeList[2];
			reportType4 = reportTypeList[3];
			reportType5 = reportTypeList[4];
		} else {
			reportType1 = "";
			reportType2 = "";
			reportType3 = "";
			reportType4 = "";
			reportType5 = "";
		}
		
		

		if (authentication(userId) != NURSE_ADMIN_NUMBER) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}

		URL url = new URL(ConstantVariable.STORAGE_CLOUD + "template/Report-Admin.xlsx");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
		try (InputStream inputStream = connection.getInputStream();
				Workbook report = WorkbookFactory.create(inputStream)) {

			if (reportType1.equals("Medicine")) {
				
				String SQL = "SELECT "
						+ "	TOP(5) * "
						+ "FROM "
						+ "	( "
						+ "	SELECT "
						+ "		MEDICINE_ID, "
						+ "		FMG.NAME , "
						+ "		FMG.PRODUCER , "
						+ "		SUM(FPD.QUANTITY) QUANTITY, "
						+ "		FMG.QUANTITY_UNIT "
						+ "	FROM "
						+ "		FHC_PRESCRIPTION_DETAIL FPD "
						+ "	LEFT JOIN FHC_MEDICAL_GOODS FMG ON "
						+ "		FPD.MEDICINE_ID = FMG.ID "
						+ "	WHERE FPD.[DATE] LIKE :contidion "
						+ "	GROUP BY "
						+ "		MEDICINE_ID, "
						+ "		FMG.NAME, "
						+ "		FMG.PRODUCER , "
						+ "		FMG.QUANTITY_UNIT "
						+ ") TEMP "
						+ "WHERE "
						+ "	QUANTITY != 0 "
						+ "GROUP BY "
						+ "	MEDICINE_ID, "
						+ "	NAME, "
						+ "	PRODUCER , "
						+ "	QUANTITY_UNIT,  "
						+ "	QUANTITY "
						+ "ORDER BY "
						+ "	QUANTITY DESC";
				
				Sheet reportSheet = report.getSheet("THUOC");
				SQL = SQL.replaceAll(":contidion", "'%" + year + "/" + month +"%'");
				ps = getSC().prepareStatement(SQL);
				rs = ps.executeQuery();
				int initRow = 15;
				int initCol = 2;
				writeToFile(6, 8, reportSheet, report, currentDate);
				writeToFile(7, 8, reportSheet, report, getFullname(userId));
				int count = 0;
				while (rs.next()) {
					count++;
					if (count > 5) {
						break;
					}
					writeToFile(initRow, initCol, reportSheet, report, String.valueOf(count));
					initCol = initCol + 1;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(2));
					initCol = initCol + 3;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(3));
					initCol = initCol + 2;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(4) + ' ' + rs.getString(5));
					initCol = 2;
					initRow++;
				}	
			}
			
			if (reportType2.equals("Diseases")) {
				String SQL = "SELECT "
						+ "	TOP(5) * "
						+ "FROM "
						+ "	( "
						+ "	SELECT "
						+ "		FF.NAME, "
						+ "		EXAMINATION_TYPE , "
						+ "		COUNT(EXAMINATION_TYPE) COUNTER "
						+ "	FROM "
						+ "		FHC_APPOINMENT FA "
						+ "	LEFT JOIN FHC_FACULTY FF ON "
						+ "		FA.EXAMINATION_TYPE = FF.ID "
						+ "	WHERE "
						+ "		FA.EXAMINATION_DATE LIKE :contidion "
						+ "	GROUP BY "
						+ "		EXAMINATION_TYPE, "
						+ "		FF.NAME) TEMP "
						+ "GROUP BY "
						+ "	EXAMINATION_TYPE, "
						+ "	NAME, "
						+ "	COUNTER "
						+ "ORDER BY "
						+ "	COUNTER DESC";
				
				Sheet reportSheet = report.getSheet("BENH");
				SQL = SQL.replaceAll(":contidion", "'%" + year + "-" + month +"%'");
				ps = getSC().prepareStatement(SQL);
				rs = ps.executeQuery();
				int initRow = 15;
				int initCol = 2;
				writeToFile(6, 8, reportSheet, report, currentDate);
				writeToFile(7, 8, reportSheet, report, getFullname(userId));
				int count = 0;
				while (rs.next()) {
					count++;
					if (count > 5) {
						break;
					}
					writeToFile(initRow, initCol, reportSheet, report, String.valueOf(count));
					initCol = initCol + 1;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(1));
					initCol = initCol + 3;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(2));
					initCol = initCol + 2;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(3));
					initCol = 2;
					initRow++;
				}
			}
			
			if (reportType3.equals("Patient")) {
				String SQL = "SELECT "
						+ "	FAC.APPOINTMENT_ID, "
						+ "	FAC.NANE, "
						+ "	FF.NAME BENH, "
						+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME DOCTOR, "
						+ "	FDR.DIAGNOSIS_START_TIME + ' - ' + FDR.DIAGNOSIS_END_TIME, "
						+ "	FAC.PLACE , "
						+ "	FDR.DIAGNOSTIC_RESULT  "
						+ "FROM "
						+ "	FHC_APPOINTMENT_CARD FAC "
						+ "LEFT JOIN FHC_DIAGNOSTIC_RESULT FDR ON "
						+ "	FAC.APPOINTMENT_ID = FDR.APPOINTMENT_ID "
						+ "LEFT JOIN FHC_FACULTY FF ON "
						+ "	FAC.FACULTY_ID = FF.ID "
						+ "LEFT JOIN FHC_USER_INFORMATION FUI ON "
						+ "	FDR.DIAGNOSTIC_DOCTOR_ID = FUI.USER_ID "
						+ "WHERE FAC.APPMOINTMENT_DATE LIKE :contidion "
						+ "ORDER BY FAC.APPMOINTMENT_DATE DESC";
				
				Sheet reportSheet = report.getSheet("LICH_KHAM");
				SQL = SQL.replaceAll(":contidion", "'%" + year + "-" + month +"%'");
				ps = getSC().prepareStatement(SQL);
				rs = ps.executeQuery();
				int initRow = 15;
				int initCol = 2;
				writeToFile(6, 12, reportSheet, report, currentDate);
				writeToFile(7, 12, reportSheet, report, getFullname(userId));
				int count = 0;
				int totalRow = 20;
				int countRecord = 0;
				int numOfSheet = 1;
				int appointmentId_Index = 3;
				int patientName_Index = 6;
				int facultyName_Index = 8;
				int doctorName_Index = 9;
				int time_Index = 10;
				int place_Index = 11;
				int result_Index = 12;
				Sheet reportSheetT = report.cloneSheet(1);
				while (rs.next()) {
					count++;
					if (countRecord == totalRow) {
						numOfSheet += 1;
						reportSheet = report.cloneSheet(report.getSheetIndex(reportSheetT));
						report.setSheetName(report.getSheetIndex(reportSheetT), "LICH_KHAM_" + numOfSheet);
						writeToFile(6, 12, reportSheet, report, currentDate);
						writeToFile(7, 12, reportSheet, report, getFullname(userId));
						countRecord = 0;
					}
					writeToFile(initRow, initCol, reportSheet, report, String.valueOf(count));
					writeToFile(initRow, appointmentId_Index, reportSheet, report, rs.getString(1));
					writeToFile(initRow, patientName_Index, reportSheet, report, rs.getString(2));
					writeToFile(initRow, facultyName_Index, reportSheet, report, rs.getString(3));
					writeToFile(initRow, doctorName_Index, reportSheet, report, rs.getString(4));
					writeToFile(initRow, time_Index, reportSheet, report, rs.getString(5));
					writeToFile(initRow, place_Index, reportSheet, report, rs.getString(6));
					writeToFile(initRow, result_Index, reportSheet, report, rs.getString(7));
					initRow++;
					countRecord += 1;
				}
				report.removeSheetAt(report.getSheetIndex(reportSheetT));
			}
			
			if (reportType4.equals("Doctor")) {
				String SQL = "SELECT "
						+ "	TOP(5) * "
						+ "FROM "
						+ "	( "
						+ "	SELECT "
						+ "		DOCTOR_ID, "
						+ "		FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME FULLNAME, "
						+ "		FUI.EMAIL, "
						+ "		COUNT(DOCTOR_ID) COUNTER "
						+ "	FROM "
						+ "		FHC_APPOINMENT FA "
						+ "	LEFT JOIN FHC_USER_INFORMATION FUI ON "
						+ "		FA.DOCTOR_ID = FUI.USER_ID "
						+ "	WHERE FA.EXAMINATION_DATE LIKE '%2023-08%' "
						+ "	GROUP BY "
						+ "		DOCTOR_ID , "
						+ "		FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME, "
						+ "		FUI.EMAIL) TEMP "
						+ "GROUP BY "
						+ "	DOCTOR_ID , "
						+ "	FULLNAME, "
						+ "	EMAIL, "
						+ "	COUNTER "
						+ "ORDER BY "
						+ "	COUNTER DESC";
				
				Sheet reportSheet = report.getSheet("BAC_SI");
				SQL = SQL.replaceAll(":contidion", "'%" + year + "-" + month +"%'");
				ps = getSC().prepareStatement(SQL);
				rs = ps.executeQuery();
				int initRow = 15;
				int initCol = 2;
				writeToFile(6, 8, reportSheet, report, currentDate);
				writeToFile(7, 8, reportSheet, report, getFullname(userId));
				int count = 0;
				while (rs.next()) {
					count++;
					if (count > 5) {
						break;
					}
					writeToFile(initRow, initCol, reportSheet, report, String.valueOf(count));
					writeToFile(initRow, 3, reportSheet, report, rs.getString(2));
					writeToFile(initRow, 6, reportSheet, report, rs.getString(3));
					writeToFile(initRow, 8, reportSheet, report, rs.getString(4));
					initCol = 2;
					initRow++;
				}
			}
			
			if (reportType5.equals("Employee")) {
				String SQL = "SELECT "
						+ "	FUI.FIRST_NAME + ' ' + FUI.MIDDLE_NAME + ' ' + FUI.LAST_NAME FULLNAME, "
						+ "	FPR.POSITION_NAME + ': ' + FF.NAME POSITION_SUM, "
						+ "	FS.CREATE_AT "
						+ "FROM "
						+ "	FHC_STAFF FS "
						+ "LEFT JOIN FHC_USER_INFORMATION FUI ON "
						+ "	FS.STAFF_ID = FUI.USER_ID "
						+ "LEFT JOIN FHC_POSITION_ROLE FPR ON "
						+ "	FS.POSITION_ID = FPR.ID "
						+ "LEFT JOIN FHC_FACULTY FF ON "
						+ "	FS.FACULTY_ID = FF.ID "
						+ "ORDER BY "
						+ "	FPR.POSITION_NAME";
				
				Sheet reportSheet = report.getSheet("NHAN_VIEN");

				ps = getSC().prepareStatement(SQL);
				rs = ps.executeQuery();
				int initRow = 15;
				int initCol = 2;
				writeToFile(6, 8, reportSheet, report, currentDate);
				writeToFile(7, 8, reportSheet, report, getFullname(userId));
				int count = 0;
				int totalRow = 20;
				int countRecord = 0;
				int numOfSheet = 1;
				int name_Index = 3;
				int position_Index = 6;
				int createDate_Index = 8;
				Sheet reportSheetT = report.cloneSheet(1);
				while (rs.next()) {
					count++;
					if (countRecord == totalRow) {
						numOfSheet += 1;
						reportSheet = report.cloneSheet(report.getSheetIndex(reportSheetT));
						report.setSheetName(report.getSheetIndex(reportSheetT), "NHAN_VIEN_" + numOfSheet);
						writeToFile(6, 8, reportSheet, report, currentDate);
						writeToFile(7, 8, reportSheet, report, getFullname(userId));
						countRecord = 0;
					}
					writeToFile(initRow, initCol, reportSheet, report, String.valueOf(count));
					writeToFile(initRow, name_Index, reportSheet, report, rs.getString(1));
					writeToFile(initRow, position_Index, reportSheet, report, rs.getString(2));
					writeToFile(initRow, createDate_Index, reportSheet, report, rs.getString(3));
					initRow++;
					countRecord += 1;
				}
				report.removeSheetAt(report.getSheetIndex(reportSheetT));
			}
			
			String projectId = "fhc-server-394702";
	        String bucketName = "fhc-server-394702.appspot.com";

	        String filename = "/Report/" + getCurrentDate("yyyyMMdd") +"/Report_" + getCurrentDate("ddMMyyyHHmmss") + ".xlsx";
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, filename);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			report.write(byteArrayOutputStream);

			byte[] bytes = byteArrayOutputStream.toByteArray();
			
			storage.create(blobInfo, bytes);
			String base64Data = Base64.getEncoder().encodeToString(bytes);

			ps.close();
			
			String SQL_INSERT_REPORT = "INSERT INTO FHC_MEDICINE_REPORT "
					+ "	(REPORT_NAME, "
					+ "	REPORT_FILE, "
					+ "	REPORT_MONTH, "
					+ "	REPORT_YEAR, "
					+ "	CREATE_AT, "
					+ "	CREATE_USER, "
					+ "	TYPE_REPORT, "
					+ " APPROVE, "
					+ " APPROVED_BY, "
					+ " APPROVED_AT) "
					+ "VALUES(?,?,?,?,?,?,1,1,?,?); ";
			
			ps = getSC().prepareStatement(SQL_INSERT_REPORT);
			ps.setString(1, reportType);
			ps.setString(2, filename);
			ps.setString(3, month);
			ps.setString(4, year);
			ps.setString(5, currentDate);
			ps.setString(6, userId);
			ps.setString(7, userId);
			ps.setString(8, currentDate);
			ps.executeUpdate();
			setReturnObject(base64Data);

		} catch (IOException e) {
			executeNo = ConstantVariable.FILE_ERROR;
			return executeNo;
		} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	public int getFeedback() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		String userId = (String) param.get("userId");
		String date = (String) param.get("date");
		String id = (String) param.get("id");
		
		if (authentication(userId) != NURSE_ADMIN_NUMBER) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		String SQL_SELECT_FB = "SELECT "
				+ "	ID, "
				+ "	NAME, "
				+ "	EMAIL, "
				+ "	SUBJECT, "
				+ "	MESSAGE, "
				+ "	CREATE_AT, "
				+ "	IS_READED "
				+ "FROM "
				+ "	FHC_FEEDBACK "
				+ "WHERE ";
		
		
		if (!id.equals("")) {
			SQL_SELECT_FB += "ID = " + id + " "; 
		} else {
			String newDate = "";
			if (!date.equals("")) {
				newDate = convertDate(date, "yyyy-MM-dd", "dd/MM/yyyy");
			}
			SQL_SELECT_FB += "CREATE_AT LIKE '%" + newDate + "%' "; 
		}

		try {
			// Tạo câu sql
			ps = getSC().prepareStatement(SQL_SELECT_FB);
			rs = ps.executeQuery();
			
			List<FeedbackModel> feedbackList = new ArrayList<>();
			while(rs.next()) {
				FeedbackModel feedbackModel = new FeedbackModel();
				feedbackModel.setId(rs.getString(1));
				feedbackModel.setName(rs.getString(2));
				feedbackModel.setEmail(rs.getString(3));
				feedbackModel.setSubject(rs.getString(4));
				feedbackModel.setMessage(rs.getString(5));
				feedbackModel.setCreateDate(rs.getString(6));
				feedbackList.add(feedbackModel);
			}
			
			if (feedbackList.size() > 0) {
				setReturnObject(feedbackList);
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
			}
		} catch (SQLException e) {
			executeNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		return executeNo;
	}
}
