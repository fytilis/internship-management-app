package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Committee;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommitteeService {

    List<Committee> getAllCommitteeMembers();
    Committee findByUsername(String username);
    void save(Committee committee);
    void deleteById(Long id);

   
    
    List<Student> retrieveTraineeshipApplications();
    void assignPosition(Long positionId, String studentUsername);
    List<TraineeshipPosition> listAssignedTraineeships();
    void completeAssignedTraineeships(Long positionId);
    
    boolean register(UserDTO userDTO);
	Optional<Committee> getById(Long id);
	
	List<TraineeshipPosition> searchPositions(String strategy, String username);
	Map<TraineeshipPosition, List<Professor>> getSuggestedSupervisors(String strategyName);
    
}
