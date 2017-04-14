import java.io.*;
import java.lang.Thread.State;
import java.sql.*;
import java.util.ArrayList;

import javax.print.attribute.standard.Media;
import javax.sound.midi.MetaEventListener;

public class DBDemo {

	public static void main(String[] args) {

		try {
			metadataExample();
			metadataExample();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void metadataExample() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		int count = 0;
		int countNullable = 0;
		int countNumeric = 0;
		String sql = "SELECT * FROM student";

		try {

			connection = DBConnection.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			System.out.println("Product name: " + databaseMetaData.getDatabaseProductName());
			System.out.println("Product version: " + databaseMetaData.getDatabaseProductVersion());
			System.out.println("Driver name: " + databaseMetaData.getDriverName());
			System.out.println("Driver version: " + databaseMetaData.getDriverVersion() + "\n");
			
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			count = resultSetMetaData.getColumnCount();
			System.out.println("Number of columns: " + count + "\n");

			System.out.println("Data types of columns: ");
			for (int i = 1; i <= count; i++) {
				System.out
						.println(resultSetMetaData.getColumnLabel(i) + " - " + resultSetMetaData.getColumnTypeName(i));
				if (resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable) {
					countNullable++;
				}
				if (resultSetMetaData.getColumnType(i) == Types.INTEGER
						|| resultSetMetaData.getColumnType(i) == Types.DOUBLE
						|| resultSetMetaData.getColumnType(i) == Types.FLOAT) {
					countNumeric++;
				}
			}
			
			System.out.println("\nNumber of nullable columns: " + countNullable);
			System.out.println("Number of numeric columns: " + countNumeric);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void getMetadata() {

		Connection connection = null;
		ResultSet rs = null;
		String catalog = null;
		String schemaPattern = null;
		String tableNamePattern = null;
		String columnNamePattern = null;
		String[] types = null;

		try {
			connection = DBConnection.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			System.out.println("ProductName=" + databaseMetaData.getDatabaseProductName());
			System.out.println("ProductVersion=" + databaseMetaData.getDatabaseProductVersion());
			System.out.println("DriverName=" + databaseMetaData.getDriverName());
			System.out.println("DriverVersion" + databaseMetaData.getDriverVersion());

			System.out.println("UserName=" + databaseMetaData.getUserName());
			System.out.println("Catalog=" + databaseMetaData.getCatalogTerm());
			System.out.println("Schema=" + databaseMetaData.getSchemaTerm());
			System.out.println("URL=" + databaseMetaData.getURL());

			rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
			System.out.println("Table names:");
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			}

			rs = databaseMetaData.getColumns(catalog, schemaPattern, "student", columnNamePattern);
			System.out.println("Column names in student:");
			while (rs.next()) {
				System.out.println(rs.getString("COLUMN_NAME") + " - " + rs.getInt("COLUMN_SIZE"));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void deleteStudent() throws NumberFormatException, IOException {

		listStudents();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("student_id=");
		int inputStudentId = Integer.parseInt(br.readLine());

		Connection connection = null;
		PreparedStatement statement = null;
		String sql = "DELETE FROM `student` WHERE `student_id` = ?";

		try {
			connection = DBConnection.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, inputStudentId);
			statement.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		listStudents();
	}

	private static void insertStudent() throws IOException {

		listStudents();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("student_firstname=");
		String inputStudentFirstname = br.readLine();
		System.out.print("student_lastname=");
		String inputStudentLastname = br.readLine();
		System.out.print("student_index=");
		String inputStudentIndex = br.readLine();

		Connection connection = null;
		PreparedStatement statement = null;
		String sql = "INSERT INTO `student`(`student_firstname`, `student_lastname`, `student_index`) VALUES (?,?,?)";

		try {
			connection = DBConnection.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, inputStudentFirstname);
			statement.setString(2, inputStudentLastname);
			statement.setString(3, inputStudentIndex);
			statement.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		listStudents();
	}

	private static void insertStudents(String inputStudentFirstname1, String inputStudentLastname1,
			String inputStudentIndex1, String inputStudentFirstname2, String inputStudentLastname2,
			String inputStudentIndex2) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DBConnection.getConnection();
			String query = "INSERT INTO student (student_firstname, student_lastname, student_index) VALUES (?,?,?)";
			statement = connection.prepareStatement(query);
			// do not commit after completed transaction
			connection.setAutoCommit(false);

			// add query to batch
			statement.setString(1, inputStudentFirstname1);
			statement.setString(2, inputStudentLastname1);
			statement.setString(3, inputStudentIndex1);
			statement.addBatch();

			statement.setString(1, inputStudentFirstname2);
			statement.setString(2, inputStudentLastname2);
			statement.setString(3, inputStudentIndex2);
			statement.addBatch();

			// execute all the queries
			statement.executeBatch();

			// commit the transaction
			connection.commit();

			listStudents();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void updateStudentIndex(int inputStudentId, String inputStudentIndex) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = "UPDATE student SET student_index = ? WHERE student_id=?";

		try {
			listStudents();
			connection = DBConnection.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, inputStudentIndex);
			statement.setInt(2, inputStudentId);
			statement.executeUpdate();
			listStudents();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void listStudents() {

		ArrayList<Student> studentsList = new ArrayList<Student>();
		ResultSet rs = null;
		Connection connection = null;
		Statement statement = null;
		Student student = null;
		String query = "SELECT * FROM `student`";

		try {
			connection = DBConnection.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			// result set metadata
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int count = resultSetMetaData.getColumnCount();
			System.out.println();
			for (int i = 1; i <= count; i++) {
				System.out.println("ColumnLabel=" + resultSetMetaData.getColumnLabel(i));
				System.out.println("ColumnType=" + resultSetMetaData.getColumnTypeName(i));
				System.out.println("IsNullable=" + resultSetMetaData.isNullable(i));
				System.out.println("IsAutoIncrement=" + resultSetMetaData.isAutoIncrement(i));
				System.out.println();
			}

			while (rs.next()) {
				student = new Student();
				student.setStudentId(rs.getInt("student_id"));
				student.setStudentFirstname(rs.getString("student_firstname"));
				student.setStudentLastname(rs.getString("student_lastname"));
				student.setStudentIndex(rs.getString("student_index"));
				studentsList.add(student);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		for (Student s : studentsList) {
			System.out.println(s.toString());
		}

	}

}
