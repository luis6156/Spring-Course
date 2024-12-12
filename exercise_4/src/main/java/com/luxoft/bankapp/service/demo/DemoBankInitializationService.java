package com.luxoft.bankapp.service.demo;

import com.luxoft.bankapp.service.feed.BankFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@Service
// Task 2.3: Make DemoBankInitializationService available only for dev environment.
@Profile("dev")
public class DemoBankInitializationService implements BankInitializationService {
    private static final String DEMO_FEED_FILE_NAME = "demo.feed";

    // Task 2.4: Add the BankFeedServiceImpl field with @Autowired annotation.
    @Autowired
    private BankFeedService feedService;

    // Task 3.1: Open DemoBankInitializationService and add fileName field with @Value annotation.
    @Value("${feed.filename}")
    private String fileName;

    @Override
    // Task 2.5: Make sure the method is called after the service bean is initialized.
    @PostConstruct
    public void createClientsForDemo() {
        // Task 3.2:         // TASK 3.2: Load feed from the file specified in application's properties if it exists.
        if (fileName == null)
        {
            fileName = DEMO_FEED_FILE_NAME;
        }

        // Task 2.6: Implement createClientsForDemo with the next code
        feedService.loadFeed(new File("demo.feed"));
    }
}
