package myy803.springboot.sb_tutorial_7_signup_signin.service;



import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;

import java.util.List;

public interface LogbookService {
    void save(Logbook logbook);
    List<Logbook> getLogbooksForStudent(Student student);
    Logbook getById(Long id);
    void deleteById(Long id);
	List<Logbook> getEntriesByStudent(Student student);
	void saveEntry(Logbook entry, Student student);
	List<Logbook> findByStudent(Student student);
}
