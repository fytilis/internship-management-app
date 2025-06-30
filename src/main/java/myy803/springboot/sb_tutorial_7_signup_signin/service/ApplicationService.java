package myy803.springboot.sb_tutorial_7_signup_signin.service;

import java.util.List;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

public interface ApplicationService {
	void applyToPosition(Student student, TraineeshipPosition position);
    boolean hasAlreadyApplied(Student student, TraineeshipPosition position);
    List<Application> getApplicationsByStudent(Student student);
    List<Application> getApplicationsByPosition(TraineeshipPosition position);
	void updateApplicationStatus(Long applicationId, ApplicationStatus status);
	
	List<Application> getAcceptedApplicationsForStudent(Student student);
	void confirmAssignedPosition(Long applicationId, String studentUsername);

}
