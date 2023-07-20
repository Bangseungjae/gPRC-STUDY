package com.bsj.grpcflix.user.service;

import com.bsj.grpcflix.common.Genre;
import com.bsj.grpcflix.user.UserGenreUpdateRequest;
import com.bsj.grpcflix.user.UserResponse;
import com.bsj.grpcflix.user.UserSearchRequest;
import com.bsj.grpcflix.user.UserServiceGrpc;
import com.bsj.grpcflix.user.entity.User;
import com.bsj.grpcflix.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();

        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
//
//        user.setGenre(request.getGenre().toString());
//        log.info(user.getGenre());
//        UserResponse build = builder.setGenre(request.getGenre())
//                .setName(user.getName())
//                .setLoginId(user.getLogin())
//                .build();
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
