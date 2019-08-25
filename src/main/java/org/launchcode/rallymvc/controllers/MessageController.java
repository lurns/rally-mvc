package org.launchcode.rallymvc.controllers;

import org.launchcode.rallymvc.models.Message;
import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.Workout;
import org.launchcode.rallymvc.models.data.MessageDao;
import org.launchcode.rallymvc.models.data.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageDao messageDao;

    @RequestMapping(value = "dashboard", method = RequestMethod.POST)
    public String newMessage(Model model, @Valid Message message, Errors errors,
                             HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (!errors.hasErrors()) {
            LocalDateTime cal = LocalDateTime.now();
            message.setDate(cal);
            message.setUser(user);
            messageDao.save(message);
            return "redirect:/dashboard";
        } else {
            Workout workout = new Workout();

            model.addAttribute("user", user);
            model.addAttribute("message", message);
            model.addAttribute("messageType", MessageType.values());
            model.addAttribute("workout", workout);
            model.addAttribute("status", "Error adding message.");
            return "dashboard";
        }

    }

    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public String viewMessages(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        /*if no user, redirect to home*/
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Iterable<Message> userMessages = messageDao.findByUserId(user.getId());
        ArrayList<Message> sortMessage = new ArrayList<>();
        for (Message message : userMessages) {
            sortMessage.add(message);
        }

        Collections.reverse(sortMessage);

        model.addAttribute("title", user.getNickname() + "'s Messages");
        model.addAttribute("user", user);
        model.addAttribute("messages", sortMessage);
        model.addAttribute("messageType", MessageType.values());
        return "messages";
    }

    @RequestMapping(value = "messages/delete", method = RequestMethod.POST)
    public String delMessage(@RequestParam int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        /*if no user, redirect to home*/
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        messageDao.delete(id);
        return "redirect:/messages";
    }

}
