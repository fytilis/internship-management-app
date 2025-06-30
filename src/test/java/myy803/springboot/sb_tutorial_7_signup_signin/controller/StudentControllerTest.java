package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import myy803.springboot.sb_tutorial_7_signup_signin.service.*;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.TraineeshipSearchService;

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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private StudentService studentService;
    @MockBean private ApplicationService applicationService;
    @MockBean private LogbookService logbookService;
    @MockBean private EvaluationService evaluationService;
    @MockBean private TraineeshipPositionService positionService;
    @MockBean private TraineeshipSearchService searchService;
    @MockBean private TraineeshipPositionDAO positionRepository;

    private Principal mockPrincipal;

    @BeforeEach
    void setup() {
        mockPrincipal = () -> "studentUser";
        when(studentService.findByUsername("studentUser")).thenReturn(new Student());
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testViewLogbook() throws Exception {
        when(logbookService.findByStudent(any())).thenReturn(List.of());

        mockMvc.perform(get("/student/logbook").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("student/logbook"))
                .andExpect(model().attributeExists("logbook"))
                .andExpect(model().attributeExists("logbookEntries"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testSubmitLogbook() throws Exception {
        mockMvc.perform(post("/student/logbook").with(csrf()).principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/logbook?success!"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testGetStudentDashboard() throws Exception {
        
        TraineeshipPosition position = new TraineeshipPosition();
        Company mockCompany = new Company();
        mockCompany.setFullName("Example Company");
        position.setCompany(mockCompany);

        when(positionRepository.findByStudentAndStatus(any(), eq(PositionStatus.ASSIGNED)))
                .thenReturn(Optional.of(position));

        mockMvc.perform(get("/student/dashboard").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"))
                .andExpect(model().attributeExists("assignedPosition"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testRetrieveProfile() throws Exception {
        mockMvc.perform(get("/student/profile").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("student/profile"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testSaveProfile() throws Exception {
        mockMvc.perform(post("/student/profile").with(csrf()).principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/profile?success"));
    }

   

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testApplyToPosition() throws Exception {
        when(positionService.getById(1L)).thenReturn(new TraineeshipPosition());

        mockMvc.perform(post("/student/apply").with(csrf()).principal(mockPrincipal)
                .param("positionId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/available-positions"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testViewAvailablePositions() throws Exception {
        when(applicationService.getApplicationsByStudent(any())).thenReturn(List.of());
        when(searchService.search(anyString(), anyString())).thenReturn(List.of());

        mockMvc.perform(get("/student/available-positions").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("student/available-positions"))
                .andExpect(model().attributeExists("positions"))
                .andExpect(model().attributeExists("appliedPositionIds"))
                .andExpect(model().attributeExists("strategy"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testConfirmAssignedPosition() throws Exception {
        mockMvc.perform(post("/student/confirm-position/10").with(csrf()).principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void testViewAcceptedPositions() throws Exception {
        when(applicationService.getAcceptedApplicationsForStudent(any())).thenReturn(List.of());

        mockMvc.perform(get("/student/assigned-positions").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("student/assigned-positions"))
                .andExpect(model().attributeExists("applications"));
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
