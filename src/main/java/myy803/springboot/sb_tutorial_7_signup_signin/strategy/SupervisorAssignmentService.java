package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
@Service
public class SupervisorAssignmentService {
	
	private final SupervisorAssignmentFactory factory;
	
	private final TraineeshipPositionDAO positionsRepository;
	
	private final ProfessorDAO professorRepository;
	
	  @Autowired
	    public SupervisorAssignmentService(SupervisorAssignmentFactory factory,
	                                       TraineeshipPositionDAO positionsRepository,
	                                       ProfessorDAO professorRepository) {
	        this.factory = factory;
	        this.positionsRepository = positionsRepository;
	        this.professorRepository = professorRepository;
	    }
    public void assignSupervisor(Long positionId, String strategyName) {
        SupervisorAssignmentStrategy strategy = factory.create(strategyName);
        strategy.assign(positionId);
    }
    public List<Professor> getSuggestedProfessors(Long positionId, String strategyName) {
        SupervisorAssignmentStrategy base = factory.create(strategyName);

        if (!(base instanceof SupervisorSuggestionStrategy)) {
            throw new IllegalArgumentException("Strategy does not support suggestions");
        }

        return ((SupervisorSuggestionStrategy) base).suggestAll(positionId);
    }
    
    public void manuallyAssignSupervisor(Long positionId, Long professorId) {
        TraineeshipPosition position = positionsRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        position.setSupervisor(professor);
        positionsRepository.save(position); 
    }
}	
