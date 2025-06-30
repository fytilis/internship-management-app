package myy803.springboot.sb_tutorial_7_signup_signin.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;


public class CompanyTest {
	 @Test
	    void testCompanyWithMockedRole() {
	        Role mockRole = Mockito.mock(Role.class);
	        Mockito.when(mockRole.getName()).thenReturn("ROLE_COMPANY");

	        Company company = new Company();
	        company.setUsername("company_user");
	        company.setPassword("securePass!");
	        company.setFullName("Company Inc.");
	        company.setLocation("Athens");
	        company.setEnabled(true);
	        company.setRole(mockRole);

	        assertEquals("company_user", company.getUsername());
	        assertEquals("securePass!", company.getPassword());
	        assertEquals("Company Inc.", company.getFullName());
	        assertEquals("Athens", company.getLocation());
	        assertTrue(company.isEnabled());
	        assertEquals("ROLE_COMPANY", company.getRole().getName());

	        Mockito.verify(mockRole).getName();
	    }

	    @Test
	    void testSetAndGetPositions() {
	        Company company = new Company();
	        Set<TraineeshipPosition> positions = new HashSet<>();
	        positions.add(new TraineeshipPosition());
	        company.setPositions(positions);

	        assertEquals(1, company.getPositions().size());
	    }
}
