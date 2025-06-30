package myy803.springboot.sb_tutorial_7_signup_signin.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class LogbookTest {
	@Test
    void testBasicFields() {
        Logbook log = new Logbook();
        LocalDateTime now = LocalDateTime.of(2025, 5, 13, 10, 0);
        log.setDate(now);
        log.setContent("Completed first week of internship.");

        assertEquals(now, log.getDate());
        assertEquals("Completed first week of internship.", log.getContent());
    }

    @Test
    void testWithMockedStudent() {
        Student mockStudent = Mockito.mock(Student.class);
        Logbook log = new Logbook();
        log.setStudent(mockStudent);

        assertEquals(mockStudent, log.getStudent());
        Mockito.verifyNoInteractions(mockStudent); // optional
    }
}
