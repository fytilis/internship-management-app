package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationDAO extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByTraineeshipPosition(TraineeshipPosition position);
    Optional<Evaluation> findByStudent(Student student);
    Optional<Evaluation> findByTraineeshipPosition_Id(Long positionId);
    Optional<Evaluation> findByStudentAndEvaluatorType(Student student, EvaluatorType evaluatorType);
    Optional<Evaluation> findByStudentAndEvaluatorTypeAndTraineeshipPosition(Student student, EvaluatorType evaluatorType, TraineeshipPosition position);
    Optional<Evaluation> findByStudentIdAndEvaluatorTypeAndTraineeshipPositionId(Long studentId, EvaluatorType evaluatorType, Long positionId);

}
