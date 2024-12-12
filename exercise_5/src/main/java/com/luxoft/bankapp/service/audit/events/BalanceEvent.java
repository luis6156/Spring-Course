package com.luxoft.bankapp.service.audit.events;

// Task 3.3
public class BalanceEvent extends AccountEvent
{
    public BalanceEvent(long accountId)
    {
        super("BALANCE CHECK", accountId);
    }
}

