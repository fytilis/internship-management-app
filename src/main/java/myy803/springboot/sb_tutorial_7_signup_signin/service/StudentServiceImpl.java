package myy803.springboot.sb_tutorial_7_signup_signin.service;



import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.ApplicationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.LogbookDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.RoleDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private ApplicationDAO applicationRepository;

	@Autowired
	private TraineeshipPositionDAO positionRepository;

	@Autowired
	private StudentDAO studentRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private LogbookDAO logbookRepository;
	
	@Autowired
	private RoleDAO roleRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Student getById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    
    

    @Override
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void saveProfile(Student student) {
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student retrieveProfile(String username) {
        return studentRepository.findByUsername(username);
    }

    @Override
	@Transactional
	public void save(UserDTO userDto) {
    	Role role = roleRepository.findRoleByName("ROLE_" + userDto.getRole().toUpperCase());
		Student student = new Student();
		student.setUsername(userDto.getUsername());
		student.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		student.setFullName(userDto.getFullName());		
		student.setRole(role);	
		
		studentRepository.save(student);
	}
    @Override
    @Transactional
    public void saveLogbook(Logbook entry, Long studentId) {
    	 Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
    	 student.addLogbookEntry(entry);
         logbookRepository.save(entry);   
    	}
    @Override
    @Transactional
    public void applyToPosition(Long studentId, Long positionId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        
		TraineeshipPosition position = positionRepository.findById(positionId).orElseThrow();

        boolean alreadyApplied = applicationRepository.findByStudentAndPosition(student, position).isPresent();
        if (alreadyApplied) {
            throw new IllegalStateException("You have already applied to this position.");
        }

        Application application = new Application();
        application.setStudent(student);
        application.setPosition(position);
        application.setStatus(ApplicationStatus.PENDING);

        applicationRepository.save(application);
    }
    
    
    @Override
    @Transactional
    public boolean register(UserDTO userDTO) {
        if (studentRepository.existsByUsername(userDTO.getUsername())) {
            return false;
        }
        Role role = roleRepository.findRoleByName("ROLE_" + userDTO.getRole().toUpperCase());
        if (role == null) return false;

        Student student = new Student();
        student.setUsername(userDTO.getUsername());
        student.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        student.setEnabled(true);
        student.setFullName(userDTO.getFullName()); 
        student.setRole(role); 
        studentRepository.save(student);

        return true;
    }
    @Override
    @Transactional
    public void updateProfile(String username, Student updatedData) {
        Student existing = studentRepository.findByUsername(username);

        existing.setFullName(updatedData.getFullName());
        existing.setUniversityId(updatedData.getUniversityId());
        existing.setPreferredLocation(updatedData.getPreferredLocation());
        existing.setSkills(updatedData.getSkills());
        existing.setInterests(updatedData.getInterests());

        studentRepository.save(existing);
    }
    
    
} 
