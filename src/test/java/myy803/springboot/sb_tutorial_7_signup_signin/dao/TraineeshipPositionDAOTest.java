package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TraineeshipPositionDAOTest {

    @Autowired
    private TraineeshipPositionDAO positionDAO;

    @Autowired
    private ProfessorDAO professorDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private CompanyDAO companyDAO;

    private Professor createProfessor(String username) {
        Professor prof = new Professor();
        prof.setUsername(username);
        prof.setPassword("pass");
        prof.setFullName("Prof. " + username);
        prof.setEnabled(true);
        return professorDAO.save(prof);
    }

    private Student createStudent(String username) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword("123");
        student.setFullName("Student " + username);
        student.setUniversityId("UNI-" + username);
        student.setEnabled(true);
        student.setApplied(false);
        return studentDAO.save(student);
    }

    private Company createCompany(String username) {
        Company company = new Company();
        company.setUsername(username);
        company.setPassword("comp123");
        company.setFullName("Company " + username);
        company.setEnabled(true);
        return companyDAO.save(company);
    }

    private TraineeshipPosition createPosition(String description, PositionStatus status, Professor supervisor, Student student, Company company) {
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setDescription(description);
        pos.setStartDate(LocalDate.now());
        pos.setEndDate(LocalDate.now().plusMonths(3));
        pos.setStatus(status);
        pos.setSupervisor(supervisor);
        pos.setStudent(student);
        pos.setCompany(company);
        pos.setEvaluated(false);
        return positionDAO.save(pos);
    }

    @Test
    void testFindByStatus() {
        createPosition("Backend", PositionStatus.AVAILABLE, null, null, createCompany("comp1"));
        List<TraineeshipPosition> results = positionDAO.findByStatus(PositionStatus.AVAILABLE);
        assertEquals(1, results.size());
    }

    @Test
    void testFindById() {
        TraineeshipPosition saved = createPosition("Frontend", PositionStatus.AVAILABLE, null, null, createCompany("comp2"));
        Optional<TraineeshipPosition> found = positionDAO.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Frontend", found.get().getDescription());
    }

    @Test
    void testFindBySupervisorAndStatus() {
        Professor prof = createProfessor("sup1");
        createPosition("Mobile", PositionStatus.ASSIGNED, prof, null, createCompany("comp3"));
        List<TraineeshipPosition> list = positionDAO.findBySupervisorAndStatus(prof, PositionStatus.ASSIGNED);
        assertEquals(1, list.size());
        assertEquals("Mobile", list.get(0).getDescription());
    }

    @Test
    void testFindByStudent() {
        Student student = createStudent("stud1");
        createPosition("AI Project", PositionStatus.ASSIGNED, null, student, createCompany("comp4"));
        Optional<TraineeshipPosition> found = positionDAO.findByStudent(student);
        assertTrue(found.isPresent());
        assertEquals("AI Project", found.get().getDescription());
    }

    @Test
    void testFindByStudentAndStatus() {
        Student student = createStudent("stud2");
        createPosition("ML Internship", PositionStatus.ASSIGNED, null, student, createCompany("comp5"));
        Optional<TraineeshipPosition> found = positionDAO.findByStudentAndStatus(student, PositionStatus.ASSIGNED);
        assertTrue(found.isPresent());
        assertEquals("ML Internship", found.get().getDescription());
    }

    @Test
    void testFindBySupervisorUsername() {
        Professor supervisor = createProfessor("sup2");
        createPosition("Cloud Role", PositionStatus.AVAILABLE, supervisor, null, createCompany("comp6"));
        List<TraineeshipPosition> results = positionDAO.findBySupervisorUsername("sup2");
        assertEquals(1, results.size());
        assertEquals("Cloud Role", results.get(0).getDescription());
    }
}
