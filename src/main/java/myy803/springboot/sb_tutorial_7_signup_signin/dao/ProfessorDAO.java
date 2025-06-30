package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfessorDAO extends JpaRepository<Professor, Long> {
	Optional<Professor> findById(Long id);
	Professor findByUsername(String username);
	boolean existsByUsername(String username);
	
	
}

