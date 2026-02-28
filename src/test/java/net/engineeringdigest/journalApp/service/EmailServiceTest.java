package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void sendEmailToSentimentTrueUsers() {
        userRepository.getUsersForSentimentAnalysis()
                .forEach(user -> {
                    emailService.sendEmail(user.getEmail(), "This is a test email"
                            , "Hi There, this is body of test email");
                });
    }
}