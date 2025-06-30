package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluation",
uniqueConstraints = {
  @UniqueConstraint(
    name = "UK_EVAL_STU_POS_TYPE", 
    columnNames = {"student_id", "position_id", "evaluator_type"}
  )
})
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "motivation")
    private int motivation;
    @Column(name = "effectiveness")
    private int effectiveness;
    @Column(name = "efficiency")
    private int efficiency;
    private Double companyAverage;
    private Double professorAverage;
    private Double totalAverage;
    
    private int grade;
    private String comments;
    private boolean passed;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluator_type", nullable = false)
    private EvaluatorType evaluatorType; // COMPANY or PROFESSOR

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private TraineeshipPosition traineeshipPosition;
    
    @ManyToOne
    @JoinColumn(name="student_id", nullable = false)
    private Student student;
    
    public Evaluation() {
        // Required for JPA
    }
    
    // --- Getters & Setters ---

    
    
    	
    
    public Long getId() {
        return id;
    }
    
    public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Double getCompanyAverage() {
		return companyAverage;
	}

	public void setCompanyAverage(Double companyAverage) {
		this.companyAverage = companyAverage;
	}

	public Double getProfessorAverage() {
		return professorAverage;
	}

	public void setProfessorAverage(Double professorAverage) {
		this.professorAverage = professorAverage;
	}

	public Double getTotalAverage() {
		return totalAverage;
	}

	public void setTotalAverage(Double totalAverage) {
		this.totalAverage = totalAverage;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public int getMotivation() {
		return motivation;
	}
    

	public void setMotivation(int motivation) {
		this.motivation = motivation;
	}

	public int getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(int effectiveness) {
		this.effectiveness = effectiveness;
	}

	public int getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setId(Long id) {
        this.id = id;
    }


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public EvaluatorType getEvaluatorType() {
        return evaluatorType;
    }

    public void setEvaluatorType(EvaluatorType evaluatorType) {
        this.evaluatorType = evaluatorType;
    }

    public TraineeshipPosition getTraineeshipPosition() {
        return traineeshipPosition;
    }

    public void setTraineeshipPosition(TraineeshipPosition traineeshipPosition) {
        this.traineeshipPosition = traineeshipPosition;
    }
}

