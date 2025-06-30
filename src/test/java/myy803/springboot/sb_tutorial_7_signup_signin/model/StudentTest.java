package myy803.springboot.sb_tutorial_7_signup_signin.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
public class StudentTest {
	@Test
    void testStudentWithMockRole() {
        Role mockRole = Mockito.mock(Role.class);
        Mockito.when(mockRole.getName()).thenReturn("ROLE_STUDENT");

        Student student = new Student();
        student.setUsername("john_doe");
        student.setPassword("securePass123");
        student.setUniversityId("uni123");
        student.setFullName("John Doe");
        student.setRole(mockRole);

        assertEquals("john_doe", student.getUsername());
        assertEquals("securePass123", student.getPassword());
        assertEquals("uni123", student.getUniversityId());
        assertEquals("John Doe", student.getFullName());
        assertEquals("ROLE_STUDENT", student.getRole().getName());

        Mockito.verify(mockRole).getName();
    }

    @Test
    void testSetAndGetInterests() {
        Student student = new Student();
        student.setInterests(Set.of("AI", "ML"));

        assertTrue(student.getInterests().contains("AI"));
        assertTrue(student.getInterests().contains("ML"));
    }
}
