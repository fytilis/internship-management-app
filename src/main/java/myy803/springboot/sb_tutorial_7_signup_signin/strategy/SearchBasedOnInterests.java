package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import java.util.HashSet;
import java.util.List;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
@Component
public class SearchBasedOnInterests implements PositionsSearchStrategy{
	private final TraineeshipPositionDAO positionRepository;
	private final StudentDAO studentRepository;
    private final double similarityThreshold;
	
	public SearchBasedOnInterests(TraineeshipPositionDAO positionRepository,StudentDAO studentRepository,@Value("${strategy.similarity-threshold}")double similarityThreshold) {
		this.positionRepository = positionRepository;
		this.studentRepository = studentRepository;
		this.similarityThreshold = similarityThreshold;
	}
	
	@Override
	public List<TraineeshipPosition> search(String applicantUsername){
		Student student = studentRepository.findByUsername(applicantUsername);
		Set<String> interests = student.getInterests();
		List<TraineeshipPosition> allPositions = positionRepository.findAll();
		 return allPositions.stream()
	                .filter(pos -> jaccardSimilarity(interests, pos.getTopics()) >= similarityThreshold)
	                .collect(Collectors.toList());
    }
	
	private double jaccardSimilarity(Set<String> a,Set<String> b) {
		Set<String> intersection = new HashSet<>(a);
		intersection.retainAll(b);
		Set<String> union = new HashSet<>(a);
		union.addAll(b);
		return union.isEmpty()? 0.0 : (double) intersection.size() / union.size(); 
	}
}
	

