package com.bsj.client.loadbalancing;

import com.bsj.models.Balance;
import com.bsj.models.BalanceCheckRequest;
import com.bsj.models.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxTestClient {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8585)
                .usePlaintext()
                .build();

        System.out.println(
                "Channel is created"
        );

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
//        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);

        System.out.println(
                "Stubs are created"
        );
    }

    @Test
    public void balanceTest() throws InterruptedException {

        for (int i = 1; i < 11; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(i)
                    .build();
            Balance balance = this.blockingStub.getBalance(balanceCheckRequest);

            System.out.println(
                    "Received: " + balance.getAmount()
            );
        }
    }
}
