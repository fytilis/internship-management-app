package myy803.springboot.sb_tutorial_7_signup_signin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.*;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

class StudentServiceImplTest {

	 @Mock private StudentDAO studentRepository;
	    @Mock private RoleDAO roleRepository;
	    @Mock private BCryptPasswordEncoder passwordEncoder;
	    @Mock private LogbookDAO logbookRepository;
	    @Mock private ApplicationDAO applicationRepository;
	    @Mock private TraineeshipPositionDAO positionRepository;

	    @InjectMocks
	    private StudentServiceImpl studentService;

	    @BeforeEach
	    void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testGetAllStudents() {
	        List<Student> mockList = List.of(new Student(), new Student());
	        when(studentRepository.findAll()).thenReturn(mockList);
	        assertEquals(2, studentService.getAllStudents().size());
	    }

	    @Test
	    void testFindByUsername() {
	        Student student = new Student();
	        when(studentRepository.findByUsername("john")).thenReturn(student);
	        assertEquals(student, studentService.findByUsername("john"));
	    }

	    @Test
	    void testGetById() {
	        Student student = new Student();
	        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
	        assertEquals(student, studentService.getById(1L));
	    }

	    @Test
	    void testSaveStudent() {
	        Student student = new Student();
	        when(studentRepository.save(student)).thenReturn(student);
	        assertEquals(student, studentService.save(student));
	    }

	    @Test
	    void testDeleteById() {
	        studentService.deleteById(5L);
	        verify(studentRepository).deleteById(5L);
	    }

	    @Test
	    void testSaveProfile() {
	        Student student = new Student();
	        studentService.saveProfile(student);
	        verify(studentRepository).save(student);
	    }

	    @Test
	    void testRetrieveProfile() {
	        Student student = new Student();
	        when(studentRepository.findByUsername("maria")).thenReturn(student);
	        assertEquals(student, studentService.retrieveProfile("maria"));
	    }

	    @Test
	    void testSaveWithUserDTO() {
	        UserDTO dto = new UserDTO();
	        dto.setUsername("maria");
	        dto.setPassword("1234");
	        dto.setRole("student");
	        dto.setFullName("Maria Papadopoulou");

	        Role role = new Role();
	        when(roleRepository.findRoleByName("ROLE_STUDENT")).thenReturn(role);
	        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

	        studentService.save(dto);
	        verify(studentRepository).save(any(Student.class));
	    }

	    @Test
	    void testSaveLogbook() {
	        Student student = spy(new Student());
	        Logbook entry = new Logbook();

	        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

	        studentService.saveLogbook(entry, 1L);

	        verify(student).addLogbookEntry(entry);
	        verify(logbookRepository).save(entry);
	    }

	    @Test
	    void testApplyToPosition_NewApplication() {
	        Student student = new Student();
	        TraineeshipPosition position = new TraineeshipPosition();

	        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
	        when(positionRepository.findById(2L)).thenReturn(Optional.of(position));
	        when(applicationRepository.findByStudentAndPosition(student, position))
	                .thenReturn(Optional.empty());

	        studentService.applyToPosition(1L, 2L);

	        verify(applicationRepository).save(any(Application.class));
	    }

	    @Test
	    void testApplyToPosition_AlreadyApplied() {
	        Student student = new Student();
	        TraineeshipPosition position = new TraineeshipPosition();

	        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
	        when(positionRepository.findById(2L)).thenReturn(Optional.of(position));
	        when(applicationRepository.findByStudentAndPosition(student, position))
	                .thenReturn(Optional.of(new Application()));

	        assertThrows(IllegalStateException.class, () -> {
	            studentService.applyToPosition(1L, 2L);
	        });
	    }

	    @Test
	    void testRegister_NewUser() {
	        UserDTO dto = new UserDTO();
	        dto.setUsername("user1");
	        dto.setPassword("pass");
	        dto.setRole("student");
	        dto.setFullName("User One");

	        when(studentRepository.existsByUsername("user1")).thenReturn(false);
	        when(roleRepository.findRoleByName("ROLE_STUDENT")).thenReturn(new Role());
	        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

	        boolean result = studentService.register(dto);

	        assertTrue(result);
	        verify(studentRepository).save(any(Student.class));
	    }

	    @Test
	    void testRegister_ExistingUser() {
	        UserDTO dto = new UserDTO();
	        dto.setUsername("user1");

	        when(studentRepository.existsByUsername("user1")).thenReturn(true);

	        boolean result = studentService.register(dto);

	        assertFalse(result);
	        verify(studentRepository, never()).save(any());
	    }

	    @Test
	    void testUpdateProfile() {
	        Student existing = new Student();
	        Student updated = new Student();
	        updated.setFullName("Updated");
	        updated.setUniversityId("U123");
	        updated.setPreferredLocation("Thessaloniki");
	        updated.setSkills(Set.of("Java"));
	        updated.setInterests(Set.of("AI"));

	        when(studentRepository.findByUsername("john")).thenReturn(existing);

	        studentService.updateProfile("john", updated);

	        verify(studentRepository).save(existing);
	        assertEquals("Updated", existing.getFullName());
	    }

}
