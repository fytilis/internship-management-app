package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.StudentSumDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface EvaluationService {
	Evaluation saveEvaluation(Evaluation evaluation);
    List<Evaluation> findByTraineeshipPosition(TraineeshipPosition position);
    void deleteById(Long id);
    Evaluation getById(Long id);
    List<Evaluation> getAllEvaluations();
    Optional<Evaluation> findByStudentAndEvaluatorType(Student student, EvaluatorType evaluatorType);
    Optional<Evaluation> findByStudentAndEvaluatorTypeAndTraineeshipPosition(Student student, EvaluatorType evaluatorType, TraineeshipPosition position);
    Optional<Evaluation> findByStudentIdAndEvaluatorTypeAndTraineeshipPositionId(Long studentId, EvaluatorType evaluatorType, Long positionId);
    void submitCompanyEvaluation(Evaluation evaluation);
    void submitProfessorEvaluation(Evaluation evaluation);
    void updateEvaluation(Evaluation newEval);
    List<StudentSumDTO> getCommitteeSummaries(Long positionId);
    
}
