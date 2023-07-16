package com.bsj.game.server;

import com.bsj.models.Die;
import com.bsj.models.GameServiceGrpc;
import com.bsj.models.GameState;
import com.bsj.models.Player;
import io.grpc.stub.StreamObserver;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder()
                .setName("client")
                .setPosition(0)
                .build();

        Player server = Player.newBuilder()
                .setName("server")
                .setPosition(0)
                .build();

        return new DieStreamingRequest(client, server, responseObserver);
    }
}
