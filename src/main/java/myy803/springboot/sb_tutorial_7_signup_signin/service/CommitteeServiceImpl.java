package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.CommitteeDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.RoleDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.PositionsSearchFactory;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.PositionsSearchStrategy;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.SupervisorAssignmentFactory;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.SupervisorAssignmentStrategy;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.SupervisorSuggestionStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CommitteeServiceImpl implements CommitteeService {
	
	@Autowired
    private  CommitteeDAO committeeRepository;
	@Autowired
    private  StudentDAO studentRepository;
	@Autowired
    private  TraineeshipPositionDAO positionRepository;
	@Autowired
	private RoleDAO roleRepository;
	@Autowired
	private SupervisorAssignmentFactory supervisorAssignmentFactory;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private PositionsSearchFactory positionsSearchFactory;
	@Autowired
	private SupervisorAssignmentFactory assignmentFactory;


    @Override
    public List<Committee> getAllCommitteeMembers() {
        return committeeRepository.findAll();
    }

    @Override
    public Committee findByUsername(String username) {
        return committeeRepository.findByUsername(username);
    }

    @Override
    public void save(Committee committee) {
        committeeRepository.save(committee);
    }

    @Override
    public void deleteById(Long id) {
        committeeRepository.deleteById(id);
    }

 
    @Override
    public List<Student> retrieveTraineeshipApplications() {
        return studentRepository.findByAppliedTrue();
    }

    @Override
    public void assignPosition(Long positionId,String studentUsername) {
        Optional<TraineeshipPosition> positionOpt = positionRepository.findById(Long.valueOf(positionId));
        Student student = studentRepository.findByUsername(studentUsername);

        if (positionOpt.isPresent() && student != null) {
            TraineeshipPosition position = positionOpt.get();
            position.setStudent(student);
            position.setStatus(PositionStatus.ASSIGNED);
            student.setAssignedPosition(position);
            positionRepository.save(position);
            studentRepository.save(student);
        }
    }

    @Override
    @Transactional
    public Optional<Committee> getById(Long id) {
        return committeeRepository.findById(id);
    }


    @Override
    @Transactional
    public List<TraineeshipPosition> listAssignedTraineeships() {
        return positionRepository.findByStatus(PositionStatus.ASSIGNED);
    }

    @Override
    @Transactional
    public void completeAssignedTraineeships(Long positionId) {
        positionRepository.findById(positionId).ifPresent(pos -> {
            pos.setStatus(PositionStatus.COMPLETED);
            positionRepository.save(pos);
        });
    }
    
    @Override
    @Transactional
    public boolean register(UserDTO userDTO) {
        if (committeeRepository.existsByUsername(userDTO.getUsername())) {
            return false;
        }
        Role role = roleRepository.findRoleByName("ROLE_" + userDTO.getRole().toUpperCase());
        if (role == null) return false;

        Committee committtee = new Committee();
        committtee.setUsername(userDTO.getUsername());
        committtee.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        committtee.setEnabled(true);
        committtee.setFullName(userDTO.getFullName()); 
        committtee.setRole(role); 
        committeeRepository.save(committtee);

        return true;
    }
    
    @Override
    @Transactional
    public List<TraineeshipPosition> searchPositions(String strategy, String username){
    	PositionsSearchStrategy searchStrategy = positionsSearchFactory.create(strategy);
    	return searchStrategy.search(username);
    }
    @Override
    public Map<TraineeshipPosition, List<Professor>> getSuggestedSupervisors(String strategyName) {
        SupervisorAssignmentStrategy baseStrategy = assignmentFactory.create(strategyName);

        if (!(baseStrategy instanceof SupervisorSuggestionStrategy suggestionStrategy)) {
            throw new IllegalArgumentException("Strategy does not support suggestions");
        }

        Map<TraineeshipPosition, List<Professor>> suggestions = new LinkedHashMap<>();

        for (TraineeshipPosition pos : positionRepository.findAll()) {
            if (pos.getSupervisor() == null) { 
                List<Professor> professors = suggestionStrategy.suggestAll(pos.getId());
                suggestions.put(pos, professors);
            }
        }

        return suggestions;
    }

    
 
}