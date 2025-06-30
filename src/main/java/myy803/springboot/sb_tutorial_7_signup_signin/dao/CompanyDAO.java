package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import java.util.List;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyDAO extends JpaRepository<Company, Long> {
	Company findByUsername(String username);
	
    boolean existsByUsername(String username);
    
}
