package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.CompanyDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.EvaluationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.RoleDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired 	
	private  CompanyDAO companyRepository;
	@Autowired 	
	private  StudentDAO studentRepository;
	@Autowired  	
	private  TraineeshipPositionDAO positionRepository;
	@Autowired   
	private  EvaluationDAO evaluationRepository;
	@Autowired
	private RoleDAO roleRepository;
	    
	@Autowired
	    
	private BCryptPasswordEncoder passwordEncoder;

	   

	    @Override
	    public Company retrieveProfile(String username) {
	        return companyRepository.findByUsername(username);
	    }

	    @Override
	    public void saveProfile(Company company) {
	        companyRepository.save(company);
	    }

	    @Override
	    public List<TraineeshipPosition> retrieveAvailablePositions(String username) {
	        return positionRepository.findByStatus(PositionStatus.AVAILABLE).stream()
	                .filter(p -> p.getCompany() != null && p.getCompany().getUsername().equals(username))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public void addPosition(String username, TraineeshipPosition position) {
	        Company company = companyRepository.findByUsername(username);
	        position.setCompany(company);
	        position.setStatus(PositionStatus.AVAILABLE);
	        positionRepository.save(position);
	    }

	    @Override
	    public List<TraineeshipPosition> retrieveAssignedPositions(String username) {
	        return positionRepository.findByStatus(PositionStatus.ASSIGNED).stream()
	                .filter(p -> p.getCompany() != null && p.getCompany().getUsername().equals(username))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public void evaluateAssignedPosition(Long positionId) {
	    
	    }

	    @Override
	    public void saveEvaluation(Long positionId, Evaluation evaluation) {
	        TraineeshipPosition position = positionRepository.findById(positionId)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid position ID"));
	        evaluation.setTraineeshipPosition(position);
	        evaluationRepository.save(evaluation);
	    }
	    
	    @Override
	    public boolean register(UserDTO userDTO) {
	    	if (companyRepository.existsByUsername(userDTO.getUsername())) {
	            return false;
	        }

	        Role role = roleRepository.findRoleByName("ROLE_" + userDTO.getRole().toUpperCase());
	        if (role == null) return false;

	        Company company = new Company();
	        company.setUsername(userDTO.getUsername());
	        company.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	        company.setEnabled(true);
	        company.setFullName(userDTO.getFullName()); 
	        company.setRole(role); 
	        companyRepository.save(company);

	        return true;
	    }
	    
	    @Override
	    @Transactional
	    public Company getById(Long id) {
	        return companyRepository.findById(id).orElse(null);
	    }
	    
	    @Override
	    @Transactional
	    public Company findByUsername(String username) {
	        return companyRepository.findByUsername(username);
	    }

	    @Override
	    @Transactional
	    public void updateProfile(String username, Company updated) {
	        Company existing = findByUsername(username);
	        existing.setFullName(updated.getFullName());
	        existing.setLocation(updated.getLocation());
	        companyRepository.save(existing);
	    }

	    @Override
	    @Transactional
	    public void createTraineeshipPosition(String username, TraineeshipPosition position) {
	       
	        Company company = companyRepository.findByUsername(username);

	        
	        position.setCompany(company);
	        position.setStatus(PositionStatus.AVAILABLE); 

	       
	        positionRepository.save(position);
	    }
	    
	    public List<TraineeshipPosition> getAssignedPositions(String companyUsername) {
	        Company company = companyRepository.findByUsername(companyUsername);
	        if (company == null) {
	            return Collections.emptyList(); 
	        }

	        return company.getPositions().stream()
	            .filter(position -> position.getStudent() != null && position.getStatus() == PositionStatus.ASSIGNED)
	            .toList();
	    }

	    @Override
	    public void removeStudentFromCompany(Long studentId) {
	        Student student = studentRepository.findById(studentId)
	            .orElseThrow(() -> new RuntimeException("Student not found"));
	        
	        TraineeshipPosition position = positionRepository.findByStudent(student)
	            .orElse(null);

	        if (position != null) {
	            position.setStudent(null);
	            position.setStatus(PositionStatus.AVAILABLE);
	            positionRepository.save(position);
	        }
	    }

	   

}