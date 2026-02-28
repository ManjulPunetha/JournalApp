package net.engineeringdigest.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "USERS_TABLE")
public class User {
    @Id
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    private int id;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Type(type = "numeric_boolean")
    @Column(name = "sentiment_analysis", columnDefinition = "NUMBER(1) DEFAULT 0")
    private Boolean sentimentAnalysis;

    // JPA Mapping: One User has many JournalEntries
    @OneToMany(mappedBy = "user",           // "user" is the field in JournalEntry class
            cascade = CascadeType.DETACH,   // Cascade all DB actions (insert, update, delete) to child
            orphanRemoval = true         // Remove child if parent no longer references it
    )
    @JsonManagedReference
    private List<JournalEntry> journalEntries;

    @ElementCollection(fetch = FetchType.EAGER)
    //creates a separate table where each row represents an element of the list.
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    //Store the elements of this list in a separate table called user_roles.
    @Column(name = "role")
    private List<String> roles;
}
