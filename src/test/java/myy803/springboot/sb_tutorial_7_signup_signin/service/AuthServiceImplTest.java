package myy803.springboot.sb_tutorial_7_signup_signin.service;
import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.*;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
	@Mock private StudentDAO studentRepository;
    @Mock private ProfessorDAO professorRepository;
    @Mock private CompanyDAO companyRepository;
    @Mock private CommitteeDAO committeeRepository;
    @Mock private RoleDAO roleRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterStudent_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("student1");
        dto.setPassword("pass");
        dto.setRole("student");
        dto.setFullName("Test Student");

        when(studentRepository.existsByUsername("student1")).thenReturn(false);
        when(professorRepository.existsByUsername("student1")).thenReturn(false);
        when(companyRepository.existsByUsername("student1")).thenReturn(false);
        when(committeeRepository.existsByUsername("student1")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_STUDENT")).thenReturn(new Role());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        boolean result = authService.registerUser(dto);

        assertTrue(result);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testRegisterProfessor_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("prof1");
        dto.setPassword("pass");
        dto.setRole("professor");
        dto.setFullName("Prof");

        when(studentRepository.existsByUsername("prof1")).thenReturn(false);
        when(professorRepository.existsByUsername("prof1")).thenReturn(false);
        when(companyRepository.existsByUsername("prof1")).thenReturn(false);
        when(committeeRepository.existsByUsername("prof1")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_PROFESSOR")).thenReturn(new Role());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        boolean result = authService.registerUser(dto);

        assertTrue(result);
        verify(professorRepository).save(any(Professor.class));
    }

    @Test
    void testRegisterCompany_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("comp1");
        dto.setPassword("pass");
        dto.setRole("company");
        dto.setFullName("MyCo");

        when(studentRepository.existsByUsername("comp1")).thenReturn(false);
        when(professorRepository.existsByUsername("comp1")).thenReturn(false);
        when(companyRepository.existsByUsername("comp1")).thenReturn(false);
        when(committeeRepository.existsByUsername("comp1")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_COMPANY")).thenReturn(new Role());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        boolean result = authService.registerUser(dto);

        assertTrue(result);
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void testRegisterCommittee_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("committee1");
        dto.setPassword("pass");
        dto.setRole("committee");
        dto.setFullName("Committee");

        when(studentRepository.existsByUsername("committee1")).thenReturn(false);
        when(professorRepository.existsByUsername("committee1")).thenReturn(false);
        when(companyRepository.existsByUsername("committee1")).thenReturn(false);
        when(committeeRepository.existsByUsername("committee1")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_COMMITTEE")).thenReturn(new Role());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        boolean result = authService.registerUser(dto);

        assertTrue(result);
        verify(committeeRepository).save(any(Committee.class));
    }

    @Test
    void testRegisterUser_AlreadyExists() {
        UserDTO dto = new UserDTO();
        dto.setUsername("exists");
        dto.setRole("student");

        when(studentRepository.existsByUsername("exists")).thenReturn(true);

        boolean result = authService.registerUser(dto);

        assertFalse(result);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testRegisterUser_InvalidRole() {
        UserDTO dto = new UserDTO();
        dto.setUsername("invalid");
        dto.setRole("notarole");

        when(studentRepository.existsByUsername("invalid")).thenReturn(false);
        when(professorRepository.existsByUsername("invalid")).thenReturn(false);
        when(companyRepository.existsByUsername("invalid")).thenReturn(false);
        when(committeeRepository.existsByUsername("invalid")).thenReturn(false);
        when(roleRepository.findRoleByName("ROLE_NOTAROLE")).thenReturn(null);

        boolean result = authService.registerUser(dto);

        assertFalse(result);
    }
}
