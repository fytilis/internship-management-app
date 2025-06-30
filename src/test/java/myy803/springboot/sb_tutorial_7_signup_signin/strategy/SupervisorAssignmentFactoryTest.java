package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupervisorAssignmentFactoryTest {

    private AssignmentBasedOnLoad loadStrategy;
    private AssignmentBasedOnInterests interestStrategy;
    private SupervisorAssignmentFactory factory;

    @BeforeEach
    void setUp() {
        loadStrategy = mock(AssignmentBasedOnLoad.class);
        interestStrategy = mock(AssignmentBasedOnInterests.class);
        factory = new SupervisorAssignmentFactory(loadStrategy, interestStrategy);
    }

    @Test
    void testCreateReturnsLoadStrategy() {
        SupervisorAssignmentStrategy strategy = factory.create("load");
        assertEquals(loadStrategy, strategy);
    }

    @Test
    void testCreateReturnsInterestsStrategy() {
        SupervisorAssignmentStrategy strategy = factory.create("interests");
        assertEquals(interestStrategy, strategy);
    }

    @Test
    void testCreateThrowsForUnknownStrategy() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.create("invalid-strategy");
        });

        assertEquals("Unknown strategy: invalid-strategy", exception.getMessage());
    }
}
