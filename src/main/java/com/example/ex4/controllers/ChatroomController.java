package com.example.ex4.controllers;

import com.example.ex4.ActiveUsers;
import com.example.ex4.repo.Message;
import com.example.ex4.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 *Controller - ChatroomController,
 * Runs the chatroom and its routes.
 */
@Controller
public class ChatroomController{

    /**
     * The message repository in SQL.
     */
    @Autowired
    private MessageRepo repo;

    /**
     *DS containing the active users for the site. Sits in Application scope.
     */
    @Autowired
    private ActiveUsers activeUsers;

    /**
     * Gets repo
     * @return repo
     */
    private MessageRepo getRepo() {
        return repo;
    }

    /**
     * gets Active users
     * @return active users
     */
    private ActiveUsers getActiveUsers() {
        return activeUsers;
    }


    /**
     * In case GET for this route.
     * @return redirects to landing
     */
    @GetMapping("/chatroom")
    public RedirectView chatroomGet() {
        return new RedirectView("/");
    }

    /**
     * Initial route for the chatroom.
     * @param text the username, (matching name for filters)
     * @param model Model for view
     * @param request the request
     * @return view of chatroom
     */
    @PostMapping("/chatroom")
    public String main(@RequestParam String text, Model model, HttpServletRequest request) {

        String username = text.trim();
        if(username.length()==0) {
            model.addAttribute("message", "Please enter not only whitespaces");
            return "error";
        }

        if(request.getSession().getAttribute("loggedIn") == null) {

            if(getActiveUsers().checkForUser(username)) {
                model.addAttribute("message", "User with this name is already signed in. Please use another name.");
                return "error";
            }
        }

        model.addAttribute("username", username);

        request.getSession().setAttribute("loggedIn", username);

        getActiveUsers().activateUser(username);

        return "chatroom";
    }

    /**
     * adds message to the DB
     * @param message to add
     * @return response status code
     */
    @PostMapping("/api/text/add-message")
    public ResponseEntity addMessage(@RequestBody Message message) {

        if(message.getMessage().trim().length() == 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/whitespace.html");
            return new ResponseEntity<String>(headers,HttpStatus.FOUND);
        }

        getRepo().save(message);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * fetches last 5 messages in db
     * @param lastID to see if anything new, if not return empty data
     * @param request the request
     * @return the messages
     */
    @PostMapping("/api/last-messages")
    public @ResponseBody List<Message> getLastMessages(@RequestParam String lastID, HttpServletRequest request) {

        List<Message> lastMessages = getRepo().findTop5ByOrderByIdDesc();
        Long id = Long.parseLong(lastID);

        if(lastMessages.size()!=0) {
            if(lastMessages.get(0).getId() == id) {
                return new ArrayList<Message>();
            }
        }

        return lastMessages;
    }

    /**
     * fetch list of active users
     * @param username of requester
     * @return list of active users
     */
    @PostMapping("/api/active-users")
    public @ResponseBody List<String> activeUsers(@RequestParam String username) {
        getActiveUsers().activateUser(username);
        return getActiveUsers().calcActiveUsers();
    }

    /**
     * logout the user
     * @param username of the user
     * @param model for view
     * @param req request
     * @return redirection to landing
     */
    @PostMapping("/api/logout")
    public RedirectView logout(@RequestParam String username, Model model, HttpServletRequest req) {
        getActiveUsers().deactivateUser(username);
        req.getSession().setAttribute("loggedIn", null);
        return new RedirectView("/");
    }


    /**
     * GET catch. Not meant to arrive here generally
     * @return redirection
     */
    @GetMapping("/api/text/search-text")
    public RedirectView searchTextGet() {
        return new RedirectView("/");
    }


    /**
     * search db for messages containing text
     * @param username of requester
     * @param text to search
     * @param model for view
     * @param request request
     * @return view of results from search
     */
    @PostMapping("/api/text/search-text")
    public String searchText(@RequestParam String username, @RequestParam String text, Model model, HttpServletRequest request) {
        getActiveUsers().activateUser(username);

        text = text.trim();
        if(text.length()==0) {
            model.addAttribute("message", "Please enter not only whitespaces");
            return "error";
        }
        model.addAttribute("results", getRepo().findMessagesByMessageContaining(text));
       // List<Message> l = repo.findMessagesByMessageContaining(text);

        return "search-results";
    }


    /**
     * GET catch. Not meant to arrive here generally
     * @return redirection
     */
    @GetMapping("/api/text/search-name")
    public RedirectView searchNameGet() {
        return new RedirectView("/");
    }

    /**
     * search db for messages by the name of 'text' (matching for filters)
     * @param username of requester
     * @param text to search by NAME
     * @param model for view
     * @param request request
     * @return view of results from search
     */
    @PostMapping("/api/text/search-name")
    public String searchName(@RequestParam String username, @RequestParam String text, Model model, HttpServletRequest request) {

        getActiveUsers().activateUser(username);

        text = text.trim();

        if(text.length() == 0) {
            model.addAttribute("message", "Please enter not only whitespaces");
            return "error";
        }
        model.addAttribute("results", getRepo().findMessagesByUsername(text));

        return "search-results";
    }
}
