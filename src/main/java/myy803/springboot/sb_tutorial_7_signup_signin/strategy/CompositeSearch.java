package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
@Component
public class CompositeSearch implements PositionsSearchStrategy {
	private final SearchBasedOnLocation locationStrategy;
    private final SearchBasedOnInterests interestsStrategy;

    public CompositeSearch(SearchBasedOnLocation locationStrategy, SearchBasedOnInterests interestsStrategy) {
        this.locationStrategy = locationStrategy;
        this.interestsStrategy = interestsStrategy;
    }

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        List<TraineeshipPosition> byLocation = locationStrategy.search(applicantUsername);
        List<TraineeshipPosition> byInterest = interestsStrategy.search(applicantUsername);

   
        return byLocation.stream()
                .filter(byInterest::contains)
                .collect(Collectors.toList());
    }
	
}
