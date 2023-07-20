package com.bsj.grpcflix.user.entity;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@ToString
@Table(name = "\"user\"")
public class User {

    @Id
    private String login;
    private String name;
    private String genre;

    protected User() {
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
