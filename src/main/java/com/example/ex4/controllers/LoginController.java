package com.example.ex4.controllers;

import com.example.ex4.ActiveUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller for landing page
 */
@Controller
public class LoginController {

    /**
     * Welcome message for view
     */
    @Value("${welcome.message}")
    private String message;

    /**
     * ds of active users
     */
    @Autowired
    private ActiveUsers users;

    /**
     * GET route for landing page
     * @param model for view
     * @param request request
     * @return view if landing
     */
    @GetMapping("/")
    public String main(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("loggedIn") != null) {

            model.addAttribute("username", request.getSession().getAttribute("loggedIn"));

            users.activateUser((String) request.getSession().getAttribute("loggedIn"));

            return "chatroom";
        }

        model.addAttribute("message", message);

        return "index";
    }
}
