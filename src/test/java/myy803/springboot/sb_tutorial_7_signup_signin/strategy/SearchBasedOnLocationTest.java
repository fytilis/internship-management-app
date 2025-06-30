package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.CompanyDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SearchBasedOnLocationTest {

    private StudentDAO studentDAO;
    private CompanyDAO companyDAO;
    private SearchBasedOnLocation strategy;

    @BeforeEach
    void setUp() {
        studentDAO = mock(StudentDAO.class);
        companyDAO = mock(CompanyDAO.class);
        strategy = new SearchBasedOnLocation(studentDAO, companyDAO);
    }

    @Test
    void testSearchReturnsPositionsMatchingStudentLocation() {
        // Given
        Student student = new Student();
        student.setPreferredLocation("Athens");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        TraineeshipPosition pos2 = new TraineeshipPosition();

        Company company1 = new Company();
        company1.setLocation("Athens");
        company1.setPositions(Set.of(pos1, pos2));

        Company company2 = new Company();
        company2.setLocation("Thessaloniki");
        company2.setPositions(Set.of(new TraineeshipPosition())); // irrelevant

        when(studentDAO.findByUsername("student1")).thenReturn(student);
        when(companyDAO.findAll()).thenReturn(List.of(company1, company2));

        // When
        List<TraineeshipPosition> result = strategy.search("student1");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(pos1));
        assertTrue(result.contains(pos2));

        verify(studentDAO).findByUsername("student1");
        verify(companyDAO).findAll();
    }
}
