package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.AbstractAccount;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.storage.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankReportServiceImpl implements BankReportService {

    private ClientRepository repository;

    @Override
    public int getNumberOfBankClients() {

        return repository.getAll().size();
    }

    @Override
    public int getAccountsNumber() {

        return repository.getAll()
                .stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toList()).size();
    }

    @Override
    public List<Client> getClientsSorted() {

        return repository.getAll()
                .stream()
                .sorted(Comparator.comparing(Client::getName))
                .collect(Collectors.toList());
    }

    @Override
    public double getBankCreditSum() {

        return repository.getAll().stream()
                .flatMap(c -> c.getAccounts().stream())
                .filter(a -> a.getClass() == CheckingAccount.class)
                .mapToDouble(AbstractAccount::getBalance)
                .filter(b -> b < 0)
                .sum();
    }

    @Override
    public Map<String, List<Client>> getClientsByCity() {

        return repository.getAll().stream()
                .collect(Collectors.groupingBy(Client::getCity));
    }

    // Ex. 2 Task 1.6: Mark storage field of BankReportServiceImpl class with @Autowired annotation
    @Autowired
    public void setRepository(ClientRepository repository) {
        this.repository = repository;
    }
}
