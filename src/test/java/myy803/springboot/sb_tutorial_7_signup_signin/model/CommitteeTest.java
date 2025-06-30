package myy803.springboot.sb_tutorial_7_signup_signin.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CommitteeTest {
	@Test
    void testBasicFieldsWithMockedRole() {
        Role mockRole = Mockito.mock(Role.class);
        Mockito.when(mockRole.getName()).thenReturn("ROLE_COMMITTEE");

        Committee committee = new Committee();
        committee.setUsername("committee_user");
        committee.setPassword("securePass123");
        committee.setFullName("Evaluation Committee");
        committee.setEnabled(true);
        committee.setRole(mockRole);

        assertEquals("committee_user", committee.getUsername());
        assertEquals("securePass123", committee.getPassword());
        assertEquals("Evaluation Committee", committee.getFullName());
        assertTrue(committee.isEnabled());
        assertEquals("ROLE_COMMITTEE", committee.getRole().getName());

        Mockito.verify(mockRole).getName();
    }
}
