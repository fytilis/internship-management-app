package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Committee;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitteeDAO extends JpaRepository<Committee, Long> {
	Committee findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Committee> findById(Long id); 
   
}

