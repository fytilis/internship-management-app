package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeshipPositionDAO extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByStatus(PositionStatus status);
    Optional<TraineeshipPosition> findById(Long id); 
    List<TraineeshipPosition> findBySupervisorAndStatus(Professor professor, PositionStatus status);
    Optional<TraineeshipPosition> findByStudent(Student student);
    Optional<TraineeshipPosition> findByStudentAndStatus(Student student, PositionStatus status);
    List<TraineeshipPosition> findBySupervisorUsername(String username);
    List<TraineeshipPosition> findByCompany(Company company);
 }
