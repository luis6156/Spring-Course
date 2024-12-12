import com.luxoft.bankapp.exceptions.ActiveAccountNotSet;
import com.luxoft.bankapp.model.AbstractAccount;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankReportService;
import com.luxoft.bankapp.service.BankReportServiceImpl;
import com.luxoft.bankapp.service.Banking;
import com.luxoft.bankapp.service.BankingImpl;
import com.luxoft.bankapp.model.Client.Gender;
import com.luxoft.bankapp.service.storage.ClientRepository;
import com.luxoft.bankapp.service.storage.MapClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Objects;

// Ex. 3 Task 1.2: Add @Configuration and @ComponentScan("com.luxoft.bankapp") annotations to BankApplication class
@Configuration
@ComponentScan("com.luxoft.bankapp")
// Ex. 3 Task 2.2: Add @PropertySource("classpath:clients.properties") annotation
@PropertySource("classpath:clients.properties")
public class BankApplication {

    private static final String[] CLIENT_NAMES =
            {"Jonny Bravo", "Adam Budzinski", "Anna Smith"};

    // Ex. 3 Task 2.3: Inject an ApplicationContext instance to manage application beans.
    @Autowired
    private ApplicationContext applicationContext;

    // Ex. 3 Task 2.4: Inject an Environment instance to access application properties.
    @Autowired
    private Environment environment;

    // Ex. 3 Task 2.5: Configure saving account bean for the first client.
    @Bean(name = "savingAccount1")
    public SavingAccount getDemoSavingAccount1() {
        int balance = Integer.parseInt(
                Objects.requireNonNull(environment.getProperty("saving-account1.balance")));

        return new SavingAccount(balance);
    }

    @Bean(name = "checkingAccount1")
    public CheckingAccount getDemoCheckingAccount1() {
        int overdraft = Integer.parseInt(
                Objects.requireNonNull(environment.getProperty("checking-account1.overdraft")));

        return new CheckingAccount(overdraft);
    }

    @Bean(name = "checkingAccount2")
    public CheckingAccount getDemoCheckingAccount2() {
        int overdraft = Integer.parseInt(
                Objects.requireNonNull(environment.getProperty("checking-account2.overdraft")));

        return new CheckingAccount(overdraft);
    }

    // Ex. 3 Task 2.6: Configure the first client bean.
    @Bean(name = "client1")
    public Client getDemoClient1() {
        String name = environment.getProperty("client1.name");
        Gender gender = Gender.valueOf(environment.getProperty("client1.gender"));
        String city = environment.getProperty("client1.city");

        Client client = new Client(name, gender);
        client.setCity(city);

        AbstractAccount saving = (SavingAccount)
                applicationContext.getBean("savingAccount1");
        AbstractAccount checking = (CheckingAccount)
                applicationContext.getBean("checkingAccount1");

        client.addAccount(saving);
        client.addAccount(checking);

        return client;
    }

    @Bean(name = "client2")
    public Client getDemoClient2() {
        String name = environment.getProperty("client2.name");
        Gender gender = Gender.valueOf(environment.getProperty("client2.gender"));
        String city = environment.getProperty("client2.city");

        Client client = new Client(name, gender);
        client.setCity(city);

        AbstractAccount checking = (CheckingAccount)
                applicationContext.getBean("checkingAccount2");

        client.addAccount(checking);

        return client;
    }

    public static void main(String[] args) {

        // Task 1.3: Create ClassPathXmlApplicationContext using application-context.xml as input in BankApplication class
//        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        // Task 3.2: Add this file to ClassPathXmlApplicationContext
//        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test-clients.xml");

        // Ex. 3 Task 1.3: Create a Spring application context based on Java-based configuration class.
        ApplicationContext context = new
                AnnotationConfigApplicationContext(BankApplication.class);

        // Task 1.7: Test workWithExistingClients and bankingServiceDemo methods work correctly.
        Banking banking = initialize(context);

        workWithExistingClients(banking);

        bankingServiceDemo(banking);

        // Task 2.3: Test bankReportsDemo method works correctly.
        bankReportsDemo(context);
    }

    // Task 2.2: Modify the bankReportsDemo method to get ApplicationContest instead of ClientRepository
    public static void bankReportsDemo(ApplicationContext context) {

        System.out.println("\n=== Using BankReportService ===\n");

        // Task 2.3: Fix compilation error. Replace BankReportServiceImpl object initialization with code bellow. In this case you will have an object with already injected repository
//        BankReportService reportService =
//                (BankReportService) context.getBean("bankReport");
        // Ex. 3 Task 2.10: Retrieve the BankReportService type bean.
        BankReportService reportService =
                context.getBean(BankReportService.class);

        System.out.println("Number of clients: " + reportService.getNumberOfBankClients());

        System.out.println("Number of accounts: " + reportService.getAccountsNumber());

        System.out.println("Bank Credit Sum: " + reportService.getBankCreditSum());
    }

    // Ex. 3 Task 2.11: Open the BankApplication#bankingServiceDemo method and replace code to get banking with next code
    public static void bankingServiceDemo(Banking banking) {

        System.out.println("\n=== Initialization using Banking implementation ===\n");

        Client anna = new Client(CLIENT_NAMES[2], Gender.FEMALE);
        anna = banking.addClient(anna);

        AbstractAccount saving = banking.createAccount(anna, SavingAccount.class);
        saving.deposit(1000);

        banking.updateAccount(anna, saving);

        AbstractAccount checking = banking.createAccount(anna, CheckingAccount.class);
        checking.deposit(3000);

        banking.updateAccount(anna, checking);

        banking.getAllAccounts(anna).stream().forEach(System.out::println);
    }

    // Ex. 3 Task 2.11: Open the BankApplication#bankingServiceDemo method and replace code to get banking with next code
    public static void workWithExistingClients(Banking banking) {

        System.out.println("\n=======================================");
        System.out.println("\n===== Work with existing clients ======");

        Client jonny = banking.getClient(CLIENT_NAMES[0]);

        try {

            jonny.deposit(5_000);

        } catch (ActiveAccountNotSet e) {

            System.out.println(e.getMessage());

            jonny.setDefaultActiveAccountIfNotSet();
            jonny.deposit(5_000);
        }

        System.out.println(jonny);

        Client adam = banking.getClient(CLIENT_NAMES[1]);
        adam.setDefaultActiveAccountIfNotSet();

        adam.withdraw(1500);

        double balance = adam.getBalance();
        System.out.println("\n" + adam.getName() + ", current balance: " + balance);

        banking.transferMoney(jonny, adam, 1000);

        System.out.println("\n=======================================");
        banking.getClients().forEach(System.out::println);
    }

    /*
     * Method that creates a few clients and initializes them with sample values
     *
     * Task 1.5: Modify initialize method to get ApplicationContest instead of ClientRepository
     */
    public static Banking initialize(ApplicationContext context) {
        // Task 1.6: Fix compilation error. Replace BankingImpl object initialization with code bellow. In this case you will have an object with already injected repository.
        Banking banking = (Banking) context.getBean("banking");

        // Task 3.7: Replace client_1 with bean client1 inside BankApplication#initialize(ApplicationContext context) method
        Client client_1 = (Client) context.getBean("client1");

        // Task 3.9: Repeat steps for client_2
        Client client_2 = (Client) context.getBean("client2");

        banking.addClient(client_1);
        banking.addClient(client_2);

        return banking;
    }
}
