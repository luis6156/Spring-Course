package com.luxoft.bankapp.service.audit;

import com.luxoft.bankapp.service.audit.events.*;

// Task 2.3: Create an empty Audit interface
//public interface Audit {
//    // Task 2.5: Now add next methods to Audit interface
//    void auditDeposit(long accountId, double amount);
//
//    void auditWithdraw(long accountId, double amount, WithdrawState state);
//
//    void auditBalance(long accountId);
//}

// Task 3.4: Once events are ready to use, replace all methods from Audit interface
public interface Audit
{
    void auditOperation(DepositEvent event);

    void auditOperation(WithdrawEvent event);

    void auditOperation(BalanceEvent event);
}

