package main.java.com.bsj.grpcflix.aggregate.service;

import com.bsj.grpcflix.common.Genre;
import com.bsj.grpcflix.movie.*;
import com.bsj.grpcflix.user.*;
import com.bsj.grpcflix.user.UserResponse;
import com.bsj.grpcflix.user.UserSearchRequest;
import com.bsj.grpcflix.user.UserServiceGrpc;
import main.java.com.bsj.grpcflix.aggregate.dto.RecommendedMovie;
import main.java.com.bsj.grpcflix.aggregate.dto.UserGenre;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestions(String loginId){
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse = this.userStub.getUserGenre(userSearchRequest);
        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = this.movieStub.getMovies(movieSearchRequest);
        return movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto -> new RecommendedMovie(
                        movieDto.getTitle(),
                        movieDto.getYear(),
                        movieDto.getRating(),
                        userResponse.getGenre().toString()
                ))
                .collect(Collectors.toList());
    }

    public void setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        UserResponse userResponse = this.userStub.updateUserGenre(userGenreUpdateRequest);
    }
}
