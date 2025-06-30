package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SearchBasedOnInterestsTest {

    private StudentDAO studentDAO;
    private TraineeshipPositionDAO positionDAO;
    private SearchBasedOnInterests strategy;

    @BeforeEach
    void setUp() {
        studentDAO = mock(StudentDAO.class);
        positionDAO = mock(TraineeshipPositionDAO.class);
        strategy = new SearchBasedOnInterests(positionDAO, studentDAO, 0.5); // threshold 50%
    }

    @Test
    void testSearchReturnsMatchingPositions() {
        
        Student student = new Student();
        Set<String> studentInterests = new HashSet<>(Arrays.asList("java", "spring", "docker"));
        student.setInterests(studentInterests);

        
        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setTopics(Set.of("java", "spring", "kubernetes"));

        
        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setTopics(Set.of("docker", "linux", "bash"));

        when(studentDAO.findByUsername("student1")).thenReturn(student);
        when(positionDAO.findAll()).thenReturn(List.of(pos1, pos2));

        
        List<TraineeshipPosition> results = strategy.search("student1");

        
        assertEquals(1, results.size());
        assertTrue(results.contains(pos1));
        assertFalse(results.contains(pos2));
    }

    @Test
    void testSearchReturnsEmptyIfNoMatch() {
        Student student = new Student();
        student.setInterests(Set.of("ai", "ml"));

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTopics(Set.of("accounting", "sales"));

        when(studentDAO.findByUsername("student2")).thenReturn(student);
        when(positionDAO.findAll()).thenReturn(List.of(pos));

        List<TraineeshipPosition> results = strategy.search("student2");

        assertTrue(results.isEmpty());
    }
}