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

class AssignmentBasedOnLoadTest {

    private TraineeshipPositionDAO positionDAO;
    private ProfessorDAO professorDAO;
    private AssignmentBasedOnLoad strategy;

    @BeforeEach
    void setUp() {
        positionDAO = mock(TraineeshipPositionDAO.class);
        professorDAO = mock(ProfessorDAO.class);
        strategy = new AssignmentBasedOnLoad(positionDAO, professorDAO);
    }

    @Test
    void testAssignAssignsProfessorWithLeastLoad() {
        Long positionId = 1L;
        TraineeshipPosition position = new TraineeshipPosition();

        Professor prof1 = new Professor();
        prof1.setFullName("Prof A");
        prof1.setSupervisedPositions(Set.of(new TraineeshipPosition(), new TraineeshipPosition(), new TraineeshipPosition()));

        Professor prof2 = new Professor();
        prof2.setFullName("Prof B");
        prof2.setSupervisedPositions(Set.of(new TraineeshipPosition())); // only 1

        when(positionDAO.findById(positionId)).thenReturn(Optional.of(position));
        when(professorDAO.findAll()).thenReturn(List.of(prof1, prof2));

        strategy.assign(positionId);

        assertEquals(prof2, position.getSupervisor());
        verify(positionDAO).save(position);
    }

    @Test
    void testAssignThrowsIfNoProfessors() {
        Long positionId = 2L;
        TraineeshipPosition position = new TraineeshipPosition();

        when(positionDAO.findById(positionId)).thenReturn(Optional.of(position));
        when(professorDAO.findAll()).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> strategy.assign(positionId));
        assertTrue(ex.getMessage().contains("No supervisors found"));
    }

    @Test
    void testSuggestAllReturnsProfessorsSortedByLoad() {
        
        Professor prof1 = new Professor();
        prof1.setFullName("Prof A");
        prof1.setSupervisedPositions(Set.of(
                new TraineeshipPosition(),
                new TraineeshipPosition(),
                new TraineeshipPosition(),
                new TraineeshipPosition()  
        ));

        Professor prof2 = new Professor();
        prof2.setFullName("Prof B");
        prof2.setSupervisedPositions(Set.of(
                new TraineeshipPosition(),
                new TraineeshipPosition()  
        ));

        Professor prof3 = new Professor();
        prof3.setFullName("Prof C");
        prof3.setSupervisedPositions(Set.of(
                new TraineeshipPosition(),
                new TraineeshipPosition(),
                new TraineeshipPosition(),
                new TraineeshipPosition(),
                new TraineeshipPosition(),
                new TraineeshipPosition() 
        ));

        
        when(professorDAO.findAll()).thenReturn(List.of(prof1, prof2, prof3));

        
        List<Professor> result = strategy.suggestAll(10L);

       
        assertEquals(List.of(prof2, prof1, prof3), result);
        assertEquals(2, result.get(0).getSupervisedCount());
        assertEquals(4, result.get(1).getSupervisedCount());
        assertEquals(6, result.get(2).getSupervisedCount());
    }

}
