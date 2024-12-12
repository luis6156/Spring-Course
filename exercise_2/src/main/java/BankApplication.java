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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BankApplication {

    private static final String[] CLIENT_NAMES =
            {"Jonny Bravo", "Adam Budzinski", "Anna Smith"};

    public static void main(String[] args) {

        // Task 1.3: Create ClassPathXmlApplicationContext using application-context.xml as input in BankApplication class
//        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        // Task 3.2: Add this file to ClassPathXmlApplicationContext
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test-clients.xml");

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
        BankReportService reportService =
                (BankReportService) context.getBean("bankReport");

        System.out.println("Number of clients: " + reportService.getNumberOfBankClients());

        System.out.println("Number of accounts: " + reportService.getAccountsNumber());

        System.out.println("Bank Credit Sum: " + reportService.getBankCreditSum());
    }

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
