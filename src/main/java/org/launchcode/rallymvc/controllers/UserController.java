package org.launchcode.rallymvc.controllers;


import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("")
    public String index(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        model.addAttribute("title", "Sign up for Rally");
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processRegistration(@ModelAttribute @Valid User newUser, Errors errors, Model model, @RequestParam String verifyPass) {

        boolean exists = false;
        User dupEmailUser = userDao.findByEmail(newUser.getEmail());
        if (dupEmailUser != null) {
            exists = true;
        }

        if (errors.hasErrors() || !newUser.getPassword().equals(verifyPass)) {
            model.addAttribute("title", "Sign up for Rally");
            return "index";
        } else if (exists) {
            model.addAttribute("duplicate", "Email associated with existing account.");
            model.addAttribute("title", "Sign up for Rally");
            return "index";
        }

        userDao.save(newUser);
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Log in to Rally");
        return "login";
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public String dashboard(Model model, User user) {
        model.addAttribute("title", "Hi, " + user.getNickname());
        return "dashboard";
    }

}
