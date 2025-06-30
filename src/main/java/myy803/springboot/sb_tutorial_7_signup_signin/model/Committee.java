package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "committee")
public class Committee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username",unique = true)
	private String username;
	
	@Column(name="password")
	private String password;

    @Column(name="fullname")
    private String fullName;
    
    @ManyToOne(fetch=FetchType.EAGER)
   	@JoinColumn(name="role_id")
   	private Role role;
    
    @Column(name = "Enabled")
    boolean enabled;

    
    public Committee() {
        // Required for JPA
    }
    
    public Committee(Long id, String username, String password, String fullName, Role role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
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

	public void setFullName(String fullName) {
        this.fullName = fullName;
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
    
}
