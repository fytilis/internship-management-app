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

class CompanyServiceImplTest {

	@Mock private CompanyDAO companyRepository;
    @Mock private StudentDAO studentRepository;
    @Mock private TraineeshipPositionDAO positionRepository;
    @Mock private EvaluationDAO evaluationRepository;
    @Mock private RoleDAO roleRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveProfile() {
        Company company = new Company();
        when(companyRepository.findByUsername("company1")).thenReturn(company);
        assertEquals(company, companyService.retrieveProfile("company1"));
    }

    @Test
    void testSaveProfile() {
        Company company = new Company();
        companyService.saveProfile(company);
        verify(companyRepository).save(company);
    }

    @Test
    void testRetrieveAvailablePositions() {
        Company company = new Company();
        company.setUsername("company1");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setCompany(company);
        pos1.setStatus(PositionStatus.AVAILABLE);

        when(positionRepository.findByStatus(PositionStatus.AVAILABLE)).thenReturn(List.of(pos1));

        List<TraineeshipPosition> result = companyService.retrieveAvailablePositions("company1");

        assertEquals(1, result.size());
    }

    @Test
    void testAddPosition() {
        Company company = new Company();
        when(companyRepository.findByUsername("comp")).thenReturn(company);

        TraineeshipPosition pos = new TraineeshipPosition();
        companyService.addPosition("comp", pos);

        assertEquals(PositionStatus.AVAILABLE, pos.getStatus());
        assertEquals(company, pos.getCompany());
        verify(positionRepository).save(pos);
    }

    @Test
    void testRetrieveAssignedPositions() {
        Company company = new Company();
        company.setUsername("comp");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setCompany(company);
        pos.setStatus(PositionStatus.ASSIGNED);

        when(positionRepository.findByStatus(PositionStatus.ASSIGNED)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = companyService.retrieveAssignedPositions("comp");

        assertEquals(1, result.size());
    }

    @Test
    void testSaveEvaluation() {
        TraineeshipPosition pos = new TraineeshipPosition();
        Evaluation eval = new Evaluation();

        when(positionRepository.findById(1L)).thenReturn(Optional.of(pos));

        companyService.saveEvaluation(1L, eval);

        assertEquals(pos, eval.getTraineeshipPosition());
        verify(evaluationRepository).save(eval);
    }

    @Test
    void testRegister_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("company1");
        dto.setPassword("pass");
        dto.setRole("company");
        dto.setFullName("Company Name");

        when(companyRepository.existsByUsername("company1")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_COMPANY")).thenReturn(new Role());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        boolean result = companyService.register(dto);

        assertTrue(result);
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void testRegister_UserExists() {
        when(companyRepository.existsByUsername("exists")).thenReturn(true);

        UserDTO dto = new UserDTO();
        dto.setUsername("exists");

        assertFalse(companyService.register(dto));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void testGetById() {
        Company company = new Company();
        when(companyRepository.findById(5L)).thenReturn(Optional.of(company));
        assertEquals(company, companyService.getById(5L));
    }

    @Test
    void testFindByUsername() {
        Company company = new Company();
        when(companyRepository.findByUsername("c1")).thenReturn(company);
        assertEquals(company, companyService.findByUsername("c1"));
    }

    @Test
    void testUpdateProfile() {
        Company existing = new Company();
        Company updated = new Company();
        updated.setFullName("Updated Name");
        updated.setLocation("Athens");

        when(companyRepository.findByUsername("comp")).thenReturn(existing);

        companyService.updateProfile("comp", updated);

        assertEquals("Updated Name", existing.getFullName());
        assertEquals("Athens", existing.getLocation());
        verify(companyRepository).save(existing);
    }

    @Test
    void testCreateTraineeshipPosition() {
        Company company = new Company();
        when(companyRepository.findByUsername("comp")).thenReturn(company);

        TraineeshipPosition pos = new TraineeshipPosition();
        companyService.createTraineeshipPosition("comp", pos);

        assertEquals(company, pos.getCompany());
        assertEquals(PositionStatus.AVAILABLE, pos.getStatus());
        verify(positionRepository).save(pos);
    }

    @Test
    void testGetAssignedStudents() {
        Student s = new Student();
        TraineeshipPosition p = new TraineeshipPosition();
        p.setStudent(s);
        p.setStatus(PositionStatus.ASSIGNED);

        Company company = new Company();
        company.setPositions(Set.of(p));

        when(companyRepository.findByUsername("comp")).thenReturn(company);

        List<Student> result = companyService.getAssignedStudents("comp");

        assertEquals(1, result.size());
        assertEquals(s, result.get(0));
    }

    @Test
    void testRemoveStudentFromCompany() {
        Student student = new Student();
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setStudent(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findByStudent(student)).thenReturn(Optional.of(pos));

        companyService.removeStudentFromCompany(1L);

        assertNull(pos.getStudent());
        assertEquals(PositionStatus.AVAILABLE, pos.getStatus());
        verify(positionRepository).save(pos);
    }


   
}
