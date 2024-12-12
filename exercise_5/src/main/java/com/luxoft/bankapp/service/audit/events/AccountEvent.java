package com.luxoft.bankapp.service.audit.events;

import org.springframework.context.ApplicationEvent;

// Task 3.2: Create abstract class AccountEvent that extends ApplicationEvent with the next code
public abstract class AccountEvent extends ApplicationEvent
{
    private long accountId;

    public AccountEvent(Object source, long accountId)
    {
        super(source);
        this.accountId = accountId;
    }

    public long getAccountId()
    {
        return accountId;
    }
}

