package org.launchcode.rallymvc.controllers;

import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserProfileController {
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String viewProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        /*if no user, redirect to default home*/
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        return "profile";
    }

}
