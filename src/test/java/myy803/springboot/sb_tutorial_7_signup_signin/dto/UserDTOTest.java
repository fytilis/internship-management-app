package myy803.springboot.sb_tutorial_7_signup_signin.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import myy803.springboot.sb_tutorial_7_signup_signin.DTO.UserDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    private UserDTO userDTO;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUserDTO() {
        userDTO.setUsername("user1");
        userDTO.setPassword("password123");
        userDTO.setFullName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setRole("student");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty(), "Should not have validation errors");
    }

    @Test
    public void testBlankUsername() {
        userDTO.setUsername("");
        userDTO.setPassword("password123");
        userDTO.setFullName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setRole("student");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Username should not be blank");
    }

    @Test
    public void testBlankPassword() {
        userDTO.setUsername("user1");
        userDTO.setPassword("");
        userDTO.setFullName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setRole("student");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Password should not be blank");
    }

    @Test
    public void testBlankFullName() {
        userDTO.setUsername("user1");
        userDTO.setPassword("password123");
        userDTO.setFullName("");
        userDTO.setEmail("test@example.com");
        userDTO.setRole("student");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Full name should not be blank");
    }

    @Test
    public void testBlankEmail() {
        userDTO.setUsername("user1");
        userDTO.setPassword("password123");
        userDTO.setFullName("Test User");
        userDTO.setEmail("");
        userDTO.setRole("student");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty(), "Email should not be blank");
    }

    @Test
    public void testRoleCanBeNull() {
        userDTO.setUsername("user1");
        userDTO.setPassword("password123");
        userDTO.setFullName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setRole(null);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty(), "Role is not validated with annotation, should pass");
    }
}
