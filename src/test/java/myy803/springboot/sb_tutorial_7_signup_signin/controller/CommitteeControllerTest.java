package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.ApplicationDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.TraineeshipPositionDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.*;
import myy803.springboot.sb_tutorial_7_signup_signin.service.ApplicationService;
import myy803.springboot.sb_tutorial_7_signup_signin.service.CommitteeService;
import myy803.springboot.sb_tutorial_7_signup_signin.strategy.SupervisorAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommitteeControllerTest {

    private MockMvc mockMvc;

    @Mock private CommitteeService committeeService;
    @Mock private ApplicationDAO applicationRepository;
    @Mock private ApplicationService applicationService;
    @Mock private SupervisorAssignmentService assignmentService;
    @Mock private TraineeshipPositionDAO positionRepository;

    @InjectMocks
    private CommitteeController committeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(committeeController).build();
    }

    @Test
    void testViewAllApplications() throws Exception {
        Application application = new Application();
        TraineeshipPosition position = new TraineeshipPosition();
        position.setDescription("Internship at ABC");
        application.setPosition(position);
        application.setStudent(new Student());

        when(applicationRepository.findAll()).thenReturn(List.of(application));

        mockMvc.perform(get("/committee/applications"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/applications"))
                .andExpect(model().attributeExists("applications"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    void testAcceptApplication() throws Exception {
        mockMvc.perform(post("/committee/applications/1/accept"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/applications"));

        verify(applicationService).updateApplicationStatus(1L, ApplicationStatus.ACCEPTED);
    }

    @Test
    void testRejectApplication() throws Exception {
        mockMvc.perform(post("/committee/applications/1/reject"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/applications"));

        verify(applicationService).updateApplicationStatus(1L, ApplicationStatus.REJECTED);
    }

    @Test
    void testShowAssignmentPage() throws Exception {
        when(committeeService.getSuggestedSupervisors("interests")).thenReturn(new HashMap<>());

        mockMvc.perform(get("/committee/assign-supervisors"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/assign-supervisors"))
                .andExpect(model().attributeExists("strategy"))
                .andExpect(model().attributeExists("suggestions"));
    }

    @Test
    void testAssignSupervisor() throws Exception {
        mockMvc.perform(post("/committee/assign-supervisor")
                        .param("positionId", "1")
                        .param("professorId", "2")
                        .param("strategy", "interests"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/assign-supervisors?strategy=interests"));

        verify(assignmentService).manuallyAssignSupervisor(1L, 2L);
    }

    @Test
    void testViewAssignedPositions() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setSupervisor(new Professor());
        when(positionRepository.findAll()).thenReturn(List.of(position));

        mockMvc.perform(get("/committee/assigned-supervisors"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/assigned-supervisors"))
                .andExpect(model().attributeExists("positions"));
    }

    @Test
    void testDashboard() throws Exception {
        when(committeeService.getById(1L)).thenReturn(Optional.of(new Committee()));

        mockMvc.perform(get("/committee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/dashboard"))
                .andExpect(model().attributeExists("committee"));
    }

    @Test
    void testSearchPositions() throws Exception {
        when(committeeService.searchPositions("interests", "student1")).thenReturn(List.of());

        mockMvc.perform(get("/committee/search-positions")
                        .param("strategy", "interests")
                        .param("username", "student1"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/search-results"))
                .andExpect(model().attributeExists("positions"));
    }
}
