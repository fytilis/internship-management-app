package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;
import java.util.HashMap;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.service.ApplicationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.CompanyService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.EvaluationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.StudentService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.TraineeshipPositionService;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import myy803.springboot.sb_tutorial_7_signup_signin.service.CompanyService;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private  CompanyService companyService;
    @Autowired
    private TraineeshipPositionService traineeshipPositionService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentDAO studentRepository;
    @Autowired
    private TraineeshipPositionDAO traineeshipPositionRepository;
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private ApplicationService applicationService;
    
    
   

    @GetMapping("/create-position")
    public String showCreatePositionForm(Model model) {
        model.addAttribute("position", new TraineeshipPosition());
        return "company/create-position";
    }

    @PostMapping("/positions/create")
    public String savePosition(@ModelAttribute("position") TraineeshipPosition position, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        position.setCompany(company);
        position.setStatus(PositionStatus.AVAILABLE); 
        traineeshipPositionService.save(position);
        return "redirect:/company/dashboard?successPosition";
    }


    
    @GetMapping("/dashboard")
    public String getStudentDashboard(Model model) {
       
        Long companyId = 1L; 
        Company company = companyService.getById(companyId);
        model.addAttribute("company", company);
        return "company/dashboard";
    }
    
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("company") Company formCompany, Principal principal) {
        companyService.updateProfile(principal.getName(), formCompany);
        return "redirect:/company/profile?success";
    }
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    
    @GetMapping("/assigned-students")
    public String viewAssignedStudents(Model model) {
        String username = getCurrentUsername();
        
        
        List<TraineeshipPosition> assignedPositions = companyService.getAssignedPositions(username);
        
        model.addAttribute("positions", assignedPositions);
        
        return "company/assigned-students";
    }
    
    @PostMapping("/remove/{studentId}")
    public String removeStudent(@PathVariable Long studentId) {
        companyService.removeStudentFromCompany(studentId);
        return "redirect:/company/students?removed";
    }

    @GetMapping("/evaluation/{studentId}/{positionId}")
    public String showEvaluationForm(@PathVariable Long studentId,
                                     @PathVariable Long positionId,
                                     Model model) {

        Student student = studentRepository
                             .findById(studentId)
                             .orElseThrow(() -> new IllegalArgumentException("Invalid student Id"));
        TraineeshipPosition position = traineeshipPositionRepository
                                          .findById(positionId)
                                          .orElseThrow(() -> new IllegalArgumentException("Invalid position Id"));

        Evaluation evaluation = new Evaluation();
        evaluation.setStudent(student);
        evaluation.setTraineeshipPosition(position);
        // set evaluator type now so the form doesn’t need to
        evaluation.setEvaluatorType(EvaluatorType.COMPANY);

        model.addAttribute("evaluation", evaluation);
        return "company/evaluation";
    }
    @PostMapping("/evaluation/submit")
    public String submitEvaluation(@ModelAttribute("evaluation") Evaluation evaluation,
                                   RedirectAttributes redirectAttributes) {
        evaluationService.submitCompanyEvaluation(evaluation);
        redirectAttributes.addFlashAttribute("successMessage", "Evaluation registered Successfuly!");
        return "redirect:/company/assigned-students";
    }
    
    @GetMapping("/my-positions")
    public String viewMyPositions(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());

        List<TraineeshipPosition> positions = traineeshipPositionService.findByCompany(company);
        
        Map<Long, String> positionStatusMap = new HashMap<>();
        for (TraineeshipPosition position : positions) {
            List<Application> applications = applicationService.getApplicationsByPosition(position);

            String status = applications.stream().anyMatch(app -> !app.getStatus().equals(ApplicationStatus.PENDING))
            	    ? "Assigned"
            	    : "Pending";

            positionStatusMap.put(position.getId(), status);
        }

        model.addAttribute("positions", positions);
        model.addAttribute("statusMap", positionStatusMap);

        return "company/my-positions";  
    }

}