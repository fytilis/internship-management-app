package myy803.springboot.sb_tutorial_7_signup_signin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.ApplicationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationDAO applicationRepository;
    @Autowired
    private TraineeshipPositionDAO positionRepository;

    @Override
    @Transactional
    public void applyToPosition(Student student, TraineeshipPosition position) {
        if (!hasAlreadyApplied(student, position)) {
            Application app = new Application();
            app.setStudent(student);
            app.setPosition(position);
            app.setStatus(ApplicationStatus.PENDING); // enum
            app.setAppliedAt(LocalDateTime.now());
            applicationRepository.save(app);
        }
    }
    
    
    
    @Override
    @Transactional
    public boolean hasAlreadyApplied(Student student, TraineeshipPosition position) {
        return applicationRepository.findByStudentAndPosition(student, position).isPresent();
    }

    @Override
    @Transactional
    public List<Application> getApplicationsByStudent(Student student) {
        return applicationRepository.findByStudent(student);
    }

    @Override
    @Transactional
    public List<Application> getApplicationsByPosition(TraineeshipPosition position) {
        return applicationRepository.findByPosition(position);
    }
    


    @Override
    public void updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application app = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found with this id: " + applicationId));

        app.setStatus(status);

        if (status == ApplicationStatus.ACCEPTED) {
            TraineeshipPosition position = app.getPosition();

            
            position.setStatus(PositionStatus.ASSIGNED);
            position.setStudent(app.getStudent());

            positionRepository.save(position);
        }

        applicationRepository.save(app);
    }

	
	
	
	@Override
	public void confirmAssignedPosition(Long applicationId, String studentUsername) {
	    Application app = applicationRepository.findById(applicationId)
	        .orElseThrow(() -> new RuntimeException("Application not found"));

	    if (!app.getStudent().getUsername().equals(studentUsername)) {
	        throw new RuntimeException("You are not authorized to confirm this application");
	    }

	    
	    TraineeshipPosition position = app.getPosition();
	    position.setStatus(PositionStatus.ASSIGNED);
	    position.setStudent(app.getStudent());

	    positionRepository.save(position);
	    applicationRepository.save(app);
	}



	@Override
	public List<Application> getAcceptedApplicationsForStudent(Student student) {
		return applicationRepository.findByStudentAndStatus(student, ApplicationStatus.ACCEPTED);
	}

}
