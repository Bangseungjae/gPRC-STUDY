package main.java.com.bsj.grpcflix.aggregate.dto;

import java.io.Serializable;

public class RecommendedMovie implements Serializable {

    private String title;
    private int year;
    private double rating;
    private String genre;

    public RecommendedMovie(String title, int year, double rating, String genre) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    public RecommendedMovie() {
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
