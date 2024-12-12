package com.luxoft.bankapp.config;

import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankReportService;
import com.luxoft.bankapp.service.BankReportServiceImpl;
import com.luxoft.bankapp.service.Banking;
import com.luxoft.bankapp.service.BankingImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan(basePackages = "com.luxoft.bankapp")
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("clients.properties"));
        return configurer;
    }

    @Bean
    public Banking banking() {
        return new BankingImpl();
    }

    @Bean
    public BankReportService bankReport() {
        return new BankReportServiceImpl();
    }

    @Bean
    @Qualifier("savingAccount1")
    public SavingAccount savingAccount1(@Value("${client1.savingAccount.balance}") double balance) {
        return new SavingAccount(balance);
    }

    @Bean
    @Qualifier("checkingAccount1")
    public CheckingAccount checkingAccount1(@Value("${client1.checkingAccount.overdraft}") double overdraft) {
        return new CheckingAccount(overdraft);
    }

    @Bean
    @Qualifier("checkingAccount2")
    public CheckingAccount checkingAccount2(@Value("${client2.checkingAccount.overdraft}") double overdraft) {
        return new CheckingAccount(overdraft);
    }

    @Bean
    public Client client1(
            @Value("${client1}") String name,
            @Value("${client1.city}") String city,
            @Qualifier("savingAccount1") SavingAccount savingAccount1,
            @Qualifier("checkingAccount1") CheckingAccount checkingAccount1) {
        Client client = new Client(name, Client.Gender.MALE);
        client.addAccount(savingAccount1);
        client.addAccount(checkingAccount1);
        client.setCity(city);
        return client;
    }

    @Bean
    public Client client2(
            @Value("${client2}") String name,
            @Value("${client2.city}") String city,
            @Qualifier("checkingAccount2") CheckingAccount checkingAccount2) {
        Client client = new Client(name, Client.Gender.MALE);
        client.addAccount(checkingAccount2);
        client.setCity(city);
        return client;
    }

    @Bean
    public Client client3(
            @Value("${client3}") String name,
            @Value("${client3.city}") String city,
            @Value("${client3.savingAccount.balance}") double savingBalance,
            @Value("${client3.checkingAccount.balance}") double checkingBalance) {
        Client client = new Client(name, Client.Gender.FEMALE);
        client.addAccount(new SavingAccount(savingBalance));
        client.addAccount(new CheckingAccount(checkingBalance));
        client.setCity(city);
        return client;
    }
}