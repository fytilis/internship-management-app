package myy803.springboot.sb_tutorial_7_signup_signin.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Set;

public class TraineeshipPositionTest {
	
	@Test
    void testBasicFields() {
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setDescription("Software Intern");
        pos.setStartDate(LocalDate.of(2025, 6, 1));
        pos.setEndDate(LocalDate.of(2025, 9, 1));
        pos.setStatus(PositionStatus.AVAILABLE);

        assertEquals("Software Intern", pos.getDescription());
        assertEquals(LocalDate.of(2025, 6, 1), pos.getStartDate());
        assertEquals(LocalDate.of(2025, 9, 1), pos.getEndDate());
        assertEquals(PositionStatus.AVAILABLE, pos.getStatus());
    }

    @Test
    void testSkillAndTopicCollections() {
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setRequiredSkills(Set.of("Java", "Spring"));
        pos.setTopics(Set.of("Web", "Security"));

        assertTrue(pos.getRequiredSkills().contains("Java"));
        assertTrue(pos.getTopics().contains("Security"));
    }

    @Test
    void testWithMockedCompany() {
        Company mockCompany = Mockito.mock(Company.class);
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setCompany(mockCompany);

        assertEquals(mockCompany, pos.getCompany());
        Mockito.verifyNoInteractions(mockCompany);
    }

}
