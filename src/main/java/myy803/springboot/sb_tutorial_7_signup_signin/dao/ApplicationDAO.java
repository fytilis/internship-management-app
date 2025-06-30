package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

public interface ApplicationDAO extends JpaRepository<Application, Long> {
    List<Application> findByPosition(TraineeshipPosition position);
    List<Application> findByStudent(Student student);
    Optional<Application> findByStudentAndPosition(Student student, TraineeshipPosition position);
    List<Application> findByStudentAndStatus(Student student, ApplicationStatus status);

    
}