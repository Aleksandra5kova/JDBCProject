import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

	private static Connection connection;
	private static String driver;
	private static String url;
	private static String user;
	private static String password;

	public static Connection getConnection() {

		if (connection == null) {
			
			try {
				InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
				Properties p = new Properties();
				p.load(is);
				driver = p.getProperty("driver");
				url = p.getProperty("url");
				user = p.getProperty("user");
				password = p.getProperty("password");
			} catch (Exception e) {
				System.out.println("Unable to read config.properties.");
				e.printStackTrace();
			}

			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				System.out.println("Invalid driver name or class cannot be found.");
				e.printStackTrace();
			}
		}
		
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			System.out.println("Unable to connect to the database.");
			System.out.println(ex.getErrorCode() + ":" + ex.getSQLState() + ":" + ex.getMessage());
			ex.printStackTrace();
		}

		return connection;
	}
}
