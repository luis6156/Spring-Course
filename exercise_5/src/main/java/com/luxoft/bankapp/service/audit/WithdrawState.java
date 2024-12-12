package com.luxoft.bankapp.service.audit;

// Task 2.4: Add withdraw enum states that will be used to log user actions before and after withdrawal.
public enum WithdrawState {
    TRYING, SUCCESSFUL, FAILED
}
