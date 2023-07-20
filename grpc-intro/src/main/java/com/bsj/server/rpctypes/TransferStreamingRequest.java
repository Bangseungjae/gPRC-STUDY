package com.bsj.server.rpctypes;

import com.bsj.models.Account;
import com.bsj.models.TransferRequest;
import com.bsj.models.TransferResponse;
import com.bsj.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }


    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccountNumber = transferRequest.getFromAccount();
        int toAccountNumber = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = AccountDataBase.getBalance(fromAccountNumber);
        TransferStatus status = TransferStatus.FAILED;

        if (balance >= amount && fromAccountNumber != toAccountNumber) {
            AccountDataBase.deductBalance(fromAccountNumber, amount);
            AccountDataBase.addBalance(toAccountNumber, amount);
            status = TransferStatus.SUCCESS;
        }

        int fromAccountBalance = AccountDataBase.getBalance(fromAccountNumber);
        Account fromAccount = Account.
                newBuilder()
                .setAccountNumber(fromAccountNumber)
                .setAmount(fromAccountBalance)
                .build();

        int toAccountBalance = AccountDataBase.getBalance(toAccountNumber);
        Account toAccount = Account
                .newBuilder()
                .setAccountNumber(toAccountNumber)
                .setAmount(toAccountBalance)
                .build();

        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(status)
                .addAccount(fromAccount)
                .addAccount(toAccount)
                .build();

        transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountDataBase.printAccountDetails(); // 모든 계정의 정보 출력
        this.transferResponseStreamObserver.onCompleted();
    }
}
