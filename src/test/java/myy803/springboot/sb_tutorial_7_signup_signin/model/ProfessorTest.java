package myy803.springboot.sb_tutorial_7_signup_signin.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

public class ProfessorTest {
	@Test
    void testBasicFieldsWithMockedRole() {
        Role mockRole = Mockito.mock(Role.class);
        Mockito.when(mockRole.getName()).thenReturn("ROLE_PROFESSOR");

        Professor prof = new Professor();
        prof.setUsername("dr.smith");
        prof.setPassword("pass123");
        prof.setFullName("Dr. John Smith");
        prof.setEnabled(true);
        prof.setRole(mockRole);

        assertEquals("dr.smith", prof.getUsername());
        assertEquals("pass123", prof.getPassword());
        assertEquals("Dr. John Smith", prof.getFullName());
        assertTrue(prof.isEnabled());
        assertEquals("ROLE_PROFESSOR", prof.getRole().getName());

        Mockito.verify(mockRole).getName();
    }

    @Test
    void testInterestsCollection() {
        Professor prof = new Professor();
        prof.setInterests(Set.of("AI", "Data Science"));

        assertTrue(prof.getInterests().contains("AI"));
        assertTrue(prof.getInterests().contains("Data Science"));
    }
}
