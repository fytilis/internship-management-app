package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompositeSearchTest {

    private SearchBasedOnLocation locationStrategy;
    private SearchBasedOnInterests interestsStrategy;
    private CompositeSearch compositeSearch;

    @BeforeEach
    void setUp() {
        locationStrategy = mock(SearchBasedOnLocation.class);
        interestsStrategy = mock(SearchBasedOnInterests.class);
        compositeSearch = new CompositeSearch(locationStrategy, interestsStrategy);
    }

    @Test
    void testSearchReturnsIntersectionOfResults() {
        
        TraineeshipPosition common = new TraineeshipPosition();
        common.setId(1L);

        when(locationStrategy.search("student")).thenReturn(List.of(common));
        when(interestsStrategy.search("student")).thenReturn(List.of(common));

        List<TraineeshipPosition> result = compositeSearch.search("student");

        assertEquals(1, result.size());
        assertTrue(result.contains(common));
    }
    @Test
    void testSearchReturnsEmptyIfNoMatch() {
        when(locationStrategy.search("student")).thenReturn(List.of());
        when(interestsStrategy.search("student")).thenReturn(List.of());

        List<TraineeshipPosition> result = compositeSearch.search("student");

        assertTrue(result.isEmpty());
    }
}
