package org.launchcode.rallymvc.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.GregorianCalendar;

@Entity
public class Workout {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotEmpty
    @Size(max=50)
    private String type;

    @NotNull
    private GregorianCalendar date;

    @NotNull
    @NotEmpty
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

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
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
}
