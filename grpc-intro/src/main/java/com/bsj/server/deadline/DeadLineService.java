package com.bsj.server.deadline;

import com.bsj.models.*;
import com.bsj.server.rpctypes.AccountDataBase;
import com.bsj.server.rpctypes.CashStreamingRequest;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class DeadLineService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDataBase.getBalance(accountNumber))
                .build();

        // simulate time-consuming call
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);

        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithDrawRequest request, StreamObserver<Money> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        int balance = AccountDataBase.getBalance(accountNumber);

        // validate
        if (balance < amount) {
            Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. You have only " + balance);
            responseObserver.onError(status.asRuntimeException());
        }

        // all the validations passed
        for (int i = 0; i < (amount/10); i++) {
            Money money = Money.newBuilder().setValue(10).build();
            Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);

            if (!Context.current().isCancelled()) {
                responseObserver.onNext(money);
                System.out.println("Delivered $10");
                AccountDataBase.deductBalance(accountNumber, 10);
            } else {
                break;
            }
            System.out.println("Completed");
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }
}
