package org.launchcode.rallymvc.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.launchcode.rallymvc.models.data.MessageDao;
import org.launchcode.rallymvc.models.data.MessageType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotEmpty
    @Size(max=300)
    private String msg;

    @NotNull
    private MessageType msg_type;

    private Timestamp date;

    /*Mappings*/
    @ManyToOne
    private User user;

    /*Methods, Getters, Setters*/
    public Message() {

    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(MessageType msg_type) {
        this.msg_type = msg_type;
    }

    public LocalDateTime getDate() {
        return date.toLocalDateTime();
    }

    public void setDate(LocalDateTime date) {
        this.date = Timestamp.valueOf(date);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
