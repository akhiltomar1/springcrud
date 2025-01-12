package com.example.rest_services.controller;

import com.example.rest_services.model.User;
import com.example.rest_services.service.BookService;
import com.example.rest_services.service.UserService;
import jakarta.servlet.http.HttpSession;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public User mockuser = new User();

    @BeforeEach
    void init(){
        System.out.println("Initialize");
        mockuser.setEmail("test@xecurify.com");
        System.out.println("Email set " + mockuser.getEmail());
        mockuser.setUsername("testname");
        System.out.println("UserName set " + mockuser.getUsername());
        mockuser.setPassword("password");
        System.out.println("Password set " + mockuser.getPassword());
    }

    @Test
    void signUpPassTest(){

        when(userService.createUser(any(User.class))).thenReturn(mockuser);
        String result = userController.signupUser(mockuser);
        System.out.println(result);
        System.out.println(mockuser.getPassword());
        assertEquals("redirect:/login", result);
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void signUpFailTest(){
        mockuser.setEmail("test@t.com");
        String result = userController.signupUser(mockuser);
        assertEquals("redirect:/signup?error=invalidEmail", result);
    }

    @Test
    void loginPasswordFail(){

        when(userService.findUserByUsername(mockuser.getUsername())).thenReturn(mockuser);
        MockHttpSession httpSession = new MockHttpSession();
        String result = userController.loginUser(mockuser.getUsername(), "password123", httpSession);
        assertEquals("redirect:/login?error", result);
        verify(userService, times(1)).findUserByUsername(mockuser.getUsername()); // Verify that the service method was called
    }

    @Test
    void loginPasswordPass(){
        signUpPassTest();
        when(userService.findUserByUsername(mockuser.getUsername())).thenReturn(mockuser);

        MockHttpSession httpSession = new MockHttpSession();
        System.out.println("Stored pass " + mockuser.getPassword());
        System.out.println("Stored USER " + mockuser.getUsername());


        String result = userController.loginUser(mockuser.getUsername(), "password", httpSession);
        System.out.println("Login result: " + result);

        assertEquals("redirect:/home", result);
        verify(userService, times(1)).findUserByUsername(mockuser.getUsername());
    }

}