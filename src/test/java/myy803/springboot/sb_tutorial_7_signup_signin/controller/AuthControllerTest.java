package myy803.springboot.sb_tutorial_7_signup_signin.controller;

import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private UserDTO testUser;

    @BeforeEach
    public void setup() {
        testUser = new UserDTO();
        testUser.setUsername("student1");
        testUser.setPassword("password");
        testUser.setRole("STUDENT");
    }

    @Test
    void testLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testRegisterFormView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testRegisterSuccess() throws Exception {
        when(authService.registerUser(Mockito.any(UserDTO.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .flashAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testRegisterFailure() throws Exception {
        when(authService.registerUser(Mockito.any(UserDTO.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .flashAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testSaveSuccess() throws Exception {
        when(authService.registerUser(Mockito.any(UserDTO.class))).thenReturn(true);

        mockMvc.perform(post("/save")
                        .with(csrf())
                        .flashAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testSaveFailure() throws Exception {
        when(authService.registerUser(Mockito.any(UserDTO.class))).thenReturn(false);

        mockMvc.perform(post("/save")
                        .with(csrf())
                        .flashAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("error"));
    }
}
