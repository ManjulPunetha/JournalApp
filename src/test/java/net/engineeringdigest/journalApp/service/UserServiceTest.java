package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest
{
    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(strings = {
            "ram",
            "gandalf",
            "cena"
    })
    void getUserByName(String userName) {
        assertNotNull(userRepository.findByUsername(userName));
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "3,2,6",
            "10,20,30"

    })
    void assertTotal(int x, int y, int z) {
        assertEquals(z, (x + y));
    }

    @Test
    void getAllUsers() {
        assertEquals(userRepository.findAll().size(), 2, "Total Users");
    }
}