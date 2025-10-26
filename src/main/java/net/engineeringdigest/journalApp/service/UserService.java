package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(String username, User updated) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
            user.setUsername(updated.getUsername());
            user.setPassword(updated.getPassword());
            return userRepository.save(user);
        }
        return null;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}