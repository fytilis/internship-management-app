package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LogbookDAOTest {

    @Autowired
    private LogbookDAO logbookDAO;

    @Autowired
    private StudentDAO studentDAO;

    private Student createStudent(String username) {
        Student student = new Student();
        student.setUsername(username);
        student.setFullName("Test Student");
        student.setPassword("1234");
        student.setEnabled(true);
        student.setApplied(true);
        return studentDAO.save(student);
    }

    @Test
    void testFindByStudent() {
        Student student = createStudent("student_log");

        Logbook log1 = new Logbook();
        log1.setDate(LocalDateTime.now());
        log1.setContent("First log entry");
        log1.setStudent(student);

        Logbook log2 = new Logbook();
        log2.setDate(LocalDateTime.now().minusDays(1));
        log2.setContent("Second log entry");
        log2.setStudent(student);

        logbookDAO.save(log1);
        logbookDAO.save(log2);

        List<Logbook> logs = logbookDAO.findByStudent(student);

        assertEquals(2, logs.size());
        assertTrue(logs.stream().anyMatch(l -> l.getContent().equals("First log entry")));
        assertTrue(logs.stream().anyMatch(l -> l.getContent().equals("Second log entry")));
    }
}
