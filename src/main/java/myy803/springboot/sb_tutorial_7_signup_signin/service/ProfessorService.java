package myy803.springboot.sb_tutorial_7_signup_signin.service;


import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    List<Professor> getAllProfessors();
    Professor getById(Long id);
    Professor findByUsername(String username);
    void saveProfile(Professor professor);
    void deleteById(Long id);
    Professor retrieveProfile(String username);
	void save(UserDTO userDto);
	List<TraineeshipPosition> retrieveAssignedPositions(String username);
    void evaluateAssignedPosition(Long positionId);
    void saveEvaluation(Long positionId, Evaluation evaluation);
    boolean register(UserDTO userDTO);
    void updateProfile(String username, Professor updatedData);
    List<TraineeshipPosition> getAssignedPositions(String professorUsername);
    Evaluation prepareEvaluationForPosition(Long positionId);
 
}
