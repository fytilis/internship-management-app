package myy803.springboot.sb_tutorial_7_signup_signin.config;

//✅ CustomUserDetailsService.java
import myy803.springboot.sb_tutorial_7_signup_signin.dao.*;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

 @Autowired private StudentDAO studentRepository;
 @Autowired private ProfessorDAO professorRepository;
 @Autowired private CompanyDAO companyRepository;
 @Autowired private CommitteeDAO committeeRepository;

 @Override
 @Transactional
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     
     Student student = studentRepository.findByUsername(username);
     if (student != null) {
         return buildUserDetails(student.getUsername(), student.getPassword(), student.getRole());
     }

     
   
     Company company = companyRepository.findByUsername(username);
     if (company != null) {
         return buildUserDetails(company.getUsername(), company.getPassword(), company.getRole());
     }

     
     Committee committee = committeeRepository.findByUsername(username);
     if (committee != null) {
         return buildUserDetails(committee.getUsername(), committee.getPassword(), committee.getRole());
     }
     

     Professor professor = professorRepository.findByUsername(username);
     if (professor != null) {
         return buildUserDetails(professor.getUsername(), professor.getPassword(), professor.getRole());
     }


     throw new UsernameNotFoundException("User not found with username: " + username);
 }

 private UserDetails buildUserDetails(String username, String password, Role role) {
     List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
     return new User(username, password, authorities);
 }
}

