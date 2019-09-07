package org.launchcode.rallymvc.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String nickname;

    @NotNull
    @Email
    @NotEmpty
    private String email;

    @NotNull
    @Size(min=8, message = "Password must be 8 characters or longer")
    private String password;

    private String pic;

    /*Mappings*/
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Message> messages = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Workout> workouts = new ArrayList<>();

    /*Methods, Getters, Setters*/

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public String encryptPass(String password) {
        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());

        return pw_hash;
    }
}
