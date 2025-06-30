package myy803.springboot.sb_tutorial_7_signup_signin.service;


import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.CommitteeDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.CompanyDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.ProfessorDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.RoleDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private StudentDAO studentRepository;

    @Autowired
    private ProfessorDAO professorRepository;

    @Autowired
    private CompanyDAO companyRepository;

    @Autowired
    private CommitteeDAO committeeRepository;

    @Autowired
    private RoleDAO roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(UserDTO userDTO) {
    	
        System.out.println("Trying to register user: " + userDTO.getUsername());
        System.out.println("Selected role from DTO: " + userDTO.getRole());
        
        boolean usernameExists = studentRepository.existsByUsername(userDTO.getUsername()) ||
                                 professorRepository.existsByUsername(userDTO.getUsername()) ||
                                 companyRepository.existsByUsername(userDTO.getUsername()) ||
                                 committeeRepository.existsByUsername(userDTO.getUsername());

        if (usernameExists) {
        	System.out.println("Username already exists.");
            return false;
        }

       
        String roleName = "ROLE_" + userDTO.getRole().toUpperCase().trim();  // for example ROLE_STUDENT
        Role role = roleRepository.findRoleByName(roleName);
        System.out.println("Resolved role from DB: " + (role != null ? role.getName() : "NOT FOUND"));
        if (role == null) {
            return false; 
        }

        switch (userDTO.getRole().toUpperCase()) {
            case "STUDENT":
                Student student = new Student();
                student.setUsername(userDTO.getUsername());
                student.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                student.setFullName(userDTO.getFullName());
                student.setEnabled(true);
                student.setRole(role);
                studentRepository.save(student);
                break;

            case "PROFESSOR":
                Professor professor = new Professor();
                professor.setUsername(userDTO.getUsername());
                professor.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                professor.setFullName(userDTO.getFullName());
                professor.setEnabled(true);
                professor.setRole(role);
                professorRepository.save(professor);
                break;

            case "COMPANY":
                Company company = new Company();
                company.setUsername(userDTO.getUsername());
                company.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                company.setFullName(userDTO.getFullName());
                company.setEnabled(true);
                company.setRole(role);
                companyRepository.save(company);
                break;

            case "COMMITTEE":
                Committee committee = new Committee();
                committee.setUsername(userDTO.getUsername());
                committee.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                committee.setFullName(userDTO.getFullName());
                committee.setEnabled(true);
                committee.setRole(role);
                committeeRepository.save(committee);
                break;

            default:
                return false;
        }

        return true;
    }
}
