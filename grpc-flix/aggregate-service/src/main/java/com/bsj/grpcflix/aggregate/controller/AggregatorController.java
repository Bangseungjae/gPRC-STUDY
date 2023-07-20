package main.java.com.bsj.grpcflix.aggregate.controller;

import main.java.com.bsj.grpcflix.aggregate.dto.RecommendedMovie;
import main.java.com.bsj.grpcflix.aggregate.dto.UserGenre;
import main.java.com.bsj.grpcflix.aggregate.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId) {
        return userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user")
    public void setUserGenre(@RequestBody UserGenre userGenre) {
        userMovieService.setUserGenre(userGenre);
    }
}
