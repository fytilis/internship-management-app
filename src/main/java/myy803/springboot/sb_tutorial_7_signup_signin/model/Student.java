package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username",unique = true)
	private String username;
	
	@Column(name="password")
	private String password;
	
    @Column(unique = true)
    private String universityId;

    @Column(nullable = false)
    private String fullName;
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="role_id")
	private Role role;

    @ElementCollection
    @CollectionTable(name = "student_interest", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "interest")
    private Set<String> interests = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "student_skill", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();

    @Column(name = "preferredLocation")
    private String preferredLocation;
    @Column(name = "applied")
    private boolean applied;

    @OneToOne(mappedBy = "student")
    private TraineeshipPosition assignedPosition;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Logbook> logbookEntries;
    @Column(name = "Enabled")
    boolean enabled;

    public Student() {
        // Required for JPA
    }
    
  
    

    public Student(Long id, String username, String password, String universityId, String fullName, Role role,
			Set<String> interests, Set<String> skills, String preferredLocation, boolean applied,
			TraineeshipPosition assignedPosition, List<Logbook> logbookEntries, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.universityId = universityId;
		this.fullName = fullName;
		this.role = role;
		this.interests = interests;
		this.skills = skills;
		this.preferredLocation = preferredLocation;
		this.applied = applied;
		this.assignedPosition = assignedPosition;
		this.logbookEntries = logbookEntries;
		this.enabled = enabled;
	}



	public void addLogbookEntry(Logbook entry) {
    	if (logbookEntries == null) {
    		logbookEntries = new ArrayList<>();
    	}
    	logbookEntries.add(entry);
    	entry.setStudent(this); 
    	}
	// --- Getters & Setters ---
    

    public String getUniversityId() {
        return universityId;
    }

    




	public boolean isEnabled() {
		return enabled;
	}




	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}




	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<String> getInterests() {
        return interests;
    }

    public void setInterests(Set<String> interests) {
        this.interests = interests;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public boolean getApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public TraineeshipPosition getAssignedPosition() {
        return assignedPosition;
    }

    public void setAssignedPosition(TraineeshipPosition assignedPosition) {
        this.assignedPosition = assignedPosition;
    }
    
    public List<Logbook> getLogbookEntries() {
        return logbookEntries;
    }

    public void setLogbookEntries(List<Logbook> logbookEntries) {
        this.logbookEntries = logbookEntries;
    }



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
    
    
}



