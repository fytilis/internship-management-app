package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentDAOTest {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private RoleDAO roleDAO;


    private Role createRole() {
        return roleDAO.findByName("ROLE_STUDENT").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_STUDENT");
            return roleDAO.save(role);
        });
    }

    private Student createStudent(String username, boolean applied) {
        Student student = new Student();
        student.setUsername(username);
        student.setFullName("Student " + username);
        student.setPassword("1234");
        student.setUniversityId("UNI" + username);
        student.setPreferredLocation("Athens");
        student.setEnabled(true);
        student.setApplied(applied);
        student.setRole(createRole());
        return studentDAO.save(student);
    }

    @Test
    void testFindByUsername() {
        Student saved = createStudent("student1", false);

        Student found = studentDAO.findByUsername("student1");
        assertNotNull(found);
        assertEquals("Student student1", found.getFullName());
    }

    @Test
    void testExistsByUsername() {
        createStudent("student2", false);

        assertTrue(studentDAO.existsByUsername("student2"));
        assertFalse(studentDAO.existsByUsername("no_user"));
    }

    @Test
    void testFindByAppliedTrue() {
        createStudent("student3", true);
        createStudent("student4", false);

        List<Student> applied = studentDAO.findByAppliedTrue();
        assertEquals(1, applied.size());
        assertEquals("student3", applied.get(0).getUsername());
    }

    @Test
    void testFindById() {
        Student saved = createStudent("student5", true);

        Optional<Student> found = studentDAO.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("student5", found.get().getUsername());
    }

    @Test
    void testFindAll() {
        createStudent("student6", true);
        createStudent("student7", false);

        List<Student> all = studentDAO.findAll();
        assertEquals(2, all.size());
    }
}

