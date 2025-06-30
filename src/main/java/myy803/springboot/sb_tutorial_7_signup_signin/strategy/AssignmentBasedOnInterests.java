package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;

@Component
public class AssignmentBasedOnInterests implements SupervisorAssignmentStrategy,SupervisorSuggestionStrategy {

    private final TraineeshipPositionDAO positionsRepository;
    private final ProfessorDAO professorRepository;
   

    @Autowired
    public AssignmentBasedOnInterests(TraineeshipPositionDAO positionsRepository,
                                      ProfessorDAO professorRepository) {
        this.positionsRepository = positionsRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public void assign(Long positionId) {
        TraineeshipPosition position = positionsRepository.findById(positionId).orElseThrow(() -> new RuntimeException("Not traineeship position with id: " + positionId));
        Set<String> topics = position.getTopics();

        Professor bestMatch = professorRepository.findAll().stream()
            .max(Comparator.comparingDouble(prof ->
                jaccardSimilarity(topics, prof.getInterests())))
            .filter(prof -> jaccardSimilarity(topics, prof.getInterests()) >= 0.5)
            .orElseThrow(() -> new RuntimeException("No suitable supervisor found."));

        position.setSupervisor(bestMatch);
        positionsRepository.save(position);
    }

    private double jaccardSimilarity(Set<String> a, Set<String> b) {
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    

    @Override
    public List<Professor> suggestAll(Long positionId) {
        TraineeshipPosition position = positionsRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        Set<String> topics = position.getTopics();

        return professorRepository.findAll().stream()
                .filter(prof -> jaccardSimilarity(topics, prof.getInterests()) >= 0.5)
                .sorted(Comparator.comparingDouble(prof -> -jaccardSimilarity(topics, prof.getInterests())))
                .toList();
    }
}

