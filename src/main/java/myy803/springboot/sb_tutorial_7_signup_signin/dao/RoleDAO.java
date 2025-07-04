package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;

public interface RoleDAO extends JpaRepository<Role, Long> {
	public Role findRoleByName(String theRoleName);
	Optional<Role> findByName(String name); 
}
