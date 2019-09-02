package org.launchcode.rallymvc.controllers;


import org.launchcode.rallymvc.models.Message;
import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.Workout;
import org.launchcode.rallymvc.models.data.MessageDao;
import org.launchcode.rallymvc.models.data.MessageType;
import org.launchcode.rallymvc.models.data.UserDao;
import org.launchcode.rallymvc.models.data.WorkoutDao;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private WorkoutDao workoutDao;

    //home & registration

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

    //login

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

    //logout

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    //dashboard get

    @RequestMapping(value="dashboard", method=RequestMethod.GET)
    public String dashboard(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        /*if no user, redirect to home*/
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        Message message = new Message();
        Workout workout = new Workout();

        ArrayList<Workout> userWorkouts = Workout.sortWorkouts(workoutDao, user);

        Iterable<Message> getMessages = messageDao.findByUserId(user.getId());
        ArrayList<Message> userMessages = new ArrayList<>();

        for (Message eachMessage : getMessages) {
            userMessages.add(eachMessage);
        }

        if (userWorkouts.size() != 0) {
            Workout recentWorkout = userWorkouts.get(0);
            model.addAttribute("recentWorkout", recentWorkout);

            //message display logic

            //get current time
            LocalDateTime today = LocalDateTime.now();
            //find disparity from current time and last workout
            long difference = recentWorkout.getDate().until(today, ChronoUnit.HOURS);
            Message currentMessage = new Message();

            if (difference <= 12) {
                ArrayList<Message> congrats = new ArrayList<>();
                for (Message eachMessage : userMessages) {
                    if (eachMessage.getMsg_type() == MessageType.CONGRATULATE) {
                        congrats.add(eachMessage);
                    }
                }

                if (congrats.size() == 1) {
                    currentMessage = congrats.get(0);
                } else if (congrats.size() > 1) {
                    int random = (int)(Math.random() * ((congrats.size() - 1) + 1)) + 0;
                    currentMessage = congrats.get(random);
                } else {
                    currentMessage.setMsg_type(MessageType.CONGRATULATE);
                    currentMessage.setMsg("I did the thing!");
                    currentMessage.setDate(today);
                    currentMessage.setUser(user);
                }
            } else if (difference < 24 && difference > 12) {
                ArrayList<Message> motivats = new ArrayList<>();
                for (Message eachMessage : userMessages) {
                    if (eachMessage.getMsg_type() == MessageType.MOTIVATE) {
                        motivats.add(eachMessage);
                    }
                }

                if (motivats.size() == 1) {
                    currentMessage = motivats.get(0);
                } else if (motivats.size() > 1) {
                    int random = (int)(Math.random() * ((motivats.size() - 1) + 1)) + 0;
                    currentMessage = motivats.get(random);
                } else {
                    currentMessage.setMsg_type(MessageType.MOTIVATE);
                    currentMessage.setMsg("There's still time to get up and get out there!");
                    currentMessage.setDate(today);
                    currentMessage.setUser(user);
                }
            } else if (difference >= 24) {
                ArrayList<Message> berats = new ArrayList<>();
                for (Message eachMessage : userMessages) {
                    if (eachMessage.getMsg_type() == MessageType.BERATE) {
                        berats.add(eachMessage);
                    }
                }

                if (berats.size() == 1) {
                    currentMessage = berats.get(0);
                } else if (berats.size() > 1) {
                    int random = (int)(Math.random() * ((berats.size() - 1) + 1)) + 0;
                    currentMessage = berats.get(random);
                } else {
                    currentMessage.setMsg_type(MessageType.BERATE);
                    currentMessage.setMsg("Lounging isn't exactly a workout, fam.");
                    currentMessage.setDate(today);
                    currentMessage.setUser(user);
                }
            }
            model.addAttribute("currentMessage", currentMessage);
        }

        //defaults
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        model.addAttribute("messageType", MessageType.values());
        model.addAttribute("workout", workout);
        return "dashboard";
    }

    //profile get


}
