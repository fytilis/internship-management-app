package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import java.util.List;
@Service
public class TraineeshipSearchService {
	private final PositionsSearchFactory searchFactory;

    @Autowired
    public TraineeshipSearchService(PositionsSearchFactory searchFactory) {
        this.searchFactory = searchFactory;
    }

    public List<TraineeshipPosition> search(String username, String strategyName) {
        PositionsSearchStrategy strategy = searchFactory.create(strategyName);
        return strategy.search(username);
    }
}
