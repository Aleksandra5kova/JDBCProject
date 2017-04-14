
public class Student {
	
	private Integer studentId;
	private String studentFirstname;
	private String studentLastname;
	private String studentIndex;
	
	public Student() {}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStudentFirstname() {
		return studentFirstname;
	}

	public void setStudentFirstname(String studentFirstname) {
		this.studentFirstname = studentFirstname;
	}

	public String getStudentLastname() {
		return studentLastname;
	}

	public void setStudentLastname(String studentLastname) {
		this.studentLastname = studentLastname;
	}

	public String getStudentIndex() {
		return studentIndex;
	}

	public void setStudentIndex(String studentIndex) {
		this.studentIndex = studentIndex;
	}
	
	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", studentFirstname=" + studentFirstname + ", studentLastname="
				+ studentLastname + ", studentIndex=" + studentIndex + "]";
	}
}
