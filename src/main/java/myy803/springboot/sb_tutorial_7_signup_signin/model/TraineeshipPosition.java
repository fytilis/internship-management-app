package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "traineeshipPosition")
public class TraineeshipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(name="startDate")
    private LocalDate startDate;
    @Column(name="endDate")
    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "position_skills", joinColumns = @JoinColumn(name = "position_id"))
    @Column(name = "skill")
    private Set<String> requiredSkills = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "position_topics", joinColumns = @JoinColumn(name = "position_id"))
    @Column(name = "topic")
    private Set<String> topics = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private PositionStatus status = PositionStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Professor supervisor;
    
    boolean evaluated ;
    // --- Getters & Setters ---
    
    

    public Long getId() {
        return id;
    }
   

	public boolean getEvaluated() {
    	return evaluated;
    }
    
    public void setEvaluated(boolean evaluated) {
    	this.evaluated = evaluated;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
	

	public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public Company getCompany() {
        return company;
    }
    

    public void setCompany(Company company) {
        this.company = company;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Professor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Professor supervisor) {
        this.supervisor = supervisor;
    }
}
