package myy803.springboot.sb_tutorial_7_signup_signin.controller;



import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Application;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.PositionStatus;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import myy803.springboot.sb_tutorial_7_signup_signin.service.ApplicationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.EvaluationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.LogbookService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.StudentService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.TraineeshipPositionService;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.TraineeshipSearchService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
public class StudentController {

 @Autowired
 private StudentService studentService;
 
 @Autowired
 private TraineeshipPositionService positionService;
 
 @Autowired
 private TraineeshipPositionDAO positionRepository;

 @Autowired
 private ApplicationService applicationService;
 
 @Autowired
 private LogbookService logbookService;

 @Autowired
 private EvaluationService evaluationService;
 
 @Autowired
 private TraineeshipSearchService searchService;

 @GetMapping("/logbook")
 public String viewLogbook(Model model, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());

     model.addAttribute("logbook", new Logbook()); 
     model.addAttribute("logbookEntries", logbookService.findByStudent(student)); 
     
     return "student/logbook"; 
 }


 @PostMapping("/logbook")
 public String submitLogbook(@ModelAttribute("logbook") Logbook logbook, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());
     logbook.setStudent(student);
     logbook.setDate(LocalDateTime.now()); 
     logbookService.save(logbook); 

     return "redirect:/student/logbook?success!";
 }


 @GetMapping("/dashboard")
 public String getStudentDashboard(Model model, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());

     TraineeshipPosition assignedPosition = positionRepository
         .findByStudentAndStatus(student, PositionStatus.ASSIGNED)
         .orElse(null); 

     model.addAttribute("assignedPosition", assignedPosition);
     model.addAttribute("student", student);

     return "student/dashboard";
 }
 @GetMapping("/profile")
 public String retrieveProfile(Model model, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());
     model.addAttribute("student", student);
     return "student/profile";
 }

 @PostMapping("/profile")
 public String saveProfile(@ModelAttribute("student") Student studentForm, Principal principal) {
     studentService.updateProfile(principal.getName(), studentForm);
     return "redirect:/student/profile?success";
 }
 @GetMapping("/applications")
 public String viewMyApplications(Model model, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());
     List<Application> applications = applicationService.getApplicationsByStudent(student);
     model.addAttribute("applications", applications);
     return "student/applications";
 }
 @GetMapping("/available-positions")
 public String viewAvailablePositions(@RequestParam(defaultValue = "interests") String strategy,Model model, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());
     List<TraineeshipPosition> positions = searchService.search(student.getUsername(), strategy);


     List<Application> applications = applicationService.getApplicationsByStudent(student);

  
     List<Long> appliedPositionIds = applications.stream()
         .map(app -> app.getPosition().getId())
         .collect(Collectors.toList());

     model.addAttribute("positions", positions);
     model.addAttribute("appliedPositionIds", appliedPositionIds);
     model.addAttribute("strategy", strategy);

     return "student/available-positions";
 }

 @PostMapping("/apply")
 public String apply(@RequestParam Long positionId, Principal principal, RedirectAttributes redirectAttributes) {
     Student student = studentService.findByUsername(principal.getName());
     TraineeshipPosition position = positionService.getById(positionId);

     applicationService.applyToPosition(student, position);

     redirectAttributes.addFlashAttribute("success", "Your application succesfully registered!");
     return "redirect:/student/available-positions";
 }
 
 @GetMapping("/assigned-positions")
 public String viewAcceptedPositions(Model model, Principal principal) {
     Student student = studentService.findByUsername(principal.getName());
     List<Application> acceptedApplications = applicationService.getAcceptedApplicationsForStudent(student);

     TraineeshipPosition assignedPosition = positionRepository
             .findByStudentAndStatus(student, PositionStatus.ASSIGNED)
             .orElse(null);

     model.addAttribute("applications", acceptedApplications);
     model.addAttribute("assignedPosition", assignedPosition);
     model.addAttribute("student", student);
     return "student/assigned-positions";
 }
 
 @PostMapping("/confirm-position/{applicationId}")
 public String confirmAssignedPosition(@PathVariable Long applicationId, Principal principal, RedirectAttributes redirectAttributes) {
     applicationService.confirmAssignedPosition(applicationId, principal.getName());
     redirectAttributes.addFlashAttribute("success", "You picked position successfuly!");
     return "redirect:/student/dashboard";
 }


}