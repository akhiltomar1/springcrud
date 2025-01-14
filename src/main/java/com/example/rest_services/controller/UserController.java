package com.example.rest_services.controller;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.example.rest_services.model.Book;
import com.example.rest_services.model.User;
import com.example.rest_services.service.BookService;
import com.example.rest_services.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("user") User user) {
//        if (user == null || user.getPassword() == null) {
//            return "redirect:/signup?error=userNull";
//        }
        String npass = hash(user.getPassword());
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashBytes = digest.digest(npass.getBytes());
//            npass =  Base64.getEncoder().encodeToString(hashBytes);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
        user.setPassword(npass);
        String email = user.getEmail();
        String emailRegex = "^[A-Za-z0-9._%+-]+@([A-Za-z0-9-]+\\.)?xecurify\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if(matcher.matches()){
            userService.createUser(user);
        }else {
            return "redirect:/signup?error=invalidEmail";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/logout")
    public String showLoginFormAfterLogout() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.findUserByUsername(username);
        System.out.println(user.getUsername());

        String npass = hash(password);


        System.out.println("Password = " + password);
        System.out.println("npass = " + npass);
        System.out.println("UserPass = " + user.getPassword());

        if(user != null && user.getPassword().equals(npass)) {
            System.out.println("Not null wala");
            session.setAttribute("username", username);
            if (userService.isAdmin(username)) {
                System.out.println("Admin");
                System.out.println("Password Equal");
                return "redirect:/admin/users";
            } else {
                return "redirect:/home";
            }
        } else {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        session.setAttribute("books", books);
        if (username != null) {
            model.addAttribute("username", username);
            return "home";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/borrow")
    public String setBookId(@RequestParam("username") String username, @RequestParam("bookId") Integer bookId) {
        User user = userService.findUserByUsername(username);

        if (user != null) {
            user.setBookId(bookId);
            userService.saveUser(user);
            return "redirect:/home";
        } else {
            return "redirect:/login?error";
        }
    }

    public String hash(String pass){
        String npass = pass;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(npass.getBytes());
            npass =  Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return npass;
    }

}




