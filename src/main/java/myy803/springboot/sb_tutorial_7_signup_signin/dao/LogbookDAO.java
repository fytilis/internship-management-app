package myy803.springboot.sb_tutorial_7_signup_signin.dao;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogbookDAO extends JpaRepository<Logbook, Long> {
    List<Logbook> findByStudent(Student student);
}
