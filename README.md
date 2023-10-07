# gPRC-STUDY
gRPC Study
---

I config gradle and maven setting 

<br/>

### How Hander *.proto?

1. add default setting like 

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "com.bsj.models";

message Die {
  int32 value = 1;
}

message Player {
  string name = 1;
  int32 position = 2;
}

message GameState {
  repeated Player player = 1;
}

service GameService {
  rpc roll(stream Die) returns (stream GameState);
}
```

<br/>

2. Generate Proto

```
./gradlew clean
./gradlew generateProto
```

<br/>
<br/>

3. You can Check generated File

![image](https://github.com/Bangseungjae/gPRC-STUDY/assets/87268026/5ac0065c-95b2-42dd-a736-2cfd12ec530a)

<br/>

4. Implementation proto file like

```java
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

```

5. Start Server

<br/>

```java
public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new GameService())
                .build();

        server.start();

        server.awaitTermination();
    }
}
```

