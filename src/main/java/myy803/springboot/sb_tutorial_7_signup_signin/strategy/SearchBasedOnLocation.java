package myy803.springboot.sb_tutorial_7_signup_signin.strategy;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.CompanyDAO;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
@Component
public class SearchBasedOnLocation implements PositionsSearchStrategy{
    private StudentDAO studentRepository;
    private CompanyDAO companyRepository;

    public SearchBasedOnLocation( StudentDAO studentRepository,CompanyDAO companyRepository) {
       
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        Student student = studentRepository.findByUsername(applicantUsername);
        String preferredLocation =  student.getPreferredLocation();
       
        return companyRepository.findAll().stream()
                .flatMap(company -> company.getPositions().stream()
                    .filter(pos -> company.getLocation().equalsIgnoreCase(preferredLocation)))
                .collect(Collectors.toList());
    }
}
