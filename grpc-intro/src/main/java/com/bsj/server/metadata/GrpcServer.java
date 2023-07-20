package com.bsj.server.metadata;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
<<<<<<< HEAD
//                .intercept(new AuthInterceptor())
=======
                .intercept(new AuthInterceptor())
>>>>>>> a7f92fc45fcd02eb99bf0bd267879c0a68b8be40
                .addService(new MetadataService())
                .build();

        server.start();

        server.awaitTermination();
    }

}
