package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.StudentSumDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.ApplicationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.ApplicationStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Committee;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import myy803.springboot.sb_tutorial_7_signup_signin.service.ApplicationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.CommitteeService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.EvaluationService;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.SupervisorAssignmentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import myy803.springboot.sb_tutorial_7_signup_signin.service.CommitteeService;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;

import java.util.List;

@Controller
@RequestMapping("/committee")
public class CommitteeController {
	@Autowired
    private  CommitteeService committeeService;
	@Autowired
    private  ApplicationDAO applicationRepository;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private SupervisorAssignmentService assignmentService;
	@Autowired
	private TraineeshipPositionDAO positionRepository;
	@Autowired
	private EvaluationService evaluationService;

    @GetMapping("/applications")
    public String viewAllApplications(Model model) {
        List<Application> applications = applicationRepository.findAll(); 
        Set<Student> students = applications.stream()
                .map(Application::getStudent)
                .collect(Collectors.toSet());

        model.addAttribute("applications", applications);
        model.addAttribute("students", students);
        return "committee/applications"; // show applicant name, status
    }
    
    @PostMapping("/applications/{appId}/accept")
    public String acceptApplication(@PathVariable Long appId) {
    	applicationService.updateApplicationStatus( appId, ApplicationStatus.ACCEPTED);
    	
        return "redirect:/committee/applications";
    }

    @PostMapping("applications/{appId}/reject")
    public String rejectApplication(@PathVariable Long appId) {
    	applicationService.updateApplicationStatus( appId, ApplicationStatus.REJECTED);
        return "redirect:/committee/applications";
    }


    @GetMapping("/assign-supervisors")
    public String showAssignmentPage(@RequestParam(defaultValue = "interests") String strategy,
                                     Model model) {
        Map<TraineeshipPosition, List<Professor>> suggestions = committeeService.getSuggestedSupervisors(strategy);
        model.addAttribute("strategy", strategy);
        model.addAttribute("suggestions", suggestions);
        return "committee/assign-supervisors";
    }
    
    @PostMapping("/assign-supervisor")
    public String assignSupervisor(@RequestParam Long positionId,
                                   @RequestParam Long professorId,
                                   @RequestParam String strategy,
                                   RedirectAttributes redirectAttributes) {

        assignmentService.manuallyAssignSupervisor(positionId, professorId);
        redirectAttributes.addFlashAttribute("success", "Supervised Successfullly.");
        return "redirect:/committee/assign-supervisors?strategy=" + strategy;
    }
    
    @GetMapping("/assigned-positions")
    public String assignedSupervisors(Model model) {
       
    	   List<TraineeshipPosition> positions =
    	            positionRepository.findAll().stream()
    	                        .filter(p -> p.getSupervisor() != null)
    	                        .collect(Collectors.toList());

    	        Map<Long, List<StudentSumDTO>> summariesMap = positions.stream()
    	            .collect(Collectors.toMap(
    	                TraineeshipPosition::getId,
    	                pos -> evaluationService.getCommitteeSummaries(pos.getId())
    	            ));

    	        model.addAttribute("positions", positions);
    	        model.addAttribute("summariesMap", summariesMap);

        return "committee/assigned-positions";
    }

    @GetMapping("/dashboard")
    public String showCommitteeDashboard(Model model) {
    	Long committeeId = 1L; // placeholder
    	Optional<Committee> committee = committeeService.getById(committeeId);
        model.addAttribute("committee", committee);
        return "committee/dashboard";
    }

    @GetMapping("/search-positions")
    public String searchPositions(@RequestParam String strategy,@RequestParam String username,Model model) {
    	List<TraineeshipPosition> positions = committeeService.searchPositions(strategy,username);
    	model.addAttribute("positions",positions);
    	return "committee/search-results";
    }
   

}