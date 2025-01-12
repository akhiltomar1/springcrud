package com.example.rest_services.controller;

import com.example.rest_services.model.Book;
import com.example.rest_services.model.User;
import com.example.rest_services.service.BookService;
import com.example.rest_services.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpSession;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private AdminController adminController;

    private HttpSession session;
    private Model model;
    private RedirectAttributes redirectAttributes;

    private User mockUser;
    private Book mockBook;

    @BeforeEach
    void setup() {
        session = new MockHttpSession();
        model = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);

        mockUser = new User();
        mockUser.setUsername("admin");
        mockUser.setPassword("password");

        mockBook = new Book();
        mockBook.setBook_name("Test Book");
        //mockBook.setBook_id(100);

        session.setAttribute("username", "admin");
    }

    @Test
    void testViewUsersAsAdmin() {
        // Mock the behavior of the services
        when(userService.isAdmin("admin")).thenReturn(true);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(mockUser));
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(mockBook));


        String result = adminController.viewUsers(session, model, redirectAttributes);

        assertEquals("admin/users", result);
        verify(userService, times(1)).isAdmin("admin");
        verify(userService, times(1)).getAllUsers();
        verify(bookService, times(1)).getAllBooks();
        verify(model, times(1)).addAttribute("users", Arrays.asList(mockUser));
        verify(model, times(1)).addAttribute("books", Arrays.asList(mockBook));
    }

    @Test
    void testViewUsersAsNonAdmin() {

        when(userService.isAdmin("user")).thenReturn(false);
        session.setAttribute("username", "user");

        String result = adminController.viewUsers(session, model, redirectAttributes);
        assertEquals("redirect:/login", result);
        verify(userService, times(1)).isAdmin("user");
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "You are not authorized to access this page");
    }
    @Test
    void testDeleteUserAsAdmin() {
        Long userId = 1L;

        when(userService.isAdmin("admin")).thenReturn(true);
        String result = adminController.deleteUser(session, userId, redirectAttributes);
        assertEquals("redirect:/admin/users", result);
        verify(userService, times(1)).isAdmin("admin");
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUserAsNonAdmin() {
        Long userId = 1L;
        when(userService.isAdmin("user")).thenReturn(false);
        session.setAttribute("username", "user");


        String result = adminController.deleteUser(session, userId, redirectAttributes);

        assertEquals("redirect:/login", result);
        verify(userService, times(1)).isAdmin("user");
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "You are not authorized to perform this action");
    }

    @Test
    void testAddBookAsAdmin() {

        when(userService.isAdmin("admin")).thenReturn(true);

        String result = adminController.addBook(session, mockBook, redirectAttributes);

        assertEquals("redirect:/admin/users", result);
        verify(userService, times(1)).isAdmin("admin");
        verify(bookService, times(1)).saveBook(mockBook);
    }

    @Test
    void testAddBookAsNonAdmin() {
        when(userService.isAdmin("user")).thenReturn(false);
        session.setAttribute("username", "user");

        String result = adminController.addBook(session, mockBook, redirectAttributes);
        assertEquals("redirect:/login", result);
        verify(userService, times(1)).isAdmin("user");
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "You are not authorized to perform this action");
    }

    @Test
    void testDeleteBookAsAdmin() {
        Long bookId = 1L;


        when(userService.isAdmin("admin")).thenReturn(true);

        String result = adminController.deleteBook(session, bookId.intValue(), redirectAttributes);

        assertEquals("redirect:/admin/users", result);
        verify(userService, times(1)).isAdmin("admin");
        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBookAsNonAdmin() {
        Long bookId = 1L;

        when(userService.isAdmin("user")).thenReturn(false);
        session.setAttribute("username", "user");


        String result = adminController.deleteBook(session, bookId.intValue(), redirectAttributes);

        assertEquals("redirect:/login", result);
        verify(userService, times(1)).isAdmin("user");
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "You are not authorized to perform this action");
    }
}