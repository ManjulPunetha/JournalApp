package net.engineeringdigest.journalApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS_TABLE")
public class User
{
    @Id
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    private int id;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    // JPA Mapping: One User has many JournalEntries
    @OneToMany(mappedBy = "user",           // "user" is the field in JournalEntry class
            cascade = CascadeType.ALL,   // Cascade all DB actions (insert, update, delete) to child
            orphanRemoval = true         // Remove child if parent no longer references it
    )
    private List<JournalEntry> journalEntries;
}
