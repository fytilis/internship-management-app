package myy803.springboot.sb_tutorial_7_signup_signin.strategy;
import java.util.List;
import  myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;


public interface PositionsSearchStrategy {
	List<TraineeshipPosition>  search(String applicantUsername);
}
