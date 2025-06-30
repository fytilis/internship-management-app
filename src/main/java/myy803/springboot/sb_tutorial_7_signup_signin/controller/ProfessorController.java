package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.EvaluationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.StudentDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.model.EvaluatorType;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Professor;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;
import myy803.springboot.sb_tutorial_7_signup_signin.model.TraineeshipPosition;
import myy803.springboot.sb_tutorial_7_signup_signin.service.EvaluationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.ProfessorService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.StudentService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.TraineeshipPositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private TraineeshipPositionDAO positionRepository;
    @Autowired
    private EvaluationService evaluationService;
    
    @Autowired
    private StudentDAO studentRepository;
    @Autowired
    private TraineeshipPositionDAO traineeshipPositionRepository;



    @GetMapping("/profile")
    public String retrieveProfile(Model model, Principal principal) {
        Professor professor = professorService.retrieveProfile(principal.getName());
        model.addAttribute("professor", professor);
        return "professor/profile";
    }

    
    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("professor") Professor professorForm, Principal principal) {
        professorService.updateProfile(principal.getName(), professorForm);
        return "redirect:/professor/profile?success";
    }

  
    @GetMapping("/positions")
    public String showAssignedPositions(Model model, Principal principal) {
        List<TraineeshipPosition> positions = professorService.retrieveAssignedPositions(principal.getName());
        model.addAttribute("positions", positions);
        return "professor/positions";
    }


    @GetMapping("/dashboard")
    public String getStudentDashboard(Model model) {

        Long professorId = 1L; // placeholder
        Professor professor = professorService.getById(professorId);
        model.addAttribute("professor", professor);
        return "professor/dashboard";
    }
    
    @GetMapping("/assigned-positions")
    public String viewAssignedPositions(Principal principal, Model model) {
        List<TraineeshipPosition> positions = professorService.getAssignedPositions(principal.getName());
        model.addAttribute("positions", positions);
        return "professor/assigned-positions";
    }
    @GetMapping("/evaluation/{studentId}/{positionId}")
    public String showEvaluationForm(
            @PathVariable Long studentId,
            @PathVariable Long positionId,
            Model model) {

        Evaluation evaluation = new Evaluation();
        evaluation.setStudent(
            studentRepository.findById(studentId)
                             .orElseThrow(() -> new IllegalArgumentException("Invalid student")));
        evaluation.setTraineeshipPosition(
            traineeshipPositionRepository.findById(positionId)
                                         .orElseThrow(() -> new IllegalArgumentException("Invalid position")));
        evaluation.setEvaluatorType(EvaluatorType.PROFESSOR);

        model.addAttribute("evaluation", evaluation);
        return "professor/evaluation";
    }

    @PostMapping("/evaluation/submit")
    public String submitEvaluation(
            @ModelAttribute("evaluation") Evaluation evaluation,
            RedirectAttributes redirectAttributes) {

        evaluationService.submitProfessorEvaluation(evaluation);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Evaluation succesfully reigistered!");
        return "redirect:/professor/assigned-positions";
    }
}

