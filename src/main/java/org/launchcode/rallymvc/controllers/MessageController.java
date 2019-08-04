package org.launchcode.rallymvc.controllers;

import org.launchcode.rallymvc.models.Message;
import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.data.MessageDao;
import org.launchcode.rallymvc.models.data.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.GregorianCalendar;

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
            GregorianCalendar cal = new GregorianCalendar();
            message.setDate(cal);
            message.setUser(user);
            messageDao.save(message);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("message", message);
            model.addAttribute("messageType", MessageType.values());
            model.addAttribute("status", "Error adding message.");
            return "dashboard";
        }

    }

}
