package myy803.springboot.sb_tutorial_7_signup_signin.service;


import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student findByUsername(String username);
    Student getById(Long id);
    Student save(Student student);
    void deleteById(Long id);
    Student retrieveProfile(String username);
    void saveProfile(Student student);
    void applyToPosition(Long studentId, Long positionId);
    void saveLogbook(Logbook entry, Long studentId);
	void save(UserDTO userDTO);
	boolean register(UserDTO userDTO);
	void updateProfile(String username, Student updatedData);
}

