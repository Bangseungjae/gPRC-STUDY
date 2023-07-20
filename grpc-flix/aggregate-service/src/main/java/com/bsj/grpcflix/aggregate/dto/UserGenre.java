package main.java.com.bsj.grpcflix.aggregate.dto;


import java.io.Serializable;

public class UserGenre implements Serializable {

    private String loginId;
    private String genre;

    public String getLoginId() {
        return loginId;
    }

    public String getGenre() {
        return genre;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public UserGenre(String loginId, String genre) {
        this.loginId = loginId;
        this.genre = genre;
    }

    public UserGenre() {
    }
}
