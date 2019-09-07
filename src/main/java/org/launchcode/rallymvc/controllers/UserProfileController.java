package org.launchcode.rallymvc.controllers;

import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "profile/change-password", method = RequestMethod.POST)
    public String processChangePassword(@RequestParam int id, @RequestParam String currentpassword,
                                        @RequestParam String newpassword, @RequestParam String retypepassword,
                                        HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        User checkUser = userDao.findOne(id);

        if (user.getId() == id) {
            if (user.getPassword().equals(currentpassword) || BCrypt.checkpw(currentpassword, user.getPassword())) {
                if (newpassword.equals(retypepassword)) {
                    user.setPassword(user.encryptPass(newpassword));
                    userDao.save(user);
                    return "redirect:/profile";
                } else {
                    return "redirect:/dashboard";
                }
            } else {
                return "redirect:/dashboard";
            }
        } else {
            return "redirect:/";
        }
    }

}
