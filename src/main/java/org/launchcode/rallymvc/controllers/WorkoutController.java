package org.launchcode.rallymvc.controllers;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.GregorianCalendar;

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
            GregorianCalendar cal = new GregorianCalendar();
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

}
