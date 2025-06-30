// ✅ Role.java
package myy803.springboot.sb_tutorial_7_signup_signin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., ROLE_STUDENT, ROLE_COMPANY

    // === Constructors ===
    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    //overriding equals method in order to use contains method in CustomAuthenticationSuccessHadler class correctly 
	public boolean equals(Object comparedObject) {
	    
	    if (this == comparedObject) {
	        return true;
	    }

	   if (!(comparedObject instanceof Role)) {
	        return false;
	    }

	    Role comparedRole = (Role) comparedObject;

	    if (this.name.equals(comparedRole.name)) {
	        return true;
	    }

	    return false;
	}


} 
