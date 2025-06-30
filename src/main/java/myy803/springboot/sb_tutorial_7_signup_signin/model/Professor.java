package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professor")
public class Professor  {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username",unique = true)
	private String username;
	
	@Column(name="password")
	private String password;

    @Column(nullable = false)
    private String fullName;
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="role_id")
	private Role role;
    
    @Column(name = "Enabled")
    boolean enabled;

    @ElementCollection
    @CollectionTable(name = "professor_interest", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "interest")
    private Set<String> interests = new HashSet<>();;

    @OneToMany(mappedBy = "supervisor")
    private Set<TraineeshipPosition> supervisedPositions = new HashSet<>();
    public Professor() {
        // Required for JPA
    }

  
public Professor(Long id, String username, String password, String fullName, Role role, Set<String> interests,
			Set<TraineeshipPosition> supervisedPositions) {
	
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
		this.interests = interests;
		this.supervisedPositions = supervisedPositions;
	}

// --- Getters & Setters ---
    
    
    
    
	public String getFullName() {
        return fullName;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

    public Set<TraineeshipPosition> getSupervisedPositions() {
        return supervisedPositions;
    }

    public void setSupervisedPositions(Set<TraineeshipPosition> supervisedPositions) {
        this.supervisedPositions = supervisedPositions;
    }
    public Integer getSupervisedCount() {
        return supervisedPositions != null ? supervisedPositions.size() : 0;
    }
    
}

