package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EvaluationDAOTest {

    @Autowired
    private EvaluationDAO evaluationDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private TraineeshipPositionDAO positionDAO;

    private Student createStudent(String username) {
        Student student = new Student();
        student.setUsername(username);
        student.setFullName("Test Student");
        student.setPassword("1234");
        student.setEnabled(true);
        student.setApplied(true);
        return studentDAO.save(student);
    }

    private TraineeshipPosition createPosition(String description) {
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setDescription(description);
        pos.setStartDate(LocalDate.now());
        pos.setEndDate(LocalDate.now().plusMonths(2));
        pos.setEvaluated(false);
        return positionDAO.save(pos);
    }

    private Evaluation createEvaluation(Student student, TraineeshipPosition position) {
        Evaluation eval = new Evaluation();
        eval.setStudent(student);
        eval.setTraineeshipPosition(position);
        eval.setMotivation(8);
        eval.setEfficiency(9);
        eval.setEffectiveness(7);
        eval.setGrade(9);
        eval.setComments("Very good work");
        eval.setEvaluatorType(EvaluatorType.COMPANY);
        eval.setPassed(true);
        return evaluationDAO.save(eval);
    }

    @Test
    void testFindByStudent() {
        Student student = createStudent("student1");
        TraineeshipPosition position = createPosition("Internship A");
        Evaluation savedEval = createEvaluation(student, position);

        Optional<Evaluation> found = evaluationDAO.findByStudent(student);
        assertTrue(found.isPresent());
        assertEquals(savedEval.getId(), found.get().getId());
    }

    @Test
    void testFindByTraineeshipPosition() {
        Student student = createStudent("student2");
        TraineeshipPosition position = createPosition("Internship B");
        createEvaluation(student, position);

        List<Evaluation> evaluations = evaluationDAO.findByTraineeshipPosition(position);
        assertEquals(1, evaluations.size());
        assertEquals("student2", evaluations.get(0).getStudent().getUsername());
    }

    @Test
    void testFindByTraineeshipPositionId() {
        Student student = createStudent("student3");
        TraineeshipPosition position = createPosition("Internship C");
        Evaluation savedEval = createEvaluation(student, position);

        Optional<Evaluation> found = evaluationDAO.findByTraineeshipPosition_Id(position.getId());
        assertTrue(found.isPresent());
        assertEquals(savedEval.getId(), found.get().getId());
    }
}
