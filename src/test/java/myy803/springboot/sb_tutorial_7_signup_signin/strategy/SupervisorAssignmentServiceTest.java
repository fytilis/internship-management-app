package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupervisorAssignmentServiceTest {

    private SupervisorAssignmentFactory factory;
    private TraineeshipPositionDAO positionDAO;
    private ProfessorDAO professorDAO;
    private SupervisorAssignmentService service;

    @BeforeEach
    void setUp() {
        factory = mock(SupervisorAssignmentFactory.class);
        positionDAO = mock(TraineeshipPositionDAO.class);
        professorDAO = mock(ProfessorDAO.class);

        factory = mock(SupervisorAssignmentFactory.class);
        positionDAO = mock(TraineeshipPositionDAO.class);
        professorDAO = mock(ProfessorDAO.class);

        service = new SupervisorAssignmentService(factory, positionDAO, professorDAO);
    }

    @Test
    void testAssignSupervisor_DelegatesToStrategy() {
        SupervisorAssignmentStrategy strategy = mock(SupervisorAssignmentStrategy.class);
        when(factory.create("load")).thenReturn(strategy);

        service.assignSupervisor(1L, "load");

        verify(strategy).assign(1L);
    }

    @Test
    void testGetSuggestedProfessors_ReturnsList() {
        SupervisorSuggestionStrategy strategy = mock(SupervisorSuggestionStrategy.class);
        when(factory.create("interests")).thenReturn((SupervisorAssignmentStrategy) strategy);

        Professor prof = new Professor();
        when(strategy.suggestAll(10L)).thenReturn(List.of(prof));

        List<Professor> result = service.getSuggestedProfessors(10L, "interests");

        assertEquals(1, result.size());
        assertSame(prof, result.get(0));
    }

    @Test
    void testGetSuggestedProfessors_ThrowsWhenUnsupported() {
        SupervisorAssignmentStrategy strategy = mock(SupervisorAssignmentStrategy.class);
        when(factory.create("invalid")).thenReturn(strategy);

        assertThrows(IllegalArgumentException.class, () -> {
            service.getSuggestedProfessors(1L, "invalid");
        });
    }

    @Test
    void testManuallyAssignSupervisor_AssignsAndSaves() {
        Long positionId = 1L;
        Long professorId = 2L;

        TraineeshipPosition position = new TraineeshipPosition();
        Professor professor = new Professor();
        when(positionDAO.findById(positionId)).thenReturn(Optional.of(position));
        when(professorDAO.findById(professorId)).thenReturn(Optional.of(professor));

        service.manuallyAssignSupervisor(positionId, professorId);

        assertEquals(professor, position.getSupervisor());
        verify(positionDAO).save(position);
    }

    @Test
    void testManuallyAssignSupervisor_PositionNotFound() {
        when(positionDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.manuallyAssignSupervisor(1L, 2L));
    }

    @Test
    void testManuallyAssignSupervisor_ProfessorNotFound() {
        when(positionDAO.findById(1L)).thenReturn(Optional.of(new TraineeshipPosition()));
        when(professorDAO.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.manuallyAssignSupervisor(1L, 2L));
    }
}
