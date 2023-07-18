package com.bsj.client.metadata;

import com.bsj.client.rpctypes.BalanceStreamObserver;
import com.bsj.client.rpctypes.MoneyStreamingResponse;
import com.bsj.models.*;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstants.getClientToken()))
                .usePlaintext()
                .build();

        System.out.println(
                "Channel is created"
        );

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);

        System.out.println(
                "Stubs are created"
        );
    }

    @Test
    public void balanceTest() throws InterruptedException {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();

        for (int i = 0; i < 20; i++) {
            try {
                int random = ThreadLocalRandom.current().nextInt(1, 4);
                System.out.println("Random : " + random);
                Balance balance = this.blockingStub
                        .withCallCredentials(new UserSessionToken("user-secret-" + random + ":prime"))
//                        .withCallCredentials(new UserSessionToken("user-secret-" + random + ":standard"))
                        .getBalance(balanceCheckRequest);

                System.out.println(
                        "Received: " + balance.getAmount()
                );
            } catch (StatusRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void withdrawTest() {
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder()
                .setAccountNumber(6)
                .setAmount(50)
                .build();

        try {
            this.blockingStub
                    .withDeadline(Deadline.after(4, TimeUnit.SECONDS))
                    .withdraw(withDrawRequest)
                    .forEachRemaining(money -> System.out.println("Received : " + money));
        } catch (StatusRuntimeException e) {
            // 예외처리
        }
    }

    @Test
    public void withdrawAsyncTest() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder()
                .setAccountNumber(8)
                .setAmount(40)
                .build();

        this.bankServiceStub.withdraw(withDrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
    }

    @Test
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> streamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest
                    .newBuilder()
                    .setAccountNumber(8)
                    .setAmount(10)
                    .build();
            streamObserver.onNext(depositRequest);
        }
        streamObserver.onCompleted();
        latch.await();
    }
}
