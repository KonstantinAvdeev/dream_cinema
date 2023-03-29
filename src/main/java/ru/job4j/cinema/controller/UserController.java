package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SimpleUserService;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(SimpleUserService simpleUserService) {
        this.userService = simpleUserService;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        var userSave = userService.save(user);
        user.setFullName("Гость");
        if (userSave.isEmpty()) {
            model.addAttribute("error", "Пользователь с такой почтой уже существует!");
            return "errors/404";
        }
        return "users/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            user.setFullName("Гость");
            return "errors/404";
        }
        var session = request.getSession();
        session.setAttribute("user", userOptional.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

}