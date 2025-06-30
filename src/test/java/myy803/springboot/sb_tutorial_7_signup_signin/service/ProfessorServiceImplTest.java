package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.*;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;

class ProfessorServiceImplTest {

	@Mock private ProfessorDAO professorRepository;
    @Mock private TraineeshipPositionDAO traineeshipPositionRepository;
    @Mock private EvaluationDAO evaluationRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private RoleDAO roleRepository;
    @Mock private TraineeshipPositionDAO positionRepository;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProfessors() {
        when(professorRepository.findAll()).thenReturn(List.of(new Professor(), new Professor()));
        assertEquals(2, professorService.getAllProfessors().size());
    }

    @Test
    void testGetById() {
        Professor prof = new Professor();
        when(professorRepository.findById(1L)).thenReturn(Optional.of(prof));
        assertEquals(prof, professorService.getById(1L));
    }

    @Test
    void testFindByUsername() {
        Professor prof = new Professor();
        when(professorRepository.findByUsername("prof1")).thenReturn(prof);
        assertEquals(prof, professorService.findByUsername("prof1"));
    }

    @Test
    void testDeleteById() {
        professorService.deleteById(1L);
        verify(professorRepository).deleteById(1L);
    }

    @Test
    void testSaveProfile() {
        Professor prof = new Professor();
        professorService.saveProfile(prof);
        verify(professorRepository).save(prof);
    }

    @Test
    void testSaveFromUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setUsername("prof");
        dto.setPassword("pass");
        dto.setFullName("Professor X");
        dto.setRole("professor");

        Role role = new Role();
        when(roleRepository.findRoleByName("ROLE_PROFESSOR")).thenReturn(role);
        when(professorRepository.save(any(Professor.class))).thenReturn(new Professor());

        professorService.save(dto);
        verify(professorRepository).save(any(Professor.class));
    }

    @Test
    void testRetrieveProfile() {
        Professor prof = new Professor();
        when(professorRepository.findByUsername("user")).thenReturn(prof);
        assertEquals(prof, professorService.retrieveProfile("user"));
    }

    @Test
    void testRetrieveAssignedPositions() {
        Professor prof = new Professor();
        when(professorRepository.findByUsername("prof")).thenReturn(prof);
        when(traineeshipPositionRepository.findBySupervisorAndStatus(eq(prof), any())).thenReturn(List.of(new TraineeshipPosition()));

        List<TraineeshipPosition> result = professorService.retrieveAssignedPositions("prof");
        assertEquals(1, result.size());
    }

    
    @Test
    void testSaveEvaluation() {
        Evaluation eval = new Evaluation();
        TraineeshipPosition pos = new TraineeshipPosition();
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(pos));

        professorService.saveEvaluation(1L, eval);

        assertEquals(pos, eval.getTraineeshipPosition());
        verify(evaluationRepository).save(eval);
    }

    @Test
    void testRegister_NewUser() {
        UserDTO dto = new UserDTO();
        dto.setUsername("newuser");
        dto.setPassword("1234");
        dto.setRole("professor");

        when(professorRepository.existsByUsername("newuser")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_PROFESSOR")).thenReturn(new Role());
        when(passwordEncoder.encode("1234")).thenReturn("encoded");

        boolean result = professorService.register(dto);

        assertTrue(result);
        verify(professorRepository).save(any());
    }

    @Test
    void testRegister_ExistingUser() {
        when(professorRepository.existsByUsername("exists")).thenReturn(true);

        UserDTO dto = new UserDTO();
        dto.setUsername("exists");

        assertFalse(professorService.register(dto));
    }

    @Test
    void testUpdateProfile() {
        Professor existing = new Professor();
        Professor updated = new Professor();
        updated.setFullName("New Name");
        updated.setInterests(Set.of("AI"));
        updated.setSupervisedPositions(Set.of());

        when(professorRepository.findByUsername("prof")).thenReturn(existing);

        professorService.updateProfile("prof", updated);

        assertEquals("New Name", existing.getFullName());
        verify(professorRepository).save(existing);
    }

    @Test
    void testGetAssignedPositions() {
        Professor prof = new Professor();
        prof.setUsername("p1");

        when(professorRepository.findByUsername("p1")).thenReturn(prof);
        when(traineeshipPositionRepository.findBySupervisorUsername("p1")).thenReturn(List.of(new TraineeshipPosition()));

        List<TraineeshipPosition> positions = professorService.getAssignedPositions("p1");

        assertEquals(1, positions.size());
    }

    @Test
    void testPrepareEvaluationForPosition() {
        TraineeshipPosition pos = new TraineeshipPosition();
        Student student = new Student();
        pos.setStudent(student);

        when(positionRepository.findById(10L)).thenReturn(Optional.of(pos));

        Evaluation eval = professorService.prepareEvaluationForPosition(10L);

        assertEquals(pos, eval.getTraineeshipPosition());
        assertEquals(student, eval.getStudent());
    }
    
    @Test
    void testEvaluateAssignedPosition() {
        TraineeshipPosition pos = new TraineeshipPosition();
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(pos));

        professorService.evaluateAssignedPosition(1L);

        verify(traineeshipPositionRepository).save(pos);
    }

}
