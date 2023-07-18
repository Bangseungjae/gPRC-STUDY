package com.bsj.client.rpctypes;

import com.bsj.models.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
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
        Balance balance = this.blockingStub.getBalance(balanceCheckRequest);

        System.out.println(
                "Received: " + balance.getAmount()
        );
    }

    @Test
    public void withdrawTest() {
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder()
                .setAccountNumber(7)
                .setAmount(40)
                .build();
        this.blockingStub.withdraw(withDrawRequest)
                .forEachRemaining(money -> System.out.println("Received : " + money));
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
