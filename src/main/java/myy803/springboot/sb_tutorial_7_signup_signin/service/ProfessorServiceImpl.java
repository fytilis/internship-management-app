package myy803.springboot.sb_tutorial_7_signup_signin.service;



import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.EvaluationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.RoleDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {
	
	private static final PositionStatus ASSIGNED = null;
	@Autowired 
    private  ProfessorDAO professorRepository;
	@Autowired
	private TraineeshipPositionDAO traineeshipPositionRepository;

	@Autowired
	private EvaluationDAO evaluationRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private RoleDAO roleRepository;
	@Autowired
	private TraineeshipPositionDAO positionRepository;

    @Override
    @Transactional
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    @Override
    @Transactional
    public Professor getById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Professor findByUsername(String username) {
        return professorRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        professorRepository.deleteById(id);
    }
    
    
    
    @Override
    @Transactional
    public void saveProfile(Professor professor) {
        professorRepository.save(professor);
    }
    
    @Override
	@Transactional
	public void save(UserDTO userDto) {
    	Role role = roleRepository.findRoleByName("ROLE_" + userDto.getRole().toUpperCase());
		Professor professor = new Professor();
		professor.setUsername(userDto.getUsername());
		professor.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		professor.setFullName(userDto.getFullName());
		professor.setRole(role);	
		
		professorRepository.save(professor);
	}

    @Override
    @Transactional
    public Professor retrieveProfile(String username) {
    	return professorRepository.findByUsername(username);
    }
    
    @Override
    @Transactional
    public List<TraineeshipPosition> retrieveAssignedPositions(String username) {
        Professor professor = professorRepository.findByUsername(username);

        return traineeshipPositionRepository.findBySupervisorAndStatus(professor,ASSIGNED);
    }
    @Override
    @Transactional
    public void evaluateAssignedPosition(Long positionId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found: " + positionId));
        position.setEvaluated(true);
        traineeshipPositionRepository.save(position);
    }

    @Override
    @Transactional
    public void saveEvaluation(Long positionId, Evaluation evaluation) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found: " + positionId));
        evaluation.setTraineeshipPosition(position); // Assuming Evaluation has a reference to TraineeshipPosition
        evaluationRepository.save(evaluation);
    }
    @Override
    @Transactional
    public boolean register(UserDTO userDTO) {
        if (professorRepository.existsByUsername(userDTO.getUsername())) {
            return false;
        }
        Role role = roleRepository.findRoleByName("ROLE_" + userDTO.getRole().toUpperCase());
        if (role == null) return false;

        Professor professor = new Professor();
        professor.setUsername(userDTO.getUsername());
        professor.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        professor.setEnabled(true);
        professor.setRole(role); 
        professorRepository.save(professor);

        return true;
    }
    
    @Override
    @Transactional
    public void updateProfile(String username, Professor updatedData) {
        Professor existing = professorRepository.findByUsername(username);

        existing.setFullName(updatedData.getFullName());
        existing.setInterests(updatedData.getInterests());
        existing.setSupervisedPositions(updatedData.getSupervisedPositions());

        professorRepository.save(existing);
    }
    
    @Override
    @Transactional
    public List<TraineeshipPosition> getAssignedPositions(String professorUsername){
    	Professor prof = professorRepository.findByUsername(professorUsername);
    	return traineeshipPositionRepository.findBySupervisorUsername(prof.getUsername());
    }
    public Evaluation prepareEvaluationForPosition(Long positionId) {
        TraineeshipPosition position = positionRepository.findById(positionId)
            .orElseThrow(() -> new RuntimeException("Traineeship not found"));

        Evaluation evaluation = new Evaluation();
        evaluation.setEvaluatorType(EvaluatorType.PROFESSOR);
        evaluation.setTraineeshipPosition(position);
        evaluation.setStudent(position.getStudent()); 

        return evaluation;
    }
    

}
