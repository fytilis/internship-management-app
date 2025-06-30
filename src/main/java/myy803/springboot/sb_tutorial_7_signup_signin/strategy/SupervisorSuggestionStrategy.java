package myy803.springboot.sb_tutorial_7_signup_signin.strategy;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import java.util.*;

public interface SupervisorSuggestionStrategy extends SupervisorAssignmentStrategy  {
	 List<Professor> suggestAll(Long positionId);
}
