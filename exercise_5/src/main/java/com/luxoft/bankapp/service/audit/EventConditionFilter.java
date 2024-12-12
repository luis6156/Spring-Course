package com.luxoft.bankapp.service.audit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Task 4.2: Go to the com.luxoft.bankapp.service.audit package and create class EventConditionFilter with the next code
@Component
public class EventConditionFilter
{
    @Value("${audit.amount.deposit}")
    public double depositLimit;

    @Value("${audit.amount.withdrawal}")
    public double withdrawalLimit;
}

