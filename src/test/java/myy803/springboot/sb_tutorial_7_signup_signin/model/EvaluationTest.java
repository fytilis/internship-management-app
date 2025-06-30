package myy803.springboot.sb_tutorial_7_signup_signin.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EvaluationTest {
	 @Test
	    void testBasicFields() {
	        Evaluation eval = new Evaluation();
	        eval.setMotivation(8);
	        eval.setEffectiveness(9);
	        eval.setEfficiency(7);
	        eval.setGrade(85);
	        eval.setComments("Well done");
	        eval.setEvaluatorType(EvaluatorType.PROFESSOR);

	        assertEquals(8, eval.getMotivation());
	        assertEquals(9, eval.getEffectiveness());
	        assertEquals(7, eval.getEfficiency());
	        assertEquals(85, eval.getGrade());
	        assertEquals("Well done", eval.getComments());
	        assertEquals(EvaluatorType.PROFESSOR, eval.getEvaluatorType());
	    }

	    @Test
	    void testWithMockedStudentAndPosition() {
	        Student mockStudent = Mockito.mock(Student.class);
	        TraineeshipPosition mockPosition = Mockito.mock(TraineeshipPosition.class);

	        Evaluation eval = new Evaluation();
	        eval.setStudent(mockStudent);
	        eval.setTraineeshipPosition(mockPosition);

	        assertEquals(mockStudent, eval.getStudent());
	        assertEquals(mockPosition, eval.getTraineeshipPosition());

	        Mockito.verifyNoInteractions(mockStudent, mockPosition); 
	    }
}
