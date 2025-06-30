package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentDAO extends JpaRepository<Student, Long> {
    List<Student> findByAppliedTrue();
    Student findByUsername(String username);
    List<Student> findAll();
    Optional<Student> findById(Long id); 
    boolean existsByUsername(String username);
   
}
