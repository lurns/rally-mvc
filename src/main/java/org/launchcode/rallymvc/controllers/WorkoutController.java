package org.launchcode.rallymvc.controllers;

import org.hibernate.jdbc.Work;
import org.launchcode.rallymvc.models.Message;
import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.Workout;
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
import java.util.ArrayList;
import java.util.Collections;

@Controller
public class WorkoutController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private WorkoutDao workoutDao;

    @RequestMapping(value = "dashboard", params = "workout-form", method = RequestMethod.POST)
    public String newWorkout(Model model, @Valid Workout workout,
                             Errors errors, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (!errors.hasErrors()) {
            LocalDateTime cal = LocalDateTime.now();
            workout.setDate(cal);
            workout.setUser(user);
            workoutDao.save(workout);
            return "redirect:/dashboard";
        } else {
            Message message = new Message();

            model.addAttribute("user", user);
            model.addAttribute("workout", workout);
            model.addAttribute("message", message);
            model.addAttribute("messageType", MessageType.values());
            model.addAttribute("status", "Error adding workout.");
            return "dashboard";
        }

    }

    @RequestMapping(value = "workouts", method = RequestMethod.GET)
    public String viewWorkouts(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        /*if no user, redirect to home*/
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("title", user.getNickname() + "'s Workouts");
        model.addAttribute("user", user);
        model.addAttribute("workouts", Workout.sortWorkouts(workoutDao, user));
        return "workouts";
    }

    @RequestMapping(value = "workouts/delete", method = RequestMethod.POST)
    public String delWorkout(@RequestParam int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        /*if no user, redirect to home*/
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        workoutDao.delete(id);
        return "redirect:/workouts";
    }
}
