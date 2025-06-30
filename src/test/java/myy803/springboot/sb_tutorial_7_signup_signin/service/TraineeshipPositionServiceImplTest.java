package myy803.springboot.sb_tutorial_7_signup_signin.service;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TraineeshipPositionServiceImplTest {
	@Mock
    private TraineeshipPositionDAO positionRepository;

    @InjectMocks
    private TraineeshipPositionServiceImpl positionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPositions() {
        when(positionRepository.findAll()).thenReturn(List.of(new TraineeshipPosition(), new TraineeshipPosition()));
        assertEquals(2, positionService.getAllPositions().size());
    }

    @Test
    void testGetById() {
        TraineeshipPosition pos = new TraineeshipPosition();
        when(positionRepository.findById(1L)).thenReturn(Optional.of(pos));
        assertEquals(pos, positionService.getById(1L));
    }

    @Test
    void testSavePosition() {
        TraineeshipPosition pos = new TraineeshipPosition();
        positionService.save(pos);
        verify(positionRepository).save(pos);
    }

    @Test
    void testDeleteById() {
        positionService.deleteById(3L);
        verify(positionRepository).deleteById(3L);
    }

    @Test
    void testGetAvailablePositions() {
        when(positionRepository.findByStatus(PositionStatus.AVAILABLE)).thenReturn(List.of(new TraineeshipPosition()));
        assertEquals(1, positionService.getAvailablePositions().size());
    }

    @Test
    void testGetAssignedPositions() {
        when(positionRepository.findByStatus(PositionStatus.ASSIGNED)).thenReturn(List.of(new TraineeshipPosition()));
        assertEquals(1, positionService.getAssignedPositions().size());
    }

    @Test
    void testUpdatePositionStatus() {
        TraineeshipPosition pos = new TraineeshipPosition();
        when(positionRepository.findById(5L)).thenReturn(Optional.of(pos));

        positionService.updatePositionStatus(5L, PositionStatus.ASSIGNED);

        assertEquals(PositionStatus.ASSIGNED, pos.getStatus());
        verify(positionRepository).save(pos);
    }

    @Test
    void testFindAssignedPositionForStudent() {
        Student student = new Student();
        TraineeshipPosition pos = new TraineeshipPosition();

        when(positionRepository.findByStudentAndStatus(student, PositionStatus.ASSIGNED))
            .thenReturn(Optional.of(pos));

        assertEquals(pos, positionService.findAssignedPositionForStudent(student));
    }
}
