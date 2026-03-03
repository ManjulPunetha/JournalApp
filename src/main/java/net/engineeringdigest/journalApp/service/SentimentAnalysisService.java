package net.engineeringdigest.journalApp.service;

import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {
    /**
     * In the future: integrate with an ML model/API.
     * Returns: 1 (Positive), 0 (Neutral), -1 (Negative)
     * For now: returns a dummy empty String.
     */
    public String getSentiment(String text) {
        // TODO: Replace with actual NLP/ML model call
        return "";  // Dummy placeholder
    }
}
