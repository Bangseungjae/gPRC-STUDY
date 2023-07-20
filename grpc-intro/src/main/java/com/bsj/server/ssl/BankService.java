package com.bsj.server.ssl;

import com.bsj.models.*;
import com.bsj.server.rpctypes.AccountDataBase;
import com.bsj.server.rpctypes.CashStreamingRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDataBase.getBalance(accountNumber))
                .build();

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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Money money = Money.newBuilder().setValue(10).build();
            responseObserver.onNext(money);
            AccountDataBase.deductBalance(accountNumber, 10);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }
}
