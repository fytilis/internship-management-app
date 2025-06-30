package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApplicationDAOTest {

    @Autowired
    private ApplicationDAO applicationDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private TraineeshipPositionDAO positionDAO;

    private Student createValidStudent(String username) {
        Student student = new Student();
        student.setUsername(username);
        student.setFullName("Test User");
        student.setPassword("1234");
        student.setEnabled(true);
        student.setApplied(false);
        return studentDAO.save(student);
    }

    private TraineeshipPosition createValidPosition(String description) {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setDescription(description);
        position.setStartDate(LocalDate.now());
        position.setEndDate(LocalDate.now().plusMonths(3));
        position.setEvaluated(false);
        return positionDAO.save(position);
    }

    @Test
    void testFindByStudentAndPosition() {
        Student student = createValidStudent("nikos");
        TraineeshipPosition position = createValidPosition("Backend Internship");

        Application application = new Application();
        application.setStudent(student);
        application.setPosition(position);
        applicationDAO.save(application);

        Optional<Application> found = applicationDAO.findByStudentAndPosition(student, position);
        assertTrue(found.isPresent());
        assertEquals("nikos", found.get().getStudent().getUsername());
    }

    @Test
    void testFindByStudent() {
        Student student = createValidStudent("maria");
        TraineeshipPosition position = createValidPosition("Data Analyst");

        Application app1 = new Application();
        app1.setStudent(student);
        app1.setPosition(position);
        applicationDAO.save(app1);

        List<Application> applications = applicationDAO.findByStudent(student);
        assertEquals(1, applications.size());
        assertEquals("maria", applications.get(0).getStudent().getUsername());
    }

    @Test
    void testFindByPosition() {
        Student student = createValidStudent("john");
        TraineeshipPosition position = createValidPosition("DevOps Internship");

        Application app = new Application();
        app.setStudent(student);
        app.setPosition(position);
        applicationDAO.save(app);

        List<Application> applications = applicationDAO.findByPosition(position);
        assertEquals(1, applications.size());
        assertEquals("john", applications.get(0).getStudent().getUsername());
    }
}
