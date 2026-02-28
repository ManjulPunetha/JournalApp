package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryCustomImplTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUsersForSentimentAnalysis() {
        List<User> usersForSentimentAnalysis = userRepository.getUsersForSentimentAnalysis();
        assertNotNull(usersForSentimentAnalysis);
    }
}