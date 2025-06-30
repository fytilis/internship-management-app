package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.EvaluationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EvaluationServiceImplTest {
	@Mock private EvaluationDAO evaluationRepository;
    @Mock private TraineeshipPositionDAO positionRepository;

    @InjectMocks
    private EvaluationServiceImpl evaluationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEvaluation() {
        Evaluation eval = new Evaluation();
        when(evaluationRepository.save(eval)).thenReturn(eval);

        Evaluation result = evaluationService.saveEvaluation(eval);

        assertEquals(eval, result);
        verify(evaluationRepository).save(eval);
    }

    @Test
    void testFindByTraineeshipPosition() {
        TraineeshipPosition pos = new TraineeshipPosition();
        when(evaluationRepository.findByTraineeshipPosition(pos)).thenReturn(List.of(new Evaluation()));

        List<Evaluation> result = evaluationService.findByTraineeshipPosition(pos);

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById() {
        evaluationService.deleteById(5L);
        verify(evaluationRepository).deleteById(5L);
    }

    @Test
    void testGetById_Found() {
        Evaluation eval = new Evaluation();
        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(eval));

        Evaluation result = evaluationService.getById(1L);
        assertEquals(eval, result);
    }

    @Test
    void testGetById_NotFound() {
        when(evaluationRepository.findById(2L)).thenReturn(Optional.empty());
        Evaluation result = evaluationService.getById(2L);
        assertNull(result);
    }

    @Test
    void testGetAllEvaluations() {
        when(evaluationRepository.findAll()).thenReturn(List.of(new Evaluation(), new Evaluation()));
        List<Evaluation> result = evaluationService.getAllEvaluations();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateEvaluation_Existing() {
        Student student = new Student();
        Evaluation newEval = new Evaluation();
        newEval.setStudent(student);
        newEval.setMotivation(4);
        newEval.setEffectiveness(5);
        newEval.setEfficiency(3);
        newEval.setComments("Great work");

        Evaluation existing = new Evaluation();
        existing.setStudent(student);

        when(evaluationRepository.findByStudent(student)).thenReturn(Optional.of(existing));

        evaluationService.updateEvaluation(newEval);

        assertEquals(4, existing.getMotivation());
        assertEquals(5, existing.getEffectiveness());
        assertEquals(3, existing.getEfficiency());
        assertEquals("Great work", existing.getComments());

        verify(evaluationRepository).save(existing);
    }

    @Test
    void testUpdateEvaluation_New() {
        Student student = new Student();
        Evaluation newEval = new Evaluation();
        newEval.setStudent(student);

        when(evaluationRepository.findByStudent(student)).thenReturn(Optional.empty());

        evaluationService.updateEvaluation(newEval);

        verify(evaluationRepository).save(newEval);
    }
}
