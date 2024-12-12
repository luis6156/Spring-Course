package com.luxoft.bankapp.service.audit;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.audit.events.BalanceEvent;
import com.luxoft.bankapp.service.audit.events.DepositEvent;
import com.luxoft.bankapp.service.audit.events.WithdrawEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

// Task 2.7: Create AuditAspect class and mark it with @Configuration and @Aspect annotations
@Configuration
@Aspect
public class AuditAspect {
    // Task 2.8: Add the Audit property to have an ability to log events
    // Task 2.7: Remove the Audit service field. We will now use events to log banking operations.
//    @Autowired
//    private Audit audit;

    // Task 3.6: Add the application context to enable event publishing.
    @Autowired
    private ApplicationContext applicationContext;

    // Task 2.9: Then create pointcuts for operations. We should know when these methods are executing
    @Pointcut("execution(* com.luxoft.bankapp.service.operations.*.deposit(..))")
    public void anyDeposit() {}

    @Pointcut("execution(* com.luxoft.bankapp.service.operations.*.withdraw(..))")
    public void anyWithdraw() {}

    @Pointcut("execution(* com.luxoft.bankapp.service.operations.*.getBalance(..))")
    public void anyBalance() {}

    // Task 2.11: Create @Before advice for deposit operation and fire event (before the method is called)
    @Before("anyDeposit()")
    public void logDeposit(JoinPoint joinPoint)
    {
        Object[] methodArgs = joinPoint.getArgs();

//        audit.auditDeposit(getAccountId(methodArgs),
//                (double) methodArgs[1]);

        // Task 3.8: Open the logDeposit method and replace audit call with the next code
        applicationContext.publishEvent(
                new DepositEvent(getAccountId(methodArgs), (double) methodArgs[1]));
    }

    // Task 2.13: Create @Before advice for balance operation yourself and run the test again. Now the getClientBalance test should pass too
    @Before("anyBalance()")
    public void logBalance(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();

        /*
        audit.auditBalance(getAccountId(methodArgs));
         */

        // Task 3.9: Open the logBalance method and replace audit call with the next code
        applicationContext.publishEvent(
                new BalanceEvent(getAccountId(methodArgs)));
    }

    // Task 2.14: Create @Around advice for withdraw operation and log user actions
    @Around("anyWithdraw()")
    public Object logWithdrawal(ProceedingJoinPoint thisJoinPoint) throws Throwable
    {
        Object[] methodArgs = thisJoinPoint.getArgs();
        long accountId = getAccountId(methodArgs);

//        audit.auditWithdraw(accountId,
//                (double) methodArgs[1], WithdrawState.TRYING);
//
//        Object result;
//
//        try
//        {
//            result = thisJoinPoint.proceed();
//
//            audit.auditWithdraw(accountId,
//                    (double) methodArgs[1],
//                    WithdrawState.SUCCESSFUL);
//        }
//        catch (Exception e)
//        {
//            audit.auditWithdraw(accountId,
//                    (double) methodArgs[1],
//                    WithdrawState.FAILED);
//
//            throw e;
//        }
//
//        return result;

        // Task 3.10: Open the logWithdrawal method and replace audit calls with the next code
        applicationContext.publishEvent(
                new WithdrawEvent(getAccountId(methodArgs), (double) methodArgs[1]));

        Object result;

        try {
            result = thisJoinPoint.proceed();

            applicationContext.publishEvent(new WithdrawEvent(getAccountId(methodArgs),
                    (double) methodArgs[1], WithdrawEvent.State.SUCCESSFUL));

        } catch (Exception e) {
            applicationContext.publishEvent(new WithdrawEvent(getAccountId(methodArgs),
                    (double) methodArgs[1], WithdrawEvent.State.FAILED));
            throw e;
        }

        return result;
    }



    // Task 2.10: Implement the method that extracts the account ID from the method arguments.
    private long getAccountId(Object[] methodArgs) {
        Account account;

        if (methodArgs[0] instanceof Client) {
            account = ((Client) methodArgs[0]).getActiveAccount();
        } else {
            account = (Account) methodArgs[0];
        }

        return account.getId();
    }
}
