package myy803.springboot.sb_tutorial_7_signup_signin.service;



import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraineeshipPositionServiceImpl implements TraineeshipPositionService {

    private final TraineeshipPositionDAO positionRepository;

    public TraineeshipPositionServiceImpl(TraineeshipPositionDAO positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public List<TraineeshipPosition> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    @Transactional
    public TraineeshipPosition getById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }
    @Override
    @Transactional
    public List<TraineeshipPosition> findByCompany(Company company){
    	 return positionRepository.findByCompany(company);
    	            
    }
    

    @Override
    @Transactional
    public void save(TraineeshipPosition position) {
        positionRepository.save(position);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<TraineeshipPosition> getAvailablePositions() {
        return positionRepository.findByStatus(PositionStatus.AVAILABLE);
    }

    @Override
    @Transactional
    public List<TraineeshipPosition> getAssignedPositions() {
        return positionRepository.findByStatus(PositionStatus.ASSIGNED);
    }
    @Override
    @Transactional
    public void updatePositionStatus(Long positionId,PositionStatus status) {
    	TraineeshipPosition pos = positionRepository.findById(positionId).orElseThrow(() -> new RuntimeException("Application not found with Id: " + positionId));
    	
    	pos.setStatus(status);
    	
    	positionRepository.save(pos);
    }
    @Override
    public TraineeshipPosition findAssignedPositionForStudent(Student student) {
        return positionRepository.findByStudentAndStatus(student, PositionStatus.ASSIGNED)
            .orElse(null);
    }
    
}