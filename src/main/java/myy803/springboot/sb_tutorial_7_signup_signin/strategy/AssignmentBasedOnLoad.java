package myy803.springboot.sb_tutorial_7_signup_signin.strategy;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

@Component
public class AssignmentBasedOnLoad implements SupervisorAssignmentStrategy,SupervisorSuggestionStrategy {

    private final TraineeshipPositionDAO positionsRepository;
    private final ProfessorDAO professorRepository;

    @Autowired
    public AssignmentBasedOnLoad(TraineeshipPositionDAO positionsRepository,
                                      ProfessorDAO professorRepository) {
        this.positionsRepository = positionsRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public void assign(Long positionId) {
        TraineeshipPosition position = positionsRepository.findById(positionId).orElseThrow(() -> new RuntimeException("Not traineeship position with id: " + positionId));

        Professor minLoadProfessor = professorRepository.findAll().stream()
        	.min(Comparator.comparing(Professor::getSupervisedCount))
            .orElseThrow(() -> new RuntimeException("No supervisors found."));

        position.setSupervisor(minLoadProfessor);
        positionsRepository.save(position);
    }
    
    @Override
    public List<Professor> suggestAll(Long positionId) {
        return professorRepository.findAll().stream()
            .sorted(Comparator.comparingInt(Professor::getSupervisedCount))
            .collect(Collectors.toList());
    }

}
