

package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company  {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@Column(name="username",unique = true)
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name = "fullname")
    private String fullName;

    @Column(name = "location",nullable=true)
    private String location;

    @OneToMany(mappedBy = "company")
    private Set<TraineeshipPosition> positions = new HashSet<>();
    
    @ManyToOne(fetch=FetchType.EAGER)
   	@JoinColumn(name="role_id")
   	private Role role;
    
    @Column(name = "Enabled")
    boolean enabled;
    
    public Company() {
        // Required for JPA
    }
    
    

    public Company(Long id, String username, String password, String fullName, String location,
			Set<TraineeshipPosition> positions, Role role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.location = location;
		this.positions = positions;
		this.role = role;
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



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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



	public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<TraineeshipPosition> getPositions() {
        return positions;
    }

    public void setPositions(Set<TraineeshipPosition> positions) {
        this.positions = positions;
    }
}
