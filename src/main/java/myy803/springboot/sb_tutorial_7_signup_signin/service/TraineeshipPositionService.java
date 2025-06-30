package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import java.util.List;

public interface TraineeshipPositionService {
    List<TraineeshipPosition> getAllPositions();
    TraineeshipPosition getById(Long id);
    void save(TraineeshipPosition position);
    void deleteById(Long id);
    List<TraineeshipPosition> getAvailablePositions();
    List<TraineeshipPosition> getAssignedPositions();
	void updatePositionStatus(Long positionId, PositionStatus status);
	TraineeshipPosition findAssignedPositionForStudent(Student student);
	List<TraineeshipPosition> findByCompany(Company company);

    
}
