package org.launchcode.rallymvc.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.launchcode.rallymvc.models.data.WorkoutDao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Timestamp;

@Entity
public class Workout {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotEmpty
    @Size(max=50)
    private String type;

    private Timestamp date;

    @NotNull
    @Range(min=1,max=180)
    private int time;

    /*Mappings*/
    @ManyToOne
    private User user;

    /*Methods, Getters, Setters*/
    public Workout() {

    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date.toLocalDateTime();
    }

    public void setDate(LocalDateTime date) {
        this.date = Timestamp.valueOf(date);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static ArrayList<Workout> sortWorkouts(WorkoutDao workoutDao, User user) {
        Iterable<Workout> userWorkouts = workoutDao.findByUserId(user.getId());
        ArrayList<Workout> sortWorkout = new ArrayList<>();
        for (Workout workout : userWorkouts) {
            sortWorkout.add(workout);
        }

        Collections.reverse(sortWorkout);

        return sortWorkout;
    }
}
