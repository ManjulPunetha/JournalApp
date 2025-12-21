package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Saves a new journal entry.
     *
     * @param journalEntry The entry to save.
     * @return The saved entry.
     */
    @Transactional // 2. Add transactional annotation for write operations
    public JournalEntry saveNewEntry(@NonNull JournalEntry journalEntry, String userName) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(journalEntry);

        User user = userService.getUserByName(userName);
        user.getJournalEntries().add(journalEntry);

        return journalEntryRepository.save(journalEntry);
    }

    /**
     * Retrieves all journal entries.
     *
     * @return A list of all journal entries.
     */
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    /**
     * Finds a journal entry by its ID.
     *
     * @param id The ID of the journal entry.
     * @return An Optional containing the entry if found, otherwise empty.
     */
    public Optional<JournalEntry> findJournalEntryById(int id) {
        // 3. Return Optional to handle not-found cases gracefully
        return journalEntryRepository.findById(id);
    }

    /**
     * Deletes a journal entry by its ID.
     *
     * @param id The ID of the entry to delete.
     * @return true if an entry was found and deleted, false otherwise.
     */
    @Transactional
    public boolean deleteJournalEntryById(int id) {
        if (journalEntryRepository.existsById(id))
        {
            journalEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public JournalEntry updateJournalEntry(int id, JournalEntry newJournalEntry) {
        JournalEntry oldEntry = findJournalEntryById(id).orElse(null);
        if (oldEntry != null)
        {
            if (!newJournalEntry.getTitle().equals(oldEntry.getTitle()))
            {
                oldEntry.setTitle(newJournalEntry.getTitle());
            }
            if (!newJournalEntry.getContent().equals(oldEntry.getContent()))
            {
                oldEntry.setContent(newJournalEntry.getContent());
            }

            oldEntry.setDate(LocalDateTime.now());
            journalEntryRepository.save(oldEntry);
        }

        return oldEntry;
    }
}
