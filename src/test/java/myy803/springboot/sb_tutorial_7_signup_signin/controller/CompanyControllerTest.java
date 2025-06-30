package myy803.springboot.sb_tutorial_7_signup_signin.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CompanyService companyService;
    @MockBean private StudentService studentService;
    @MockBean private EvaluationService evaluationService;
    @MockBean private TraineeshipPositionService traineeshipPositionService;

    private Principal mockPrincipal;

    @BeforeEach
    void setup() {
        mockPrincipal = () -> "companyUser";
        when(companyService.findByUsername("companyUser")).thenReturn(new Company());
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testShowCreatePositionForm() throws Exception {
        mockMvc.perform(get("/company/create-position"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/create-position"))
                .andExpect(model().attributeExists("position"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testSavePosition() throws Exception {
        mockMvc.perform(post("/company/positions/create")
                        .with(csrf())
                        .principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/dashboard?successPosition"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testGetStudentDashboard() throws Exception {
        when(companyService.getById(1L)).thenReturn(new Company());

        mockMvc.perform(get("/company/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/dashboard"))
                .andExpect(model().attributeExists("company"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testShowProfile() throws Exception {
        mockMvc.perform(get("/company/profile").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("company/profile"))
                .andExpect(model().attributeExists("company"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testUpdateProfile() throws Exception {
        mockMvc.perform(post("/company/profile")
                        .with(csrf())
                        .principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/profile?success"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testViewAssignedStudents() throws Exception {
        when(companyService.getAssignedStudents(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/company/assigned-students"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/assigned-students"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testRemoveStudent() throws Exception {
        mockMvc.perform(post("/company/remove/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/students?removed"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testShowEvaluationForm() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getById(1L)).thenReturn(student);

        mockMvc.perform(get("/company/evaluation/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/evaluation"))
                .andExpect(model().attributeExists("evaluation"));
    }

    @Test
    @WithMockUser(username = "companyUser", roles = "COMPANY")
    void testSubmitEvaluation() throws Exception {
        mockMvc.perform(post("/company/evaluation/submit")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/assigned-students?success=true"));
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
