package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import myy803.springboot.sb_tutorial_7_signup_signin.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ProfessorService professorService;
    @MockBean private StudentService studentService;
    @MockBean private EvaluationService evaluationService;
    @MockBean
    private TraineeshipPositionDAO positionRepository;

    private Principal mockPrincipal;

    @BeforeEach
    void setup() {
        mockPrincipal = () -> "professorUser";
        when(professorService.retrieveProfile("professorUser")).thenReturn(new Professor());
    }

   

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testRetrieveProfile() throws Exception {
        mockMvc.perform(get("/professor/profile").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/profile"))
                .andExpect(model().attributeExists("professor"));
    }

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testSaveProfile() throws Exception {
        mockMvc.perform(post("/professor/profile").with(csrf()).principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/profile?success"));
    }

  
    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testEvaluatePosition() throws Exception {
        mockMvc.perform(post("/professor/assigned-positions/evaluation-form")
                        .param("positionId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/positions"));
    }

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testSaveEvaluation() throws Exception {
        mockMvc.perform(post("/professor/evaluation/save")
                        .param("positionId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/positions"));
    }

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testGetStudentDashboard() throws Exception {
        when(professorService.getById(1L)).thenReturn(new Professor());

        mockMvc.perform(get("/professor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/dashboard"))
                .andExpect(model().attributeExists("professor"));
    }

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testViewAssignedPositions() throws Exception {
        when(professorService.getAssignedPositions("professorUser")).thenReturn(List.of(new TraineeshipPosition()));

        mockMvc.perform(get("/professor/assigned-positions").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/assigned-positions"))
                .andExpect(model().attributeExists("positions"));
    }

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testShowEvaluationForm() throws Exception {
        when(professorService.prepareEvaluationForPosition(1L)).thenReturn(new Evaluation());

        mockMvc.perform(get("/professor/evaluation/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/evaluation"))
                .andExpect(model().attributeExists("evaluation"));
    }

    @Test
    @WithMockUser(username = "professorUser", roles = "PROFESSOR")
    void testSubmitEvaluation() throws Exception {
        mockMvc.perform(post("/professor/evaluation/submit")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/assigned-positions"));
    }

    @TestConfiguration
    static class DummyViewResolverConfig {
        @Bean
        public ViewResolver viewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix("/templates/");
            resolver.setSuffix(".html");
            return resolver;
        }
    }
}
