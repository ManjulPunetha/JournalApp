package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> getUsersForSentimentAnalysis();
}
