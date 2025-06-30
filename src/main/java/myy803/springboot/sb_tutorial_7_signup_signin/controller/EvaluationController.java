package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Evaluation;
import myy803.springboot.sb_tutorial_7_signup_signin.service.EvaluationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.EvaluationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    
    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@Valid@RequestBody Evaluation evaluation) {
        Evaluation savedEvaluation = evaluationService.saveEvaluation(evaluation);
        return ResponseEntity.ok(savedEvaluation);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluation(@PathVariable Long id) {
        Evaluation evaluation = evaluationService.getById(id);
        if (evaluation != null) {
            return ResponseEntity.ok(evaluation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}