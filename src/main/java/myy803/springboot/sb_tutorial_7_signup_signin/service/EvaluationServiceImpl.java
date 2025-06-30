package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.StudentSumDTO;
import java.util.*;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.EvaluationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImpl implements EvaluationService {
	@Autowired
    private  EvaluationDAO evaluationRepository;
	@Autowired
	private TraineeshipPositionDAO positionRepository;
    

    @Override
    public Evaluation saveEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @Override
    public List<Evaluation> findByTraineeshipPosition(TraineeshipPosition position) {
        return evaluationRepository.findByTraineeshipPosition(position);
    }

    @Override
    public void deleteById(Long id) {
        evaluationRepository.deleteById(id);
    }

    @Override
    public Evaluation getById(Long id) {
        return evaluationRepository.findById(id).orElse(null);
    }

	public List<Evaluation> getAllEvaluations() {
		return evaluationRepository.findAll();
	}
	@Override
	public Optional<Evaluation> findByStudentAndEvaluatorTypeAndTraineeshipPosition(Student student, EvaluatorType evaluatorType, TraineeshipPosition position) {
	    return evaluationRepository.findByStudentAndEvaluatorTypeAndTraineeshipPosition(student, evaluatorType, position);
	}
	@Override
	public Optional<Evaluation> findByStudentIdAndEvaluatorTypeAndTraineeshipPositionId(Long studentId, EvaluatorType evaluatorType, Long positionId) {
	    return evaluationRepository.findByStudentIdAndEvaluatorTypeAndTraineeshipPositionId(studentId, evaluatorType, positionId);
	}

	@Override
	@Transactional
	public void updateEvaluation(Evaluation newEval) {
	    Optional<Evaluation> existingOpt =
	        evaluationRepository.findByStudentAndEvaluatorTypeAndTraineeshipPosition(
	            newEval.getStudent(),
	            newEval.getEvaluatorType(),
	            newEval.getTraineeshipPosition()
	        );

	    if (existingOpt.isPresent()) {
	        Evaluation existing = existingOpt.get();
	        // copy over all the fields you care about
	        existing.setMotivation(newEval.getMotivation());
	        existing.setEfficiency(newEval.getEfficiency());
	        existing.setEffectiveness(newEval.getEffectiveness());
	        existing.setComments(newEval.getComments());
	        existing.setGrade(newEval.getGrade());
	        existing.setCompanyAverage(newEval.getCompanyAverage());
	        existing.setProfessorAverage(newEval.getProfessorAverage());
	        existing.setTotalAverage(newEval.getTotalAverage());
	        existing.setPassed(newEval.isPassed());

	        evaluationRepository.save(existing);
	    } else {
	        // first time for this (student, type, position) → insert
	        evaluationRepository.save(newEval);
	    }
	}

	@Override
	public void submitCompanyEvaluation(Evaluation evaluation) {
	    evaluation.setEvaluatorType(EvaluatorType.COMPANY);
	    updateEvaluation(evaluation);
	}

	@Override
	public void submitProfessorEvaluation(Evaluation evaluation) {
	    evaluation.setEvaluatorType(EvaluatorType.PROFESSOR);
	    updateEvaluation(evaluation);
	}

	@Override
	public Optional<Evaluation> findByStudentAndEvaluatorType(Student student, EvaluatorType evaluatorType) {
	    return evaluationRepository.findByStudentAndEvaluatorType(student, evaluatorType);
	}
	
	 @Override
	    public List<StudentSumDTO> getCommitteeSummaries(Long positionId) {
	      
	        List<Evaluation> allEvals = evaluationRepository.findAll()
	            .stream()
	            .filter(e -> positionId.equals(e.getTraineeshipPosition().getId()))
	            .collect(Collectors.toList());

	        
	        Map<Long, List<Evaluation>> byStudent = allEvals.stream()
	            .collect(Collectors.groupingBy(e -> e.getStudent().getId()));

	        List<StudentSumDTO> result = new ArrayList<>();

	        for (Map.Entry<Long, List<Evaluation>> entry : byStudent.entrySet()) {
	            List<Evaluation> evals = entry.getValue();
	            
	            Optional<Evaluation> profEvalOpt = evals.stream()
	                .filter(e -> e.getEvaluatorType() == EvaluatorType.PROFESSOR)
	                .findFirst();
	            Optional<Evaluation> compEvalOpt = evals.stream()
	                .filter(e -> e.getEvaluatorType() == EvaluatorType.COMPANY)
	                .findFirst();

	            if (profEvalOpt.isPresent() && compEvalOpt.isPresent()) {
	                Evaluation prof = profEvalOpt.get();
	                Evaluation comp = compEvalOpt.get();

	                
	                double profAvg = (prof.getMotivation()
	                                + prof.getEfficiency()
	                                + prof.getEffectiveness()) / 3.0;
	                double compAvg = (comp.getMotivation()
	                                + comp.getEfficiency()
	                                + comp.getEffectiveness()) / 3.0;

	                double overall = (profAvg + compAvg) / 2.0;

	                String studentName = prof.getStudent().getFullName();

	                result.add(new StudentSumDTO(
	                    studentName,
	                    round(profAvg),
	                    round(compAvg),
	                    round(overall)
	                ));
	            }
	        }

	        result.sort(Comparator.comparingDouble(StudentSumDTO::getOverallAvg).reversed());
	        return result;
	    }

	    private double round(double value) {
	        return Math.round(value * 100.0) / 100.0;
	    }


	
}
	

