package myy803.springboot.sb_tutorial_7_signup_signin.service;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.ApplicationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApplicationServiceImplTest {
	@Mock private ApplicationDAO applicationRepository;
    @Mock private TraineeshipPositionDAO positionRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyToPosition_NotAlreadyApplied_SavesApplication() {
        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();

        when(applicationRepository.findByStudentAndPosition(student, position)).thenReturn(Optional.empty());

        applicationService.applyToPosition(student, position);

        verify(applicationRepository).save(argThat(app ->
            app.getStudent() == student &&
            app.getPosition() == position &&
            app.getStatus() == ApplicationStatus.PENDING
        ));
    }

    @Test
    void testApplyToPosition_AlreadyApplied_DoesNotSave() {
        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();

        when(applicationRepository.findByStudentAndPosition(student, position)).thenReturn(Optional.of(new Application()));

        applicationService.applyToPosition(student, position);

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void testHasAlreadyApplied() {
        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();

        when(applicationRepository.findByStudentAndPosition(student, position)).thenReturn(Optional.of(new Application()));

        assertTrue(applicationService.hasAlreadyApplied(student, position));
    }

    @Test
    void testGetApplicationsByStudent() {
        Student student = new Student();
        when(applicationRepository.findByStudent(student)).thenReturn(List.of(new Application()));
        assertEquals(1, applicationService.getApplicationsByStudent(student).size());
    }

    @Test
    void testGetApplicationsByPosition() {
        TraineeshipPosition pos = new TraineeshipPosition();
        when(applicationRepository.findByPosition(pos)).thenReturn(List.of(new Application()));
        assertEquals(1, applicationService.getApplicationsByPosition(pos).size());
    }

    @Test
    void testUpdateApplicationStatus_ACCEPTED() {
        Application app = new Application();
        app.setStudent(new Student());
        TraineeshipPosition pos = new TraineeshipPosition();
        app.setPosition(pos);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        applicationService.updateApplicationStatus(1L, ApplicationStatus.ACCEPTED);

        assertEquals(ApplicationStatus.ACCEPTED, app.getStatus());
        assertEquals(PositionStatus.ASSIGNED, pos.getStatus());
        assertEquals(app.getStudent(), pos.getStudent());

        verify(positionRepository).save(pos);
        verify(applicationRepository).save(app);
    }

    @Test
    void testConfirmAssignedPosition_ValidStudent() {
        Student student = new Student();
        student.setUsername("john");

        TraineeshipPosition pos = new TraineeshipPosition();
        Application app = new Application();
        app.setStudent(student);
        app.setPosition(pos);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        applicationService.confirmAssignedPosition(1L, "john");

        assertEquals(student, pos.getStudent());
        assertEquals(PositionStatus.ASSIGNED, pos.getStatus());

        verify(positionRepository).save(pos);
        verify(applicationRepository).save(app);
    }

    @Test
    void testConfirmAssignedPosition_UnauthorizedStudent_Throws() {
        Student student = new Student();
        student.setUsername("maria");

        Application app = new Application();
        app.setStudent(student);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        assertThrows(RuntimeException.class, () ->
            applicationService.confirmAssignedPosition(1L, "john")
        );
    }

    @Test
    void testGetAcceptedApplicationsForStudent_NotImplemented() {
        Student student = new Student();
        assertNull(applicationService.getAcceptedApplicationsForStudent(student)); // Method returns null (TODO)
    }

}
