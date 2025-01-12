package com.example.rest_services.service;

import com.example.rest_services.model.User;
import com.example.rest_services.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("testUser1");
        user1.setEmail("testuser1@xecurify.com");
        user1.setPassword("password123");

        user2 = new User();
        user2.setUsername("testUser2");
        user2.setEmail("testuser2@xecurify.com");
        user2.setPassword("password456");
    }

    @Test
    void testGetAllUsers() {

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testCreateUser() {

        when(userRepository.save(user1)).thenReturn(user1);

        User createdUser = userService.createUser(user1);

        verify(userRepository, times(1)).save(user1);
        assertEquals(user1, createdUser);
    }

    @Test
    void testFindUserByUsername() {

        when(userRepository.findByUsername("testUser1")).thenReturn(user1);

        User foundUser = userService.findUserByUsername("testUser1");

        verify(userRepository, times(1)).findByUsername("testUser1");

        assertEquals(user1, foundUser);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testIsAdmin() {
        assertTrue(userService.isAdmin("admin"));
        assertFalse(userService.isAdmin("testUser"));
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(user1)).thenReturn(user1);
        User savedUser = userService.saveUser(user1);
        verify(userRepository, times(1)).save(user1);
        assertEquals(user1, savedUser);
    }
}
