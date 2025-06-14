package fpt.fu.dn.fpthospitalcare.fhc.controller.Medicine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import fpt.fu.dn.fpthospitalcare.fhc.model.MedicineModel;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.ConstantVariable;
import fpt.fu.dn.fpthospitalcare.fhc.fhcService.FHCCommonService;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Medicine.Model.MedicineImportModel;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Medicine.Model.FileInfo;
import fpt.fu.dn.fpthospitalcare.fhc.controller.Medicine.Model.WorkingTime;

public class MedicineService extends FHCCommonService {

	@Override
	public int run(int workProgram) throws Exception {
		int rs = ConstantVariable.SUCCESS_NUMBER;
		switch (workProgram) {
		case 1:
			rs = importMedicines();
			break;
		case 2:
			rs = searchMedicine();
			break;
		case 3:
			rs = createReport();
			break;
		case 4:
			rs = updateMedicine();
			break;
		case 5:
			rs = searchReport();
			break;
		case 6:
			rs = downloadReport();
			break;
		case 7:
			rs = createImportRequest();
			break;
		case 8:
			rs = approveRequest();
			break;
		case 9:
			rs = importQuantityFromFile();
			break;
		default:
			rs = 0;
			break;
		}
		return rs;
	}
	
	public int importMedicines() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		MultipartFile medicinFile = (MultipartFile) param.get("medicinFile");
		String userId = (String)param.get("userId");
		
		if (authentication(userId) != 5) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		if (medicinFile == null || medicinFile.isEmpty()) {
            return ConstantVariable.ERROR_NUMBER;
        }
		StringBuilder sqlCreate = new StringBuilder();
		
		String SQL_INSERT_MEDICINE = "INSERT INTO FHC_MEDICAL_GOODS "
				+ "	(NAME, "
				+ "	DESCRIPTION, "
				+ "	QUANTITY, "
				+ "	IS_MEDICINE, "
				+ "	IS_ACTIVE, "
				+ "	DRUG_INGREDIENTS, "
				+ "	URL_IMAGE, "
				+ " PRODUCER, "
				+ " QUANTITY_UNIT,"
				+ " BASIC_QUANTITY ) VALUES";
		
		//String projectId = "fhc-server-394702";
        //String bucketName = "fhc-server-394702.appspot.com";
		
		sqlCreate.append(SQL_INSERT_MEDICINE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(medicinFile.getInputStream()));
		//Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		try {
	        String line;
	        int countRecord = 0;
	        while ((line = reader.readLine()) != null) {
	            String[] medicinInfo = line.split("\",\"");

	            if (medicinInfo.length < 6) {
	            	executeNo = ConstantVariable.FILE_FORMAT_ERROR;
	            	return executeNo;
	            }
	            
	            String cloudPath = "img/medicine.jpg";
	            
	            sqlCreate.append("(");
	            sqlCreate.append("N'" + medicinInfo[0] + "',");
	            sqlCreate.append("N'" + medicinInfo[1] + "',");
	            sqlCreate.append("'0',");
	            sqlCreate.append("'1',");
	            sqlCreate.append("'1',");
	            sqlCreate.append("N'" + medicinInfo[3] + "',");
	            sqlCreate.append("'" + cloudPath + "',");
	            sqlCreate.append("N'" + medicinInfo[2] + "',");
	            sqlCreate.append("N'" + medicinInfo[4] + "',");
	            sqlCreate.append("" + medicinInfo[5] + "");
	            sqlCreate.append("),");
		        
//		        Path filePath = Paths.get(medicinInfo[4]);
//		        byte[] imageData = Files.readAllBytes(filePath);;
//		        BlobId blobId = BlobId.of(bucketName, cloudPath);
//		        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/*").build();
//		        storage.create(blobInfo, imageData);
	            countRecord++;
	        }
	        
	        if (countRecord > 0) {
	        	ps = getSC().prepareStatement(sqlCreate.toString().trim().replaceFirst(",$", ""));
	        	int count = 0;
	        	count = ps.executeUpdate();
	        	
	        	if (count != countRecord) {
	        		executeNo = ConstantVariable.FILE_FORMAT_ERROR;
	        	}
	        } else {
	        	return ConstantVariable.FILE_EMPTY;
	        }
	        
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		} finally {
			reader.close();
		}

		return executeNo;
	}
	
	public int searchMedicine() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		String userId = (String)param.get("userId");
		String ingredients = (String)param.get("ingredients");
		String uses = (String)param.get("uses");
		String name = (String)param.get("name");
		String quantity = (String)param.get("quantity");
		int quantityCount = 0;
		try {
			quantityCount = Integer.parseInt(quantity); 
		} catch (Exception e) {
			quantityCount = -1;
		}
		int role = authentication(userId);
		if (role != 5 && role != 2) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
		}

		StringBuilder sqlSelect = new StringBuilder();
		
		String SQL_SELECT_MEDICINE = "SELECT "
				+ "	ID, "
				+ "	NAME, "
				+ "	DESCRIPTION, "
				+ "	QUANTITY, "
				+ "	IS_MEDICINE, "
				+ "	IS_ACTIVE, "
				+ "	DRUG_INGREDIENTS, "
				+ "	URL_IMAGE, "
				+ "	PRODUCER, "
				+ " QUANTITY_UNIT "
				+ "FROM "
				+ "	FHC_MEDICAL_GOODS ";	
		
		sqlSelect.append(SQL_SELECT_MEDICINE);
		
		/**
		 * Validate các item 
		 */
		if (ingredients.equals("") && uses.equals("")
				&& name.equals("") && quantity.equals("")) {
			executeNo = ConstantVariable.INPUT_EMPTY_VALUE;
			return executeNo;
		} else {
			sqlSelect.append("WHERE ");
		}
		
		boolean isAppend = false;
		
		if (!name.equals("")) {
			if (!isAppend) {
				sqlSelect.append("NAME LIKE N'%" + name + "%' ");
			} else {
				sqlSelect.append("AND NAME LIKE N'%" + name + "%' ");
			}
			isAppend = true;
		}
		
		if (!ingredients.equals("")) {
			if (!isAppend) {
				sqlSelect.append("DRUG_INGREDIENTS LIKE N'%" + ingredients + "%' ");
			} else {
				sqlSelect.append("AND DRUG_INGREDIENTS LIKE N'%" + ingredients + "%' ");
			}
			isAppend = true;
		}
		
		if (!uses.equals("")) {
			if (!isAppend) {
				sqlSelect.append("DESCRIPTION LIKE N'%" + uses + "%' ");
			} else {
				sqlSelect.append("AND DESCRIPTION LIKE N'%" + uses + "%' ");
			}
			isAppend = true;
		}
		
		if (quantityCount >= 0) {
			if (!isAppend) {
				sqlSelect.append("QUANTITY <= " + quantityCount + " ");
			} else {
				sqlSelect.append("AND QUANTITY <= " + quantityCount + " ");
			}
			isAppend = true;
		}
		
		try {

	        ps = getSC().prepareStatement(sqlSelect.toString());
	        rs = ps.executeQuery();
	        List<MedicineModel> medicineList = new ArrayList<>();
	        while (rs.next()) {
	        	MedicineModel medicine = new MedicineModel();
	        	medicine.setId(rs.getString(1));
	        	medicine.setName(rs.getString(2));
	        	medicine.setDescription(rs.getString(3));
	        	medicine.setQuantity(rs.getInt(4));
	        	medicine.setIsActive(rs.getString(6));
	        	medicine.setDrugIngredients(rs.getString(7));
	        	medicine.setUrlImage(ConstantVariable.STORAGE_CLOUD + rs.getString(8));
	        	medicine.setProducer(rs.getString(9));
	        	medicine.setUnit(rs.getString(10) == null ? "Không xác định" : rs.getString(10));
	        	
	        	medicineList.add(medicine);
	        }

	        if (medicineList.size() == 0) {
	        	executeNo = ConstantVariable.DB_NOTFOUND;
	        	return executeNo;
	        }
	        setReturnObject(medicineList);
		} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	public int updateMedicine() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		MultipartFile medicinFile = (MultipartFile) param.get("medicinFile");
		String model = (String)param.get("model");
		
		MedicineImportModel medicineImport = new MedicineImportModel();
		ObjectMapper mapper = new ObjectMapper();
		medicineImport = mapper.readValue(model, MedicineImportModel.class);
		
		if (authentication(medicineImport.getUserId()) != 5) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		String SQL_INSERT_MEDICINE = "UPDATE "
				+ "	FHC_MEDICAL_GOODS "
				+ "SET "
				+ "	DESCRIPTION = ?, "
				+ "	DRUG_INGREDIENTS = ?, "
				+ "	URL_IMAGE = ?, "
				+ "	NAME = ?, "
				+ "	PRODUCER = ? "
				+ "WHERE "
				+ "	ID = ? ;";
		
		String SQL_INSERT_MEDICINE_NO_IMG = "UPDATE "
				+ "	FHC_MEDICAL_GOODS "
				+ "SET "
				+ "	DESCRIPTION = ?, "
				+ "	DRUG_INGREDIENTS = ?, "
				+ "	NAME = ?, "
				+ "	PRODUCER = ? "
				+ "WHERE "
				+ "	ID = ? ;";

		try {
	        int countRecord = 0;
	        
	        String currentDate = getCurrentDate("yyyyMMdd");
            String filename = medicineImport.getMedicineName().trim().replaceAll(" ", "");
            String cloudPath = "Medicine/" + currentDate + "/" + filename;
            if (medicinFile == null || medicinFile.isEmpty()) {
            	ps = getSC().prepareStatement(SQL_INSERT_MEDICINE_NO_IMG);
            	ps.setNString(1, medicineImport.getMedicineUses()); 
    	        ps.setNString(2, medicineImport.getMedicineIngredients());
    	        ps.setNString(3, medicineImport.getMedicineName());
    	        ps.setNString(4, medicineImport.getMedicineProducer());
    	        ps.setNString(5, medicineImport.getMedicineId());
            } else {
            	ps = getSC().prepareStatement(SQL_INSERT_MEDICINE);
            	ps.setNString(1, medicineImport.getMedicineUses()); 
    	        ps.setNString(2, medicineImport.getMedicineIngredients());
    	        ps.setString(3, cloudPath);
    	        ps.setNString(4, medicineImport.getMedicineName());
    	        ps.setNString(5, medicineImport.getMedicineProducer());
    	        ps.setNString(6, medicineImport.getMedicineId());
            }
	        countRecord = ps.executeUpdate();
	        
	        if (countRecord <= 0) {
	        	executeNo = ConstantVariable.ERROR_NUMBER;
	        	return executeNo;
	        }
	        
	        if (medicinFile != null && !medicinFile.isEmpty()) {
	        	String projectId = "fhc-server-394702";
	        	String bucketName = "fhc-server-394702.appspot.com";

	        	Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
	        
	        	byte[] imageData = medicinFile.getBytes();
            
	        	BlobId blobId = BlobId.of(bucketName, cloudPath);
	        	BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(medicinFile.getContentType()).build();
            
	        	storage.create(blobInfo, imageData);
            }
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}

	public int createReport() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();

		String userId = (String) param.get("userId");
		String year = (String) param.get("year");
		String month = (String) param.get("month");
		String reportType = (String) param.get("reportType");
		
		String reportType1;
		String reportType2;
		String reportType3;
		String[] reportTypeList = reportType.split("/");
		if (reportTypeList.length == 1) {
			reportType1 = reportTypeList[0];
			reportType2 = reportTypeList[0];
			reportType3 = reportTypeList[0];
		} else if (reportTypeList.length == 3) {
			reportType1 = reportTypeList[0];
			reportType2 = reportTypeList[1];
			reportType3 = reportTypeList[2];
		} else {
			reportType1 = "";
			reportType2 = "";
			reportType3 = "";
		}
		
		

		if (authentication(userId) != 5) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}

		StringBuilder sqlSelect = new StringBuilder();

		String SQL_SELECT_MEDICINE = "SELECT " 
				+ "	ID, " 
				+ "	NAME, " 
				+ "	DESCRIPTION, " 
				+ "	QUANTITY, "
				+ "	IS_MEDICINE, " 
				+ "	IS_ACTIVE, " 
				+ "	DRUG_INGREDIENTS, " 
				+ "	URL_IMAGE, " 
				+ "	PRODUCER, "
				+ " QUANTITY_UNIT "
				+ "FROM " 
				+ "	FHC_MEDICAL_GOODS ";
		
		String SQL_REPORT_2 = "SELECT  "
				+ "	FMG.NAME, "
				+ "	SUM(FPD.QUANTITY) NEWQUANTITY, "
				+ "	OLDMONTH.OLDQUANTITY, "
				+ "	CASE WHEN SUM(FPD.QUANTITY) > OLDMONTH.OLDQUANTITY THEN N'Tăng'  "
				+ "		 WHEN SUM(FPD.QUANTITY) < OLDMONTH.OLDQUANTITY THEN N'Giảm' "
				+ "		 WHEN SUM(FPD.QUANTITY) = OLDMONTH.OLDQUANTITY THEN N'Bằng' "
				+ "		 ELSE N'Không có dữ liệu của tháng trước' "
				+ "	END AS COMPARE, "
				+ "	FPD.UNIT "
				+ "FROM FHC_PRESCRIPTION_DETAIL FPD "
				+ "LEFT JOIN FHC_MEDICAL_GOODS FMG ON FPD.MEDICINE_ID = FMG.ID "
				+ "LEFT JOIN (SELECT  "
				+ "		FMG.NAME AS NAME, "
				+ "		SUM(FPD.QUANTITY) AS OLDQUANTITY "
				+ "	FROM FHC_PRESCRIPTION_DETAIL FPD "
				+ "	LEFT JOIN FHC_MEDICAL_GOODS FMG ON FPD.MEDICINE_ID = FMG.ID "
				+ "	WHERE FPD.[DATE] LIKE :param1 "
				+ "	GROUP BY FMG.NAME "
				+ "	) OLDMONTH ON FMG.NAME = OLDMONTH.NAME "
				+ "WHERE FPD.[DATE] LIKE :param2 "
				+ "GROUP BY FMG.NAME, OLDMONTH.OLDQUANTITY, FPD.UNIT";

		sqlSelect.append(SQL_SELECT_MEDICINE);

		URL url = new URL(ConstantVariable.STORAGE_CLOUD + "template/Report.xlsx");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
		try (InputStream inputStream = connection.getInputStream();
				Workbook report = WorkbookFactory.create(inputStream)) {

			if (reportType1.equals("quantity")) {
				Sheet reportSheet;

				ps = getSC().prepareStatement(sqlSelect.toString());
				rs = ps.executeQuery();
				List<MedicineModel> medicineList = new ArrayList<>();
				while (rs.next()) {
					MedicineModel medicine = new MedicineModel();
					medicine.setId(rs.getString(1));
					medicine.setName(rs.getString(2));
					medicine.setDescription(rs.getString(3));
					medicine.setQuantity(rs.getInt(4));
					medicine.setIsActive(rs.getString(6));
					medicine.setDrugIngredients(rs.getString(7));
					medicine.setUrlImage(ConstantVariable.STORAGE_CLOUD + rs.getString(8));
					medicine.setProducer(rs.getString(9));
					medicine.setUnit(rs.getString(10));
					medicineList.add(medicine);
				}
				
				int numOfSheet = 1;
				if (medicineList.size() > 20) {
					int modSheet = medicineList.size() % 20;
					numOfSheet = medicineList.size() / 20;
					if (modSheet != 0) {
						numOfSheet += 1;
					}
				}
				for (int n = 1; n <= numOfSheet; n++) {
					if (n != 1) {
						reportSheet = report.cloneSheet(0);
						report.setSheetName(report.getSheetIndex(reportSheet), "BAO_CAO_" + n);
					}
				}
				int countRecord = 0;
				for (int n = 1; n <= numOfSheet; n++) {
					if (n == 1) {
						reportSheet = report.getSheet("BAO_CAO");
					} else {
						reportSheet = report.getSheet("BAO_CAO_" + n);
					}
					
					writeToFile(6, 8, reportSheet, report, currentDate);
					writeToFile(7, 8, reportSheet, report, getFullname(userId));
					
					int initRow = 15;
					int initCol = 2;
					int count = 0;
					for (int i = countRecord; i < medicineList.size(); i++) {
						
						int stt = i + 1;
						writeToFile(initRow, initCol, reportSheet, report,  String.valueOf(stt));
						initCol = initCol + 1;
						writeToFile(initRow, initCol, reportSheet, report, medicineList.get(i).getName());
						initCol = initCol + 3;
						writeToFile(initRow, initCol, reportSheet, report, medicineList.get(i).getProducer());
						initCol = initCol + 1;
						writeToFile(initRow, ++initCol, reportSheet, report, String.valueOf(medicineList.get(i).getQuantity()) + "/" + medicineList.get(i).getUnit());
						initCol = 2;
						initRow++;
						countRecord++;
						count++;
						if (count == 20) {
							break;
						}
					}
				}	
			} else {
				report.removeSheetAt(report.getSheetIndex("BAO_CAO"));
			}
			
			if (reportType2.equals("amountOfMedicine")) {
				int numMonth = Integer.parseInt(month);
				String newMonth;
				if (numMonth < 10) {
					newMonth = year + "/0" + numMonth;
				} else {
					newMonth = year + "/" + numMonth;
				}
				
				int numOldMonth = numMonth - 1;
				String oldMonth;
				if (numOldMonth < 10) {
					oldMonth = year + "/0" + numOldMonth;
				} else {
					oldMonth = year + "/" + numOldMonth;
				}
				Sheet reportSheet = report.getSheet("TIEU_THU");

				String param1 = "'%" + oldMonth + "%'";
				String param2 = "'%" + newMonth + "%'";
				
				SQL_REPORT_2 = SQL_REPORT_2.replaceAll(":param1", param1);
				SQL_REPORT_2 = SQL_REPORT_2.replaceAll(":param2", param2);
				ps = getSC().prepareStatement(SQL_REPORT_2);
				rs = ps.executeQuery();
				int i = 0;
				int initRow = 17;
				int initCol = 2;
				writeToFile(6, 8, reportSheet, report, currentDate);
				writeToFile(7, 8, reportSheet, report, getFullname(userId));
				writeToFile(8, 8, reportSheet, report, numMonth < 10 ? "0" + numMonth + "/" + year : numMonth + "/" + year);	
				int totalRow = 31;
				int countRecord = 0;
				int numOfSheet = 1;
				int count = 0;
				Sheet reportSheetT = report.cloneSheet(1);
				while (rs.next()) {
					if (countRecord == totalRow) {
						numOfSheet += 1;
						reportSheet = report.cloneSheet(report.getSheetIndex(reportSheetT));
						report.setSheetName(report.getSheetIndex(reportSheetT), "TIEU_THU_" + numOfSheet);
						writeToFile(6, 8, reportSheet, report, currentDate);
						writeToFile(7, 8, reportSheet, report, getFullname(userId));
						writeToFile(8, 8, reportSheet, report, numMonth < 10 ? "0" + numMonth + "/" + year : numMonth + "/" + year);
						countRecord = 0;
					}
					int stt = i + 1;
					writeToFile(initRow, initCol, reportSheet, report,  String.valueOf(stt));
					initCol = initCol + 1;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(1));
					initCol = initCol + 3;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(2) + " " + rs.getString(5));
					initCol = initCol + 1;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(3) == null ? "" : rs.getString(3) + " " + rs.getString(5));
					initCol = initCol + 1;
					writeToFile(initRow, initCol, reportSheet, report, rs.getString(4));
					initCol = 2;
					initRow++;
					i++;
					countRecord += 1;
					count++;
				}
				report.removeSheetAt(report.getSheetIndex(reportSheetT));
				if (count == 0) {
					writeToFile(initRow, initCol, reportSheet, report,  String.valueOf(1));
					initCol = initCol + 1;
					writeToFile(initRow, initCol, reportSheet, report, "Không có dữ liệu của tháng " + month + "/" + year);
				}
			} else {
				report.removeSheetAt(report.getSheetIndex("TIEU_THU"));
			}
			
			if (reportType3.equals("analysis")) {
//				TODO: Chưa có ý tưởng nên phân tích cái gì
			} else {
				report.removeSheetAt(report.getSheetIndex("PHAN_TICH"));
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

		} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	public int searchReport() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		String year = (String)param.get("year");
		String month = (String)param.get("month");
		String type = (String)param.get("type");
		String userId = (String)param.get("userId");
		String typeValue = (String)param.get("typeValue");
		
		if (authentication(userId) != 5 && authentication(userId) != 1) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		String SQL_SELECT_REPORT = "SELECT "
				+ "	REPORT_FILE,"
				+ " CREATE_USER,"
				+ " CREATE_AT,"
				+ " APPROVED_AT,"
				+ " APPROVE "
				+ "FROM "
				+ "	FHC_MEDICINE_REPORT "
				+ "WHERE "
				+ "	REPORT_MONTH = ? "
				+ "	AND REPORT_YEAR = ? "
				+ " AND TYPE_REPORT = ? "
				+ " AND APPROVE = ? ;";

		try {
	        int countRecord = 0;
            if (typeValue.equals("3")) {
            	if(month.length() == 1) {
            		month = "0" + month;
            	}
            	String SQL_WORKING_REPORT = "SELECT "
        				+ "	N'Nghỉ phép', "
        				+ "	STAFF_ID, "
        				+ "	CREATE_TIME, "
        				+ "	APPROVE_TIME, "
        				+ "	APPROVED, "
        				+ " OFF_DATE, "
        				+ " APPROVER_ID, "
        				+ " REASON "
        				+ "FROM "
        				+ "	FHC_WORKIING_TIME "
        				+ "WHERE "
        				+ "	APPROVED = 2 "
        				+ "	AND OFF_DATE LIKE '%" + year + "-" + month + "%';";
            	ps = getSC().prepareStatement(SQL_WORKING_REPORT);
            	rs = ps.executeQuery();
    	        
    	        List<FileInfo> filename = new ArrayList<>();
    	        List<WorkingTime> workingTimes = new ArrayList<>();
    	        while(rs.next()) {
    	        	FileInfo fi = new FileInfo();
    	        	fi.setFilename(rs.getString(1));
    	        	fi.setCreateUserId(rs.getString(2));
    	        	fi.setCreateUser(getFullname(rs.getString(2)));
    	        	fi.setCreateDate(rs.getString(3));
    	        	fi.setApprovedDate(rs.getString(4));
    	        	fi.setApproved(rs.getString(5));
    	        	filename.add(fi);
    	        	
    	        	WorkingTime wt = new WorkingTime();
    	        	wt.setStaffId(rs.getString(2));
    	        	wt.setCreateTime(rs.getString(3));
    	        	wt.setApproveTime(rs.getString(4));
    	        	wt.setApproved(rs.getString(5));
    	        	wt.setOffDate(rs.getString(6));
    	        	wt.setApproverId(rs.getString(7));
    	        	wt.setReason(rs.getString(8));
    	        	workingTimes.add(wt);
    	        	
    	        	countRecord++;
    	        }
    	        if (countRecord == 0) {
    	        	executeNo = ConstantVariable.DB_NOTFOUND;
    	        } 
    	        List<List> listReturn = new ArrayList<>(); 
    	        listReturn.add(filename);
    	        listReturn.add(workingTimes);
    	        
    	        setReturnObject(listReturn);
            	return executeNo;
            }
	        ps = getSC().prepareStatement(SQL_SELECT_REPORT);
	        ps.setString(1, month);
	        ps.setString(2, year);
	        ps.setString(3, type);
	        ps.setString(4, typeValue);
	        rs = ps.executeQuery();
	        
	        List<FileInfo> filename = new ArrayList<>();
	        while(rs.next()) {
	        	FileInfo fi = new FileInfo();
	        	fi.setFilename(rs.getString(1));
	        	fi.setCreateUserId(rs.getString(2));
	        	fi.setCreateUser(getFullname(rs.getString(2)));
	        	fi.setCreateDate(rs.getString(3));
	        	fi.setApprovedDate(rs.getString(4));
	        	fi.setApproved(rs.getString(5));
	        	filename.add(fi);
	        	countRecord++;
	        }
	        
	        if (countRecord == 0) {
	        	executeNo = ConstantVariable.DB_NOTFOUND;
	        } 
	        setReturnObject(filename);
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	public int downloadReport() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		String filename = (String)param.get("filename");
		String userId = (String)param.get("userId");
		
		if (authentication(userId) != 5 && authentication(userId) != 1) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		
		
		try {
			
			URL url = new URL(ConstantVariable.STORAGE_CLOUD + filename);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET"); 
			
			InputStream inputStream = connection.getInputStream();
			Workbook report = WorkbookFactory.create(inputStream);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			report.write(byteArrayOutputStream);

			byte[] bytes = byteArrayOutputStream.toByteArray();
			
			String base64Data = Base64.getEncoder().encodeToString(bytes);
			
			setReturnObject(base64Data);
		} catch (Exception e) {
			executeNo = ConstantVariable.FILE_EMPTY;
			e.printStackTrace();
			return executeNo;
		}
		
		return executeNo;
	}
	
	public int createImportRequest() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;

		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		String userId = (String)param.get("userId");
		
		if (authentication(userId) != 5) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}


		String SQL_SELECT_MEDICINE_REQUEST = "SELECT "
				+ "	FMG.NAME, "
				+ "	FMG.PRODUCER, "
				+ "	1000 - FMG.QUANTITY, "
				+ "	CASE "
				+ "		WHEN (1000 - FMG.QUANTITY) % FMG.BASIC_QUANTITY != 0 THEN ((1000- FMG.QUANTITY) / FMG.BASIC_QUANTITY) + 1 "
				+ "		WHEN (1000 - FMG.QUANTITY) % FMG.BASIC_QUANTITY = 0 THEN ((1000 - FMG.QUANTITY) / FMG.BASIC_QUANTITY) "
				+ "	END AS BUY_QUANTITY, "
				+ "	FMG.QUANTITY_UNIT "
				+ "FROM "
				+ "	FHC_MEDICAL_GOODS FMG "
				+ "WHERE "
				+ "	FMG.QUANTITY < 1000";

		URL url = new URL(ConstantVariable.STORAGE_CLOUD + "template/Request.xlsx");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
		String current = getCurrentDate("ddMMyyyHHmmss");
		try (InputStream inputStream = connection.getInputStream();
				Workbook request = WorkbookFactory.create(inputStream)) {

			String filename = "Request/" +getCurrentDate("yyyyMMdd") + "/Request_" + current + ".xlsx";
			
			String SQL_INSERT_REPORT = "INSERT INTO FHC_MEDICINE_REPORT "
					+ "	(REPORT_NAME, "
					+ "	REPORT_FILE, "
					+ "	REPORT_MONTH, "
					+ "	REPORT_YEAR, "
					+ "	CREATE_AT, "
					+ "	CREATE_USER, "
					+ " TYPE_REPORT, "
					+ " APPROVE, NUMBER) "
					+ "VALUES(?,?,?,?,?,?,2,2,?); ";
			
			ps = getSC().prepareStatement(SQL_INSERT_REPORT);
			ps.setString(1, "Request");
			ps.setString(2, filename);
			ps.setString(3, getCurrentDate("M"));
			ps.setString(4, getCurrentDate("yyyy"));
			ps.setString(5, currentDate);
			ps.setString(6, userId);
			ps.setString(7, "S" + current);
			ps.executeUpdate();
			
			Sheet requestSheet = request.getSheet("NHAP_THUOC");

			ps = getSC().prepareStatement(SQL_SELECT_MEDICINE_REQUEST);
			rs = ps.executeQuery();

			int i = 0;
			int initRow = 19;
			int initCol = 2;
			writeToFile(6, 10, requestSheet, request, currentDate);
			writeToFile(7, 10, requestSheet, request, getFullname(userId));
			writeToFile(10, 10, requestSheet, request, "Tạo mới");
			writeToFile(11, 10, requestSheet, request, "S" + current);
			int totalRow = 20;
			int countRecord = 0;
			int numOfSheet = 1;
			Sheet requestSheetT = request.cloneSheet(0);
			//request.setSheetName(request.getSheetIndex(requestSheetT), "NHAP_THUOC_T");
			while (rs.next()) {
				if (countRecord == totalRow) {
					numOfSheet += 1;
					requestSheet = request.cloneSheet(request.getSheetIndex(requestSheetT));
					request.setSheetName(request.getSheetIndex(requestSheet), "NHAP_THUOC_" + numOfSheet);
						writeToFile(6, 10, requestSheet, request, currentDate);
						writeToFile(7, 10, requestSheet, request, getFullname(userId));
						writeToFile(10, 10, requestSheet, request, "Tạo mới");	
						countRecord = 0;
				}
				int stt = i + 1;
				writeToFile(initRow, initCol, requestSheet, request,  String.valueOf(stt));
				initCol = initCol + 1;
				writeToFile(initRow, initCol, requestSheet, request, rs.getString(1));
				initCol = initCol + 3;
				writeToFile(initRow, initCol, requestSheet, request, rs.getString(2));
				initCol = initCol + 2;
				writeToFile(initRow, initCol, requestSheet, request, rs.getString(3));
				initCol = initCol + 1;
				writeToFile(initRow, initCol, requestSheet, request, rs.getString(4));
				initCol = 2;
				initRow++;
				if (initRow == 39) {
					initRow = 19;
				}
				i++;
				countRecord += 1;
			}
			request.removeSheetAt(request.getSheetIndex(requestSheetT));
			
			if (i == 0) {
				executeNo = ConstantVariable.DB_NOTFOUND;
				return executeNo;
			}
			
			String projectId = "fhc-server-394702";
	        String bucketName = "fhc-server-394702.appspot.com";

			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, filename);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			request.write(byteArrayOutputStream);

			byte[] bytes = byteArrayOutputStream.toByteArray();
			
			storage.create(blobInfo, bytes);
			String base64Data = Base64.getEncoder().encodeToString(bytes);

			ps.close();
			
			setReturnObject(base64Data);

		} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	public int approveRequest() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
		String filename = (String)param.get("filename");
		String userId = (String)param.get("userId");
		String type = (String)param.get("type");
		
		if (authentication(userId) != 1) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		if (type.equals("3")) {
			String UPDATE_REPORT = "UPDATE "
					+ "	FHC_WORKIING_TIME "
					+ "SET "
					+ "	APPROVED = 1, "
					+ "	APPROVER_ID = '" + userId + "', "
					+ "	APPROVE_TIME = '" + currentDate + "' "
					+ "WHERE  "
					+ "	CREATE_TIME = '" + filename + "'";
			
			ps = getSC().prepareStatement(UPDATE_REPORT);
			int count = ps.executeUpdate();
			if (count > 0) {
				ps.close();
				String sql = "SELECT ID FROM FHC_WORKIING_TIME WHERE APPROVED = 1 AND APPROVER_ID = '" + userId + "' AND APPROVE_TIME = '" + currentDate + "' ";
				ps = getSC().prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					setReturnObject(rs.getInt(1));
				}
			}
			return executeNo;
		}
		
		String projectId = "fhc-server-394702";
        String bucketName = "fhc-server-394702.appspot.com";
		
		URL url = new URL(ConstantVariable.STORAGE_CLOUD + filename);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET"); 
		
		try (InputStream inputStream = connection.getInputStream();
				Workbook request = WorkbookFactory.create(inputStream)){
			
			Sheet requestSheet = request.getSheet("NHAP_THUOC");
			
			writeToFile(8, 10, requestSheet, request, currentDate);
			writeToFile(10, 10, requestSheet, request, "Đã chấp thuận");
			int index = 2;
			while(request.getSheet("NHAP_THUOC_" + index) != null) {
				requestSheet = request.getSheet("NHAP_THUOC_" + index);
				
				writeToFile(8, 10, requestSheet, request, currentDate);
				writeToFile(10, 10, requestSheet, request, "Đã chấp thuận");
				index++;
			}
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, filename);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			request.write(byteArrayOutputStream);
			byte[] bytes = byteArrayOutputStream.toByteArray();
			storage.create(blobInfo, bytes);
			
			String UPDATE_REPORT = "UPDATE "
					+ "	FHC_MEDICINE_REPORT "
					+ "SET "
					+ "	APPROVE = 1, "
					+ "	APPROVED_BY = '" + userId + "', "
					+ "	APPROVED_AT = '" + currentDate + "' "
					+ "WHERE  "
					+ "	REPORT_FILE = N'" + filename + "'";
			
			ps = getSC().prepareStatement(UPDATE_REPORT);
			ps.executeUpdate();
			
			String base64Data = Base64.getEncoder().encodeToString(bytes);
			setReturnObject(base64Data);
		} catch (Exception e) {
			executeNo = ConstantVariable.FILE_EMPTY;
			e.printStackTrace();
			return executeNo;
		}
		
		return executeNo;
	}
	
	public int importQuantityFromFile() throws Exception {
		int executeNo = ConstantVariable.SUCCESS_NUMBER;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> param = (HashMap<String, Object>) getReciveObject();
		
		MultipartFile medicinFile = (MultipartFile) param.get("medicinFile");
		String userId = (String)param.get("userId");
		
		if (authentication(userId) != 5) {
			executeNo = ConstantVariable.AUTHOR_ERROR_NUMBER;
			return executeNo;
		}
		
		if (medicinFile == null || medicinFile.isEmpty()) {
            return ConstantVariable.ERROR_NUMBER;
        }
		
		String SQL_CHECK_MEDICINE = "SELECT "
				+ "	APPROVE "
				+ "FROM "
				+ "	FHC_MEDICINE_REPORT "
				+ "WHERE "
				+ "	NUMBER = ?; ";
		
		String projectId = "fhc-server-394702";
        String bucketName = "fhc-server-394702.appspot.com";
		
        
		try {
			Workbook request = WorkbookFactory.create(medicinFile.getInputStream());
			
			Sheet requestSheet = request.getSheet("NHAP_THUOC");
			if (requestSheet == null) {
				executeNo = ConstantVariable.FILE_FORMAT_ERROR;
				return executeNo;
			}
			Row row = requestSheet.getRow(11);
			Cell cell = row.getCell(10);
			var number = cell.getStringCellValue();
			
			if (number.equals("")) {
				executeNo = ConstantVariable.NUMBER_REQUEST_NOT_FOUND;
				return executeNo;
			}
			ps = getSC().prepareStatement(SQL_CHECK_MEDICINE);
			ps.setString(1, number);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals("2")) {
					executeNo = ConstantVariable.NOT_APPROVED;
					return executeNo;
				}
			} else {
				executeNo = ConstantVariable.DB_NOTFOUND;
				return executeNo;
			}
			
			String currentDate = getCurrentDate("dd/MM/yyyy HH:mm:ss");
			String current = getCurrentDate("ddMMyyyyHHmmss");
			int iCount = 0;
			int initRow = 19;
			int initCol = 2;
			int countRecord = 0;
			int numOfSheet = 1;
			int nameCol = 3;
			int producerCol = 6;
			int quantityUnitBasicCol = 9;
			int newProducerCol = 12;
			writeToFile(9, 10, requestSheet, request, currentDate);
			while (true) {
				
				row = requestSheet.getRow(initRow);
				cell = row.getCell(initCol);
				String value = cell.getStringCellValue();
				if (value.isEmpty()) {
					if (iCount > 0) {
						break;
					} else {
						executeNo = ConstantVariable.FILE_EMPTY;
						return executeNo;
					}
				}
				
				cell = row.getCell(nameCol);
				String name = cell.getStringCellValue();
				
				cell = row.getCell(producerCol);
				String currentProducer = cell.getStringCellValue();
				
				cell = row.getCell(quantityUnitBasicCol);
				int importQuantity = Integer.parseInt(cell.getStringCellValue());
				
				cell = row.getCell(newProducerCol);
				String newProducer = cell.getStringCellValue();
				
				int count = 0;
				if (newProducer.equals("")) {
					count = executeProcessInsUpd(name, currentProducer, newProducer, importQuantity);
				} else {
					count = executeProcessInsUpd(name, newProducer, newProducer, importQuantity);
				}
				
				if (count == 0) {
					executeNo = ConstantVariable.ERROR_NUMBER;
					return executeNo;
				}
				
				initRow++;
				iCount++;
				countRecord += 1;
				
				if (countRecord == 20) {
					numOfSheet += 1;
					requestSheet = request.getSheet("NHAP_THUOC_"+ numOfSheet);
					countRecord = 0;
					initRow = 19;
					writeToFile(9, 10, requestSheet, request, currentDate);
				}
			}

			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, "Import/Medicine/Request_" + current);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			request.write(byteArrayOutputStream);
			byte[] bytes = byteArrayOutputStream.toByteArray();
			storage.create(blobInfo, bytes);
			
		} catch (IOException e) {
			executeNo = ConstantVariable.FILE_ERROR;
		} catch (SQLException e) {
    		throw e;
    	} catch (Exception e) {
			throw e;
		}

		return executeNo;
	}
	
	private int executeProcessInsUpd(String name, String currentProducer, String newProducer, int quantity) throws Exception {
		
		String SQL_CHECK_MEDICINE_EXIST = "SELECT "
				+ "	ID, "
				+ "	NAME, "
				+ "	DESCRIPTION, "
				+ "	QUANTITY, "
				+ "	IS_MEDICINE, "
				+ "	IS_ACTIVE, "
				+ "	DRUG_INGREDIENTS, "
				+ "	URL_IMAGE, "
				+ "	PRODUCER, "
				+ "	QUANTITY_UNIT, "
				+ "	BASIC_QUANTITY "
				+ "FROM "
				+ "	FHC_MEDICAL_GOODS "
				+ "WHERE "
				+ "	NAME = ? "
				+ "	AND PRODUCER = ?;";
		
		try {
			
			String SQL_INSERT_MEDICINE = "INSERT INTO FHC_MEDICAL_GOODS "
					+ "	(NAME, "
					+ "	DESCRIPTION, "
					+ "	QUANTITY, "
					+ "	IS_MEDICINE, "
					+ "	IS_ACTIVE, "
					+ "	DRUG_INGREDIENTS, "
					+ "	URL_IMAGE, "
					+ " PRODUCER, "
					+ " QUANTITY_UNIT,"
					+ " BASIC_QUANTITY ) VALUES";
			
			String SQL_UPDATE_MEDICINE = "UPDATE "
					+ "	FHC_MEDICAL_GOODS "
					+ "SET "
					+ "	QUANTITY = :quantity "
					+ "WHERE "
					+ "	NAME = N':name' "
					+ "	AND PRODUCER = N':producer'; ";
			
			StringBuilder sqlCreate = new StringBuilder();
			PreparedStatement ps = getSC().prepareStatement(SQL_CHECK_MEDICINE_EXIST);
			ps.setString(1, name);
			ps.setString(2, currentProducer);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int newQuantity = rs.getInt(4) + (quantity * rs.getInt(11));
				SQL_UPDATE_MEDICINE = SQL_UPDATE_MEDICINE.replaceAll(":quantity", newQuantity + "");
				SQL_UPDATE_MEDICINE = SQL_UPDATE_MEDICINE.replaceAll(":name", name);
				SQL_UPDATE_MEDICINE = SQL_UPDATE_MEDICINE.replaceAll(":producer", currentProducer);
				sqlCreate.append(SQL_UPDATE_MEDICINE);
			} else {
				sqlCreate.append(SQL_INSERT_MEDICINE);
				sqlCreate.append("(");
	            sqlCreate.append("N'" + rs.getString(2) + "',");
	            sqlCreate.append("N'" + rs.getString(3) + "',");
	            int newQuantity = quantity * rs.getInt(11);
	            sqlCreate.append(newQuantity + ",");
	            sqlCreate.append(rs.getInt(5) + ",");
	            sqlCreate.append(rs.getInt(6) + ",");
	            sqlCreate.append("N'" + rs.getString(7) + "',");
	            sqlCreate.append("'" + rs.getString(8) + "',");
	            sqlCreate.append("N'" + newProducer + "',");
	            sqlCreate.append("N'" + rs.getString(10) + "',");
	            sqlCreate.append("'" + rs.getString(11) + "'");
	            sqlCreate.append(");");
			}
			rs.close();
			ps.close();
			
			ps = getSC().prepareStatement(sqlCreate.toString());
			int count = ps.executeUpdate();
			
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
