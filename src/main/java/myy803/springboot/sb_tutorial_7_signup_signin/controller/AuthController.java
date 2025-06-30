package myy803.springboot.sb_tutorial_7_signup_signin.controller;


import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;
import myy803.springboot.sb_tutorial_7_signup_signin.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/signin";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/signup"; 
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        boolean success = authService.registerUser(userDTO);
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Username might exist or role is invalid.");
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("success", "Registration successful! You can now login.");
        return "redirect:/login";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        boolean success = authService.registerUser(userDTO); 
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Save failed. Username might exist or role is invalid.");
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("success", "User saved successfully.");
        return "redirect:/dashboard"; 
    }
}
