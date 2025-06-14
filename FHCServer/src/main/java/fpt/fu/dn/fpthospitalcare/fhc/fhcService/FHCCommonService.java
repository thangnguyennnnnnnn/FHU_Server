package fpt.fu.dn.fpthospitalcare.fhc.fhcService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import fpt.fu.dn.fpthospitalcare.fhc.model.Faculty;

public abstract class FHCCommonService extends DBService{
	
	public Connection SC;
	public PreparedStatement ps;
	public ResultSet rs;
	public int resultNo = ConstantVariable.SUCCESS_NUMBER;
	public Object returnObject;
	public Object reciveObject;
	public CryptoService cs;
	public DateTimeService ds;
	//public String sessionAuthor;

	/**
	 * Khởi tạo ...
	 * @return
	 * @throws Exception 
	 */
	public int init() throws Exception {
		try {
			cs = new CryptoService();
			ds = new DateTimeService();
			SC = super.getConnection();
			SC.setAutoCommit(false);
//			if (sessionAuthor != null) {
//				ss.setListSession(new HashMap<>());
//				resultNo = ss.checkSession(sessionAuthor, SC);
//			}
		} catch (Exception e) {
			resultNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		}
		return resultNo;
	}
	
	/**
	 * Hàm abstract cần phải override lại để xử lý các chức năng
	 * @param workProgram
	 * @return
	 * @throws Exception
	 */
	public abstract int run(int workProgram) throws Exception;
	
	/**
	 * Thực thi chính của main
	 * @param workProgram
	 * @return
	 * @throws Exception
	 */
	public int execute(int workProgram) throws Exception {
	
		// Khai báo số thực thi
		resultNo = ConstantVariable.SUCCESS_NUMBER;
		try {
			
			// Bước 1: sẽ gọi đến init (Khởi tạo connect DB ...)
			resultNo = init();
			
			// Nếu init không lỗi
			if (resultNo == ConstantVariable.SUCCESS_NUMBER) {
				// Thực thi main run
				resultNo = run(workProgram);
			}
			
		} catch (Exception e) {
			resultNo = ConstantVariable.ERROR_NUMBER;
			throw e;
		} finally {
			
			// Xử lý kết thúc
			resultNo = finish(resultNo);
		};
		return resultNo;
	}
	
	public int finish(int resultNumber) throws SQLException {
		try {
			if (resultNumber == ConstantVariable.SUCCESS_NUMBER) {
				SC.commit();
			} else {
				SC.rollback();
			}
			if (ps != null) {
				ps.close();	
			}
			
			if (rs != null) {
				rs.close();
			}
			
			if (SC != null) {			
				SC.close();
			}
		} catch (Exception e) {
			SC.rollback();
			resultNo = ConstantVariable.ERROR_NUMBER;
		}
		return resultNo;
	}

	public Connection getSC() {
		return SC;
	}

	public void setSC(Connection SC) {
		this.SC = SC;
	}

	public PreparedStatement getPs() {
		return ps;
	}

	public void setPs(PreparedStatement ps) {
		this.ps = ps;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public int getResultNo() {
		return resultNo;
	}

	public void setResultNo(int resultNo) {
		this.resultNo = resultNo;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	
	public Object getReciveObject() {
		return reciveObject;
	}

	public void setReciveObject(Object reciveObject) {
		this.reciveObject = reciveObject;
	}
	
	public String getCurrentDate(String format) throws SQLException {
//		String SELECT_CURRENT_DATE = "SELECT FORMAT(GETDATE(), 'ddMMyyyyhhmmss') AS currentDate;";
//		String currentDate;
		try {
//			PreparedStatement ps1 = SC.prepareStatement(SELECT_CURRENT_DATE);
//			ResultSet rs1 = ps1.executeQuery();
//			if (rs1.next()) {
//				currentDate =  rs1.getString(1);	
//			} else {
//				currentDate = null;
//			}
//			rs1.close();
//			ps1.close();
			LocalDateTime currentDate = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			return currentDate.format(formatter);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public String getStatus(int id) throws SQLException {
		String SELECT_STATUS = "SELECT ID, STATUS_NAME "
				+ "FROM FHC_STATUS "
				+ "WHERE ID = ?; ";
		String stt;
		try {
			PreparedStatement ps1 = SC.prepareStatement(SELECT_STATUS);
			ps1.setInt(1, id);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				stt =  rs1.getString(2);	
			} else {
				stt = null;
			}
			rs1.close();
			ps1.close();
			return stt;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Faculty getFacultyName(int id) throws SQLException {
		String SELECT_STATUS = "SELECT ID, NAME, DESCRIPTION, IS_ACTIVE "
				+ "FROM FHC.dbo.FHC_FACULTY "
				+ "WHERE ID=?; ";
		Faculty f = new Faculty();
		try {
			PreparedStatement ps1 = SC.prepareStatement(SELECT_STATUS);
			ps1.setInt(1, id);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				f.setId(rs1.getString(1));
				f.setName(rs1.getString(2));
				f.setDescription(rs1.getString(3));
			}
			rs1.close();
			ps1.close();
			return f;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public int authentication(String username) throws SQLException {
		String SELECT_AUTHEN = "SELECT "
				+ "	FUR.ROLE_ID "
				+ "FROM "
				+ "	FHC_USER FU "
				+ "LEFT JOIN FHC_USER_ROLE FUR ON "
				+ "	FU.ID = FUR.USER_ID "
				+ "WHERE "
				+ "	FU.ID = ?;";
		int roleId;
		try {
			PreparedStatement ps1 = SC.prepareStatement(SELECT_AUTHEN);
			ps1.setString(1, username);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				roleId = rs1.getInt(1);
			} else {
				roleId = -1;
			}
			rs1.close();
			ps1.close();
			return roleId;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public String getFullname(String userId) throws SQLException {
		String SELECT_FULLNAME = "SELECT "
				+ "	FIRST_NAME + ' ' + MIDDLE_NAME + ' ' + LAST_NAME "
				+ "FROM "
				+ "	FHC_USER_INFORMATION "
				+ "WHERE  "
				+ "	USER_ID = ?; ";
		String fullname = "";
		try {
			PreparedStatement ps1 = SC.prepareStatement(SELECT_FULLNAME);
			ps1.setString(1, userId);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				fullname = rs1.getString(1);
			}
			rs1.close();
			ps1.close();
			return fullname;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void writeToFile(int rowIndex, int colIndex, Sheet sheet, Workbook workbook ,String value) {
		Row row = sheet.getRow(rowIndex);
		
		if (row == null) {
            row = sheet.createRow(rowIndex);
        }
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		CellStyle sourceCellStyle = cell.getCellStyle();
		cell.setCellValue(value);
		
		
        CellStyle targetCellStyle = workbook.createCellStyle();
        targetCellStyle.cloneStyleFrom(sourceCellStyle);
        cell.setCellStyle(targetCellStyle);
	}
	
	public String convertDate(String value, String origin, String newFormat) {
		
		SimpleDateFormat inputDateFormat = new SimpleDateFormat(origin);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(newFormat);
		try {
            // Chuyển đổi chuỗi thành đối tượng Date
            Date date = inputDateFormat.parse(value);
            
            // Định dạng lại thành chuỗi ngày đúng định dạng
            String formattedDateStr = outputDateFormat.format(date);
            
            return formattedDateStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return value;
	}
}
