package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.*;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommitteeServiceImplTest {

    @Mock private CommitteeDAO committeeRepository;
    @Mock private StudentDAO studentRepository;
    @Mock private TraineeshipPositionDAO positionRepository;
    @Mock private RoleDAO roleRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private PositionsSearchFactory positionsSearchFactory;
    @Mock private SupervisorAssignmentFactory assignmentFactory;

    @InjectMocks
    private CommitteeServiceImpl committeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCommitteeMembers() {
        when(committeeRepository.findAll()).thenReturn(List.of(new Committee()));
        assertEquals(1, committeeService.getAllCommitteeMembers().size());
    }

    @Test
    void testFindByUsername() {
        Committee c = new Committee();
        when(committeeRepository.findByUsername("comm")).thenReturn(c);
        assertEquals(c, committeeService.findByUsername("comm"));
    }

    @Test
    void testSave() {
        Committee c = new Committee();
        committeeService.save(c);
        verify(committeeRepository).save(c);
    }

    @Test
    void testDeleteById() {
        committeeService.deleteById(1L);
        verify(committeeRepository).deleteById(1L);
    }

    @Test
    void testRetrieveTraineeshipApplications() {
        when(studentRepository.findByAppliedTrue()).thenReturn(List.of(new Student()));
        assertEquals(1, committeeService.retrieveTraineeshipApplications().size());
    }

    @Test
    void testAssignPosition() {
        TraineeshipPosition pos = new TraineeshipPosition();
        Student student = new Student();
        student.setUsername("s1");

        when(positionRepository.findById(1L)).thenReturn(Optional.of(pos));
        when(studentRepository.findByUsername("s1")).thenReturn(student);

        committeeService.assignPosition(1L, "s1");

        assertEquals(PositionStatus.ASSIGNED, pos.getStatus());
        assertEquals(student, pos.getStudent());
        verify(positionRepository).save(pos);
        verify(studentRepository).save(student);
    }

    @Test
    void testGetById() {
        Committee c = new Committee();
        when(committeeRepository.findById(2L)).thenReturn(Optional.of(c));
        assertEquals(Optional.of(c), committeeService.getById(2L));
    }

    @Test
    void testListAssignedTraineeships() {
        when(positionRepository.findByStatus(PositionStatus.ASSIGNED)).thenReturn(List.of(new TraineeshipPosition()));
        assertEquals(1, committeeService.listAssignedTraineeships().size());
    }

    @Test
    void testCompleteAssignedTraineeships() {
        TraineeshipPosition pos = new TraineeshipPosition();
        when(positionRepository.findById(1L)).thenReturn(Optional.of(pos));

        committeeService.completeAssignedTraineeships(1L);

        assertEquals(PositionStatus.COMPLETED, pos.getStatus());
        verify(positionRepository).save(pos);
    }

    @Test
    void testRegister_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("comm1");
        dto.setPassword("1234");
        dto.setRole("committee");
        dto.setFullName("Committee");

        when(committeeRepository.existsByUsername("comm1")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_COMMITTEE")).thenReturn(new Role());
        when(passwordEncoder.encode("1234")).thenReturn("encoded");

        boolean result = committeeService.register(dto);

        assertTrue(result);
        verify(committeeRepository).save(any(Committee.class));
    }

    @Test
    void testRegister_AlreadyExists() {
        UserDTO dto = new UserDTO();
        dto.setUsername("comm1");

        when(committeeRepository.existsByUsername("comm1")).thenReturn(true);

        boolean result = committeeService.register(dto);

        assertFalse(result);
        verify(committeeRepository, never()).save(any());
    }

    @Test
    void testSearchPositions() {
        PositionsSearchStrategy strategy = mock(PositionsSearchStrategy.class);
        when(positionsSearchFactory.create("default")).thenReturn(strategy);
        when(strategy.search("user")).thenReturn(List.of(new TraineeshipPosition()));

        List<TraineeshipPosition> result = committeeService.searchPositions("default", "user");

        assertEquals(1, result.size());
    }

 

    @Test
    void testGetSuggestedSupervisors_InvalidStrategy_Throws() {
        SupervisorAssignmentStrategy invalidStrategy = mock(SupervisorAssignmentStrategy.class);
        when(assignmentFactory.create("invalid")).thenReturn(invalidStrategy);

        assertThrows(IllegalArgumentException.class, () -> {
            committeeService.getSuggestedSupervisors("invalid");
        });
    }
}
