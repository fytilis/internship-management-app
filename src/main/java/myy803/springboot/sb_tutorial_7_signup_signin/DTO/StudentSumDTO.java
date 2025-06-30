package myy803.springboot.sb_tutorial_7_signup_signin.DTO;

public class StudentSumDTO {
    private String studentFullName;
    private double professorAvg;
    private double companyAvg;
    private double overallAvg;
	public StudentSumDTO(String studentFullName, double professorAvg, double companyAvg, double overallAvg) {
		super();
		this.studentFullName = studentFullName;
		this.professorAvg = professorAvg;
		this.companyAvg = companyAvg;
		this.overallAvg = overallAvg;
	}
	public String getStudentFullName() {
		return studentFullName;
	}
	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}
	public double getProfessorAvg() {
		return professorAvg;
	}
	public void setProfessorAvg(double professorAvg) {
		this.professorAvg = professorAvg;
	}
	public double getCompanyAvg() {
		return companyAvg;
	}
	public void setCompanyAvg(double companyAvg) {
		this.companyAvg = companyAvg;
	}
	public double getOverallAvg() {
		return overallAvg;
	}
	public void setOverallAvg(double overallAvg) {
		this.overallAvg = overallAvg;
	}
    
    
}