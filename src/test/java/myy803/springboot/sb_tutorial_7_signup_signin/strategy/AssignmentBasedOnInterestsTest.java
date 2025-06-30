package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignmentBasedOnInterestsTest {

    private TraineeshipPositionDAO positionDAO;
    private ProfessorDAO professorDAO;
    private AssignmentBasedOnInterests strategy;

    @BeforeEach
    void setUp() {
        positionDAO = mock(TraineeshipPositionDAO.class);
        professorDAO = mock(ProfessorDAO.class);
        strategy = new AssignmentBasedOnInterests(positionDAO, professorDAO);
    }

    @Test
    void testAssignFindsBestProfessor() {
        // Given
        Long positionId = 1L;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics(Set.of("ai", "ml"));

        Professor prof1 = new Professor();
        prof1.setInterests(Set.of("ai", "ml", "data"));

        Professor prof2 = new Professor();
        prof2.setInterests(Set.of("java", "spring"));

        when(positionDAO.findById(positionId)).thenReturn(Optional.of(position));
        when(professorDAO.findAll()).thenReturn(List.of(prof1, prof2));

        // When
        strategy.assign(positionId);

        // Then
        assertEquals(prof1, position.getSupervisor());
        verify(positionDAO).save(position);
    }

    @Test
    void testAssignThrowsIfNoProfessorMatch() {
        Long positionId = 2L;
        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics(Set.of("ai", "ml"));

        Professor unrelatedProf = new Professor();
        unrelatedProf.setInterests(Set.of("business", "finance"));

        when(positionDAO.findById(positionId)).thenReturn(Optional.of(position));
        when(professorDAO.findAll()).thenReturn(List.of(unrelatedProf));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> strategy.assign(positionId));
        assertTrue(ex.getMessage().contains("No suitable supervisor"));
    }

    @Test
    void testSuggestAllReturnsSortedProfessors() {
        Long positionId = 3L;
        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics(Set.of("ai", "ml", "cloud"));

        Professor prof1 = new Professor();
        prof1.setFullName("Prof A");
        prof1.setInterests(Set.of("ai", "ml")); // Jaccard ~0.66

        Professor prof2 = new Professor();
        prof2.setFullName("Prof B");
        prof2.setInterests(Set.of("cloud", "ai", "ml", "devops")); // Jaccard ~0.75

        Professor prof3 = new Professor();
        prof3.setFullName("Prof C");
        prof3.setInterests(Set.of("history", "literature")); // Jaccard ~0.0

        when(positionDAO.findById(positionId)).thenReturn(Optional.of(position));
        when(professorDAO.findAll()).thenReturn(List.of(prof1, prof2, prof3));

        List<Professor> suggestions = strategy.suggestAll(positionId);

        assertEquals(2, suggestions.size());
        assertEquals(prof2, suggestions.get(0)); // Best match first
        assertEquals(prof1, suggestions.get(1));
    }
}
