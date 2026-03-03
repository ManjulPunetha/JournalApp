package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AppCache appCache;


    @Scheduled(cron = "0 0 9 * * SUN") // Every Sunday at 9:00 AM
    public void fetchUsersAndSendSentimentAnalysisMail() {
        List<User> users = userRepository.getUsersForSentimentAnalysis();

        for (User user : users) {
            List<JournalEntry> entries = user.getJournalEntries();

            // Step 2: Get last 7 days' journal entries for this user
            List<String> lastSevenDayEntry = entries.stream()
                    .filter(journalEntries ->
                            journalEntries.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(JournalEntry::getContent).collect(Collectors.toList());

            // Step 3: Combine all entries into a single String for analysis
            String combinedText = String.join(" ", lastSevenDayEntry);

            String sentiment = sentimentAnalysisService.getSentiment(combinedText);

            emailService.sendEmail(user.getEmail(), "Last 7 days sentiment", sentiment);
        }
    }

    @Scheduled(cron = "0 0/1 * * * *") //every 1 min
    public void refreshCache(){
        appCache.init();
    }
}
