package myy803.springboot.sb_tutorial_7_signup_signin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;


public interface CompanyService {
	Company retrieveProfile(String username);
	void saveProfile(Company company);
	List<TraineeshipPosition> retrieveAvailablePositions(String username);
	void addPosition(String username, TraineeshipPosition position);
	List<TraineeshipPosition> retrieveAssignedPositions(String username);
	void evaluateAssignedPosition(Long positionId);
	void saveEvaluation(Long positionId, Evaluation evaluation);
	 boolean register(UserDTO userDTO);
	Company getById(Long id);
	Company findByUsername(String username);
	void updateProfile(String username, Company updated);
	void createTraineeshipPosition(String username, TraineeshipPosition position);
	List<TraineeshipPosition> getAssignedPositions(String companyUsername);
	public void removeStudentFromCompany(Long studentId);
	


	}


