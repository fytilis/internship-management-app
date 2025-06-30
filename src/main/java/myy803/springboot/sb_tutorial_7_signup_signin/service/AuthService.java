package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;

public interface AuthService {
	boolean registerUser(UserDTO userDTO);
}
