package fpt.fu.dn.fpthospitalcare.fhc.fhcService;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBService {
	
	@SuppressWarnings("unused")
	public Connection connection;
	
	//@Value("${spring.datasource.url}")
	//private String connectionString = "jdbc:sqlserver://;serverName=fhc.cec4vdiuuico.us-east-1.rds.amazonaws.com;databaseName=FHC;encrypt=true;trustServerCertificate=true;";
	private String connectionString = "jdbc:sqlserver://;serverName=THAWNGS\\SQLEXPRESS;databaseName=FHC;encrypt=true;trustServerCertificate=true;";
	
	//@Value("${spring.datasource.username}")
	private String user = "fhcadmin";
	
	//@Value("${spring.datasource.password}")
	private String password = "fhcadmin";
	
	//@Value("${spring.datasource.driverClassName}")
	String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public DBService() {
//		try {	
//			//connection = getConnection();
//			//connection.setAutoCommit(false);
//		} catch (SQLException e) {
//			connection = null;
//		}
	}
	
	public Connection getConnection() {
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(connectionString,user,password);
	        return con;
		} catch (Exception e) {	
			return null;
		}		
	}
}
