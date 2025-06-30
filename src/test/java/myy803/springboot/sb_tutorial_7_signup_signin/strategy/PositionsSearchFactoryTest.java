package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PositionsSearchFactoryTest {

    private SearchBasedOnLocation locationStrategy;
    private SearchBasedOnInterests interestStrategy;
    private CompositeSearch compositeStrategy;
    private PositionsSearchFactory factory;

    @BeforeEach
    void setUp() {
        locationStrategy = mock(SearchBasedOnLocation.class);
        interestStrategy = mock(SearchBasedOnInterests.class);
        compositeStrategy = mock(CompositeSearch.class);

        factory = new PositionsSearchFactory(locationStrategy, interestStrategy, compositeStrategy);
    }

    @Test
    void testCreateInterestStrategy() {
        PositionsSearchStrategy strategy = factory.create("interests");
        assertSame(interestStrategy, strategy);
    }

    @Test
    void testCreateLocationStrategy() {
        PositionsSearchStrategy strategy = factory.create("location");
        assertSame(locationStrategy, strategy);
    }

    @Test
    void testCreateCompositeStrategy() {
        PositionsSearchStrategy strategy = factory.create("composite");
        assertSame(compositeStrategy, strategy);
    }

    @Test
    void testCreateUnknownStrategyThrows() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.create("unknown");
        });
        assertEquals("Unknown strategy: unknown", exception.getMessage());
    }
}
