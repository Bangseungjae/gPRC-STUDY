package com.bsj.client.metadata;

<<<<<<< HEAD
import com.bsj.models.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.protobuf.ProtoUtils;

public class ClientConstants {

    public static final Metadata.Key<WithdrawalError> WITHDRAWAL_ERROR_KEY = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
=======
import io.grpc.Metadata;

public class ClientConstants {

>>>>>>> a7f92fc45fcd02eb99bf0bd267879c0a68b8be40
    public static final Metadata.Key<String> USER_TOKEN = Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata METADATA = new Metadata();

    static {
        METADATA.put(
                Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER),
                "bank-client-secret"
        );
    }

    public static Metadata getClientToken() {
        return METADATA;
    }
}
