package com.bsj.grpcflix.movie.service;

import com.bsj.grpcflix.movie.MovieDto;
import com.bsj.grpcflix.movie.MovieSearchRequest;
import com.bsj.grpcflix.movie.MovieSearchResponse;
import com.bsj.grpcflix.movie.MovieServiceGrpc;
import com.bsj.grpcflix.movie.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    private final MovieRepository movieRepository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtoList = this.movieRepository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setRating(movie.getYear())
                        .setRating(movie.getRating())
                        .build()
                ).collect(Collectors.toList());

        MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder()
                .addAllMovie(movieDtoList)
                .build();

        responseObserver.onNext(movieSearchResponse);
        responseObserver.onCompleted();

        super.getMovies(request, responseObserver);
    }
}
