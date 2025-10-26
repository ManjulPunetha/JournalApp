package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "JOURNAL_ENTRY")
public class JournalEntry
{
    @Id
    @SequenceGenerator(name = "JOURNAL_SEQ", sequenceName = "JOURNAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="JOURNAL_SEQ")
    private int id;

    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "RECORD_DATE")
    private LocalDateTime date;

    // Many Journal Entries belong to one User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)  // Foreign Key in journal_entries pointing to users.id
    private User user;
}
