package com.example.rest_services.controller;

import com.example.rest_services.model.Book;
import com.example.rest_services.model.User;
import com.example.rest_services.repository.BookRepository;
import com.example.rest_services.service.BookService;
import com.example.rest_services.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/users")
    public String viewUsers(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("IN ADMIN/USERS");
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);

            List<Book> books = bookService.getAllBooks();
            model.addAttribute("books", books);

            model.addAttribute("newBook", new Book());

            return "admin/users";

        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to access this page");
            return "redirect:/login";
        }
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            userService.deleteUser(id);
            return "redirect:/admin/users";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
    }

    @PostMapping("/book/add")
    public String addBook(HttpSession session,@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        System.out.println("IN ADMIN/BOOKS");

        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            bookService.saveBook(book);
            return "redirect:/admin/users";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBook(HttpSession session,@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            bookService.deleteBook(Long.valueOf(id));
            return "redirect:/admin/users";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
      }
}