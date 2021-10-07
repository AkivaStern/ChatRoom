package com.example.ex4.repo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * The "schema" for a Message, to be saved in DB
 */
@Entity
public class Message {

    /**
     * unique id for message
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * The message itself
     */
    @NotBlank(message = "Message is mandatory")
    private String message;

    /**
     * username of sender
     */
    private String username;

    /**
     * constructor
     */
    public Message() {}

    /**
     * constructor
     * @param username username of sender
     * @param message the message
     */
    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() { return username; }


    @Override
    public String toString() {
        return username + ": " + message;
    }
}

