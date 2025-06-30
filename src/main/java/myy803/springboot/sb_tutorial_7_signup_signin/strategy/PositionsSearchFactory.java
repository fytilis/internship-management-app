package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PositionsSearchFactory {
    private SearchBasedOnLocation searchBasedOnLocation;
    private SearchBasedOnInterests searchBasedOnInterests;
    private CompositeSearch compositeSearch;
    @Autowired
    public PositionsSearchFactory(SearchBasedOnLocation loc,
                                  SearchBasedOnInterests inter,
                                  CompositeSearch comp) {
        this.searchBasedOnLocation = loc;
        this.searchBasedOnInterests = inter;
        this.compositeSearch = comp;
    }

    public PositionsSearchStrategy create(String strategy) {
        return switch (strategy.toLowerCase()) {
            case "interests" -> searchBasedOnInterests;
            case "location" -> searchBasedOnLocation;
            case "composite" -> compositeSearch;
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategy);
        };
    }
}