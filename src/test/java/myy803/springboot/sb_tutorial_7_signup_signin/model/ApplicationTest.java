package myy803.springboot.sb_tutorial_7_signup_signin.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class ApplicationTest {
	 @Test
	    void testDefaultStatusAndDate() {
	        Application app = new Application();

	        assertEquals(ApplicationStatus.PENDING, app.getStatus());
	        assertNotNull(app.getAppliedAt());
	        assertTrue(app.getAppliedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
	    }

	    @Test
	    void testWithMockedStudentAndPosition() {
	        Student mockStudent = Mockito.mock(Student.class);
	        TraineeshipPosition mockPosition = Mockito.mock(TraineeshipPosition.class);

	        Application app = new Application();
	        app.setStudent(mockStudent);
	        app.setPosition(mockPosition);
	        app.setStatus(ApplicationStatus.ACCEPTED);

	        assertEquals(mockStudent, app.getStudent());
	        assertEquals(mockPosition, app.getPosition());
	        assertEquals(ApplicationStatus.ACCEPTED, app.getStatus());

	        Mockito.verifyNoInteractions(mockStudent, mockPosition); // optional
	    }

}
