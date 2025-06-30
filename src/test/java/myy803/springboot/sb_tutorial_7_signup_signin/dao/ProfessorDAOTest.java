package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProfessorDAOTest {

    @Autowired
    private ProfessorDAO professorDAO;

    @Autowired
    private RoleDAO roleDAO;

    private Role createRole() {
        Role role = new Role();
        role.setName("ROLE_PROFESSOR");
        return roleDAO.save(role);
    }

    private Professor createProfessor(String username) {
        Professor prof = new Professor();
        prof.setUsername(username);
        prof.setPassword("secret");
        prof.setFullName("Dr. " + username);
        prof.setEnabled(true);
        prof.setRole(createRole());
        return professorDAO.save(prof);
    }

    @Test
    void testFindByUsername() {
        Professor saved = createProfessor("profA");

        Professor found = professorDAO.findByUsername("profA");
        assertNotNull(found);
        assertEquals("Dr. profA", found.getFullName());
    }

    @Test
    void testExistsByUsername() {
        createProfessor("profB");

        assertTrue(professorDAO.existsByUsername("profB"));
        assertFalse(professorDAO.existsByUsername("nonexistent"));
    }

    @Test
    void testFindById() {
        Professor saved = createProfessor("profC");

        Optional<Professor> found = professorDAO.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("profC", found.get().getUsername());
    }
}
