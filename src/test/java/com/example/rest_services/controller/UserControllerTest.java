package com.example.rest_services.controller;

import com.example.rest_services.model.User;
import com.example.rest_services.service.BookService;
import com.example.rest_services.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    private UserController userController;

    private MockHttpSession mocksession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mocksession = new MockHttpSession();
    }

    @Test
    public void testSignupUserInvalidEmail(){

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("invalid@e.com");

        doNothing().when(userService).createUser(user);
        String result = userController.signupUser(user);

        verify(userService, never()).createUser(user);
        assertEquals("redirect:/signup?error=invalidEmail", result);
    }
}