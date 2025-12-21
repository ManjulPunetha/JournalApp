package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController
{
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        journalEntryService.saveNewEntry(journalEntry,userName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntryByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getUserByName(userName);

        List<JournalEntry> allJournals = user.getJournalEntries();
        if (!allJournals.isEmpty())
        {
            return new ResponseEntity<>(allJournals, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{ID}")
    public ResponseEntity<JournalEntry> getById(@PathVariable(name = "ID") int id) {
        JournalEntry journalEntry = journalEntryService.findJournalEntryById(id).orElse(null);
        if (journalEntry != null)
        {
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable(name = "ID") int id) {
        journalEntryService.deleteJournalEntryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{ID}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable(name = "ID") int id, @RequestBody JournalEntry newJournalEntry) {
        JournalEntry journalEntry = journalEntryService.updateJournalEntry(id, newJournalEntry);
        return (journalEntry != null) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
