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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        model.addAttribute("title", "Sign up for Rally");
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processRegistration(@ModelAttribute @Valid User newUser, Errors errors,
                                      Model model, @RequestParam String verifyPass,
                                      HttpServletRequest request) {

        //check for duplicate email
        boolean exists = false;
        User dupEmailUser = userDao.findByEmail(newUser.getEmail());
        if (dupEmailUser != null) {
            exists = true;
            model.addAttribute("duplicate", "Email associated with existing account.");
            model.addAttribute("title", "Sign up for Rally");
            return "index";
        }

        //check for overall errors
        if (errors.hasErrors()) {
            model.addAttribute("title", "Sign up for Rally");
            return "index";
        }

        //check passwords match
        if (!newUser.getPassword().equals(verifyPass)) {
            model.addAttribute("title", "Sign up for Rally");
            model.addAttribute("verifyPass", "Passwords must match.");
            return "index";
        }

        //pass validation, ready to create user and make session
        userDao.save(newUser);

        HttpSession session = request.getSession();
        session.setAttribute("user", newUser);

        return "redirect:/dashboard";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        User checkUser = new User();

        model.addAttribute("user", checkUser);
        model.addAttribute("title", "Log in to Rally");
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(Model model, @Valid User checkUser, Errors errors,
                               HttpServletRequest request) {

        //check if user exists
        boolean exists = false;
        User existingUser = userDao.findByEmail(checkUser.getEmail());
        if (existingUser != null) {
            exists = true;
            if (checkUser.getPassword().equals(existingUser.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", existingUser);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("title", "Log in to Rally");
                model.addAttribute("invalid","Invalid password.");
                return "login";
            }
        } else {
            model.addAttribute("title", "Log in to Rally");
            model.addAttribute("none", "No user associated with that e-mail.");
            return "login";
        }

    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "index";
    }

    @RequestMapping(value="dashboard", method=RequestMethod.GET)
    public String dashboard(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);
        return "dashboard";
    }

}
