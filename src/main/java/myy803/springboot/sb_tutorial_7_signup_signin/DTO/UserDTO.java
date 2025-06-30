package myy803.springboot.sb_tutorial_7_signup_signin.DTO;


import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String username;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String password;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String fullName;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String email;
	
	private String role;
	
	public UserDTO() {
		
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public  String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}