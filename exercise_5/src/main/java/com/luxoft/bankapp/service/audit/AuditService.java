package com.luxoft.bankapp.service.audit;

import com.luxoft.bankapp.service.audit.events.AccountEvent;
import com.luxoft.bankapp.service.audit.events.BalanceEvent;
import com.luxoft.bankapp.service.audit.events.DepositEvent;
import com.luxoft.bankapp.service.audit.events.WithdrawEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Task 2.6: Create AuditService class that implements Audit and mark it with @Service annotation
//@Service
//public class AuditService implements Audit
//{
//    @Override
//    public void auditDeposit(long accountId,
//                             double amount)
//    {
//        System.out.println("ACCOUNT ID: "
//                + accountId + " DEPOSIT: " + amount);
//    }
//
//    @Override
//    public void auditWithdraw(long accountId,
//                              double amount, WithdrawState state)
//    {
//        System.out.println("ACCOUNT ID: "
//                + accountId + " " + state
//                + " WITHDRAWAL: " + amount);
//    }
//
//    @Override
//    public void auditBalance(long accountId)
//    {
//        System.out.println("ACCOUNT ID: "
//                + accountId + " BALANCE CHECK");
//    }
//}

// Task 3.5: Once events are ready to use, replace all methods from Audit interface
@Service
public class AuditService implements Audit
{
    private List<AccountEvent> events;

    public AuditService()
    {
        this.events = new ArrayList<>(100);
    }

    @Override
    // Task 4.3
    @EventListener(condition = "#event.amount >= @eventConditionFilter.depositLimit")
    public void auditOperation(DepositEvent event)
    {
        events.add(event);
        System.out.println("ACCOUNT ID: "
                + event.getAccountId() + " "
                + event.getSource() +
                ": " + event.getAmount());
    }

    @Override
    // Task 4.3
    @EventListener(condition = "#event.amount >= @eventConditionFilter.withdrawalLimit")
    public void auditOperation(WithdrawEvent event)
    {
        events.add(event);
        System.out.println("ACCOUNT ID: "
                + event.getAccountId() + " "
                + event.getState() + " "
                + event.getSource() + ": "
                + event.getAmount());
    }

    @Override
    @EventListener
    public void auditOperation(BalanceEvent event)
    {
        events.add(event);
        System.out.println("ACCOUNT ID: "
                + event.getAccountId() + " "
                + event.getSource());
    }

    public List<AccountEvent> getEvents()
    {
        return new ArrayList<>(events);
    }
}


